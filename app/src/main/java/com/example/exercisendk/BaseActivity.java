package com.example.exercisendk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;

public class BaseActivity extends AppCompatActivity {

    private boolean isIntercept = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isIntercept) return true;
        return super.dispatchTouchEvent(ev);
    }

    protected void setIntercept(boolean isIntercept){
        this.isIntercept = isIntercept;
    }


    @Override
    public void onBackPressed() {
        if(isIntercept) return;
        super.onBackPressed();
    }
}
