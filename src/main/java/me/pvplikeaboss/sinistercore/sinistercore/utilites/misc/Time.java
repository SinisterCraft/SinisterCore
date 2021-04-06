package me.pvplikeaboss.sinistercore.sinistercore.utilites.misc;

import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Time {
    public static Date getCurrentDateTime() {
        return Calendar.getInstance().getTime();
    }

    /*public static String getTimeLeftStr(Date endDate) {
        Date currentDate = getCurrentDateTime();
        if(currentDate.before(endDate)) {
            Date date = new Date(endDate.getTime() - currentDate.getTime());
            DateFormat dateFormat = new SimpleDateFormat("MM:dd:HH:mm:ss");
            String strDate = dateFormat.format(date);
            return strDate;
        } else {
            return null;
        }
    }*/

    public static Date addDateSeconds(int Seconds) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND,Seconds);
        return cal.getTime();
    }

    public static int getTimeLeft(Date endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d2 = endDate;
        if(getCurrentDateTime().before(d2)) {
            long difference = d2.getTime()-getCurrentDateTime().getTime();
            long timeLeft = difference/1000;
            return (int) timeLeft;
        }
        return -1;
    };

    public static String getTimeLeftStr(Date endDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date d1 = getCurrentDateTime();
        Date d2 = endDate;
        if(d1.before(d2)) {
            long difference_In_Time = d2.getTime() - d1.getTime();

            long difference_In_Seconds = (difference_In_Time / 1000) % 60;

            long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;

            long difference_In_Hours = (difference_In_Time / (1000 * 60 * 60)) % 24;

            long difference_In_Years = (difference_In_Time / (1000l * 60 * 60 * 24 * 365));

            long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;

            StringBuilder string = new StringBuilder();
            string.append(difference_In_Years + " Years ");
            string.append(difference_In_Days + " Days ");
            string.append(difference_In_Hours + " Hours ");
            string.append(difference_In_Minutes + " Minutes ");
            string.append(difference_In_Seconds + " Seconds ");
            return string.toString();
        }
        return null;
    }

    public static Date parseDate(String input) {
        Calendar cal = Calendar.getInstance();
        if(input.contains("d")) {
            String[] tmp = input.split("d");
            try {
                int days = Integer.parseInt(tmp[0]);
                cal.add(Calendar.DAY_OF_MONTH, days);
                return cal.getTime();
            } catch(NumberFormatException e) {
                return null;
            }
        } else if(input.contains("m")) {
            String[] tmp = input.split("m");
            try {
                int minutes = Integer.parseInt(tmp[0]);
                cal.add(Calendar.MINUTE, minutes);
                return cal.getTime();
            } catch(NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
