package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.AccountTypeCert;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

public interface AccountTypeCertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountTypeCert record);

    AccountTypeCert selectByPrimaryKey(Integer id);

    List<AccountTypeCert> selectAll();

    int updateByPrimaryKey(AccountTypeCert record);

    List<Map<String, Object>> queryCertAcctType();

    int insertAcctTypeCert(Map<String, Object> map);

    int deleteAcctTypeCert(Map<String, Object> map);
}