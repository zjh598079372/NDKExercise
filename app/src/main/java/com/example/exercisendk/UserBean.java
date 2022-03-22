package com.example.exercisendk;

import com.me.support.utils.LogUtil;

import org.litepal.crud.DataSupport;

public final class UserBean extends DataSupport {
    private String name;
    private int age;

    public static int no = 55;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}
