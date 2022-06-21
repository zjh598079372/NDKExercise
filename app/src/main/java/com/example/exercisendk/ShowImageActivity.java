package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.exercisendk.bitmap.HandWriteView;

public class ShowImageActivity extends AppCompatActivity {

    private HandWriteView handWriteView;
    private TextView clearTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        handWriteView = findViewById(R.id.handWriteView);
        clearTV = findViewById(R.id.clearTV);
        clearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handWriteView.clear();
            }
        });
    }
}
