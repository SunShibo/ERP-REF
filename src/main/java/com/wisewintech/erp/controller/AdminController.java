package com.wisewintech.erp.controller;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wisewintech.erp.controller.base.BaseCotroller;
import com.wisewintech.erp.entity.bo.AdminBO;
import com.wisewintech.erp.entity.bo.MenuBO;
import com.wisewintech.erp.entity.dto.ResultDTO;
import com.wisewintech.erp.entity.dto.ResultDTOBuilder;
import com.wisewintech.erp.service.AdminService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员
 */
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseCotroller {

    @Resource
    private AdminService adminService;

    /**
     * 管理员登录
     *
     * @param request
     * @param response
     * @param mobile   手机号
     * @param password 密码
     * @return
     */
    @RequestMapping("/adminLogin")
    public ResultDTO adminLogin(HttpServletRequest request, HttpServletResponse response, String mobile, String password, Integer hotelId, Integer classesId) {
        //验证参数
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return ResultDTOBuilder.failure("00001");
        }
        //验证账号
        AdminBO adminBO = adminService.queryAdminInfoByMobile(mobile);
        if (adminBO == null || !SecureUtil.md5(password).equals(adminBO.getPassword())) {
            return ResultDTOBuilder.failure("01000");
        }
        adminBO.setPassword("");
        super.putLoginAdmin(adminBO, response);
        List<MenuBO> roleMenuSuccess = adminService.getRoleMenuSuccess(adminBO.getRoleId());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("menu", roleMenuSuccess);
        resultMap.put("admin", adminBO);
        return ResultDTOBuilder.success(resultMap);
    }


    /**
     * 添加管理员
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/adminRegister")
    public ResultDTO adminRegister(HttpServletRequest request, HttpServletResponse response, AdminBO admin) {

        //验证参数
        if (StringUtils.isEmpty(admin.getPassword()) || StringUtils.isEmpty(admin.getMobile())
                || StringUtils.isEmpty(admin.getName()) || admin.getRoleId() == null) {
            return ResultDTOBuilder.failure("00001");
        }
        //判断手机号是否注册过
        int count = adminService.selectCountByMobile(admin.getMobile());
        if (count > 0) {
            return ResultDTOBuilder.failure("01001");
        }
        //注册管理员
        adminService.adminRegister(admin);
        return ResultDTOBuilder.success("00000");
    }


    /**
     * 根据条件查询管理员信息
     *  @param request
     * @param response
     * @param userId   用戶id
     * @param roleId   角色id
     * @param userName 用戶名
     * @return
     */
    @RequestMapping("/getAdminById")
    public ResultDTO getAdminById(HttpServletRequest request, HttpServletResponse response,
                                  Integer userId, Integer roleId, String userName, Integer pageNo, Integer pageSize) {

        if(pageNo==null || pageSize==null){
            return ResultDTOBuilder.failure("00001");
        }

        Map<String,Object>  paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("roleId",roleId);
        paramMap.put("userName",userName);

        //分页的用户信息
        IPage<AdminBO> adminByCond = adminService.getAdminByCond(pageNo, pageSize, paramMap);
        return ResultDTOBuilder.success(adminByCond);

        /* /用户信息总数
        Integer count=adminService.getAdminCountByCond(map);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("adminBOList",adminBOList);
        resultMap.put("count",count);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success(resultMap)) ;
        super.safeJsonPrint(response, result);*/
    }

    /*
    /**
     * 批量删除用户
     *
     * @param request
     * @param response
     * @param idArr    需要刪除的用戶的id
     * @return
     */
    @RequestMapping("/delAdmins")
    public ResultDTO delAdmin(HttpServletRequest request, HttpServletResponse response, String idArr) {
        // 非空判断
        if (StringUtils.isEmpty(idArr) || idArr.length() <= 2) {
            return ResultDTOBuilder.failure("00001");
        }
        //转换为数组 批量删除
        JSONArray objects = JSONUtil.parseArray(idArr);
        Integer[] ids = (Integer[]) objects.toArray();
        //Integer[] ids= JsonUtils.getIntegerArray4Json(idArr);
     //   adminService.delAdminById(ids);
        return ResultDTOBuilder.failure("00000");
    }

    /**
     * 修改admin用戶信息
     *
     * @param
     * @param request
     * @param response
     * @param param
     * @return
     */
    @RequestMapping("/updateAdminUser")
    public ResultDTO updateAdminUser(HttpServletRequest request, HttpServletResponse response, AdminBO param) {

        //验证参数
        if (param.getId() == null || StringUtils.isEmpty(param.getMobile()) || StringUtils.isEmpty(param.getName())) {
            return ResultDTOBuilder.failure("00001");
        }

        //验证手机号
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", param.getId());

        //?? List<AdminBO> adminBOS=adminService.getAdminByCond(map);
        /*if(adminBOS==null||adminBOS.size()==0){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001" , "参数异常")) ;
            super.safeJsonPrint(response , result);
            return null;
        }
        String oldMobile=adminBOS.get(0).getMobile();
        //不等于旧手机号则验证是否重复
        if(!param.getMobile().equals(oldMobile)){
            int count = adminService.selectCountByMobile(param.getMobile());
            if(count > 0 ){
                String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000093" , "账号已存在")) ;
                super.safeJsonPrint(response , result);
                return null;
            }
        }
        adminService.updateAdminUser(param);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success( "修改成功")) ;
        super.safeJsonPrint(response , result);
        return null;
    }


    *//**
         * 修改密码
         * @param request
         * @param response
         *//*
    @RequestMapping("/changePassword")
    public void changePassword(HttpServletRequest request, HttpServletResponse response, String passWord, String newPassword){
        // 非空判断
        AdminBO loginAdmin = super.getLoginAdmin(request);

        //验证参数
        if(StringUtils.isEmpty(passWord) || StringUtils.isEmpty(newPassword)){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001" , "参数异常")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        //判断之前的密码是否一样
        AdminBO adminBO = adminService.queryAdminInfoByMobile(loginAdmin.getMobile());
        if(!adminBO.getPassword().equals(MD5Util.digest(passWord))){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000208")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        adminBO.setPassword(newPassword);
        adminService.updateAdminUser(adminBO);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success( "修改成功")) ;
        super.safeJsonPrint(response , result);

    }


    *//**
         * 创建角色
         * 并赋予权限
         * @param request
         * @param response
         * @param roleName  角色名称
         * @param menuIds  权限ids
         *//*
    @RequestMapping("/addRoleGrantAuthority")
    public void addRoleGrantAuthority(HttpServletRequest request, HttpServletResponse response,
                                      String roleName, String menuIds, String hotelIds){
        //参数验证
        if(StringUtils.isEmpty(roleName) || StringUtils.isEmpty(menuIds)||StringUtils.isEmpty(hotelIds)){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        // 判断角色名称是否存在
        Integer nameCount = adminService.selectCountByRoleName(roleName);
        if(nameCount>0){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000092" , "该角色已存在")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        //创建角色 返回角色id
        RoleBO roleBO=new RoleBO();
        roleBO.setRoleName(roleName);
        adminService.addRole(roleBO);
        System.err.println(roleBO.getId());
        //添加权限
        Integer[] menuIdArr= JsonUtils.getIntegerArray4Json(menuIds);
        Integer[] hotelIdArr= JsonUtils.getIntegerArray4Json(hotelIds);
        adminService.addRoleMenu(roleBO.getId(),menuIdArr);
        adminService.addRoleHotel(roleBO.getId(),hotelIdArr);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success("添加成功")) ;
        super.safeJsonPrint(response , result);

    }

    *//**
         * 角色信息列表查询
         * 角色名模糊查询
         * 查询角色对应的权限
         * @param request
         * @param response
         *//*
    @RequestMapping("/getAllRoleMenu")
    public void getAllRoleMenu(HttpServletRequest request, HttpServletResponse response, String roleName){
        List<RoleBO> ros = adminService.getRoleByRoleName(roleName);
        String json = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success(ros));
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success(json)) ;
        super.safeJsonPrint(response, result);
    }


    *//**
         * 编辑对应角色的权限
         * @param request
         * @param response
         * @param roleId  角色id
         * @param menuIds  权限id
         *//*
    @RequestMapping("/grantAuthority")
    public void grantAuthority(HttpServletRequest request, HttpServletResponse response, Integer roleId, String menuIds, String hotelIds, String roleName){
        //AdminBO loginAdmin = super.getLoginAdmin(request);
        //验证参数
        if(StringUtils.isEmpty(String.valueOf(roleId)) || StringUtils.isEmpty(String.valueOf(menuIds))|| StringUtils.isEmpty(roleName)){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001" , "参数异常")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        //验证角色名
        RoleBO roleBO = adminService.getRoleById(roleId);
        if(!roleBO.getRoleName().equals(roleName)){
            // 不一致查询是否重复
            Integer nameCount = adminService.selectCountByRoleName(roleName);
            if(nameCount>0){
                String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000092" , "该角色已存在")) ;
                super.safeJsonPrint(response , result);
                return ;
            }
        }
        //修改角色姓名信息
        adminService.updateRoleNameByRoleId(roleId,roleName);

        //修改角色的权限
        adminService.updRoleToMenu(roleBO,menuIds);
        adminService.updRoleToHotel(roleBO,hotelIds);

        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success("修改成功")) ;
        super.safeJsonPrint(response, result);
    }

    *//**
         * 角色下拉框
         * 查询所有角色
         *//*
    @RequestMapping("/getRoleList")
    public void getRoleList(String status, HttpServletRequest request, HttpServletResponse response){
        List<RoleBO> roleList = adminService.getRoleList(status);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success(roleList)) ;
        super.safeJsonPrint(response, result);
    }

    *//**
         * 删除角色信息 根据角色id
         * @param request
         * @param roleIds
         *//*
    @RequestMapping("/delRoleByIds")
    public void delRoleByIds(HttpServletRequest request, HttpServletResponse response, String roleIds){

        //验证参数
        if(StringUtils.isEmpty(String.valueOf(roleIds)) || roleIds.length()<=2){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001" , "参数异常")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        //判断该角色下是否用户
        Integer id = adminService.checkRoleUser(roleIds);
        if(id!=null){
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001",id+"该角色下存在用户" )) ;
            super.safeJsonPrint(response , result);
            return;
        }
        //删除成功
        Integer[] roleIdArr=JsonUtils.getIntegerArray4Json(roleIds);
        adminService.delRoleById(roleIdArr);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success("删除成功")) ;
        super.safeJsonPrint(response, result);

    }


    //退出登录
    @RequestMapping("/exitLogin")
    public void exit(HttpServletResponse response, HttpServletRequest request){
        //退出登录
        String clientLoginID = super.getClientLoginID(request);
        if (StringUtils.isEmpty(clientLoginID)) {
            String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.failure("0000001" , "没有获取到clientLoginID！")) ;
            super.safeJsonPrint(response , result);
            return ;
        }
        String key = super.createKey(clientLoginID, SysConstants.CURRENT_LOGIN_USER);
        //从redis中删除用户信息
        RedissonHandler.getInstance().delete(key);
        //删除cookie
        super.removeCookie(request,response,key);
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success("" )) ;
        super.safeJsonPrint(response , result);
        return ;
    }

    //查询所有权限菜单
    @RequestMapping("getPermissionsMenu")
    public void getPermissionsMenu(HttpServletResponse response, HttpServletRequest request){

        //查询所有权限
        List<MenuBO> menuBOList=adminService.getMenuList();
        String result = JsonUtils.getJsonString4JavaPOJO(ResultDTOBuilder.success(menuBOList)) ;
        super.safeJsonPrint(response, result);*/
        return null;
    }


}
