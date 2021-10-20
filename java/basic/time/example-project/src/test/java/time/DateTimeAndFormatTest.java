package time;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.Calendar;
import java.util.Date;

import static java.time.DayOfWeek.TUESDAY;
import static java.time.temporal.TemporalAdjusters.*;

@DisplayName("날짜와 시간 & 형식화 예제")
class DateTimeAndFormatTest {

    @Nested
    @DisplayName("calendar (java.util)")
    class CalendarTest {

        String toString(Calendar date) {
            return date.get(Calendar.YEAR) + "년 " + (date.get(Calendar.MONTH) + 1) + "월 " + date.get(Calendar.DATE) + "일 ";
        }

        @Test
        @DisplayName("조회")
        public void get() {

            // 기본적으로 현재 시간으로 설정된다.
            Calendar today = Calendar.getInstance();
            System.out.println("이 해의 년도 : " + today.get(Calendar.YEAR));
            System.out.println("월 (0(1월)~11) : " + today.get(Calendar.MONTH));
            System.out.println("이 해의 몇 째 주 : " + today.get(Calendar.WEEK_OF_YEAR));
            System.out.println("이 달의 몇 째 주 : " + today.get(Calendar.WEEK_OF_MONTH));

            // DATE 와 DAY_OF_MONTH 는 같다.
            System.out.println("이 달의 몇 일 : " + today.get(Calendar.DATE));
            System.out.println("이 달의 몇 일 : " + today.get(Calendar.DAY_OF_MONTH));
            System.out.println("이 해의 몇 일 : " + today.get(Calendar.DAY_OF_YEAR));
            System.out.println("요일 (1(일요일)~7) : " + today.get(Calendar.DAY_OF_WEEK));
            System.out.println("이 달의 몇 째 요일 : " + today.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("오전_오후 (0: 오전, 1 오후) : " + today.get(Calendar.AM_PM));
            System.out.println("시간 (0~11) : " + today.get(Calendar.HOUR));
            System.out.println("시간 (0~23) : " + today.get(Calendar.HOUR_OF_DAY));
            System.out.println("분 (0~59) : " + today.get(Calendar.MINUTE));
            System.out.println("초 (0~59) : " + today.get(Calendar.SECOND));
            System.out.println("1000분의 1초(millisecond) : " + today.get(Calendar.MILLISECOND));

            // 천분의 1초를 시간으로 표시하기 위해 3600000으로 나누었다. (1시간 = 60 * 60초)
            System.out.println("TimeZone (-12 ~ +12) : " + today.get(Calendar.ZONE_OFFSET));
            System.out.println("이 달의 마지막 날 : : " + today.getActualMaximum(Calendar.DATE));

        }

        @Test
        @DisplayName("값 설정")
        public void set() {

            // 요일은 1부터 시작하기 때문에, DAY_OF_WEEK[0]은 비워두었다.
            final String[] DAY_OF_WEEK = {"", "일", "월", "화", "수", "목", "금", "토"};

            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            // month 의 경우 0부터 시작하기 때문에 8월인 경우, 7로 지정해야한다.
            // date1.set(2015, Calendar.AUGUST, 15); 와 같이 할 수도 있다.
            date1.set(2015, 7, 15); // 2015년 8월 15일로 날짜를 설정한다.
            System.out.println("date1은 " + toString(date1) + DAY_OF_WEEK[date1.get(Calendar.DAY_OF_WEEK)] + "요일이고,");
            System.out.println("오늘(date2)은 " + toString(date2) + DAY_OF_WEEK[date2.get(Calendar.DAY_OF_WEEK)] + "요일입니다.");

            // 두 날짜간의 차이를 얻으려면, getTimeInMillis() 천분의 일초 단위로 변환해야한다.
            long difference = (date2.getTimeInMillis() - date1.getTimeInMillis()) / 1000;
            System.out.println("그 날(date1)부터 지금(date2)까지 " + difference + "초가 지났습니다.");
            System.out.println("일(day)로 계산하면 " + difference / (24 * 60 * 60) + "일입니다.");    // 1일 = 24 * 60 * 60
        }

        @Test
        @DisplayName("차이 계산")
        public void calculateDifference() {

            final int[] TIME_UNIT = {3600, 60, 1}; // 큰 단위를 앞에 놓는다.
            final String[] TIME_UNIT_NAME = {"시간 ", "분 ", "초 "};

            Calendar time1 = Calendar.getInstance();
            Calendar time2 = Calendar.getInstance();

            // time1의 시간을 10시 20분 30초로 설정한다.
            time1.set(Calendar.HOUR_OF_DAY, 10);
            time1.set(Calendar.MINUTE, 20);
            time1.set(Calendar.SECOND, 30);

            // time2의 시간을 20시 30분 10초로 설정한다.
            time2.set(Calendar.HOUR_OF_DAY, 20);
            time2.set(Calendar.MINUTE, 30);
            time2.set(Calendar.SECOND, 10);

            System.out.println("time1 :" + time1.get(Calendar.HOUR_OF_DAY) + "시 " + time1.get(Calendar.MINUTE) + "분 " + time1.get(Calendar.SECOND) + "초");
            System.out.println("time2 :" + time2.get(Calendar.HOUR_OF_DAY) + "시 " + time2.get(Calendar.MINUTE) + "분 " + time2.get(Calendar.SECOND) + "초");

            long difference = Math.abs(time2.getTimeInMillis() - time1.getTimeInMillis()) / 1000;
            System.out.println("time1과 time2의 차이는 " + difference + "초 입니다.");

            StringBuilder tmp = new StringBuilder();

            for (int i = 0; i < TIME_UNIT.length; i++) {
                tmp.append(difference / TIME_UNIT[i]).append(TIME_UNIT_NAME[i]);
                difference %= TIME_UNIT[i];
            }

            System.out.println("시분초로 변환하면 " + tmp + "입니다.");
        }

        @Test
        @DisplayName("날짜 추가")
        public void addAndRoll() {

            Calendar date = Calendar.getInstance();

            date.set(2005, 7, 31);    // 2005년 8월 31일

            System.out.println("======= 현재 ========");
            System.out.println(toString(date));

            System.out.println("======== 1일 후 ========");
            date.add(Calendar.DATE, 1);
            System.out.println(toString(date));

            System.out.println("======== 6달 전 ========");
            date.add(Calendar.MONTH, -6);
            System.out.println(toString(date));

            System.out.println("======== 31일 후 (roll) ========");
            date.roll(Calendar.DATE, 31);
            System.out.println(toString(date));

            System.out.println("======== 31일 후 (add) ========");
            date.add(Calendar.DATE, 31);
            System.out.println(toString(date));

        }

        @ParameterizedTest
        @CsvSource({"2021, 6", "2020, 3", "1992, 8"})
        @DisplayName("달력 비쥬얼라이징")
        public void visualize(Integer year, Integer month) {

            int START_DAY_OF_WEEK = 0;
            int END_DAY = 0;

            Calendar sDay = Calendar.getInstance();       // 시작일
            Calendar eDay = Calendar.getInstance();       // 끝일

            // 월의 경우 0 부터 11까지의 값을 가지므로 1을 빼주어야 한다.
            // 예를 들어, 2004년 11월 1일은 sDay.set(2004, 10, 1);과 같이 해줘야 한다.
            sDay.set(year, month - 1, 1);
            eDay.set(year, month, 1);

            // 다음달의 첫날에서 하루를 빼면 현재달의 마지막 날이 된다.
            // 12월 1일에서 하루를 빼면 11월 30일이 된다.
            eDay.add(Calendar.DATE, -1);

            // 첫 번째 요일이 무슨 요일인지 알아낸다.
            START_DAY_OF_WEEK = sDay.get(Calendar.DAY_OF_WEEK);
            // eDay에 지정된 날짜를 얻어온다.
            END_DAY = eDay.get(Calendar.DATE);

            System.out.println("      " + year + "년 " + month + "월");
            System.out.println(" SU MO TU WE TH FR SA");

            // 해당 월의 1일이 어느 요일인지에 따라서 공백을 출력한다.
            // 만일 1일이 수요일이라면 공백을 세 번 찍는다.(일요일부터 시작)
            for (int i = 1; i < START_DAY_OF_WEEK; i++) {
                System.out.print("   ");
            }

            for (int i = 1, n = START_DAY_OF_WEEK; i <= END_DAY; i++, n++) {
                System.out.print((i < 10) ? "  " + i : " " + i);
                if (n % 7 == 0) System.out.println();
            }
        }

    }

