package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class OrderRepository {
    private HashMap < String, Order> orderHashMap;
    private HashMap < String, DeliveryPartner> deliveryPartnerHashMap;
    private HashMap < String, ArrayList < String >> deliveryPartnerOrderHashMap;
    private HashMap < String, Boolean> orderAssignedHashMap;
    private HashMap < String, String> orderDeliveryPartnerMap;

    public OrderRepository(HashMap< String, Order> orderHashMap,
                           HashMap< String, DeliveryPartner> deliveryPartnerHashMap,
                           HashMap< String, ArrayList< String>> deliveryPartnerOrderHashMap,
                           HashMap< String, Boolean> orderAssignedHashMap,
                           HashMap< String, String> orderDeliveryPartnerMap) {
        this.orderHashMap = orderHashMap;
        this.deliveryPartnerHashMap = deliveryPartnerHashMap;
        this.deliveryPartnerOrderHashMap = deliveryPartnerOrderHashMap;
        this.orderAssignedHashMap = orderAssignedHashMap;
        this.orderDeliveryPartnerMap = orderDeliveryPartnerMap;
    }

//    1
    public void addOrder(Order order){
        orderHashMap.put(order.getId(),order);
    }

//    2
    public void addPartner(String partnerId){
        DeliveryPartner d =new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(d.getId(),d);
    }

//    3
    public void addOrderPartnerPair(String orderId, String partnerId){
        if(orderHashMap.containsKey(orderId)
                && deliveryPartnerHashMap.containsKey(partnerId)){
            ArrayList<String> orders = new ArrayList<>();
            if(deliveryPartnerOrderHashMap.containsKey(partnerId))
                orders = deliveryPartnerOrderHashMap.get(partnerId);
            orders.add(orderId);
            orderAssignedHashMap.put(orderId,false);
            orderDeliveryPartnerMap.put(orderId,partnerId);
            DeliveryPartner d = deliveryPartnerHashMap.get(partnerId);
            d.setNumberOfOrders(orders.size());
            deliveryPartnerOrderHashMap.put(partnerId,orders);
        }
    }

//    4
    public Order getOrderById(String orderId){
        Order order = null;
        if(orderHashMap.containsKey(orderId))
            order = orderHashMap.get(orderId);
        return order;
    }

//    5
    public DeliveryPartner getPartnerById(String partnerId){
        DeliveryPartner deliveryPartner = null;
        if(deliveryPartnerHashMap.containsKey(partnerId))
            deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        return deliveryPartner;
    }

//    6
    public Integer getOrderCountByPartnerId(String partnerId){
        Integer result = null;
        if(deliveryPartnerHashMap.containsKey(partnerId)) {
            DeliveryPartner partner = deliveryPartnerHashMap.get(partnerId);
            result = partner.getNumberOfOrders();
        }
        return result;
    }

//    7
    public ArrayList<String> getOrdersByPartnerId(String partnerId){
        ArrayList<String> orders = null;
        if(deliveryPartnerOrderHashMap.containsKey(partnerId))
            orders = deliveryPartnerOrderHashMap.get(partnerId);
        return orders;
    }

//    8
    public ArrayList<String> getAllOrders(){
        return new ArrayList<>(orderHashMap.keySet());
    }

//    9
    public int getCountOfUnassignedOrders(){
        return orderHashMap.size()-orderAssignedHashMap.size();
    }

//    10
    public Integer getOrdersLeftAfterGivenTime(String time, String partnerId){
        Integer count  = null;
        int timeT = (Integer.parseInt(time.substring(0,1))*60 + Integer.parseInt(time.substring(3,4)));
        ArrayList<String> orderIds = new ArrayList<>();
        if(deliveryPartnerOrderHashMap.containsKey(partnerId)) {
            orderIds = deliveryPartnerOrderHashMap.get(partnerId);
            int leftCount = orderIds.size();
            for (String orderId : orderIds) {
                Order order = orderHashMap.get(orderId);
                if(timeT >= order.getDeliveryTime()){
                    orderAssignedHashMap.put(order.getId(),true);
                    leftCount--;
                }
            }
            count  = leftCount;
        }
        return count;
    }

//    11
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String lastDeliveryTime = "";
        ArrayList<String> orderIds = new ArrayList<>();
        if(deliveryPartnerOrderHashMap.containsKey(partnerId)) {
            orderIds = deliveryPartnerOrderHashMap.get(partnerId);
            int time = 0;
            for (String orderId : orderIds) {
                if (orderAssignedHashMap.get(orderId)) {
                    Order order = orderHashMap.get(orderId);
                    if (time < order.getDeliveryTime()) {
                        time = order.getDeliveryTime();
                    }
                }
            }
            lastDeliveryTime = intToStringTime(time);
        }
        return lastDeliveryTime;
    }

    public String intToStringTime(int time){
        int min = time%60;
        int hour = time/60;
        String result = Integer.toString(hour) + ":" + Integer.toString(min);
        return result;
    }
//    12
    public void deletePartnerById(String partnerId){
        if(deliveryPartnerHashMap.containsKey(partnerId)) {
            if (deliveryPartnerOrderHashMap.containsKey(partnerId)) {
                ArrayList<String> orders = new ArrayList<>();
                orders = deliveryPartnerOrderHashMap.get(partnerId);
                for (String orderId : orders) {
                    if (orderAssignedHashMap.containsKey(orderId)) {
                        orderAssignedHashMap.remove(orderId);
                    }
                }
                deliveryPartnerOrderHashMap.remove(partnerId);
            }
            deliveryPartnerHashMap.remove(partnerId);
        }
    }

//    13
    public void deleteOrderById(String orderId){
        if(orderHashMap.containsKey(orderId)){
            if(orderAssignedHashMap.containsKey(orderId)){
                ArrayList<String> orders = new ArrayList<>();
                String partnerId = orderDeliveryPartnerMap.get(orderId);
                orders = deliveryPartnerOrderHashMap.get(partnerId);
                orders.remove(orderId);
                deliveryPartnerOrderHashMap.put(partnerId,orders);
                orderDeliveryPartnerMap.remove(orderId);
                orderAssignedHashMap.remove(orderId);
            }
            orderHashMap.remove(orderId);
        }
    }
}
