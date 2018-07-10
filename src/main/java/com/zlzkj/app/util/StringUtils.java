package com.zlzkj.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class description
 *
 * @author wxf
 * @version 1.0.0, 18/05/8
 */
public class StringUtils {

    public static void main(String[] args) {
        List<Map> maps = new ArrayList<Map>() {{
            add(new HashMap() {{
                put("url", "/1/pretemp.jpg");
                put("suffix", "jpg");
                put("name", "附件");
            }});
            add(new HashMap() {{
                put("url", "/1/1533323434213.mp4");
                put("suffix", "mp4");
                put("name", "里斯");
            }});
        }};
        //System.out.println(JSONArray.toJSONString(maps));
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }


    //num 被补0的数 补完后的长度 len
    public static String lpad(int num, int len) {
        String str = String.valueOf(num);
        int length = len - str.length();
        for (int i = 0; i < length; i++) {
            str = 0 + str;
        }
        return str;
    }

    public static String delLastChar(String str) {
        return delLast(str, 1);
    }

    public static String delLastChar(String str, String char_) {
        return str.substring(0, str.lastIndexOf(char_));
    }

    public static String delLast(String str, int len) {
        if (str == null || str.equals(""))
            return "";
        return str.substring(0, str.length() - len);
    }

    public static String parentheses(String str) {
        return "(" + str + ")";
    }

    public static String trim(String str) {
        return str.replaceAll("\\s*", "");
    }

    public String join(String[] strs) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strs.length == 0) {
            return "";
        }
        stringBuilder.append(strs[0]);
        for (int i = 1; i < strs.length; i++) {
            stringBuilder.append(strs[i]);
        }
        return stringBuilder.toString();

    }

    public String join(List<String> strs) {
        StringBuilder stringBuilder = new StringBuilder();
        if (strs.size() == 0) {
            return "";
        }
        stringBuilder.append(strs.get(0));
        for (int i = 1; i < strs.size(); i++) {
            stringBuilder.append(strs.get(i));
        }
        return stringBuilder.toString();

    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;

    }
}
