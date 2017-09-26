package com.zhiliao.module.web.cms;

import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.base.BaseController;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.common.utils.UserUtil;
import com.zhiliao.module.web.cms.service.ModelService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TCmsModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * Description:模型控制器
 *
 * @author Jin
 * @create 2017-05-12
 **/
@Controller
@RequestMapping("/system/cms/model")
public class ModelController extends BaseController<TCmsModel> {

    @Autowired
    private ModelService modelService;

    @RequiresPermissions({"model:admin"})
    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsModel pojo,
                        Model model) {
        UserVo userVo = UserUtil.getSysUserVo();
        pojo.setSiteId(userVo.getSiteId());
        model.addAttribute("model",modelService.page(pageNumber,pageSize,pojo));
        model.addAttribute("pojo",pojo);
        return "cms/model_list";
    }

    @SysLog("模型添加")
    @RequiresPermissions({"model:input"})
    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "id",required = false) Integer Id, Model model) {
        if(Id!=null)
            model.addAttribute("pojo",modelService.findById(Id));
        return "cms/model_input";
    }

    @RequiresPermissions({"model:save"})
    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TCmsModel pojo) throws SQLException {
        UserVo userVo = UserUtil.getSysUserVo();
        pojo.setSiteId(userVo.getSiteId());
        if(pojo.getModelId()!=null)
           return modelService.update(pojo);
        return modelService.save(pojo);
    }

    @SysLog("模型删除")
    @RequiresPermissions({"model:delete"})
    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) {
        return modelService.delete(ids);
    }

    @RequestMapping("/checkModelName")
    @ResponseBody
    public String checkModelName(@RequestParam("modelName") String modelName) {
        if(modelService.findModelByModelName(PinyinUtil.convertLower(modelName))!=null)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }

    @RequestMapping("/checkTableName")
    @ResponseBody
    public String checkTableName(@RequestParam("tableName") String tableName) {
        if(modelService.findModelByTableName(PinyinUtil.convertLower(tableName))!=null)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }
}
