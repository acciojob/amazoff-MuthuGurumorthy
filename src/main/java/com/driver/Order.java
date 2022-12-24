package com.driver;

public class Order {

    private String id;
    private int deliveryTim;


    public Order(String id, String deliveryTime) {
        int hour = Integer.parseInt(deliveryTime.substring(0,2));
        int minute = Integer.parseInt(deliveryTime.substring(3));
        // The deliveryTime has to converted from string to int and then stored in the attribute
        int time = ((hour * 60) + minute);
        this.id = id;
        this.deliveryTim = time;
    }

    public Integer getId() {
        Integer integer = Integer.valueOf(id);
        return integer;
    }

    public int getDeliveryTime() {
        return deliveryTim;
    }
}
