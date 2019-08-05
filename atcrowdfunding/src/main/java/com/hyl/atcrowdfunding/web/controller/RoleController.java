package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.model.Role;
import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.service.PermissionService;
import com.hyl.atcrowdfunding.service.RoleService;
import com.hyl.atcrowdfunding.service.UserService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import com.hyl.atcrowdfunding.utils.Data;
import com.hyl.atcrowdfunding.utils.Page;
import com.hyl.atcrowdfunding.utils.StringUtil;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/14
 **/

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;



    @RequestMapping("/index")
    public String index(){
        return "role/index";
    }


    @RequestMapping("/add")
    public String add(){
        return "role/add";
    }

    @RequestMapping("/edit")
    public String edit(Integer id,Map<String, Object> map){
        Role role = roleService.getRole(id);
        map.put("role",role);
        return "role/edit";
    }


    @RequestMapping("/assignPermission")
    public String assignPermission(){
        return "role/assignPermission";
    }


    @RequestMapping("doAssignPermission")
    @ResponseBody
    public Object doAssignPermission(Integer roleid,Data datas){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.saveRolePermissionRelationship(roleid,datas);

            result.setSuccess(count == datas.getIds().size());
        }catch (Exception e){
            result.setSuccess(false);
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping("doLoadDataAsync")
    @ResponseBody
    public Object doLoadDataAsync(Integer roleid){

        List<Permission> root = new ArrayList<Permission>();

        List<Permission> childredPermissions = permissionService.queryAllPermission();

        //根据角色id查询该角色之前所分配过的许可
        List<Integer> permissionIdsForRoleId = permissionService.queryPermissionIdsByRoleId(roleid);

        Map<Integer,Permission> map = new HashMap<Integer, Permission>();

        for (Permission innerPermission : childredPermissions) {
            map.put(innerPermission.getId(),innerPermission);

            if (permissionIdsForRoleId.contains(innerPermission.getId())){
                innerPermission.setChecked(true);
            }
        }

        for (Permission permission : childredPermissions) {

            //通过子查找父
            //子菜单
            Permission child = permission;  //假设为子菜单
            if (child.getPid() == null){
                root.add(permission);
            }else{
                //父节点
                Permission parent = map.get(child.getPid());
                parent.getChildren().add(child);
            }

        }

        return root;
    }




    @ResponseBody
    @RequestMapping("doBatchDelete")
    public Object doBatchDelete(Data datas){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.batchDeleteRole(datas);
            if (count == datas.getDatas().size()){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
            }
        }catch (Exception e){
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("doDelete")
    public Object doDelete(Integer uid){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.deleteRole(uid);
            if (count == 1){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
            }
        }catch (Exception e){
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("doEdit")
    public Object doEdit(Role role){
        AjaxResult result = new AjaxResult();
        try {
            int count = roleService.updateRole(role);
            if (count == 1){
                result.setSuccess(true);
            }else{
                result.setSuccess(false);
            }
        }catch (Exception e){
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("doAdd")
    public Object doAdd(Role role){
        AjaxResult result = new AjaxResult();
        try {
            roleService.saveRole(role);
            result.setSuccess(true);
        }catch (Exception e){
            result.setSuccess(false);
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 异步分页查询
     * @param queryText
     * @param pageno
     * @param pagesize
     * @return
     */
    @RequestMapping("pageQuery")
    @ResponseBody
    public Object pageQuery(String queryText,
                            @RequestParam(required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(required = false,defaultValue = "2")Integer pagesize ){

        AjaxResult result = new AjaxResult();

        try{
            Map<String, Object> paramMap = new HashMap<String, Object> ();
            paramMap.put("pageno",pageno);
            paramMap.put("pagesize",pagesize);

            System.out.println("pagesize : "+pagesize);

            if (StringUtil.isNotEmpty(queryText)){
                queryText = queryText.replaceAll("%","\\\\%");
            }

            paramMap.put("queryText",queryText);

            //分页查询数据
            Page<Role> rolePage = roleService.pageQuery(paramMap);

            result.setPage(rolePage);
            result.setSuccess(true);
        }catch (Exception e){

            result.setSuccess(false);
            e.printStackTrace();
        }
       return  result;
    }





}