    @Nested
    @DisplayName("format (java.text)")
    class FormatTest {

        @Nested
        @DisplayName("Decimal 포맷팅")
        class DecimalFormatting {

            @Test
            @DisplayName("패턴 별 포맷팅")
            public void format() {
                double number = 1234567.89;

                String[] patterns = {
                        "0",
                        "#",
                        "0.0",
                        "#.#",
                        "0000000000.0000",
                        "##########.####",
                        "#.#-",
                        "-#.#",
                        "#,###.##",
                        "#,####.##",
                        "#E0",
                        "0E0",
                        "##E0",
                        "00E0",
                        "####E0",
                        "0000E0",
                        "#.#E0",
                        "0.0E0",
                        "0.000000000E0",
                        "00.00000000E0",
                        "000.0000000E0",
                        "#.#########E0",
                        "##.########E0",
                        "###.#######E0",
                        "#,###.##+;#,###.##-",
                        "#.#%",
                        "#.#\u2030",
                        "\u00A4 #,###",
                        "'#'#,###",
                        "''#,###",
                };

                for (String pattern : patterns) {
                    DecimalFormat df = new DecimalFormat(pattern);
                    System.out.printf("%19s : %s\n", pattern, df.format(number));
                }
            }

            @Test
            @DisplayName("문자 포함된 문자열 -> 숫자 파싱")
            public void parseStringNumber() {
                DecimalFormat df = new DecimalFormat("#,###.##");
                DecimalFormat df2 = new DecimalFormat("#.###E0");

                try {
                    Number num = df.parse("1,234,567.89");
                    System.out.print("1,234,567.89" + " -> ");

                    double d = num.doubleValue();
                    System.out.print(d + " -> ");

                    System.out.println(df2.format(num));
                } catch (Exception ignored) {
                }
            }

        }

