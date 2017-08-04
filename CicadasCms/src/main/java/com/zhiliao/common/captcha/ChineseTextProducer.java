package com.zhiliao.common.captcha;

import com.google.code.kaptcha.text.TextProducer;
import java.util.Random;

public class ChineseTextProducer implements TextProducer {
    private String[] simplifiedChineseTexts = new String[]{
            "渔父引 ", "闲中好" ,"纥那曲" ,"拜新月" ,"梧桐影","罗贡曲" ,"醉妆词" ,
            "庆先和", "南歌子" ,"占春芳" ,"荷叶杯" ,"回波集" ,"舞马词" ,"柘枝词 " ,
            "晴偏好 " , "凭栏人" ,"花非花" ,"摘得新" ,"梧叶儿" ,"唉乃曲" ,"采莲子" ,
            "浪淘沙" , "杨柳枝" ,"八拍蛮" ,"字字变" ,"十样花" ,"天净沙" ,"甘州曲" ,
            "喜春来" , "踏歌词" , "秋风清" , "抛球乐" , "一叶秋" ,"忆王孙" ,"金字经" ,
            "古调笑" , "遐方怨","西溪子" ,"天仙子 " ,"风流子 " , "归字谣 " ,"饮马歌 " ,
            "相见欢 " ,"定西番 " , "江城子 " , "望江怨", "平易近人", "宽宏大度", "冰清玉洁",
            "足智多谋", "融会贯通", "举一反三", "相貌堂堂", "神采奕奕", "悠然自得", "欣喜若狂", "呆若木鸡",
            "望梅止渴", "铁杵成针", "欢呼雀跃", "血浓于水", "温故知新", "画龙点睛", "滥竽充数", "守株待兔",
            "无懈可击", "春意盎然", "冰天雪地", "热火朝天", "巧夺天工", "高耸入云", "水天一色", "波光粼粼",
            "湖光山色", "大雨如注", "百依百顺", "道听途说", "南辕北辙", "左推右挡" };

    public ChineseTextProducer() {
    }

    public String getText() {
        return this.simplifiedChineseTexts[(new Random()).nextInt(this.simplifiedChineseTexts.length)];
    }
}
