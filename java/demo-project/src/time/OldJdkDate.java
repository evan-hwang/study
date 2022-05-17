package time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarTest {

    public static void main(String[] args) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utc);
        calendar.set(1582, Calendar.OCTOBER, 4);
        String pattern = "yyyy.MM.dd";
        String theDay = toString(calendar, pattern, utc);
    }

    private static String toString(Calendar calendar, String pattern, TimeZone zone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(zone);
        return format.format(calendar.getTime());
    }
}
