package com.hyl.atcrowdfunding.service;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/12
 **/
public interface UserService {


    User queryUserlogin(Map<String, Object> paramMap);

    //Page queryPage(Integer pageno, Integer pagesize);
    
    Page queryPage(Map<String, Object> paramMap);
    
    int saveUser(User user);

    User getUserById(Integer id);

    int updateUser(User user);

    int deleteUser(Integer id);

    int deleteBatchUser(Integer[] id);

    List<Role> queryAllRole();

    List<Integer> queryRoleByUserId(Integer id);

    int deleteUserRoleRelationShip(Integer userid, Data data);

    int saveUserRoleRelationShip(Integer userid, Data data);

    List<Permission> queryPermissionByUserId(Integer id);
}
