package com.hyl.atcrowdfunding.service.impl;

import com.hyl.atcrowdfunding.exception.LoginFailException;
import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.dao.UserMapper;
import com.hyl.atcrowdfunding.service.UserService;
import com.hyl.atcrowdfunding.utils.Const;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.MD5Util;
import com.hyl.atcrowdfunding.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/12
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public User queryUserlogin(Map<String, Object> paramMap) {
        //System.out.println("queryUserlogin"+paramMap);

        User user =  userMapper.queryUserlogin(paramMap);
        //System.out.println(user.toString());

        if (user == null){
            throw new LoginFailException("用户帐号或密码不正确！");
        }
        return user;
    }

    public Page queryPage(Map<String, Object> paramMap) {


        Page page = new Page((Integer)paramMap.get("pageno"),
                (Integer) paramMap.get("pagesize"));

        Integer startIndex = page.getStartIndex();
        paramMap.put("startIndex",startIndex);


        List datas = userMapper.queryList(paramMap);
        page.setData(datas);


        Integer totalsize = userMapper.queryCount(paramMap);
        page.setTotalsize(totalsize);


        return page;
    }




    public int saveUser(User user) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String createtime = sdf.format(date);

        user.setCreatetime(createtime);
        user.setUserpswd(MD5Util.digest(Const.PASSWORD));

        return userMapper.insert(user);
    }

    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    public int updateUser(User user) {
        return userMapper.updateByPrimaryKey(user);
    }

    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    public int deleteBatchUser(Integer[] ids) {

        int totalCount = 0;
        for (Integer id : ids) {
            int count = userMapper.deleteByPrimaryKey(id);
            totalCount += count;
            
        }

        if (totalCount != ids.length){
            throw new RuntimeException("批量删除失败！");
        }
        return totalCount;
    }

    public List<Role> queryAllRole() {
        return userMapper.queryAllRole();
    }

    public List<Integer> queryRoleByUserId(Integer id) {
        return userMapper.queryRoleByUserId(id);
    }

    public int deleteUserRoleRelationShip(Integer userid, Data data) {
        System.out.println("doUnAssignRole" + userid + "data" + data.toString());
        return userMapper.deleteUserRoleRelationShip(userid,data);
    }

    public int saveUserRoleRelationShip(Integer userid, Data data) {
        return userMapper.saveUserRoleRelationShip(userid,data);
    }

    public List<Permission> queryPermissionByUserId(Integer id) {
        return userMapper.queryPermissionByUserId(id);
    }
}
