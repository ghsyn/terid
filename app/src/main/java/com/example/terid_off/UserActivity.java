package com.example.terid_off;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;



public class UserActivity extends AppCompatActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;

    private Toolbar toolbar;

    private final int ONE_DAY = 24 * 60 * 60 * 1000; // Millisecond 형태의 하루(24시간)
    private Calendar mCalendar, sCalendar; // 현재 날짜를 알기 위해 사용
    private TextView mTvResult, tv_study; // D-day Result
    private Button btn_study;

    String shared = "file";
    String shared_s = "files";

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) { // D-day 계산 결과 출력
            mTvResult.setText(getDday(a_year, a_monthOfYear, a_dayOfMonth));
        }
    };

    // DatePicker 에서 날짜 선택 시 호출 - Study
    private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) { // D-day 계산 결과 출력
            tv_study.setText(getStudy(a_year, a_monthOfYear, a_dayOfMonth));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Toolbar 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // Dday 설정
        Locale.setDefault(Locale.KOREAN); // 한국어 설정 (ex. date picker)
        mCalendar = new GregorianCalendar(); // 현재 날짜를 알기 위해 사용
        mTvResult = findViewById(R.id.tv_Dday); // D-day 보여주기


        // Study 설정
        btn_study = findViewById(R.id.btn_study);
        sCalendar = Calendar.getInstance();
        tv_study = findViewById(R.id.tv_study); // Study day 보여주기

        // SharedPreferences 를 이용하여 파일 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(shared, MODE_PRIVATE);
        String value = sharedPreferences.getString("UserDday", "");
        mTvResult.setText(value);

        SharedPreferences sharedPreferences_s = getSharedPreferences(shared_s, MODE_PRIVATE);
        String value_s = sharedPreferences_s.getString("Study", "");
        tv_study.setText(value_s);

        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                contextEditText.setText("");
                checkDay(year, month, dayOfMonth);
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(readDay);
                str = contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
        // Input date click 시 date picker 호출 - Dday
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(UserActivity.this, mDateSetListener, year, month, day);
                dialog.show();
            }

        };

        findViewById(R.id.btn_input_date).

                setOnClickListener(clickListener);

        // Input date click 시 date picker 호출 - Study
        View.OnClickListener clickListener_s = new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = sCalendar.get(Calendar.YEAR);
                final int month = sCalendar.get(Calendar.MONTH);
                final int day = sCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog_s = new DatePickerDialog(UserActivity.this, sDateSetListener, year, month, day);

                // 현재 날짜만 선택하게 고정
                dialog_s.getDatePicker().setMinDate(sCalendar.getTime().getTime());
                dialog_s.getDatePicker().setMaxDate(sCalendar.getTimeInMillis());

                btn_study.setEnabled(false);
                tv_study.setVisibility(View.VISIBLE);
                btn_study.setVisibility(View.INVISIBLE);
                dialog_s.show();

            }
        };

        findViewById(R.id.btn_study).

                setOnClickListener(clickListener_s);
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

    public void checkDay(int cYear, int cMonth, int cDay)
    {
        readDay = "" + cYear + "-" + (cMonth + 1) + "" + "-" + cDay + ".txt";
        FileInputStream fis;

        try
        {
            fis = openFileInput(readDay);

            byte[] fileData = new byte[fis.available()];
            fis.read(fileData);
            fis.close();

            str = new String(fileData);

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(readDay);
                }
            });
            if(textView2.getText().toString().equals(""))
            {
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = "";
            fos.write((content).getBytes());
            fos.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay)
    {
        FileOutputStream fos;
        try
        {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS);
            String content = contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
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

        //출력시 D-day 에 맞게 표시
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

    // Study day 반환
    private String getStudy(int a_year, int a_monthOfYear, int a_dayOfMonth) {

        // D-day 설정
        final Calendar studyCalendar = Calendar.getInstance();
        studyCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today의 차를 구한다.
        final long start_day = studyCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = start_day - today;

        //출력시 D-day 에 맞게 표시
        final String strFormat;
        if (result <= 0) {
            result = result * -1 + 1;
            strFormat = "공부한 지\n%d 일";
        }
        else {
            strFormat = "오류입니다.";
        }

        final String strCount = (String.format(strFormat, result));

        return strCount;

    }

    // SharedPreferences 를 이용하여 파일 저장
    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences(shared, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = mTvResult.getText().toString();
        editor.putString("UserDday", value);

        SharedPreferences sharedPreferences_s = getSharedPreferences(shared_s, MODE_PRIVATE);
        SharedPreferences.Editor editor_s = sharedPreferences.edit();
        String value_s = tv_study.getText().toString();
        editor_s.putString("Study", value_s);

        editor.commit();
        editor_s.commit();

    }
}