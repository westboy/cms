package com.zhiliao.module.web.cms;

import com.google.common.collect.Maps;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.*;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.ModelFiledService;
import com.zhiliao.module.web.cms.service.ModelService;
import com.zhiliao.module.web.cms.vo.TCmsContentVo;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsContent;
import com.zhiliao.mybatis.model.TCmsModel;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:内容管理控制器
 *
 * @author Jin
 * @create 2017-04-17
 **/
@Controller
@RequestMapping("/system/cms/content")
public class ContentController{

    @Autowired
    private ContentService contentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelFiledService modelFiledService;

    @RequestMapping
    public String index() {
        return "cms/content";
    }


    @RequiresPermissions("content:admin")
    @RequestMapping("/page")
    public String page(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                       TCmsContentVo content,
                        Model model){
        Subject currentUser = SecurityUtils.getSubject();
        TCmsCategory category =categoryService.findById(content.getCategoryId());
        if(!CmsUtil.isNullOrEmpty(content.getCategoryId())&&!StrUtil.isBlank(category.getPermissionKey())&&!currentUser.isPermitted(category.getPermissionKey()))
            throw new CmsException("对不起,您没有当前栏目的管理权限！");
        UserVo userVo = ((UserVo)ControllerUtil.getHttpSession().getAttribute(CmsConst.SITE_USER_SESSION_KEY));
        if(CmsUtil.isNullOrEmpty(userVo))
            throw  new UnauthenticatedException();
        content.setSiteId(userVo.getSiteId());
        content.setUserId(userVo.getUserId());
        model.addAttribute("model",contentService.page(pageNumber,pageSize,content));
        model.addAttribute("pojo",content);
         return "cms/content_list";
    }

    @SysLog("内容添加")
    @RequiresPermissions("content:input")
    @RequestMapping("/input")
    public String input(@RequestParam(value = "categoryId",required = false) Long categoryId,
                        @RequestParam(value = "contentId",required = false) Long contentId,
                        @RequestParam(value = "isWindow",defaultValue = "NO") String isWindow,
                        Model model) {
        TCmsCategory category =categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category))
            throw new CmsException("当前栏目已被删除！");
        Subject currentUser = SecurityUtils.getSubject();
        if(!StrUtil.isBlank(category.getPermissionKey())&&!currentUser.isPermitted(category.getPermissionKey()))
            throw new CmsException("对不起,您没有当前栏目的管理权限！");
        TCmsModel cmsModel = modelService.findById(category.getModelId());
        List<TCmsModelFiled> cmsModelFileds = modelFiledService.findModelFiledListByModelId(cmsModel.getModelId());
        if(contentId!=null)
            model.addAttribute("content",contentService.findContentByContentIdAndTableName(contentId,cmsModel.getTableName()));
        model.addAttribute("modelFiled",cmsModelFileds);
        model.addAttribute("category",category);
        model.addAttribute("isWindow",isWindow);
        return "cms/content_input";
    }


    @RequestMapping("/excel")
    public ModelAndView excel(){
        ExcelUtil.exports2007("123",contentService.page(1,20,new TCmsContentVo()).getList());
        return null;
    }

    @RequiresPermissions("content:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsContent content, HttpServletRequest request,
                       @RequestParam(value = "tag",required = false) String[] tags
                      ) throws SQLException {
        TCmsCategory category =categoryService.findById(content.getCategoryId());
        TCmsModel cmsModel = modelService.findById(category.getModelId());
        List<TCmsModelFiled> cmsModelFileds = modelFiledService.findModelFiledListByModelId(category.getModelId());
        UserVo userVo = UserUtil.getSysUserVo();
        content.setSiteId(userVo.getSiteId());
        content.setUserId(userVo.getUserId());
        content.setInputdate(new Date());
        content.setModelId(category.getModelId());
        /*Jin：使用Map接收：遍历获取自定义模型字段*/
        Map<String, Object> formParam =Maps.newHashMap();
        for (TCmsModelFiled filed : cmsModelFileds) {
            if(filed.getFiledClass().equals("checkbox")||filed.getFiledClass().equals("image")) {
                String[] filedValue = request.getParameterValues(filed.getFiledName());
                if (filedValue!=null) {
                    formParam.put(filed.getFiledName(), filedValue);
                }
            }else {
                String filedValue = request.getParameter(filed.getFiledName());
                if (!StrUtil.isBlank(filedValue)) {
                    formParam.put(filed.getFiledName(), filedValue);
                }
            }
        }
        if(content.getContentId()!=null)
            return contentService.update(content,cmsModel.getTableName(),cmsModelFileds,formParam,tags);
        return contentService.save(content,cmsModel.getTableName(),formParam,tags);
    }

    @SysLog("内容删除")
    @RequiresPermissions("content:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false) Long[] ids) {
        return contentService.delete(ids);
    }


    @RequestMapping("/recovery")
    @ResponseBody
    public String recovery(@RequestParam(value = "ids",required = false) Long[] ids) {
        return contentService.recovery(ids);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
