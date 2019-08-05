package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.AccountTypeCertMapper;
import com.hyl.atcrowdfunding.service.CertTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/28
 **/

@Service
public class CertTypeServiceImpl implements CertTypeService {

    @Autowired
    private AccountTypeCertMapper accountTypeCertMapper;

    @Override
    public List<Map<String, Object>> queryCertAcctType() {
        return accountTypeCertMapper.queryCertAcctType();
    }

    @Override
    public int insertAccttypeCert(Map<String, Object> map) {
        return accountTypeCertMapper.insertAcctTypeCert(map);
    }

    @Override
    public int deleteAccttypeCert(Map<String, Object> map) {
        return accountTypeCertMapper.deleteAcctTypeCert(map);
    }
}
