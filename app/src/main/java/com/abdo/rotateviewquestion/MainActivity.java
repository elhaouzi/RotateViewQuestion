package com.abdo.rotateviewquestion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FixedPosLayout fixedPosLayout = findViewById(R.id.fixedPosLayout);

        MyView myView = new MyView(this);

        FixedPosLayout.LayoutParams params = new FixedPosLayout.LayoutParams(300, 80, 200, 400);
        fixedPosLayout.addView(myView, params);
    }
}