        @Nested
        @DisplayName("Date 포맷팅")
        class SimpleDateFormatting {

            @Test
            @DisplayName("Date 포맷팅")
            public void dateFormatting() {

                Date today = new Date();

                SimpleDateFormat sdf1, sdf2, sdf3, sdf4;
                SimpleDateFormat sdf5, sdf6, sdf7, sdf8, sdf9;

                sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                sdf2 = new SimpleDateFormat("''yy년 MMM dd일 E요일");
                sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

                sdf5 = new SimpleDateFormat("오늘은 올 해의 D번째 날입니다.");
                sdf6 = new SimpleDateFormat("오늘은 이 달의 d번째 날입니다.");
                sdf7 = new SimpleDateFormat("오늘은 올 해의 w번째 주입니다.");
                sdf8 = new SimpleDateFormat("오늘은 이 달의 W번째 주입니다.");
                sdf9 = new SimpleDateFormat("오늘은 이 달의 F번째 E요일입니다.");

                System.out.println(sdf1.format(today));    // format(Date d)
                System.out.println(sdf2.format(today));
                System.out.println(sdf3.format(today));
                System.out.println(sdf4.format(today));
                System.out.println();
                System.out.println(sdf5.format(today));
                System.out.println(sdf6.format(today));
                System.out.println(sdf7.format(today));
                System.out.println(sdf8.format(today));
                System.out.println(sdf9.format(today));
            }

            @Test
            @DisplayName("Calendar 포맷팅")
            public void calendarFormatting() {

                // Calendar 와 Date 간의 변환은 다음과 같이 한다.
                Calendar cal = Calendar.getInstance();
                cal.set(2005, 9, 3);    // 2005년 10월 3일 - Month는 0~11의 범위를 갖는다.

                // Date 타입만 format()을 사용할 수 있다.
                Date day = cal.getTime();

                SimpleDateFormat sdf1, sdf2, sdf3, sdf4;
                sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                sdf2 = new SimpleDateFormat("yy-MM-dd E요일");
                sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                sdf4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

                System.out.println(sdf1.format(day)); // format(Date d)
                System.out.println(sdf2.format(day));
                System.out.println(sdf3.format(day));
                System.out.println(sdf4.format(day));
            }

