package com.example.exercisendk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.exercisendk.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

public class TestETActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_etactivity);
        viewpager = findViewById(R.id.viewpager);
        fragments.add(TestFragment.newInstance());
        fragments.add(TestFragment.newInstance());
        fragments.add(TestFragment.newInstance());
        fragments.add(TestFragment.newInstance());
        fragments.add(TestFragment.newInstance());
        viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }
}