package com.moitbytes.coolieapp;

public class fcmTokenPojo
{
    String phone;
    String fcmToken;

    public  fcmTokenPojo()
    {

    }
    public fcmTokenPojo(String phone, String fcmToken) {
        this.phone = phone;
        this.fcmToken = fcmToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
