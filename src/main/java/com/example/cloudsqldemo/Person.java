package com.example.cloudsqldemo;

public class Person {

    private String firstName;
    private String sex;
    private int qty;
    private int year;
  
    public Person() {
    }
  
    public Person(String firstName, String sex, int qty, int year) {
      this.firstName = firstName;
      this.sex = sex;
      this.qty = qty;
      this.year = year;
      
    }


    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }
  
    @Override
    public String toString() {
      return "firstName: " + firstName + ", year: " + year;
    }
  
  }