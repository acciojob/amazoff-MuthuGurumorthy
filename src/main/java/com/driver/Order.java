package com.driver;

public class Order {

    private String id;
    private int deliveryTime;
    public Order(String id, int deliveryTim) {
        this.id = id;
        this.deliveryTime = deliveryTime;
    }

    public Order(String id, String deliveryTime) {
        int hour = Integer.parseInt(deliveryTime.substring(0,2));
        int minute = Integer.parseInt(deliveryTime.substring(3));
        this.id = id;
        this.deliveryTime = ((hour * 60) + minute);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }
}
