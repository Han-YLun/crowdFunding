package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Cert;
import com.hyl.atcrowdfunding.model.MemberCert;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/28
 **/
public interface CertService {
/*
    public Cert queryCert(Map<String, Object> paramMap);

    public Page<Cert> pageQuery(Map<String, Object> paramMap);

    public int queryCount(Map<String, Object> paramMap);

    public void insertCert(Cert cert);

    public Cert queryById(Integer id);

    public int updateCert(Cert cert);

    public int deleteCert(Integer id);

    public int deleteCerts(Data ds);
    */
    List<Cert> queryCertByAcctType(String accttype);

    List<Cert> queryAllCert();

    void saveMemberCert(List<MemberCert> certimgs);


 /*   public List<Map<String, Object>> queryAllAccttypeCert();


    public void saveMemberCert(List<MemberCert> certimgs);*/
}
