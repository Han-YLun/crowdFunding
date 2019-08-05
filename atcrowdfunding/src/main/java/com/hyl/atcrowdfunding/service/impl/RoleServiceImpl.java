package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.dao.RoleMapper;
import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.RolePermission;
import com.hyl.atcrowdfunding.service.RoleService;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/22
 **/
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    public Page<Role> pageQuery(Map<String, Object> paramMap) {

        Page<Role> rolePage = new Page<Role>((Integer) paramMap.get("pageno"),
                (Integer)paramMap.get("pagesize"));

        System.out.println("pageQuery : "+(Integer)paramMap.get("pagesize"));

        paramMap.put("startIndex",rolePage.getStartIndex());

        List<Role> roles = roleMapper.pageQuery(paramMap);

        //获取数据的总条数
        int count = roleMapper.queryCount(paramMap);
        rolePage.setData(roles);
        rolePage.setTotalsize(count);

        return rolePage;
    }

    public Role getRole(Integer id) {
        return roleMapper.getRole(id);
    }

    public int updateRole(Role role) {
        return roleMapper.update(role);
    }

    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    public int deleteRole(Integer uid) {
        return roleMapper.delete(uid);
    }

    public int batchDeleteRole(Data datas) {
        return roleMapper.batchDeleteRole(datas);
    }

    public int saveRolePermissionRelationship(Integer roleid, Data datas) {

        roleMapper.deleteRolePermissionRelationship(roleid);

        int totalCount = 0;
        List<Integer> ids = datas.getIds();
        for (Integer id : ids) {

            RolePermission rp = new RolePermission();
            rp.setRoleid(roleid);
            rp.setPermissionid(id);
            int count = roleMapper.insertRolePermission(rp);
            totalCount += count;
        }
        return totalCount;
    }
}
