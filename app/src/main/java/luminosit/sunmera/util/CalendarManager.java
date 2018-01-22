package luminosit.sunmera.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by GaryTang on 4/12/15.
 */

public class CalendarManager {

    Calendar calendar;

    String date;
    String dateToSeconds;

    public CalendarManager(){
        date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        dateToSeconds = new SimpleDateFormat("yyyyMMdd"
                + DatabaseHelper.UID_NAME_SEPARATOR + "HHmmss").format(new Date());
    }

    public String getToday(){
        //return today's date ex: 20141221
        return date;
    }

    public String getTodayToSeconds(){
        //return today's date down to seconds ex: 20150314_092657
        return dateToSeconds;
    }

    public String convertTimeCodeToNormalFormat(String dateToSeconds){
        String[] today = dateToSeconds.split(DatabaseHelper.UID_NAME_SEPARATOR);
        String[] time = today[2].split(""); //0 is username now
        String hour = "",
                minute = "",
                second = "";

        //counting digit...
        for(int i = 1; i <= time.length; i++){
            if(i <= 2){
                hour = hour + time[i];
            }
            if (i > 2 && i <= 4){
                minute = minute + time[i];
            }
            if (i > 4 && i <= 6){
                second = second + time[i];
            }
        }

        return hour + ":" + minute + ":" + second;
    }
}
