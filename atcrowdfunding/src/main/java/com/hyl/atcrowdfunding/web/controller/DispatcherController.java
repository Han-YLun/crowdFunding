package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.service.MemberService;
import com.hyl.atcrowdfunding.service.UserService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Const;
import com.hyl.atcrowdfunding.utils.MD5Util;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.security.acl.PermissionImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAKey;
import java.util.*;

/**
 * @author: hyl
 * @date: 2019/07/12
 **/

@Controller
public class DispatcherController {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/index")
    public String index(){
        return "index";
    }


    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpSession session){

        //判断是否需要自动登录
        //如果之前登陆过,cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证

        boolean needLogin = true;
        String logintype = null;
        Cookie [] cookies = request.getCookies();

        if (cookies != null){
            //如果客户端禁用了Cookie,那么无法获取Cookie信息

            for (Cookie cookie : cookies) {
                if ("logincode".equals(cookie.getName())){
                    String logincode = cookie.getValue();
                    System.out.println("获取到的Cookie中的键值对"+cookie.getName()+"====" + logincode);

                    String[] split = logincode.split("&");
                    if (split.length == 3){
                        String loginacct = split[0].split("=")[1];
                        String userpwd = split[1].split("=")[1];
                        logintype = split[2].split("=")[1];

                        Map<String, Object> paramMap = new HashMap<String, Object>();
                        paramMap.put("loginacct",loginacct);
                        paramMap.put("userpswd", userpwd);
                        paramMap.put("type",logintype);


                        if ("user".equals(logintype)){

                            User dbLogin = userService.queryUserlogin(paramMap);

                            if (dbLogin != null){
                                session.setAttribute(Const.LOGIN_USER,dbLogin);
                                needLogin = false;
                            }

                            getPermissionsTree(session, dbLogin);


                        }else if ("member".equals(logintype)){
                            Member dbLogin = memberService.queryMemberLogin(paramMap);

                            if (dbLogin != null){
                                session.setAttribute(Const.LOGIN_MEMBER,dbLogin);
                                needLogin = false;
                            }
                        }
                    }
                    
                }
            }
        }

        if (needLogin){
            return "login";
        }else{
            if ("user".equals(logintype)){
                return "redirect:/main.htm";
            }else if ("member".equals(logintype)){
                return "redirect:/member.htm";
            }
        }



        return "login";
    }

    @RequestMapping("/main")
    public String main(){
        return "main";                                        
    }

    @RequestMapping("/member")
    public String member(){
        return "member/member";
    }


    @RequestMapping("/logout")
    public String logout(HttpSession session){

        //销毁session对象
        session.invalidate();
        return "redirect:/index.htm";
    }



     //异步请求
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(String loginacct, String userpswd, String type, String rememberme,
                          HttpSession session, HttpServletResponse response){

        AjaxResult result = new AjaxResult();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("loginacct",loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type",type);


            if ("member".equals(type)){
                Member member = memberService.queryMemberLogin(paramMap);

                session.setAttribute(Const.LOGIN_MEMBER,member);

                if ("1".equals(rememberme)){

                    String logincode = "loginacct="+member.getLoginacct()+"&userpwd="+member.getUserpswd()+"&logintype=member";

                    System.out.println("member : " + logincode);

                    Cookie cookie = new Cookie("logincode",logincode);

                    cookie.setMaxAge(60*60*24*14);  //cookie过期时间为两周,单位为秒
                    cookie.setPath("/");    //表示任何路径都可访问cookie

                    response.addCookie(cookie);
                }


            }else if ("user".equals(type)){
                User user = userService.queryUserlogin(paramMap);

                session.setAttribute(Const.LOGIN_USER,user);

                if ("1".equals(rememberme)){

                    String logincode = "loginacct="+user.getLoginacct()+"&userpwd="+user.getUserpswd()+"&logintype=user";

                    System.out.println("user :　" + logincode);

                    Cookie cookie = new Cookie("logincode",logincode);

                    cookie.setMaxAge(60*60*24*14);  //cookie过期时间为两周,单位为秒
                    cookie.setPath("/");    //表示任何路径都可访问cookie

                    response.addCookie(cookie);
                }
                getPermissionsTree(session, user);


            }else{
                
            }
            result.setSuccess(true);
            result.setData(type);
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("登录失败！");
        } 

        return result;
    }

    private void getPermissionsTree(HttpSession session, User user) {
        //加载当前登录用户所拥有的许可权限

        List<Permission> myPermissions = userService.queryPermissionByUserId(user.getId());

        Permission permissionRoot = null;

        Map<Integer,Permission> map = new HashMap<Integer, Permission>();

        //用于拦截器拦截许可权限
        Set<String> myUris = new HashSet<String>();

        for (Permission innerPermission : myPermissions) {
            map.put(innerPermission.getId(),innerPermission);

            myUris.add("/"+innerPermission.getUrl());
        }

        session.setAttribute(Const.MY_URIS,myUris);

        for (Permission permission : myPermissions) {
            //通过子查找父
            //子菜单
            Permission child = permission;  //假设为子菜单
            if(child.getPid() == null){
                permissionRoot = permission;
            }else{
                Permission parent = map.get(child.getPid());
                parent.getChildren().add(child);
            }
        }

        System.out.println("permissionRoot : " + permissionRoot);

        session.setAttribute("permissionRoot",permissionRoot);
    }

}
