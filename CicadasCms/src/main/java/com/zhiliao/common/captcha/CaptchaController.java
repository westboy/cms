package com.zhiliao.common.captcha;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.zhiliao.common.utils.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@Controller
public class CaptchaController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Producer captchaProducer;


    @RequestMapping("/verify")
    public ModelAndView doGet(HttpServletResponse response, HttpSession session)  {
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY,capText);
        try {
        ServletOutputStream out = response.getOutputStream();
        log.debug("captchaProducer.createText："+capText);
        ImageIO.write(bi, "jpg", out);
        out.flush();
        out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/verify/validate")
    @ResponseBody
    public String validateVerifyCode(@RequestParam("verifyCode") String verify,HttpSession session){
        JSONObject result = new JSONObject();
        if (validate(verify,session)){
            result.put("ok","验证码输入正确！");
        }else{
            result.put("error","验证码错误,请刷新或重新输入！");
        }
    return result.toJSONString();
    }

    public static  boolean validate(String verifyCode,HttpSession session) {
        String text = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (StrUtil.isBlank(text))
            return false;
        return verifyCode.equals(text);
    }
}
