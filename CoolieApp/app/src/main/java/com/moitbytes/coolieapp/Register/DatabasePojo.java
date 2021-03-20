package com.moitbytes.coolieapp.Register;

public class DatabasePojo
{
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String password;
    private String station_name;
    private boolean coolie;
    private boolean status;
    private float trolleyRate;
    private float bagRate;
    private float containerRate;
    private int tot_orders;
    private float wallet_balance;

    public DatabasePojo()
    {

    }

    public DatabasePojo(String first_name, String last_name,
                        String email, String phone, String password,
                        String station_name, boolean coolie, boolean status,
                        float trolleyRate, float bagRate,
                        float containerRate, int tot_orders, float wallet_balance) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.station_name = station_name;
        this.coolie = coolie;
        this.status = status;
        this.trolleyRate = trolleyRate;
        this.bagRate = bagRate;
        this.containerRate = containerRate;
        this.tot_orders = tot_orders;
        this.wallet_balance = wallet_balance;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public boolean isCoolie() {
        return coolie;
    }

    public void setCoolie(boolean coolie) {
        this.coolie = coolie;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public float getTrolleyRate() {
        return trolleyRate;
    }

    public void setTrolleyRate(float trolleyRate) {
        this.trolleyRate = trolleyRate;
    }

    public float getBagRate() {
        return bagRate;
    }

    public void setBagRate(float bagRate) {
        this.bagRate = bagRate;
    }

    public float getContainerRate() {
        return containerRate;
    }

    public void setContainerRate(float containerRate) {
        this.containerRate = containerRate;
    }

    public int getTot_orders() {
        return tot_orders;
    }

    public void setTot_orders(int tot_orders) {
        this.tot_orders = tot_orders;
    }

    public float getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(float wallet_balance) {
        this.wallet_balance = wallet_balance;
    }
}
