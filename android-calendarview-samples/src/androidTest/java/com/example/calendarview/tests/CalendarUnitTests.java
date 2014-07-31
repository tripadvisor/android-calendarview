package com.example.calendarview.tests;

import android.content.Context;
import android.content.res.Configuration;
import android.test.InstrumentationTestCase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.calendarview.R;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import static java.util.Calendar.JANUARY;

/**
 * Created by Kurry Tran
 * Email: ktran@tripadvisor.com
 * Date: 6/25/14
 */
public class CalendarUnitTests extends InstrumentationTestCase {
    public static final String MONTH_YEAR_FORMAT = "MMMM yyyy";

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        Context context = getInstrumentation().getTargetContext();
        Locale locale = new Locale("en", "US");
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public void testHeaderMonthNameFormat() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        Locale locale = new Locale("da");
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("de");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("de", "AT");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("el");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "AU");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "CA");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "GB");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "IE");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "IN");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "MY");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "NZ");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "PH");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "SG");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "UK");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "US");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("en", "ZA");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es", "AR");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es", "CL");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es", "CO");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es", "MX");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("es", "PE");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("FI");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("fr");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("id");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("in");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("it");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("ja");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("ko");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("nb");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("nl");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("pl");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("pt");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("ru");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("sv");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("th");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("tr");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("vi");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));

        locale = new Locale("zh");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        assertEquals(((SimpleDateFormat) DateFormat.getPatternInstance(MONTH_YEAR_FORMAT, locale)).toPattern(), context.getResources().getString(R.string.header_month_name_format));
    }

    public void testHeaderNameFormatString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, JANUARY, 1);
        Date january2014 = calendar.getTime();
        Context context = getInstrumentation().getTargetContext();
        Locale locale = new Locale("da");
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("januar 2014", dateFormat.format(january2014));

        locale = new Locale("de");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Januar 2014", dateFormat.format(january2014));

        locale = new Locale("de", "AT");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Jänner 2014", dateFormat.format(january2014));

        locale = new Locale("el");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Ιανουάριος 2014", dateFormat.format(january2014));

        locale = new Locale("en", "AU");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "CA");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "GB");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "IE");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "IN");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "MY");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "NZ");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "PH");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "SG");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "UK");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "US");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("en", "ZA");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("January 2014", dateFormat.format(january2014));

        locale = new Locale("es");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("es", "AR");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("es", "CL");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("es", "CO");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("es", "MX");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("es", "PE");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("enero de 2014", dateFormat.format(january2014));

        locale = new Locale("FI");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("tammikuu 2014", dateFormat.format(january2014));

        locale = new Locale("fr");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("janvier 2014", dateFormat.format(january2014));

        locale = new Locale("id");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Januari 2014", dateFormat.format(january2014));

        locale = new Locale("in");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Januari 2014", dateFormat.format(january2014));

        locale = new Locale("it");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("gennaio 2014", dateFormat.format(january2014));

        locale = new Locale("ja");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("2014年1月", dateFormat.format(january2014));

        locale = new Locale("ko");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("2014년 1월", dateFormat.format(january2014));

        locale = new Locale("nb");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("januar 2014", dateFormat.format(january2014));

        locale = new Locale("nl");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("januari 2014", dateFormat.format(january2014));

        locale = new Locale("pl");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("styczeń 2014", dateFormat.format(january2014));

        locale = new Locale("pt");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("janeiro de 2014", dateFormat.format(january2014));

        locale = new Locale("ru");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Январь 2014", dateFormat.format(january2014));

        locale = new Locale("sv");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("januari 2014", dateFormat.format(january2014));

        locale = new Locale("th");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("มกราคม 2014", dateFormat.format(january2014));

        locale = new Locale("tr");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("Ocak 2014", dateFormat.format(january2014));

        locale = new Locale("vi");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("tháng một 2014", dateFormat.format(january2014));

        locale = new Locale("zh");
        config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
        dateFormat = new java.text.SimpleDateFormat(context.getResources().getString(R.string.header_month_name_format), locale);
        assertEquals("2014年1月", dateFormat.format(january2014));
    }
}