            @Test
            @DisplayName("파싱")
            public void parsing() {

                DateFormat df = new SimpleDateFormat("yyyy년 MM월 dd일");
                DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");

                try {
                    Date d = df.parse("2015년 12월 23일");
                    System.out.println(df2.format(d));
                } catch (Exception ignored) {
                }
            }

        }

        @Nested
        @DisplayName("Choice 포맷팅")
        class ChoiceFormatting {

            /*
             * ChoiceFormat(double[] limits, String[] grades)
             *
             * limits, grades 간의 순서와 개수를 맞춰야 함
             * limits : double, 반드시 모두 오름차순으로 정렬되어있어야 함
             * grades : String, 치환 될 문자열의 개수 = limits 에 정의된 개수
             */
            @Test
            @DisplayName("배열을 이용한 포맷팅")
            public void formatWithArray() {

                // 낮은 값부터 큰 값의 순서로 적어야한다.
                double[] limits = {60, 70, 80, 90};

                // limits, grades 간의 순서와 개수를 맞추어야 한다.
                String[] grades = {"D", "C", "B", "A"};

                int[] scores = {100, 95, 88, 70, 52, 60, 70};

                ChoiceFormat form = new ChoiceFormat(limits, grades);

                for (int score : scores) {
                    System.out.println(score + ":" + form.format(score));
                }
            }

            /*
             * ChoiceFormat(String newPattern)
             *
             * pattern : 배열대신 패턴을 사용하여 간결하게 처리
             * - # : 경계값을 범위에 포함
             * - < : 경계값은 포함하지 않음
             */
            @Test
            @DisplayName("패턴을 이용한 포맷팅")
            public void formatWithPattern() {

                // # - 경계값 포함
                // < - 경계값 미포함
                String pattern = "60#D|70#C|80<B|90#A";
                int[] scores = {91, 90, 80, 88, 70, 52, 60};

                ChoiceFormat form = new ChoiceFormat(pattern);

                for (int score : scores) {
                    System.out.println(score + ":" + form.format(score));
                }
            }
        }

        @Nested
        @DisplayName("Message 포맷팅")
        class MessageFormatting {

            @Test
            @DisplayName("포맷팅")
            public void format() {

                String msg = "Name: {0} \nTel: {1} \nAge:{2} \nBirthday:{3}";

                Object[] arguments = {
                        "황혁진", "02-123-1234", "27", "07-09"
                };

                String result = MessageFormat.format(msg, arguments);
                System.out.println(result);
            }

            @Test
            @DisplayName("파싱")
            public void parse() throws ParseException {

                String[] data = {
                        "INSERT INTO PRESIDENT_INFO VALUES ('박근혜','02-123-1234',27,'07-09');",
                        "INSERT INTO PRESIDENT_INFO VALUES ('문재인','032-333-1234',33,'10-07');"
                };

                String pattern = "INSERT INTO PRESIDENT_INFO VALUES ({0},{1},{2},{3});";
                MessageFormat mf = new MessageFormat(pattern);

                for (String d : data) {
                    Object[] objs = mf.parse(d);
                    for (Object obj : objs) {
                        System.out.print(obj + ",");
                    }
                    System.out.println();
                }
            }

        }

    }

    @Nested
    @DisplayName("java.time")
    class TimePackage {
        public void localTime() {

        }

        /**
         * Instant
         * - 에포크 타임 (1970-01-01 00:00:00 UTC)로부터 경과된 시간을 나노초로 표현
         * - Human Readable 하진 않지만 날짜 계산에는 편리하다.
         * - 초 단위와 나노 단위를 나누어 저장.
         * - JDK 1의 Date 를 대체하기 위함.
         */
        @Test
        @DisplayName("Instant")
        public void instant() {

            Instant now = Instant.now();
            Instant now2 = Instant.ofEpochSecond(now.getEpochSecond());
            System.out.println(now);
            System.out.println(now2);

            long epochSec = now.getEpochSecond();
            int nano = now.getNano();
            System.out.println("epochSec : " + epochSec);
            System.out.println("nano : " + nano);

            Instant now3 = Instant.ofEpochSecond(now.getEpochSecond(), now.getNano());
            System.out.println(now3);

        }

