package com.hyl.atcrowdfunding.interceptor;

import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.utils.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: hyl
 * @date: 2019/07/22
 **/
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //定义哪些是不需要拦截的
        Set<String> uri = new HashSet<String>();

        uri.add("/user/reg.do");
        uri.add("/user/reg.htm");
        uri.add("/login.htm");
        uri.add("/doLogin.do");
        uri.add("/logout.do");

        //System.out.println("preHandle : "+uri);


        //获取请求路径
        String servletPath = request.getServletPath();

        if (uri.contains(servletPath)){
            return true;
        }

        //System.out.println("preHandle : "+servletPath);

        //判断用户是否登录,如果登录就放行
        HttpSession session = request.getSession();
        User login_user = (User) session.getAttribute(Const.LOGIN_USER);
        Member login_member = (Member) session.getAttribute(Const.LOGIN_MEMBER);

        //System.out.println("preHandle : "+user);
        if (login_user != null || login_member != null) {
            return true;
        }else{
            response.sendRedirect(request.getContextPath()+"/login.htm");
            return false;
        }

    }
}
