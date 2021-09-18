package com.example.demo.cal;

public class Calculator {
    public static void main(String[] args) {

    }
    public static Integer addNums(Integer a, Integer b){
        if(a==null ||b==null) throw new ArithmeticException("can not add nulls");
        return a +b;
    }
}
