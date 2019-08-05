package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Permission;

import java.util.List;

/**
 * @author: hyl
 * @date: 2019/07/18
 **/
public interface PermissionService {
    Permission getRootPermission();

    List<Permission> getChildrenPermissionByPid(Integer id);

    List<Permission> queryAllPermission();

    int savePermission(Permission permission);

    Permission getPermissionById(Integer id);

    int updatePermission(Permission permission);

    int deletePermission(Integer id);

    List<Integer> queryPermissionIdsByRoleId(Integer roleid);
}
