package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Advert;
import com.hyl.atcrowdfunding.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: hyl
 * @date: 2019/07/23
 **/
@Controller
@RequestMapping("/advert")
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    @RequestMapping("/index")
    public String index(){
        return "advert/index";
    }

    @RequestMapping("/add")
    public String add(){
        return "advert/add";
    }

    @RequestMapping("/edit")
    public String edit(Integer id, Model model){

      /*  //根据主键查询资质信息
        Advert advert =  advertService.queryById(id);

        model.addAttribute("advert",advert); */
        return "advert/edit";
    }
}
