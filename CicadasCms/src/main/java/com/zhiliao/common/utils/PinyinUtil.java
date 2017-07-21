package com.zhiliao.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;
 

public class PinyinUtil{
     
    /**
     * 转为大写字母, 如：中国人民银行 =====>ZHONGGUORENMINYINHAN
     * @author lance
     * 2016年1月16日 下午4:56:07
     */
    public static String convertUpper(String text){
        return convert(text, HanyuPinyinCaseType.UPPERCASE, false);
    }
     
    /**
     * 转为小写字母, 如：中国人民银行 =====>zhongguorenminyinhang
     * @author lance
     * 2016年1月16日 下午4:56:07
     */
    public static String convertLower(String text){
        return convert(text, HanyuPinyinCaseType.LOWERCASE, false);
    }
     
    /**
     * 首字母大写, 如：中国人民银行 =====>ZhongGuoRenMinYinHang
     * @author lance
     * 2016年1月16日 下午5:04:11
     */
    public static String converCapitalize(String text){
        return convert(text, null, true);
    }
     
    /**
     * 所有中文的第一个字母大写, 如：中国人民银行 =====>ZGRMYH
     * @author lance
     * 2016年1月17日 下午10:16:19
     */
    public static String capitalizeLetter(String text){
        String c = converCapitalize(text);
        if(StringUtils.isBlank(c)) {
            return "";
        }
         
        return StringUtils.replacePattern(c, "[a-z]", "");
    }
     
    /**
     * 获取首字母, 如：中国人民银行 =====>Z
     * @author lance
     * 2016年1月17日 下午10:11:57
     */
    public static String firstLetter(String text){
        String c = converCapitalize(text);
        if(StringUtils.isBlank(c)) {
            return "";
        }
         
        return StringUtils.substring(c, 0, 1);
    }
     
    /**
     * 转为拼音
     * @param text          待转化的中文字符
     * @param caseType      转化类型, 即大写小写
     * @param isCapitalize  是否首字母大写
     * @author lance
     * 2016年1月17日 下午10:28:05
     */
    public static String convert(String text, HanyuPinyinCaseType caseType, boolean isCapitalize) {
        if(StringUtils.isBlank(text)){
            return "";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        if(caseType != null) {
            format.setCaseType(caseType);
            isCapitalize = false;
        }
         
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = StringUtils.trimToEmpty(text).toCharArray();
        StringBuilder builder = new StringBuilder();
        try {
            for (char c: input) {
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if(isCapitalize) {
                        builder.append(StringUtils.capitalize(temp[0]));
                    }else {
                        builder.append(temp[0]);
                    }
                } else {
                    if(isCapitalize) {
                        builder.append(StringUtils.capitalize(Character.toString(c)));
                    }else {
                         builder.append(Character.toString(c));
                    }
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination ex) {
            ex.printStackTrace();
        }
 
        return builder.toString();
    }

    public static  void main(String[] args){
      System.out.print( convertLower("哈哈"));
    }
}