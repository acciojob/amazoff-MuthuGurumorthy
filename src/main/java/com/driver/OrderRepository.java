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
        Order order = orderHashMap.get(orderId);
        return order;
    }

//    5
    public DeliveryPartner getPartnerById(String partnerId){
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        return deliveryPartner;
    }

//    6
    public int getOrderCountByPartnerId(String partnerId){
         DeliveryPartner partner = deliveryPartnerHashMap.get(partnerId);
         return partner.getNumberOfOrders();
    }

//    7
    public ArrayList<Order> getOrdersByPartnerId(String partnerId){
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<String> orderIds = deliveryPartnerOrderHashMap.get(partnerId);
        for (String orderId : orderIds){
            Order order = orderHashMap.get(orderId);
            orders.add(order);
        }
        return orders;
    }

//    8
    public ArrayList<Order> getAllOrders(){
        ArrayList<Order> orders = new ArrayList<>();
        for(Order order : orderHashMap.values()){
            orders.add(order);
        }
        return orders;
    }

//    9
    public int getCountOfUnassignedOrders(){
        return orderHashMap.size()-orderAssignedHashMap.size();
    }

//    10
    public int getOrdersLeftAfterGivenTime(String time, String partnerId){
        int timeT = (Integer.parseInt(time.substring(0,1))*60 + Integer.parseInt(time.substring(3,4)));
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds = deliveryPartnerOrderHashMap.get(partnerId);
        int leftCount = orderIds.size();
        for (String orderId : orderIds) {
            Order order = orderHashMap.get(orderId);
            if(timeT >= order.getDeliveryTime()){
                orderAssignedHashMap.put(order.getId(),true);
                leftCount--;
            }
        }
        return leftCount;
    }

//    11
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        ArrayList<String> orderIds = new ArrayList<>();
        orderIds = deliveryPartnerOrderHashMap.get(partnerId);
        int time  = 0 ;
        for(String orderId : orderIds){
            if(orderAssignedHashMap.get(orderId)){
                Order order = orderHashMap.get(orderId);
                if(time < order.getDeliveryTime()) {
                    time = order.getDeliveryTime();
                }
            }
        }
        String result = intToStringTime(time);
        return result;
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
