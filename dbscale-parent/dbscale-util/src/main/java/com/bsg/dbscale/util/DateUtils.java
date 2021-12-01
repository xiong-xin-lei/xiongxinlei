package com.bsg.dbscale.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期处理工具类
 * 
 * @author HCK
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm", "yyyy.MM", "yyyy-MM-dd'T'HH:mm:ss'Z'" };

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, parsePatterns[0]);
    }

    public static String dateTimeToString(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.format(date, parsePatterns[1]);
    }

    public static Date parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return parseDate(dateStr, parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }
}
