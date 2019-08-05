package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.MemberMapper;
import com.hyl.atcrowdfunding.exception.LoginFailException;
import com.hyl.atcrowdfunding.model.Member;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/26
 **/
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public Member queryMemberLogin(Map<String, Object> paramMap) {
        Member member =  memberMapper.queryMemberLogin(paramMap);
        //System.out.println(user.toString());

        if (member == null){
            throw new LoginFailException("用户帐号或密码不正确！");
        }
        return member;
    }

    public void updateAcctType(Member loginMember) {
        memberMapper.updateAcctType(loginMember);
    }

    public void updateBasicInfo(Member loginMember) {
        memberMapper.updateBasicInfo(loginMember);
    }

    @Override
    public void updateEmail(Member member) {
        memberMapper.updateEmail(member);
    }

    @Override
    public void updateAuthStatus(Member loginMember) {
        memberMapper.updateAuthStatus(loginMember);
    }

    @Override
    public Member getMemberById(Integer memberid) {
        return memberMapper.getMemberById(memberid);
    }

    @Override
    public List<Map<String, Object>> queryCertByMemberId(Integer memberid) {
        return memberMapper.queryCertByMemberId(memberid);
    }
}
