package com.zhiliao.module.web.cms;

import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.common.utils.UserUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.ModelService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:内容
 *
 * @author Jin
 * @create 2017-04-18
 **/
@Controller
@RequestMapping("/system/cms/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelService modelService;

    @RequiresPermissions("category:admin")
    @RequestMapping
    public String index(){
        return "cms/category";
    }



    @SysLog("栏目添加")
    @RequiresPermissions("category:input")
    @RequestMapping("/input")
    public String input(@RequestParam(value = "id",required = false) Long Id, Model model) {
        List<TCmsModel> models = modelService.findModelListByStatus(true);
        model.addAttribute("models",models);
        if(Id!=null)
            model.addAttribute("pojo",categoryService.findById(Id));
        return "cms/category_input";
    }


    @RequiresPermissions("category:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsCategory pojo)  {
        UserVo userVo = UserUtil.getSysUserVo();
        pojo.setSiteId(userVo.getSiteId());
        if(pojo.getCategoryId()!=null)
          return   categoryService.update(pojo);
        return categoryService.save(pojo);
    }

    @SysLog("栏目删除")
    @RequiresPermissions("category:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("ids") Long[] ids) {
        return categoryService.delete(ids);
    }

    @RequestMapping("/checkCategory")
    @ResponseBody
    public String checkDomain(@RequestParam(value = "alias",required = false) String categoryName){
        if(!CmsUtil.isNullOrEmpty(categoryService.findByAlias(PinyinUtil.convertLower(HtmlKit.getText(categoryName)))))
            return "{\"error\": \"栏目已存在\"}";
        return "{\"ok\": \"验证通过\"}";

    }
}