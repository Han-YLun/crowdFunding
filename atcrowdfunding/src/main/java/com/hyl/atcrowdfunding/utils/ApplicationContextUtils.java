package com.hyl.atcrowdfunding.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: hyl
 * @date: 2019/07/29
 **/
@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtils.applicationContext = applicationContext;
    }
}
