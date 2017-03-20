package com.tx.lfclient.time;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.tx.lfclient.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/16
 */
public class TimePicker implements OnWheelChangedListener {

    @ViewInject(R.id.year)
    private WheelView view_year;
    @ViewInject(R.id.month)
    private WheelView view_month;
    @ViewInject(R.id.day)
    private WheelView view_day;
    @ViewInject(R.id.hour)
    private WheelView view_hour;
    @ViewInject(R.id.min)
    private WheelView view_minute;
    @ViewInject(R.id.sec)
    private WheelView view_second;


    private List<String> big_month;
    private List<String> small_month;
    private View view;

    private static final int START_YEAR = 1990;
    private static final int END_YEAR = 2100;

    private Calendar calendar;


    public TimePicker(Context context) {
        init(context);
        initData();
        initView();
    }


    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.post_time_picker, null);
        x.view().inject(this, view);
        big_month = Arrays.asList(context.getResources().getStringArray(R.array.big_month));
        small_month = Arrays.asList(context.getResources().getStringArray(R.array.small_month));
        setSetting(view_year, "年");
        setSetting(view_month, "月");
        setSetting(view_day, "日");
        setSetting(view_hour, "时");
        setSetting(view_minute, "分");
        setSetting(view_second, "秒");
        view_year.addChangingListener(this);
        view_month.addChangingListener(this);

    }

    private void initData() {
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }


    public void setCurrentTime(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date d = format.parse(time);
            calendar.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        setNumberAdatper();
        view_year.setCurrentItem(calendar.get(Calendar.YEAR) - START_YEAR);
        view_month.setCurrentItem(calendar.get(Calendar.MONTH));
        view_hour.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
        view_minute.setCurrentItem(calendar.get(Calendar.MINUTE));
        view_second.setCurrentItem(calendar.get(Calendar.SECOND));
        setDayAdapter();
        view_day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH));
    }


    public View getView() {
        return view;
    }


    private void setSetting(WheelView wv, String name) {
        wv.setCyclic(true);
        wv.setLabel(name);
    }

    private void setNumberAdatper() {
        view_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR, "%04d"));
        view_month.setAdapter(new NumericWheelAdapter(1, 12, "%02d"));
        view_hour.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        view_minute.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        view_second.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
    }

    private void setDayAdapter() {

        int year_num = view_year.getCurrentItem() + START_YEAR;
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (big_month.contains(String.valueOf(view_month.getCurrentItem() + 1))) {
            view_day.setAdapter(new NumericWheelAdapter(1, 31, "%02d"));
        } else if (small_month.contains(String.valueOf(view_month.getCurrentItem() + 1))) {
            view_day.setAdapter(new NumericWheelAdapter(1, 30, "%02d"));
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0)
                view_day.setAdapter(new NumericWheelAdapter(1, 29, "%02d"));
            else
                view_day.setAdapter(new NumericWheelAdapter(1, 28, "%02d"));
        }
    }


    public String getTime() {
        String res = String.format(Locale.CHINA, "%04d-%02d-%02d %02d:%02d:%02d", view_year.getCurrentItem() + START_YEAR,
                view_month.getCurrentItem() + 1,
                view_day.getCurrentItem() + 1,
                view_hour.getCurrentItem(),
                view_minute.getCurrentItem(),
                view_second.getCurrentItem());
        assert res != null;
        return res;
    }


    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        setDayAdapter();
    }
}
