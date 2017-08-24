package com.zhiliao.common.utils;

import com.google.code.kaptcha.Constants;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Description:util
 *
 * @author Jin
 * @create 2017-04-06
 **/
public class ControllerUtil {

    private ControllerUtil() {
        throw new Error("工具类不能实例化！");
    }

    /**
     * 判断是否为Ajav请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request){

        if (!"XMLHttpRequest" .equalsIgnoreCase(request.getHeader("X-Requested-With")))
            return false;
        return true;
    }

    /**
     * 判断验证码是否正确
     * @param verifyCode
     * @param request
     * @return
     */
    public static  boolean validate(String verifyCode,HttpServletRequest request) {
        String text = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (StrUtil.isBlank(text))
            return false;
        return verifyCode.equals(text);
    }


    /**
     * post请求
     * @return
     */
    public static  boolean isPost() {
        HttpServletRequest request = getHttpServletRequest();
        String requersMethod = request.getMethod();
        if (requersMethod.equals("POST") || "POST".equals(requersMethod)) {
            return true;
        }
        return false;
    }

    /**
     * get请求
     *
     * @return
     */
    public static  boolean isGet() {
        HttpServletRequest request = getHttpServletRequest();
        String requersMethod = request.getMethod();
        if (requersMethod.equals("GET") || "GET".equals(requersMethod)) {
            return true;
        }
        return false;
    }

