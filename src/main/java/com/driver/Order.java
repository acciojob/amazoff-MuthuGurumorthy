package com.driver;

public class Order {

    private String id;
    private int deliveryTim;

    private String setDeliveryTime;
    public Order(String id, int deliveryTim) {
        this.id = id;
        this.deliveryTim = deliveryTim;
    }

    public Order(String id, String deliveryTime) {
        int hour = Integer.parseInt(deliveryTime.substring(0,2));
        int minute = Integer.parseInt(deliveryTime.substring(3));
        // The deliveryTime has to converted from string to int and then stored in the attribute
        int time = ((hour * 60) + minute);
        this.id = id;
        this.deliveryTim = time;
    }

    public String getSetDeliveryTime(String time){
        this.setDeliveryTime=time;
    }


    public Integer getId() {
        Integer integer = Integer.valueOf(id);
        return integer;
    }

    public int getDeliveryTime() {
        return deliveryTim;
    }
}
