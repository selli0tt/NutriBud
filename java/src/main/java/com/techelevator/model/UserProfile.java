package com.techelevator.model;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class UserProfile {

    private long userId;
    private String firstName;
    private String lastName;
    private int age;
    private LocalDate birthday;
    private double height;
    private double currentWeight;
    private double desiredWeight;
    private int calorieGoal;
    private String displayName;
    private int mealsPerDay;
    private String fileData;
    private int starCount;


    public UserProfile(){ }

    public UserProfile(long userId, String firstName, String lastName, int age, LocalDate birthday, double height,
                       double currentWeight, double desiredWeight, int calorieGoal, String displayName,
                       int mealsPerDay, String fileData, int starCount) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthday = birthday;
        this.height = height;
        this.currentWeight = currentWeight;
        this.desiredWeight = desiredWeight;
        this.calorieGoal = calorieGoal;
        this.displayName = displayName;
        this.mealsPerDay = mealsPerDay;
        this.fileData = fileData;
        this.starCount = starCount;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(int calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public double getDesiredWeight() {
        return desiredWeight;
    }

    public void setDesiredWeight(double desiredWeight) {
        this.desiredWeight = desiredWeight;
    }

    public int getMealsPerDay() { return mealsPerDay; }

    public void setMealsPerDay(int mealsPerDay) {this.mealsPerDay = mealsPerDay; }

    @Override
    public String toString() {
        return "UserProfile{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                ", height=" + height +
                ", currentWeight=" + currentWeight +
                ", desiredWeight=" + desiredWeight +
                ", calorieGoal=" + calorieGoal +
                ", displayName='" + displayName + '\'' +
                ", mealsPerDay=" + mealsPerDay +
                ", fileData='" + fileData + '\'' +
                ", starCount=" + starCount +
                '}';
    }
}
