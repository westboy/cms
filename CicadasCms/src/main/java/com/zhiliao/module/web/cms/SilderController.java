package com.zhiliao.module.web.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.SilderService;
import com.zhiliao.mybatis.model.TCmsAdSilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * Description:幻灯片广告
 *
 * @author Jin
 * @create 2017-06-12
 **/
@Controller
@RequestMapping("/system/cms/ad/silder")
public class SilderController {

    @Autowired
    private SilderService silderService;

    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                        TCmsAdSilder pojo, Model model) {
        PageInfo page = silderService.page(pageNumber,pageSize,pojo);
        model.addAttribute("model",page);
        model.addAttribute("pojo",pojo);
        return "cms/ad_silder";
    }


    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsAdSilder pojo){
       if(StrUtil.isBlank(pojo.getImg()))
           pojo.setImg(null);
        if(pojo.getId()!=null)
            return silderService.update(pojo);
        return silderService.save(pojo);
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) throws SQLException {
        return silderService.delete(ids);
    }
}
