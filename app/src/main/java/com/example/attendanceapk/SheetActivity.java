package com.example.attendanceapk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.Calendar;

public class SheetActivity extends AppCompatActivity {

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);

        setToolBar();
        showTable();
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.toolbar);
        TextView tittle = toolbar.findViewById(R.id.tittle_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);

        tittle.setText("Attendance Sheet");
        subtitle.setText("Detail");

        back.setOnClickListener(v -> onBackPressed());
    }


    private void showTable() {
        DbHelper dbHelper = new DbHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        long [] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String month = getIntent().getStringExtra("month");

        int DAY_IN_MONTH = getDayInMonth(month);

        //Table row setting
        int rowSize = idArray.length + 1;

        TableRow[] rows = new TableRow[rowSize];
        TextView[] roll_textviews = new TextView[rowSize];
        TextView[] name_textviews = new TextView[rowSize];
        TextView[][] status_textviews = new TextView[rowSize][DAY_IN_MONTH + 1];

        for (int i = 0; i < rowSize; i++) {
            roll_textviews[i] = new TextView(this);
            name_textviews[i] = new TextView(this);

            for (int j = 1; j < DAY_IN_MONTH ; j++) {
                status_textviews[i][j] = new TextView(this);
            }
        }
        //Header
        roll_textviews[0].setText("roll");
        roll_textviews[0].setTypeface(roll_textviews[0].getTypeface(), Typeface.BOLD);
        name_textviews[0].setText("name");
        name_textviews[0].setTypeface(name_textviews[0].getTypeface(), Typeface.BOLD);

        for (int i = 1; i < DAY_IN_MONTH; i++) {
            status_textviews[0][i].setText(String.valueOf(i));
            status_textviews[0][i].setTypeface(status_textviews[0][i].getTypeface(), Typeface.BOLD);
        }

        for (int i = 1; i < rowSize; i++) {
            roll_textviews[i].setText(String.valueOf(rollArray[i - 1]));
            name_textviews[i].setText(nameArray[i-1]);

            for (int j = 1; j < DAY_IN_MONTH; j++) {
                String day = String.valueOf(j);
                if(day.length() == 1) day = "0" + day;

                String date = day + "." + month;
                String status = dbHelper.getStatus(idArray[i - 1],date);
                status_textviews[i][j].setText(status);
            }
        }

        for (int i = 0; i < rowSize; i++) {
            rows[i] = new TableRow(this);

            if(i % 2 == 0)
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            else
                rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            roll_textviews[i].setPadding(16,16,16,16);
            name_textviews[i].setPadding(16,16,16,16);

            rows[i].addView(roll_textviews[i]);
            rows[i].addView(name_textviews[i]);

            for (int j = 1; j < DAY_IN_MONTH; j++) {
                status_textviews[i][j].setPadding(16,16,16,16);
                rows[i].addView(status_textviews[i][j]);
            }

            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);
    }

    private int getDayInMonth(String month) {
        int monthIndex = Integer.valueOf(month.substring(0,2))-1;
        int yearIndex = Integer.valueOf((month.substring(3)));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,yearIndex);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}