    /**
     * 获取请求域名，域名不包括http请求协议头
     *
     * @return 返回域名地址
     *
     */
    public static  String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        String path =  request.getContextPath();
        String domain = request.getServerName();
        if (request.getServerPort() == 80) {
            domain += path;
        } else {
            domain += ":" + request.getServerPort() + path;
        }
        return domain;
    }

    /**
     * 读取服务器主机ip信息
     *
     * @return 返回主机IP，异常将会获取不到ip
     */
    public static  String getHostIp() {
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            return addr.getHostAddress().toString();// 获得本机IP
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *
     * 获取请求客户端ip
     *
     * @param request
     *
     * @return ip地址
     *
     */
    public static  String getRemoteAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void renderErrorHtml(String msg, String desc, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            StringBuffer content = new StringBuffer();
            content.append("<!DOCTYPE html>");
            content.append("<html>");
            content.append("<head>");
            content.append("<meta charset=\"UTF-8\">");
            content.append("<title>"+msg+"</title>");
            content.append("</head>");
            content.append("<body>");
            content.append("<p style=\"font-size:22px;padding-left:10px;\"><b>"+msg+"</b></p>");
            content.append("<p style=\"font-size:18px;padding-left:10px;\">描述 : " + desc + "</p>");
            content.append("<p style=\"font-size:18px;padding-left:10px;\">地址 : " +request.getRequestURL()+"</p>");
            content.append("</body>");
            content.append("</html>");
            PrintWriter out = response.getWriter();
            out.println(content.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void renderErrorJson(String msg, HttpServletResponse response) {
        response.setHeader("Content-type", "application/x-json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.println(JsonUtil.toERROR(msg));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void renderTimeoutJson(String msg, HttpServletResponse response){
        response.setHeader("Content-type", "application/x-json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.println(JsonUtil.toTIMEOUT(msg));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HttpServletRequest getHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static HttpServletResponse getHttpServletResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        return response;
    }


    public static HttpSession getHttpSession(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request.getSession();
    }

    public static boolean isMobile() {
        HttpServletRequest request = getHttpServletRequest();
        boolean b = false;
        boolean pcFlag = false;
        boolean mobileFlag = false;
        String via = request.getHeader("Via");
        String userAgent = request.getHeader("user-agent");
        for (int i = 0; via != null && !via.trim().equals("") && i < mobileGateWayHeaders.length; i++) {
            if (via.contains(mobileGateWayHeaders[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; !mobileFlag && userAgent != null && !userAgent.trim().equals("")
                && i < mobileUserAgents.length; i++) {
            if (userAgent.contains(mobileUserAgents[i])) {
                mobileFlag = true;
                break;
            }
        }
        for (int i = 0; userAgent != null && !userAgent.trim().equals("") && i < pcHeaders.length; i++) {
            if (userAgent.contains(pcHeaders[i])) {
                pcFlag = true;
                break;
            }
        }
        if (mobileFlag == true && pcFlag == false) {
            b = true;
        }
        return b;

    }

    private static String mobileGateWayHeaders[] = new String[] { "ZXWAP", // 中兴提供的wap网关的via信息，例如：Via=ZXWAP

            // GateWayZTE

            // Technologies，

            "chinamobile.com", // 中国移动的诺基亚wap网关，例如：Via=WTP/1.1

            // GDSZ-PB-GW003-WAP07.gd.chinamobile.com (Nokia

            // WAP Gateway 4.1 CD1/ECD13_D/4.1.04)

            "monternet.com", // 移动梦网的网关，例如：Via=WTP/1.1

            // BJBJ-PS-WAP1-GW08.bj1.monternet.com. (Nokia WAP

            // Gateway 4.1 CD1/ECD13_E/4.1.05)

            "infoX", // 华为提供的wap网关，例如：Via=HTTP/1.1 GDGZ-PS-GW011-WAP2
            // (infoX-WISG

            // Huawei Technologies)，或Via=infoX WAP Gateway V300R001

            // Huawei Technologies

            "XMS 724Solutions HTG", // 国外电信运营商的wap网关，不知道是哪一家

            "wap.lizongbo.com", // 自己测试时模拟的头信息

            "Bytemobile",// 貌似是一个给移动互联网提供解决方案提高网络运行效率的，例如：Via=1.1 Bytemobile OSN

            // WebProxy/5.1

    };

    private static String[] pcHeaders = new String[] { "Windows 98", "Windows ME", "Windows 2000", "Windows XP",
            "Windows NT", "Ubuntu" };

    private  static String[] mobileUserAgents = new String[] { "Nokia", // 诺基亚，有山寨机也写这个的，总还算是手机，Mozilla/5.0

            // (Nokia5800

            // XpressMusic)UC

            // AppleWebkit(like

            // Gecko)

            // Safari/530

            "SAMSUNG", // 三星手机

            // SAMSUNG-GT-B7722/1.0+SHP/VPP/R5+Dolfin/1.5+Nextreaming+SMM-MMS/1.2.0+profile/MIDP-2.1+configuration/CLDC-1.1

            "MIDP-2", // j2me2.0，Mozilla/5.0 (SymbianOS/9.3; U; Series60/3.2

            // NokiaE75-1 /110.48.125 Profile/MIDP-2.1

            // Configuration/CLDC-1.1 ) AppleWebKit/413 (KHTML like

            // Gecko) Safari/413

            "CLDC1.1", // M600/MIDP2.0/CLDC1.1/Screen-240X320

            "SymbianOS", // 塞班系统的，

            "MAUI", // MTK山寨机默认ua

            "UNTRUSTED/1.0", // 疑似山寨机的ua，基本可以确定还是手机

            "Windows CE", // Windows CE，Mozilla/4.0 (compatible; MSIE 6.0;

            // Windows CE; IEMobile 7.11)

            "iPhone", // iPhone是否也转wap？不管它，先区分出来再说。Mozilla/5.0 (iPhone; U; CPU

            // iPhone OS 4_1 like Mac OS X; zh-cn) AppleWebKit/532.9

            // (KHTML like Gecko) Mobile/8B117

            "iPad", // iPad的ua，Mozilla/5.0 (iPad; U; CPU OS 3_2 like Mac OS X;

            // zh-cn) AppleWebKit/531.21.10 (KHTML like Gecko)

            // Version/4.0.4 Mobile/7B367 Safari/531.21.10

            "Android", // Android是否也转wap？Mozilla/5.0 (Linux; U; Android

            // 2.1-update1; zh-cn; XT800 Build/TITA_M2_16.22.7)

            // AppleWebKit/530.17 (KHTML like Gecko) Version/4.0

            // Mobile Safari/530.17

            "BlackBerry", // BlackBerry8310/2.7.0.106-4.5.0.182

            "UCWEB", // ucweb是否只给wap页面？ Nokia5800

            // XpressMusic/UCWEB7.5.0.66/50/999

            "ucweb", // 小写的ucweb貌似是uc的代理服务器Mozilla/6.0 (compatible; MSIE 6.0;)

            // Opera ucweb-squid

            "BREW", // 很奇怪的ua，例如：REW-Applet/0x20068888 (BREW/3.1.5.20; DeviceId:

            // 40105; Lang: zhcn) ucweb-squid

            "J2ME", // 很奇怪的ua，只有J2ME四个字母

            "YULONG", // 宇龙手机，YULONG-CoolpadN68/10.14 IPANEL/2.0 CTC/1.0

            "YuLong", // 还是宇龙

            "COOLPAD", // 宇龙酷派YL-COOLPADS100/08.10.S100 POLARIS/2.9 CTC/1.0

            "TIANYU", // 天语手机TIANYU-KTOUCH/V209/MIDP2.0/CLDC1.1/Screen-240X320

            "TY-", // 天语，TY-F6229/701116_6215_V0230 JUPITOR/2.2 CTC/1.0

            "K-Touch", // 还是天语K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223

            // Release/30.07.2008 Browser/WAP2.0

            "Haier", // 海尔手机，Haier-HG-M217_CMCC/3.0 Release/12.1.2007

            // Browser/WAP2.0

            "DOPOD", // 多普达手机

            "Lenovo", // 联想手机，Lenovo-P650WG/S100 LMP/LML Release/2010.02.22

            // Profile/MIDP2.0 Configuration/CLDC1.1

            "LENOVO", // 联想手机，比如：LENOVO-P780/176A

            "HUAQIN", // 华勤手机

            "AIGO-", // 爱国者居然也出过手机，AIGO-800C/2.04 TMSS-BROWSER/1.0.0 CTC/1.0

            "CTC/1.0", // 含义不明

            "CTC/2.0", // 含义不明

            "CMCC", // 移动定制手机，K-Touch_N2200_CMCC/TBG110022_1223_V0801 MTK/6223

            // Release/30.07.2008 Browser/WAP2.0

            "DAXIAN", // 大显手机DAXIAN X180 UP.Browser/6.2.3.2(GUI) MMP/2.0

            "MOT-", // 摩托罗拉，MOT-MOTOROKRE6/1.0 LinuxOS/2.4.20 Release/8.4.2006

            // Browser/Opera8.00 Profile/MIDP2.0 Configuration/CLDC1.1

            // Software/R533_G_11.10.54R

            "SonyEricsson", // 索爱手机，SonyEricssonP990i/R100 Mozilla/4.0

            // (compatible; MSIE 6.0; Symbian OS; 405) Opera

            // 8.65 [zh-CN]

            "GIONEE", // 金立手机

            "HTC", // HTC手机

            "ZTE", // 中兴手机，ZTE-A211/P109A2V1.0.0/WAP2.0 Profile

            "HUAWEI", // 华为手机，

            "webOS", // palm手机，Mozilla/5.0 (webOS/1.4.5; U; zh-CN)

            // AppleWebKit/532.2 (KHTML like Gecko) Version/1.0

            // Safari/532.2 Pre/1.0

            "GoBrowser", // 3g GoBrowser.User-Agent=Nokia5230/GoBrowser/2.0.290

            // Safari

            "IEMobile", // Windows CE手机自带浏览器，

            "WAP2.0"// 支持wap 2.0的

    };
}
