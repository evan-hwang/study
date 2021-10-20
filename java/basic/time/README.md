![image-20210623143609092](/Users/addpage/Library/Application Support/typora-user-images/image-20210623143609092.png)

그림. java.util 패키지 구조





![image-20210623143143447](/Users/addpage/Library/Application Support/typora-user-images/image-20210623143143447.png)

그림. java.time 패키지 폴더 구조





![image-20210623143904594](/Users/addpage/Library/Application Support/typora-user-images/image-20210623143904594.png)

그림. Calendar 추상 클래스 구조



Calendar.getInstance

```java
/**
 * Gets a calendar using the default time zone and locale. The
 * <code>Calendar</code> returned is based on the current time
 * in the default time zone with the default
 * {@link Locale.Category#FORMAT FORMAT} locale.
 *
 * @return a Calendar.
 */
public static Calendar getInstance()
{
  return createCalendar(TimeZone.getDefault(), Locale.getDefault(Locale.Category.FORMAT));
}
```



Calendar.createCalendar

```java
private static Calendar createCalendar(TimeZone zone,
                                           Locale aLocale)
{
  CalendarProvider provider =
    LocaleProviderAdapter.getAdapter(CalendarProvider.class, aLocale)
    .getCalendarProvider();
  if (provider != null) {
    try {
      return provider.getInstance(zone, aLocale);
    } catch (IllegalArgumentException iae) {
      // fall back to the default instantiation
    }
  }

  Calendar cal = null;

  if (aLocale.hasExtensions()) {
    String caltype = aLocale.getUnicodeLocaleType("ca");
    if (caltype != null) {
      switch (caltype) {
        case "buddhist":
          cal = new BuddhistCalendar(zone, aLocale);
          break;
        case "japanese":
          cal = new JapaneseImperialCalendar(zone, aLocale);
          break;
        case "gregory":
          cal = new GregorianCalendar(zone, aLocale);
          break;
      }
    }
  }
  if (cal == null) {
    // If no known calendar type is explicitly specified,
    // perform the traditional way to create a Calendar:
    // create a BuddhistCalendar for th_TH locale,
    // a JapaneseImperialCalendar for ja_JP_JP locale, or
    // a GregorianCalendar for any other locales.
    // NOTE: The language, country and variant strings are interned.
    if (aLocale.getLanguage() == "th" && aLocale.getCountry() == "TH") {
      cal = new BuddhistCalendar(zone, aLocale);
    } else if (aLocale.getVariant() == "JP" && aLocale.getLanguage() == "ja"
               && aLocale.getCountry() == "JP") {
      cal = new JapaneseImperialCalendar(zone, aLocale);
    } else {
      cal = new GregorianCalendar(zone, aLocale);
    }
  }
  return cal;
}
```



> ❓ **DAY_OF_WEEK vs DAY_OF_WEEK_IN_MONTH** ([참고 링크](https://stackoverflow.com/questions/6538791/what-is-the-difference-between-calendar-week-of-month-and-calendar-day-of-week-i/41265107))
>
> - DAY_OF_WEEK : 현재 주의 몇 번째 요일
> - DAY_OF_WEEK_IN_MONTH : 현재 요일이 현재 월의 몇 번째 날인지



>  **❓add vs roll**

