package com.bsg.dbscale.util;

import java.math.BigDecimal;

public class NumberUnits {

    /**
     * 四舍五入
     * 
     * @param dValue
     *            待转换数值
     * @param digits
     *            保留小数位数
     * @return
     */
    public static double retainDigits(Double dValue, int digits) {
        if (dValue == null) {
            return 0;
        }
        BigDecimal bg = new BigDecimal(dValue).setScale(digits, BigDecimal.ROUND_HALF_UP);
        return bg.doubleValue();
    }

    public static double retainDigits(Double dValue) {
        return retainDigits(dValue, 1);
    }

    public static int ceil(double dValue) {
        return new Double(Math.ceil(dValue)).intValue();
    }

    public static boolean isInt(double dValue) {
        return (dValue % 1) == 0;
    }

    public static double toDouble(Object value) {
        if (value == null) {
            throw new ClassCastException("null");
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof Integer) {
            return Double.valueOf(((Integer) value).toString());
        } else {
            throw new ClassCastException(value.getClass().getName());
        }
    }

    public static Long toLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            throw new ClassCastException(value.getClass().getName());
        }
    }
}
