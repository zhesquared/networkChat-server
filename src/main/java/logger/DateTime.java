package logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {

    public static String getCurrentTime() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
        String date = format.format(new Date());
        return "'" + date + "' ";
    }
}