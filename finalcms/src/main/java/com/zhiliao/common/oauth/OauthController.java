//package com.zhiliao.common.oauth;
//
//import com.alibaba.fastjson.JSONObject;
//import com.zhiliao.common.oauth.oauth.OauthQQ;
//import com.zhiliao.common.oauth.oauth.OauthSina;
//import com.zhiliao.common.oauth.util.TokenUtil;
//import com.zhiliao.common.utils.StrUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.RedirectView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import java.util.Map;
//
//@Controller
//public class OauthController {
//
//    public static final Logger log = LoggerFactory.getLogger(OauthController.class);
//
//    @Autowired
//    private OauthQQ oauthQQ;
//    @Autowired
//    private OauthSina oauthSina;
////    @Autowired
////    private UserService userService;
////    @Autowired
////    private OAuthService oAuthService;
//
//    @RequestMapping("/api/{oauthType}/login")
//    public RedirectView oauth(@PathVariable String oauthType){
//        RedirectView redirectView = new RedirectView();
//        String state = TokenUtil.randomState();
//        if(oauthType.equals("qq")) {
//            redirectView.setUrl(oauthQQ.getAuthorizeUrl(state));
//        }
//        if(oauthType.equals("sina")) {
//            redirectView.setUrl(oauthSina.getAuthorizeUrl(state));
//        }
//        return redirectView;
//    }
//
//    @RequestMapping("/api/{oauthType}/callback")
//    public ModelAndView callback(@PathVariable String oauthType, @RequestParam("code") String code,
//                                 @RequestParam("state") String state,
//                                 Model model, HttpSession session) {
//        ModelAndView view = new ModelAndView();
//        String session_state=null;
//        String openid = null;
//        String nickname =null;
//        String photoUrl =null;
//
//        if(oauthType.equals("qq")) {
//            session.setAttribute(OauthQQ.SESSION_STATE, state);
////            session_state = ShiroUtil.getSessionStr(OauthQQ.SESSION_STATE);
//        }
//        if(oauthType.equals("sina")) {
//            session.setAttribute(OauthSina.SESSION_STATE, state);
////            session_state = ShiroUtil.getSessionStr(OauthSina.SESSION_STATE);
//        }
//        if (StrUtil.isBlank(state) || StrUtil.isBlank(session_state) || !state.equals(session_state) || StrUtil.isBlank(code)) {
//            view.setViewName("redirect:/login");
//            return view;
//        }
//
//
//            if(oauthType.equals("qq")) {
//                session.removeAttribute(OauthQQ.SESSION_STATE);
//                JSONObject userInfo = oauthQQ.getUserInfoByCode(code);
//                openid = userInfo.getString("openid");
//                nickname = userInfo.getString("nickname");
//                photoUrl = userInfo.getString("figureurl_2");
//            }
//            if(oauthType.equals("sina")) {
//                session.removeAttribute(OauthSina.SESSION_STATE);
//                JSONObject userInfo = oauthSina.getUserInfoByCode(code);
//                openid = userInfo.getString("id");
//                nickname = userInfo.getString("screen_name");
//                photoUrl = userInfo.getString("profile_image_url");
//            }
//            TUser  user = userService.findUserByOpenId(openid);
//            if (user!=null){
//               Map<String,Object> result = userService.login(user.getEmail(),user.getPassword(),false, DefaultUsernamePasswordToken.OAUTH,getRemoteAddress(request));
//                if ((Boolean) result.get("success")){
//                    view.setViewName("redirect:/");
//                }else{
//                    view.addObject("info","第三方账号登陆失败，请使用账号密码登陆！");
//                    view.setViewName("forward:/login");
//                }
//            }else {
//                log.info("第三方账号登陆失败,开始绑定本站账号！");
//                model.addAttribute("openid", openid);
//                model.addAttribute("nickname", nickname);
//                model.addAttribute("avatar", photoUrl);
//                view.setViewName("oauth-bind");
//                view.addObject("oathType",oauthType);
//            }
//            return view;
//
//        view.addObject("info","第三方账号登陆失败，请使用本站账号登陆！");
//        view.setViewName("forward:/login");
//        return view;
//    }
//
//    @RequestMapping("/api/{oauthType}/toRegisterBind")
//    public ModelAndView toRegisterBind(
//            @PathVariable String oauthType,
//            HttpServletRequest request,@ModelAttribute TUser user,
//            @RequestParam("openId") String openId){
//        ModelAndView view = new ModelAndView();
//        if (userService.findUserByEmail(user.getEmail())==null&&userService.save(user)>0){
//            TOauth oauth = new TOauth();
//            oauth.setAvatar(user.getAvatar());
//            oauth.setUserId(user.getUserId());
//            oauth.setOpenid(openId);
//            oauth.setNickname(user.getNickname());
//            if(oauthType.equals("qq")){oauth.setOauthType(OauthQQ.OAUTH_TYPE);}
//            if(oauthType.equals("sina")){oauth.setOauthType(OauthSina.OAUTH_TYPE);}
//            oAuthService.save(oauth);
//            Map<String,Object> result = userService.login(user.getEmail(),user.getPassword(),false,DefaultUsernamePasswordToken.OAUTH,getRemoteAddress(request));
//            if ((Boolean) result.get("success")){
//                view.setViewName("redirect:/");
//            }
//            return view;
//        }
//        view.setViewName("redirect:/api/"+oauthType+"/login");
//        return view;
//    }
//
//    @RequestMapping("/api/{oauthType}/toLoginBind")
//    public ModelAndView toLoginBind(@PathVariable String oauthType,
//                                    @RequestParam("openid") String openId,
//                                    @RequestParam("email") String email,
//                                    @RequestParam("nickname") String nickname,
//                                    @RequestParam("password") String password,
//                                    @RequestParam("avatar") String avatar,
//                                    HttpServletRequest request) {
//        ModelAndView view = new ModelAndView();
//        Map result=userService.login(email,password,false,DefaultUsernamePasswordToken.USER,getRemoteAddress(request));
//        if ((Boolean) result.get("success")){
//            TUser user = userService.findUserByEmail(email);
//            log.info("userId:"+user.getUserId());
//            if (user.getAvatar()==null) {
//                user.setAvatar(avatar);
//            }
//            if (user.getNickname()==null) {
//                user.setNickname(nickname);
//            }
//            userService.update(user);
//            TOauth oauth = new TOauth();
//            oauth.setAvatar(avatar);
//            oauth.setNickname(nickname);
//            oauth.setOpenid(openId);
//            oauth.setUserId(user.getUserId());
//            if(oauthType.equals("qq")){oauth.setOauthType(OauthQQ.OAUTH_TYPE);}
//            if(oauthType.equals("sina")){oauth.setOauthType(OauthSina.OAUTH_TYPE);}
//            oAuthService.save(oauth);
//            view.setViewName("redirect:/");
//            return view;
//        }
//        view.setViewName("redirect:/api/"+oauthType+"/login");
//        return view;
//    }
//
//}
