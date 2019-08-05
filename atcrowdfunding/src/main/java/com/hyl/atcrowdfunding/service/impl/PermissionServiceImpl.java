package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.PermissionMapper;
import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: hyl
 * @date: 2019/07/18
 **/

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    public Permission getRootPermission() {
        return permissionMapper.getRootPermission();
    }

    public List<Permission> getChildrenPermissionByPid(Integer id) {
        return permissionMapper.getChildrenPermissionByPid(id);
    }

    public List<Permission> queryAllPermission() {
        return permissionMapper.queryAllPermission();
    }

    public int savePermission(Permission permission) {
        return permissionMapper.insert(permission);
    }

    public Permission getPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    public int updatePermission(Permission permission) {
        return permissionMapper.updateByPrimaryKey(permission);
    }

    public int deletePermission(Integer id) {
        return permissionMapper.deleteByPrimaryKey(id);
    }

    public List<Integer> queryPermissionIdsByRoleId(Integer roleid) {
        return permissionMapper.queryPermissionIdsByRoleId(roleid);
    }
}
