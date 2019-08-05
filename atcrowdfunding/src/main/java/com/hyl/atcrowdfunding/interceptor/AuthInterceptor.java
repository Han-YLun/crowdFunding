package com.hyl.atcrowdfunding.interceptor;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.service.PermissionService;
import com.hyl.atcrowdfunding.utils.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: hyl
 * @date: 2019/07/22
 **/
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //查询所有的许可

        Set<String> allURIs = (Set<String>) request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);


        //判断请求路径是否在许可范围内
        String servletPath = request.getServletPath();
        if (allURIs.contains(servletPath)){

            //判断请求路径是否在用户所拥有的权限内
            Set<String> myURIs = (Set<String>) request.getSession().getAttribute(Const.MY_URIS);
            if (myURIs.contains(servletPath)){
                return true;
            }else{
                response.sendRedirect(request.getContextPath() + "/login.htm");
                return false;
            }
        }else{
            //不在拦截范围内
            return true;
        }
    }
}
