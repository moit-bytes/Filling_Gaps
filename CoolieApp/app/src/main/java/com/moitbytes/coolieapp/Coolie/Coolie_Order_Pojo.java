package com.moitbytes.coolieapp.Coolie;

public class Coolie_Order_Pojo
{
    String phone;
    String order_id;
    String customer_first_name;
    String customer_last_name;
    String customer_phone;
    String order_time;
    String order_date;
    String station_name;
    String train_name;
    int n_wheelchair;
    int n_trolley;
    int n_containers;
    int n_bags;
    float amount;
    boolean order_completed;
    boolean order_cancelled;
    String booked_at;

    public Coolie_Order_Pojo()
    {

    }

    public Coolie_Order_Pojo(String phone, String order_id, String customer_first_name,
                             String customer_last_name, String customer_phone, String order_time,
                             String order_date, String station_name, String train_name,
                             int n_wheelchair, int n_trolley, int n_containers, int n_bags,
                             float amount,
                             boolean order_completed, boolean order_cancelled, String booked_at) {
        this.phone = phone;
        this.order_id = order_id;
        this.customer_first_name = customer_first_name;
        this.customer_last_name = customer_last_name;
        this.customer_phone = customer_phone;
        this.order_time = order_time;
        this.order_date = order_date;
        this.station_name = station_name;
        this.train_name = train_name;
        this.n_wheelchair = n_wheelchair;
        this.n_trolley = n_trolley;
        this.n_containers = n_containers;
        this.n_bags = n_bags;
        this.amount = amount;
        this.order_completed = order_completed;
        this.order_cancelled = order_cancelled;
        this.booked_at = booked_at;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_first_name() {
        return customer_first_name;
    }

    public void setCustomer_first_name(String customer_first_name) {
        this.customer_first_name = customer_first_name;
    }

    public String getCustomer_last_name() {
        return customer_last_name;
    }

    public void setCustomer_last_name(String customer_last_name) {
        this.customer_last_name = customer_last_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
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

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
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
