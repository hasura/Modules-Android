package io.hasura.drive_android.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jaison on 29/03/17.
 */

public class DateManager {

    public static String getStringDate(Date date, String dateFormat) {
        DateFormat newDf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        return newDf.format(date);
    }

    public static String getFormattedExpiryDate(Date date) {
        return getStringDate(date,"MM/yyyy");
    }

    public static String getFormattedExpiryDate(String dateString) {
        return getStringDate(getDateFromString("yyyy-MM-dd",dateString),"MM/yyyy");
    }

    public static String getHasuraFormatExpiryDate(String dateString) {
        return getStringDate(getDateFromString("MM/yyyy",dateString),"yyyy-MM-dd");
    }

    public static String getFormattedModifiedData(String dateString) {
        return getStringDate(getDateFromString("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ",dateString),"dd MMM yyyy, HH:mm");
    }

    public static String getHasuraFormattedModifiedDate(Date date) {
        return getStringDate(date,"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    private static Date getDateFromString(String format, String dateString){
        SimpleDateFormat sdf1 = new SimpleDateFormat(format, Locale.getDefault());
        Date date = new Date();
        try {
            date = sdf1.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
