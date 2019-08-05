package com.hyl.atcrowdfunding.web.controller;

import com.hyl.atcrowdfunding.model.Permission;
import com.hyl.atcrowdfunding.service.PermissionService;
import com.hyl.atcrowdfunding.utils.AjaxResult;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hyl
 * @date: 2019/07/18
 **/

@Controller
@RequestMapping("permission")
public class PermissioncController {

    @Autowired
    private PermissionService permissionService;


    @RequestMapping("/index")
    public String index(){
        return "permission/index";
    }

    @RequestMapping("/toAdd")
    public String toAdd(){
        return "permission/add";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){

        Permission permission = permissionService.getPermissionById(id);
        map.put("permission",permission);
        return "permission/update";
    }


    @ResponseBody
    @RequestMapping("deletePermission")
    public Object deletePermission(Integer id){
        AjaxResult result = new AjaxResult();
        try {

            int count = permissionService.deletePermission(id);
            result.setSuccess(count == 1);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("删除许可树数据失败！");
            e.printStackTrace();
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("doAdd")
    public Object doAdd(Permission permission){

        AjaxResult result = new AjaxResult();
        try {
            int count = permissionService.savePermission(permission);
            result.setSuccess(count == 1);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("保存许可树数据失败！");
            e.printStackTrace();
        }
        return result;
    }


    @ResponseBody
    @RequestMapping("doUpdate")
    public Object doUpdate(Permission permission){

        System.out.println("Permission : "+permission);

        AjaxResult result = new AjaxResult();
        try {

            int count = permissionService.updatePermission(permission);
            result.setSuccess(count == 1);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("修改许可树数据失败！");
            e.printStackTrace();
        }
        return result;
    }

    @ResponseBody
    @RequestMapping("loadData")
    public Object loadData(){

        AjaxResult result = new AjaxResult();
        try {

            List<Permission> root = new ArrayList<Permission>();

            List<Permission> childrenPermissions = permissionService.queryAllPermission();

            Map<Integer,Permission> map = new HashMap<Integer, Permission>();
            for (Permission innerPermission : childrenPermissions) {
                map.put(innerPermission.getId(),innerPermission);
            }


            for (Permission permission : childrenPermissions) {
                //通过子查找父
                //子菜单
                Permission child = permission;//假设为子菜单
                if (child.getPid() == null){
                    root.add(child);//根节点
                }else{
                    Permission parent = map.get(child.getPid());
                    parent.getChildren().add(child);
                }
            }


            result.setSuccess(true);
            result.setData(root);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("加载许可树数据失败！");
            e.printStackTrace();
        }

        return result;
    }



}
