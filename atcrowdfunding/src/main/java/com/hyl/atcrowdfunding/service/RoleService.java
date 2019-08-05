package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/22
 **/
public interface RoleService {
    Page<Role> pageQuery(Map<String, Object> paramMap);

    Role getRole(Integer id);

    int updateRole(Role role);

    void saveRole(Role role);

    int deleteRole(Integer uid);

    int batchDeleteRole(Data datas);

    int saveRolePermissionRelationship(Integer roleid, Data datas);
}
