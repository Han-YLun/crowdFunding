package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Cert;
import com.hyl.atcrowdfunding.service.CertService;
import com.hyl.atcrowdfunding.service.CertTypeService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/28
 **/
@Controller
@RequestMapping("/certtype")
public class CertTypeController {

    @Autowired
    private CertTypeService certTypeService;

    @Autowired
    private CertService certService;


    @RequestMapping("/index")
    public String index(Map<String, Object> map){

        //查询所有资质
        List<Cert> queryCert = certService.queryAllCert();
        map.put("allCert",queryCert);

        //查询资质与账户类型的关系(之前分配给账户类型的资质)
        List<Map<String,Object>> certAcctTypeList = certTypeService.queryCertAcctType();
        map.put("certAcctTypeList",certAcctTypeList);

        return "certtype/index";
    }

    @ResponseBody
    @RequestMapping("insertAcctTypeCert")
    public Object insertAcctTypeCert(Integer certid,String accttype){
        AjaxResult result = new AjaxResult();

        try {
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid",certid);
            paramMap.put("accttype",accttype);

            int count = certTypeService.insertAccttypeCert(paramMap);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("deleteAcctTypeCert")
    public Object deleteAcctTypeCert(Integer certid,String accttype){
        AjaxResult result = new AjaxResult();

        try {
            Map<String,Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid",certid);
            paramMap.put("accttype",accttype);

            int count = certTypeService.deleteAccttypeCert(paramMap);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }
}
