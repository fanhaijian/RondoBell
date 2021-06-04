package org.rondobell.racailum;

import java.math.BigDecimal;

public class NumericTool {
    public static boolean isNumeric(String str) {
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isNumeric("-3232"));
        System.out.println(isNumeric("-111111.11111111111"));
        System.out.println(isNumeric("-1"));
        System.out.println(isNumeric("1"));
        System.out.println(isNumeric("0123"));
        System.out.println(isNumeric("-"));
        System.out.println(isNumeric("--"));
        System.out.println(isNumeric("."));
        System.out.println(isNumeric(".."));
        System.out.println(isNumeric("a"));
        System.out.println(isNumeric("aa"));
        System.out.println(isNumeric("-a"));
        System.out.println(isNumeric("-aa"));
        System.out.println(isNumeric("-a.aa"));












    }
}
