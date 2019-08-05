package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.utils.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

	User queryUserlogin(Map<String, Object> paramMap);

    List queryList(Map<String, Object> paramMap);

    Integer queryCount(Map<String, Object> paramMap);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserId(Integer id);

    int deleteUserRoleRelationShip(@Param("userid") Integer userid,@Param("data") Data data);

    int saveUserRoleRelationShip(@Param("userid") Integer userid,@Param("data") Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}