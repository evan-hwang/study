package datetime;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

public class OldJdkDateTest {

    @Test
    public void shouldGetAfterOneDay() {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        Calendar calendar = Calendar.getInstance(utc);
        calendar.set(1582, Calendar.OCTOBER , 4);
        String pattern = "yyyy.MM.dd";
        String theDay = toString(calendar, pattern, utc);
        assertThat(theDay).isEqualTo("1582.10.04");


        calendar.add(Calendar.DATE, 1);
        String nextDay = toString(calendar, pattern, utc);
        assertThat(nextDay).isEqualTo("1582.10.05");
    }

    private String toString(Calendar calendar, String pattern, TimeZone zone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(zone);
        return format.format(calendar.getTime());
    }

    @Test
    public void shouldGetAfterOneHour() {
        TimeZone seoul = TimeZone.getTimeZone("Asia/Seoul");
        Calendar calendar = Calendar.getInstance(seoul);
        calendar.set(1988, Calendar.MAY , 7, 23, 0);
        String pattern = "yyyy.MM.dd HH:mm";
        String theTime = toString(calendar, pattern, seoul);
        assertThat(theTime).isEqualTo("1988.05.07 23:00");

        calendar.add(Calendar.HOUR_OF_DAY, 1);
        String after1Hour = toString(calendar, pattern, seoul);
        assertThat(seoul.inDaylightTime(calendar.getTime())).isTrue();
        assertThat(after1Hour).isEqualTo("1988.05.08 01:00");
    }

    @Test
    @SuppressWarnings("deprecation")
    public void shouldGetDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, Calendar.JANUARY, 1);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        assertThat(dayOfWeek).isEqualTo(Calendar.WEDNESDAY);
        assertThat(dayOfWeek).isEqualTo(4);
        Date theDate = calendar.getTime();
        assertThat(theDate.getDay()).isEqualTo(3);
    }
}
