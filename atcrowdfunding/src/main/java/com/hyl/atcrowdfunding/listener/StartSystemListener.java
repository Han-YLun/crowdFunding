package com.hyl.atcrowdfunding.listener;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.service.PermissionService;
import com.hyl.atcrowdfunding.utils.Const;
import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: hyl
 * @date: 2019/07/22
 **/
public class StartSystemListener implements ServletContextListener {

    //在服务器启动时，创建application对象时需要执行的方法
    public void contextInitialized(ServletContextEvent sce) {

        //将上下文路径存放到application域中
        ServletContext application = sce.getServletContext();
        String contextPath = application.getContextPath();
        application.setAttribute("APP_PATH",contextPath);

        System.out.println("StartSystemListener : "+contextPath);


        //加载所有许可路径
        ApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(application);
        PermissionService permissionService = ioc.getBean(PermissionService.class);
        List<Permission> permissions = permissionService.queryAllPermission();

        Set<String> allURIs = new HashSet<String>();

        for (Permission permission : permissions) {
            allURIs.add("/"+permission.getUrl());
        }

        System.out.println("StartSystemListener : "+allURIs.toString());

        application.setAttribute(Const.ALL_PERMISSION_URI,allURIs);
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }
}
