package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class ShowBigImageActivity extends AppCompatActivity {

    private BigImageView bigImageView;
    private Button loadingBigPictureBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);
        bigImageView = findViewById(R.id.bigImageView);
        loadingBigPictureBtn = findViewById(R.id.loadingBigPictureBtn);
        bigImageView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    bigImageView.loadImage(getAssets().open("big_picture.jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loadingBigPictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bigImageView.loadImage(getAssets().open("big_picture.jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}