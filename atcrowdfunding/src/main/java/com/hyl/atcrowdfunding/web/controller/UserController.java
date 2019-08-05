package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.service.UserService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;
import com.hyl.atcrowdfunding.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/14
 **/

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;



    @RequestMapping("/index")
    public String index(){
        return "user/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){

        User user = userService.getUserById(id);
        map.put("user",user);

        return "user/update";
    }


    //分配角色
    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid, Data data){

        AjaxResult result = new AjaxResult();
        try {
            userService.saveUserRoleRelationShip(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("分配角色数据失败！");
            e.printStackTrace();
        }
        return result;
    }

    //取消角色
    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object doUnAssignRole(Integer userid, Data data){


        System.out.println("doUnAssignRole" + userid + "data" + data.toString());
        AjaxResult result = new AjaxResult();
        try {
            userService.deleteUserRoleRelationShip(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("取消分配角色数据失败！");
            e.printStackTrace();
        }
        return result;
    }
    

    //显示分配界面数据
    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map map){

        List<Role> allList = userService.queryAllRole();

        List<Integer> roleIds = userService.queryRoleByUserId(id);

        List<Role> leftRoleList = new ArrayList<Role>(); //未分配角色
        List<Role> rightRoleList = new ArrayList<Role>();  //已分配角色

        for (Role role : allList) {

            if (roleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else{
                leftRoleList.add(role);
            }
        }

        map.put("leftRoleList",leftRoleList);
        map.put("rightRoleList",rightRoleList);

        return "user/assignrole";
    }




    //批量用户
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Integer[] id){
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteBatchUser(id);

            result.setSuccess(count == id.length);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("修改数据失败！");
            e.printStackTrace();
        }
        return result;
    }


    //删除用户
    @ResponseBody
    @RequestMapping("/doDelete")
    public Object doDelete(Integer id){
        AjaxResult result = new AjaxResult();
        try {
            int count = userService.deleteUser(id);

            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("修改数据失败！");
            e.printStackTrace();
        }
        return result;
    }



    //修改用户
    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(User user){

        AjaxResult result = new AjaxResult();
        try {
            int count = userService.updateUser(user);

            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("修改数据失败！");
            e.printStackTrace();
        }
        return result;
    }


    //添加用户
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(User user){

        AjaxResult result = new AjaxResult();
        try {
            int count = userService.saveUser(user);

            result.setSuccess(count == 1);
            System.out.println(result.getPage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存数据失败！");
            e.printStackTrace();
        }
        return result;
    }




    //条件查询
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize,
                        String queryText){

        AjaxResult result = new AjaxResult();
        try {

            Map paramMap = new HashMap();
            paramMap.put("pageno",pageno);
            paramMap.put("pagesize",pagesize);


            if (StringUtil.isNotEmpty(queryText)){
                if (queryText.contains("%")){
                    queryText = queryText.replaceAll("%","\\\\%");
                }

                paramMap.put("queryText",queryText);

            }

            Page page = userService.queryPage(paramMap);

            result.setSuccess(true);
            result.setPage(page);
            System.out.println(result.getPage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("查询数据失败！");
            e.printStackTrace();
        }

        //将对象序列化为JSON字符串
        return result;
    }
}
