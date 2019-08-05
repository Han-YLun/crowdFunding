package com.hyl.atcrowdfunding.service;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/28
 **/
public interface CertTypeService {


    List<Map<String, Object>> queryCertAcctType();


    public int insertAccttypeCert(Map<String, Object> map);

    public int deleteAccttypeCert(Map<String, Object> map);
}
