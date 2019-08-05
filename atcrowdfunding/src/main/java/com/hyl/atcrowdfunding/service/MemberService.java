package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Member;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/26
 **/
public interface MemberService {
    Member queryMemberLogin(Map<String, Object> paramMap);

    void updateAcctType(Member loginMember);

    void updateBasicInfo(Member loginMember);

    void updateEmail(Member member);

    void updateAuthStatus(Member loginMember);

    Member getMemberById(Integer memberid);

    List<Map<String, Object>> queryCertByMemberId(Integer memberid);
}
