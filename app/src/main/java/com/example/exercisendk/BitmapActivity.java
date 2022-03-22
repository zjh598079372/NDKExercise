package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BitmapActivity extends AppCompatActivity {
    private LinearLayout container_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);
        container_ll = findViewById(R.id.container_ll);
        addBtn(container_ll);
    }

    private void addBtn(LinearLayout container_ll) {
        TextView handWritting = new TextView(BitmapActivity.this);
        handWritting.setText("图片涂鸦");
        handWritting.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
        handWritting.setTextColor(Color.BLACK);
        handWritting.setGravity(Gravity.CENTER);
        handWritting.setBackgroundColor(Color.RED);
        handWritting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BitmapActivity.this,ShowImageActivity.class));
            }
        });
        int padding = (int) (getWindowManager().getDefaultDisplay().getWidth()*0.04f);
        handWritting.setPadding(0,padding,0,padding);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        container_ll.addView(handWritting,layoutParams);

    }


}
