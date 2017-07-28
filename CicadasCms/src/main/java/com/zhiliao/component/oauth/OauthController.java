package com.zhiliao.component.oauth;

import com.alibaba.fastjson.JSONObject;
import com.zhiliao.component.oauth.oauth.OauthQQ;
import com.zhiliao.component.oauth.oauth.OauthSina;
import com.zhiliao.component.oauth.util.TokenUtil;
import com.zhiliao.common.utils.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class OauthController {

    public static final Logger log = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private OauthQQ oauthQQ;
    @Autowired
    private OauthSina oauthSina;


    @RequestMapping("/api/{oauthType}/login")
    public RedirectView oauth(@PathVariable String oauthType,HttpSession session){
        RedirectView redirectView = new RedirectView();
        String state = TokenUtil.randomState();
        if(oauthType.equals("qq")) {
            session.setAttribute(OauthQQ.SESSION_STATE, state);
            redirectView.setUrl(oauthQQ.getAuthorizeUrl(state));
        }
        if(oauthType.equals("sina")) {
            session.setAttribute(OauthSina.SESSION_STATE, state);
            redirectView.setUrl(oauthSina.getAuthorizeUrl(state));
        }
        return redirectView;
    }

    @RequestMapping("/api/{oauthType}/callback")
    @ResponseBody
    public String callback(@PathVariable String oauthType, @RequestParam("code") String code,
                                 @RequestParam("state") String state,
                                 Model model, HttpSession session) {
        String session_state="",openid = "", nickname ="", photoUrl ="";
        if(oauthType.equals("qq"))
            session_state = (String) session.getAttribute(OauthQQ.SESSION_STATE);
        if(oauthType.equals("sina"))
            session_state = (String) session.getAttribute(OauthSina.SESSION_STATE);
        if (StrUtil.isBlank(state) || StrUtil.isBlank(session_state) || !state.equals(session_state) || StrUtil.isBlank(code))
            return "登陆失败";
        if(oauthType.equals("qq")) {
            session.removeAttribute(OauthQQ.SESSION_STATE);
            JSONObject userInfo = oauthQQ.getUserInfoByCode(code);
            log.debug(userInfo.toJSONString());
            openid = userInfo.getString("openid");
            nickname = userInfo.getString("nickname");
            photoUrl = userInfo.getString("figureurl_2");
        }
        if(oauthType.equals("sina")) {
            session.removeAttribute(OauthSina.SESSION_STATE);
            JSONObject userInfo = oauthSina.getUserInfoByCode(code);
            log.debug(userInfo.toJSONString());
            openid = userInfo.getString("id");
            nickname = userInfo.getString("screen_name");
            photoUrl = userInfo.getString("profile_image_url");
        }
        return "["+nickname+"]登陆成功";
    }

}
