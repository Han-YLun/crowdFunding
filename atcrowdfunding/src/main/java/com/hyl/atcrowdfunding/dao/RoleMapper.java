package com.hyl.atcrowdfunding.dao;

import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.RolePermission;
import com.hyl.atcrowdfunding.utils.Data;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    Role selectByPrimaryKey(Integer id);

    List<Role> pageQuery(Map<String, Object> paramMap);

    int updateByPrimaryKey(Role record);

    int queryCount(Map<String, Object> paramMap);

    Role getRole(Integer id);

    int update(Role role);

    int delete(Integer uid);

    int batchDeleteRole(Data datas);

    void deleteRolePermissionRelationship(Integer roleid);

    int insertRolePermission(RolePermission rp);
}