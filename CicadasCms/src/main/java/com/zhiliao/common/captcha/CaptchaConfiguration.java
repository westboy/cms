package com.zhiliao.common.captcha;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfiguration {

    @Bean
    public DefaultKaptcha getCaptchaProducer(@Value("${kcaptcha.img.width}") String imgWidth,
                                              @Value("${kcaptcha.img.hight}") String imgHight,
                                              @Value("${kcaptcha.font.size}")  String fontSize,
                                              @Value("${kcaptcha.font.color}") String fontColor,
                                              @Value("${kcaptcha.char.length}") String charLength){

        DefaultKaptcha kcaptcha = new DefaultKaptcha();
        Properties prop= new Properties();
        prop.setProperty(Constants.KAPTCHA_BORDER,"no");
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,fontColor);
        prop.setProperty(Constants.KAPTCHA_IMAGE_WIDTH,imgWidth);
        prop.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT,imgHight);
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE,fontSize);
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,charLength);
        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE,"4");
//        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES,"宋体,楷体,微软雅黑");
        prop.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL,"com.google.code.kaptcha.impl.ShadowGimpy");
        prop.setProperty(Constants.KAPTCHA_NOISE_COLOR,"green");
        prop.setProperty(Constants.KAPTCHA_NOISE_IMPL,"com.google.code.kaptcha.impl.NoNoise");
//        prop.setProperty(Constants.KAPTCHA_TEXTPRODUCER_IMPL,"com.zhiliao.common.captcha.ChineseTextProducer");
        Config cfg = new Config(prop);
        kcaptcha.setConfig(cfg);
        return kcaptcha;

    }


}
