package com.zhiliao.module.web.system;

import com.zhiliao.module.web.system.service.AttachmentService;
import com.zhiliao.mybatis.model.TSysAttachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-08
 **/
@Controller
@RequestMapping("/system/attachment")
public class AttachmentController{

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping
    public String index(@RequestParam(value = "pageCurrnet",defaultValue = "0") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50")Integer pageSize,
                        TSysAttachment pojo, Model model) {
        model.addAttribute("model",attachmentService.page(pageNumber,pageSize,pojo));
        return "system/att_list";
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false) Long[] ids) throws SQLException {
        return attachmentService.delete(ids);
    }
}
