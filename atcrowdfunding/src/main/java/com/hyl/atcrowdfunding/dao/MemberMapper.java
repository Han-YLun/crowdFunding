package com.hyl.atcrowdfunding.dao;


import com.hyl.atcrowdfunding.model.Member;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Member record);

    Member selectByPrimaryKey(Integer id);

    List<Member> selectAll();

    int updateByPrimaryKey(Member record);

	void updateAcctType(Member loginMember);

	void updateBasicinfo(Member loginMember);

	void updateEmail(Member loginMember);

	void updateAuthstatus(Member loginMember);

	Member getMemberById(Integer memberid);

	List<Map<String, Object>> queryCertByMemberid(Integer memberid);

	Member queryMemberLogin(Map<String, Object> paramMap);

	void updateBasicInfo(Member loginMember);

    void updateAuthStatus(Member loginMember);

	List<Map<String, Object>> queryCertByMemberId(Integer memberid);
}