        @Test
        @DisplayName("LocalDateTime, ZonedDateTime, OffsetDateTime")
        public void dateTimeAndZone() {

            LocalDate date = LocalDate.of(2015, 12, 31); // 2015년 12월 31일
            LocalTime time = LocalTime.of(12, 34, 56);     // 12시 23분 56초

            // 2015년 12월 31일 12시 23분 56초
            LocalDateTime dt = LocalDateTime.of(date, time);

            ZoneId zid = ZoneId.of("Asia/Seoul");
            ZonedDateTime zdt = dt.atZone(zid);
            String strZid = zdt.getZone().getId();

            ZonedDateTime seoulTime = ZonedDateTime.now();
            ZoneId nyId = ZoneId.of("America/New_York");
            ZonedDateTime nyTime = ZonedDateTime.now().withZoneSameInstant(nyId);

            // ZonedDatetime -> OffsetDateTime
            OffsetDateTime odt = zdt.toOffsetDateTime();

            System.out.println(dt);
            System.out.println(zid);
            System.out.println(zdt);
            System.out.println(seoulTime);
            System.out.println(nyTime);
            System.out.println(odt);
        }

        @Test
        @DisplayName("TemporalAdjusters")
        public void temporalAdjusters() {

            LocalDate today = LocalDate.now();
            LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            LocalDate firstDayOfNextYear = today.with(TemporalAdjusters.firstDayOfNextYear());

            System.out.println("오늘 : " + today);
            System.out.println("다음주 월요일 : " + nextMonday);
            System.out.println("내년 첫 날 : " + firstDayOfNextYear);

            System.out.println(today.with(firstDayOfNextMonth()));        // 다음 달의 첫 날
            System.out.println(today.with(firstDayOfMonth()));            // 이 달의 첫 날
            System.out.println(today.with(lastDayOfMonth()));             // 이 달의 마지막 날
            System.out.println(today.with(firstInMonth(TUESDAY)));        // 이 달의 첫번째 화요일
            System.out.println(today.with(lastInMonth(TUESDAY)));         // 이 달의 마지막 화요일
            System.out.println(today.with(previous(TUESDAY)));            // 지난 주 화요일
            System.out.println(today.with(previousOrSame(TUESDAY)));      // 지난 주 화요일(오늘 포함)
            System.out.println(today.with(next(TUESDAY)));                // 다음 주 화요일
            System.out.println(today.with(nextOrSame(TUESDAY)));          // 다음 주 화요일(오늘 포함)
            System.out.println(today.with(dayOfWeekInMonth(4, TUESDAY))); // 이 달의 4번째 화요일
        }

        @Test
        @DisplayName("Duration")
        public void duration() {

            // between 을 통해 얻기
            LocalTime time1 = LocalTime.MIN;
            LocalTime time2 = LocalTime.MAX;
            Duration du = Duration.between(time1, time2);
            System.out.println("time1 : " + time1);
            System.out.println("time2 : " + time2);

            System.out.println("=========== get with ChronoUnit ===========");
            System.out.println("duration seconds : " + du.get(ChronoUnit.SECONDS));
            System.out.println("duration nano : " + du.get(ChronoUnit.NANOS));

            LocalTime tmpTime = LocalTime.of(0, 0).plusSeconds(du.getSeconds());

            System.out.println("=========== localtime conversion ===========");
            System.out.println("duration hour : " + tmpTime.getHour());
            System.out.println("duration minute : " + tmpTime.getMinute());
            System.out.println("duration second : " + tmpTime.getSecond());
            System.out.println("duration nano : " + tmpTime.getNano());

        }

