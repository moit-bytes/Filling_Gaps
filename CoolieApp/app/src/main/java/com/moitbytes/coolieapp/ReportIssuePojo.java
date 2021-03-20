package com.moitbytes.coolieapp;

public class ReportIssuePojo
{
    String phone;
    String subject;
    String body;

    public ReportIssuePojo()
    {

    }
    public ReportIssuePojo(String phone, String subject, String body) {
        this.phone = phone;
        this.subject = subject;
        this.body = body;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
