package string.utils;

/**
 * Created by Administrator on 2018/8/2.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BlankUtil {
    public BlankUtil() {
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() <= 0;
    }

    public static boolean isBlank(Character cha) {
        return cha == null || cha.equals(Character.valueOf(' '));
    }

    public static boolean isBlank(Object obj) {
        return obj == null;
    }

    public static boolean isBlank(Object[] objs) {
        return objs == null || objs.length <= 0;
    }

    public static boolean isBlank(Collection<?> obj) {
        return obj == null || obj.size() <= 0;
    }

    public static boolean isBlank(Set<?> obj) {
        return obj == null || obj.size() <= 0;
    }

    public static boolean isBlank(Serializable obj) {
        return obj == null;
    }

    public static boolean isBlank(Map<Object, Object> obj) {
        return obj == null || obj.size() <= 0;
    }

    public static final String toDBFilter(String str) {
        return trimString(str).replaceAll("\\\'", "\'\'");
    }

    public static String trimString(Object obj) {
        return obj == null?"":String.valueOf(obj).trim();
    }
}