        @Test
        @DisplayName("Period")
        public void period() {

            // between 을 통해 얻기
            LocalDate date1 = LocalDate.of(2020, 1, 1);
            LocalDate date2 = LocalDate.of(2021, 6, 21);
            Period pe = Period.between(date1, date2);
            System.out.println("date1 : " + date1);
            System.out.println("date2 : " + date2);
            System.out.println("=========== get ===========");
            System.out.println("period year : " + pe.getYears());
            System.out.println("period month : " + pe.getMonths());
            System.out.println("period day : " + pe.getDays());

            System.out.println("=========== get with ChronoUnit ===========");
            System.out.println("period year : " + pe.get(ChronoUnit.YEARS));
            System.out.println("period month : " + pe.get(ChronoUnit.MONTHS));
            System.out.println("period day : " + pe.get(ChronoUnit.DAYS));

        }

        @Test
        @DisplayName("Format")
        public void format() {

            ZonedDateTime zdateTime = ZonedDateTime.now();

            String[] patternArr = {
                    "yyyy-MM-dd HH:mm:ss",
                    "''yy년 MMM dd일 E요일",
                    "yyyy-MM-dd HH:mm:ss.SSS Z VV",
                    "yyyy-MM-dd hh:mm:ss a",
                    "오늘은 올 해의 D번째 날입니다.",
                    "오늘은 이 달의 d번째 날입니다.",
                    "오늘은 올 해의 w번째 주입니다.",
                    "오늘은 이 달의 W번째 주입니다.",
                    "오늘은 이 달의 W번째 E요일입니다."
            };

            for(String p : patternArr) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(p);
                System.out.println(zdateTime.format(formatter));
            }

        }

        @Test
        @DisplayName("Parse")
        public void parse() {

            LocalDate newYear = LocalDate.parse("2016-01-01", DateTimeFormatter.ISO_LOCAL_DATE);

            LocalDate     date     = LocalDate.parse("2001-01-01");
            LocalTime     time     = LocalTime.parse("23:59:59");
            LocalDateTime dateTime = LocalDateTime.parse("2001-01-01T23:59:59");

            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endOfYear   = LocalDateTime.parse("2015-12-31 23:59:59", pattern);

            System.out.println(newYear);
            System.out.println(date);
            System.out.println(time);
            System.out.println(dateTime);
            System.out.println(endOfYear);

        }

    }

    @Nested
    @DisplayName("Example")
    public class Example {

        public Long getMilliseconds(Long l) {
            if (l.toString().length() == 10) {
                return l * 1000;
            }
            return l;
        }


        @Test
        @DisplayName("ZonedDateTime end of day")
        public void zonedDateTimeEndOfDay() {

            LocalDateTime now = LocalDateTime.now();
            ZonedDateTime nowSeoul = now.atZone(ZoneId.of("America/New_York"));

            System.out.println("==============================================================");
            System.out.println("서울 현재 시각 : " + nowSeoul);

            System.out.println("==============================================================");
            System.out.println("서울 오늘의 시작 시각 : " + nowSeoul.with(LocalTime.of(0,0, 0)));
            System.out.println("서울 오늘의 시작 시각 : " + nowSeoul.with(LocalTime.MIN));
            System.out.println("서울 오늘의 시작 시각 : " + nowSeoul.with(ChronoField.NANO_OF_DAY, LocalTime.MIN.toNanoOfDay()));
            System.out.println("서울 오늘의 시작 시각 : " + nowSeoul.withHour(0).withMinute(0).withSecond(0).withNano(0));

            System.out.println("==============================================================");
            System.out.println("서울 오늘의 마지막 시각 : " + nowSeoul.with(LocalTime.of(23,59, 59)));
            System.out.println("서울 오늘의 마지막 시각 : " + nowSeoul.with(LocalTime.MAX));
            System.out.println("서울 오늘의 마지막 시각 : " + nowSeoul.with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay()));
            System.out.println("서울 오늘의 마지막 시각 : " + nowSeoul.withHour(23).withMinute(59).withSecond(59).withNano(999999999));
            System.out.println("그리니치 오늘의 마지막 시각 : " +
                    new Date(getMilliseconds(nowSeoul.with(LocalTime.MAX).toEpochSecond())));
            System.out.println("그리니치 오늘의 마지막 시각 : " +
                    new Date(getMilliseconds(nowSeoul.withHour(23).withMinute(59).withSecond(59).withNano(999999999).toEpochSecond())));

        }

    }

}
