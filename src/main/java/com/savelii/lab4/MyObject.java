package com.savelii.lab4;

import lombok.Data;

@Data
public class MyObject {
    private int number;

    private String title;

//    private MyObject nestedObject;

    public MyObject(int number) {
        this.number = number;
        this.title =  "Я элеменит под номером "+number;
    }


}
