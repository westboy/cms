package com.zhiliao.module.web.system.service.impl;

import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.module.web.system.service.RoleService;
import com.zhiliao.mybatis.mapper.TSysPermissionMapper;
import com.zhiliao.mybatis.mapper.TSysRoleMapper;
import com.zhiliao.mybatis.mapper.TSysRolePermissionMapper;
import com.zhiliao.mybatis.model.TSysPermission;
import com.zhiliao.mybatis.model.TSysRole;
import com.zhiliao.mybatis.model.TSysRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:角色service
 *
 * @author Jin
 * @create 2017-04-14
 **/
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private TSysRoleMapper  roleMapper;

    @Autowired
    private TSysPermissionMapper permissionMapper;

    @Autowired
    private TSysRolePermissionMapper rolePermissionMapper;


    @Override
    public TSysRole findByid(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TSysRole> findByUserId(Integer userId) {
        return roleMapper.selectRoleByUserId(userId);
    }

    @Override
    public String delete(Integer id) {

        if(roleMapper.deleteByPrimaryKey(id)>0){
            rolePermissionMapper.delectByRoleId(id);
            return JsonUtil.toSUCCESS("删除成功！","role",true);
        }
        return JsonUtil.toERROR("删除失败！");
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager")
    public String update(TSysRole role,Integer[] permissionId) {
        role.setRolename(PinyinUtil.convertLower(HtmlKit.getText(role.getRolename()).trim()));
        if(roleMapper.updateByPrimaryKey(role)>0){
            if(!CmsUtil.isNullOrEmpty(permissionId)) {
                /*清空当前角色的权限*/
                rolePermissionMapper.delectByRoleId(role.getRoleId());
            }
            if(!CmsUtil.isNullOrEmpty(permissionId)&&permissionId.length>0){
                for (int id : permissionId) {
                    TSysRolePermission rp = new TSysRolePermission();
                    rp.setRoleId(role.getRoleId());
                    rp.setPermissonId(id);
                    SaveRolePermission(rp);
                }
             }
            return JsonUtil.toSUCCESS("操作成功！","role",true);
        }

        return JsonUtil.toERROR("操作失败！");
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager")
    public String save(TSysRole role,Integer[] permissionId) {
        role.setRolename(PinyinUtil.convertLower(HtmlKit.getText(role.getRolename()).trim()));
        if(role!=null){
            if(roleMapper.insert(role)>0)
                if(permissionId!=null&&permissionId.length>0){
                   /* 遍历添加角色权限*/
                   for(int id:permissionId){
                       TSysRolePermission rp = new  TSysRolePermission();
                       rp.setPermissonId(id);
                       rp.setRoleId(role.getRoleId());
                       SaveRolePermission(rp);
                   }
                }
                return JsonUtil.toSUCCESS("操作成功","role",true);
        }
        return JsonUtil.toERROR("操作失败！");
    }

    @Override
    public List<TSysRole> findAll(){
        return roleMapper.selectAll();
    }

    @Override
    public List<TSysPermission> findPermissonByPid(Integer pid) {
        return permissionMapper.selectByPid(pid);
    }

    @Override
    public Integer findPermissionCountByRoleId(Integer roleId, Integer permissionId) {
        return rolePermissionMapper.selectCountByRoleIdAndPermissionId(roleId,permissionId);
    }

    @Override
    public Integer  SaveRolePermission(TSysRolePermission  rolePermission){
        return  rolePermissionMapper.insert(rolePermission);
    }

    @Override
    public TSysPermission findPermissonByid(Integer pid) {
        return permissionMapper.selectByPrimaryKey(pid);
    }

    @Override
    public String save(TSysPermission permission) {
        permission.setName(PinyinUtil.convertLower(HtmlKit.getText(permission.getName())));
        if(permissionMapper.insert(permission)>0)
            return  JsonUtil.toSUCCESS("权限增加成功","permission",true);
        return JsonUtil.toERROR("权限增加失败");
    }

    @Override
    public String update(TSysPermission permission) {
        permission.setName(PinyinUtil.convertLower(HtmlKit.getText(permission.getName())));
        if(permissionMapper.selectByPrimaryKey(permission.getPermissionId()).getPid()==0&&permission.getPid()!=0){
            /* 父类交换 */
            TSysPermission p = permissionMapper.selectByPrimaryKey(permission.getPid());
            p.setPid(0);
            permission.setPid(permission.getPid());
            permissionMapper.updateByPrimaryKey(p);
        }
        if(permissionMapper.updateByPrimaryKey(permission)>0)
            return  JsonUtil.toSUCCESS("权限更新成功","permission",true);
        return JsonUtil.toERROR("权限更新失败");
    }

    @Override
    @Transactional(transactionManager = "masterTransactionManager")
    public String delectPermissionById(Integer id) {

        if(permissionMapper.deleteByPrimaryKey(id)>0){
            /* 删除角色权限 */
            rolePermissionMapper.delectByPermissionId(id);
            /* 删除子节点的角色关联*/
            List<TSysPermission> permissions = permissionMapper.selectByPid(id);
            if(permissions!=null&&permissions.size()>0){
                for(TSysPermission p:permissions){
                    rolePermissionMapper.delectByPermissionId(p.getPermissionId());
                }
            }
            /*删除子节点*/
            permissionMapper.deleteByPid(id);
            return  JsonUtil.toSUCCESS("权限删除成功","permission",true);
        }

        return JsonUtil.toERROR("权限删除失败");
    }


}
