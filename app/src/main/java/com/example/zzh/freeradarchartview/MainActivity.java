package com.example.zzh.freeradarchartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private RadarChartView radarView;
    private HashMap<String, Float> valueHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        radarView = (RadarChartView) findViewById(R.id.radarView);

        valueHash = new LinkedHashMap<>();
        valueHash.put("I", 33F);
        valueHash.put("II", 100F);
        valueHash.put("III", 66F);
        valueHash.put("Ⅳ", 77F);
        valueHash.put("Ⅴ", 88F);
        valueHash.put("Ⅵ", 100F);
        valueHash.put("Ⅶ", 22F);
        valueHash.put("Ⅷ", 22F);
        valueHash.put("Ⅸ", 62F);
        valueHash.put("Ⅹ", 82F);
        valueHash.put("Ⅺ", 22F);
        valueHash.put("Ⅻ", 92F);
        valueHash.put("XIII", 72F);
        radarView.addData(valueHash);
        radarView.setAxisNumb(6);

        SeekBar axisNumb = (SeekBar) findViewById(R.id.sb_axisNumb);
        SeekBar axis = (SeekBar) findViewById(R.id.sb_axisTick);
        SeekBar sb_value = (SeekBar) findViewById(R.id.sb_value);

        CheckBox cb_fillStroke = (CheckBox) findViewById(R.id.cb_fillStroke);
        CheckBox cb_circle = (CheckBox) findViewById(R.id.cb_circle);
        CheckBox cb_fill_and_stroke = (CheckBox) findViewById(R.id.cb_fill_and_stroke);
        CheckBox cb_fillStrok2 = (CheckBox) findViewById(R.id.cb_fillStrok2);
        CheckBox cb_gridding = (CheckBox) findViewById(R.id.cb_gridding);
        CheckBox cb_dot = (CheckBox) findViewById(R.id.cb_dot);

        setOnProgressChangeListener(axisNumb, 0);
        setOnProgressChangeListener(axis, 1);
        setOnProgressChangeListener(sb_value, 2);
        cb_fillStroke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setStroke(!isChecked);
                radarView.refreshView();
            }
        });
        cb_circle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setCircle(isChecked);
                radarView.refreshView();
            }
        });
        cb_fill_and_stroke.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setFillAndStrock(isChecked);
                radarView.refreshView();
            }
        });
        cb_fillStrok2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setValeStroke(isChecked);
                radarView.refreshView();
            }
        });
        cb_gridding.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setGridding(isChecked);
                radarView.refreshView();
            }
        });
        cb_dot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                radarView.setDot(isChecked);
                radarView.refreshView();
            }
        });

    }

    private void setOnProgressChangeListener(SeekBar seekBar, final int param) {
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (param == 0) {
                    radarView.setAxisNumb(progress + 3);
                } else if (param == 1) {
                    radarView.setAxisTick(progress + 2);
                } else if (param == 2) {
                    radarView.changeParams("III", progress);
                }
                radarView.refreshView();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    }
}
