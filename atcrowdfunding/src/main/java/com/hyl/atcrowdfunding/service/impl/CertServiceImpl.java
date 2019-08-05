package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.CertMapper;
import com.hyl.atcrowdfunding.model.Cert;
import com.hyl.atcrowdfunding.model.MemberCert;
import com.hyl.atcrowdfunding.service.CertService;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/28
 **/

@Service
public class CertServiceImpl implements CertService {

    @Autowired
    private CertMapper certMapper;

    @Override
    public List<Cert> queryCertByAcctType(String accttype) {
        return certMapper.queryCertByAcctType(accttype);
    }

    @Override
    public List<Cert> queryAllCert() {
        return certMapper.queryAllCert();
    }

    @Override
    public void saveMemberCert(List<MemberCert> certimgs) {
        for (MemberCert memberCert : certimgs) {
            certMapper.insertMemberCert(memberCert);
        }
    }

}
