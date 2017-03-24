package com.clock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 时钟样式01
 * Created by sunyan on 17/3/22.
 */

public class Clock01Activity extends AppCompatActivity {
    private Aclock01 clock01;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock01);
        clock01 = (Aclock01) findViewById(R.id.clock01);
        clock01.start();
    }

    public static void activityStart(Context context){
        Intent intent = new Intent(context,Clock01Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clock01.stop();
    }
}
