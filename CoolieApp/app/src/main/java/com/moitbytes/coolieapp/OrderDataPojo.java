package com.moitbytes.coolieapp.User;

public class OrderDataPojo
{
    String type;
    String phone;
    String coolie_first_name;
    String coolie_last_name;
    String coolie_phone;
    String order_time;
    String order_date;
    String station_name;
    int n_wheelchair;
    int n_trolley;
    int n_containers;
    int n_bags;
    float amount;
    boolean order_completed;
    boolean order_cancelled;
    String booked_at;

    public OrderDataPojo()
    {

    }

    public OrderDataPojo(String type, String phone, String coolie_first_name,
                         String coolie_last_name, String coolie_phone, String order_time,
                         String order_date, String station_name, int n_wheelchair,
                         int n_trolley, int n_containers, int n_bags, float amount,
                         boolean order_completed, boolean order_cancelled, String booked_at) {
        this.type = type;
        this.phone = phone;
        this.coolie_first_name = coolie_first_name;
        this.coolie_last_name = coolie_last_name;
        this.coolie_phone = coolie_phone;
        this.order_time = order_time;
        this.order_date = order_date;
        this.station_name = station_name;
        this.n_wheelchair = n_wheelchair;
        this.n_trolley = n_trolley;
        this.n_containers = n_containers;
        this.n_bags = n_bags;
        this.amount = amount;
        this.order_completed = order_completed;
        this.order_cancelled = order_cancelled;
        this.booked_at = booked_at;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCoolie_first_name() {
        return coolie_first_name;
    }

    public void setCoolie_first_name(String coolie_first_name) {
        this.coolie_first_name = coolie_first_name;
    }

    public String getCoolie_last_name() {
        return coolie_last_name;
    }

    public void setCoolie_last_name(String coolie_last_name) {
        this.coolie_last_name = coolie_last_name;
    }

    public String getCoolie_phone() {
        return coolie_phone;
    }

    public void setCoolie_phone(String coolie_phone) {
        this.coolie_phone = coolie_phone;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public int getN_wheelchair() {
        return n_wheelchair;
    }

    public void setN_wheelchair(int n_wheelchair) {
        this.n_wheelchair = n_wheelchair;
    }

    public int getN_trolley() {
        return n_trolley;
    }

    public void setN_trolley(int n_trolley) {
        this.n_trolley = n_trolley;
    }

    public int getN_containers() {
        return n_containers;
    }

    public void setN_containers(int n_containers) {
        this.n_containers = n_containers;
    }

    public int getN_bags() {
        return n_bags;
    }

    public void setN_bags(int n_bags) {
        this.n_bags = n_bags;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isOrder_completed() {
        return order_completed;
    }

    public void setOrder_completed(boolean order_completed) {
        this.order_completed = order_completed;
    }

    public boolean isOrder_cancelled() {
        return order_cancelled;
    }

    public void setOrder_cancelled(boolean order_cancelled) {
        this.order_cancelled = order_cancelled;
    }

    public String getBooked_at() {
        return booked_at;
    }

    public void setBooked_at(String booked_at) {
        this.booked_at = booked_at;
    }
}
