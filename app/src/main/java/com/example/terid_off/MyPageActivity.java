package com.example.terid_off;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MyPageActivity extends AppCompatActivity {

    // Toolbar 변수
    private Toolbar toolbar;

    // D-day 변수
    private final int ONE_DAY = 24 * 60 * 60 * 1000; // Millisecond 형태의 하루(24시간)
    private Calendar mCalendar; // 현재 날짜를 알기 위해 사용
    private TextView mTvResult; // D-day Result

    // 현재 날짜 변수
    CalendarView calendarView;
    TextView nowdate, click_date;

    // 일정 기록 및 수정 변수
    public String readDay = null;
    public String str = null;
    public Button cha_Btn, del_Btn, save_Btn;
    //public TextView textView2, textView3;
    public EditText contextEditText;

    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) { // D-day 계산 결과 출력
            mTvResult.setText(getDday(a_year, a_monthOfYear, a_dayOfMonth));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // 일정 수정 및 기록 변수
        calendarView = findViewById(R.id.calendarView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        //textView2 = findViewById(R.id.textView2);
        //textView3 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);

        Locale.setDefault(Locale.KOREAN); // 한국어 설정 (ex. date picker)
        mCalendar = new GregorianCalendar(); // 현재 날짜를 알기 위해 사용


        mTvResult = findViewById(R.id.tv_result); // D-day 보여주기

        // Input date click 시 date picker 호출
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MyPageActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        };
        findViewById(R.id.btn_input_date).setOnClickListener(clickListener);

        // Toolbar 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // 현재 날짜 설정
        nowdate = findViewById(R.id.nowdate);
        nowdate.setText(getTime());

        // Calendar 설정
        calendarView = findViewById(R.id.calendarView);
        click_date = findViewById(R.id.click_date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                click_date.setText(year + "년 " + (month+1) + "월 " + day + " 일");
            }
        });

    }

    // Toolbar back button 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // Calendar 현재 시간 구하기
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String getTime = dateFormat.format(date);

        return getTime;
    }


    // D-day 반환
    private String getDday(int a_year, int a_monthOfYear, int a_dayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        //출력시 d-day에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "D+%d";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }


}