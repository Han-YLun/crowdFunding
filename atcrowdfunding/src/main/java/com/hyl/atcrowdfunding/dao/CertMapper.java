package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.Cert;
import com.hyl.atcrowdfunding.model.MemberCert;

import java.util.List;

public interface CertMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cert record);

    Cert selectByPrimaryKey(Integer id);

    List<Cert> selectAll();

    int updateByPrimaryKey(Cert record);

    List<Cert> queryAllCert();

    List<Cert> queryCertByAcctType(String accttype);

    void insertMemberCert(MemberCert memberCert);
}