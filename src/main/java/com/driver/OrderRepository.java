package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
public class OrderRepository {
    private HashMap < Integer, Order> orderHashMap;
    private HashMap < Integer, DeliveryPartner> deliveryPartnerHashMap;
    private HashMap < Integer, ArrayList < Integer >> deliveryPartnerOrderHashMap;
    private HashMap < Integer, Boolean> orderAssignedHashMap;
    private HashMap < Integer, Integer> orderDeliveryPartnermap;

    public OrderRepository(HashMap< Integer, Order> orderHashMap,
                           HashMap< Integer, DeliveryPartner> deliveryPartnerHashMap,
                           HashMap< Integer, ArrayList< Integer>> deliveryPartnerOrderHashMap,
                           HashMap< Integer, Boolean> orderAssignedHashMap,
                           HashMap< Integer, Integer> orderDeliveryPartnermap) {
        this.orderHashMap = orderHashMap;
        this.deliveryPartnerHashMap = deliveryPartnerHashMap;
        this.deliveryPartnerOrderHashMap = deliveryPartnerOrderHashMap;
        this.orderAssignedHashMap = orderAssignedHashMap;
        this.orderDeliveryPartnermap = orderDeliveryPartnermap;
    }

//    1
    public void addOrder(Order order){
        orderHashMap.put(order.getId(),order);
    }

//    2
    public void addPartner(String partnerId){
        DeliveryPartner d =new DeliveryPartner(partnerId);
        deliveryPartnerHashMap.put(Integer.parseInt(partnerId),d);
    }

//    3
    public void addOrderPartnerPair(String orderId, String partnerId){
        int pId = Integer.parseInt(partnerId);
        int oId = Integer.parseInt(orderId);
        if(orderHashMap.containsKey(oId)
                && deliveryPartnerHashMap.containsKey(pId)){
            ArrayList<Integer> orders = new ArrayList<>();
            if(deliveryPartnerOrderHashMap.containsKey(pId))
                orders = deliveryPartnerOrderHashMap.get(pId);
            orders.add(oId);
            orderAssignedHashMap.put(oId,false);
            orderDeliveryPartnermap.put(oId,pId);
            DeliveryPartner d = deliveryPartnerHashMap.get(pId);
            d.setNumberOfOrders(orders.size());
            deliveryPartnerOrderHashMap.put(pId,orders);
        }
    }

//    4
    public Order getOrderById(String orderId){
        int oId = Integer.parseInt(orderId);
        Order order = orderHashMap.get(oId);
        order.getSetDeliveryTime(intToStringTime(order.getDeliveryTime()));
        return order;
    }

//    5
    public DeliveryPartner getPartnerById(String partnerId){
        int pId = Integer.parseInt(partnerId);
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(pId);
        return deliveryPartner;
    }

//    6
    public int getOrderCountByPartnerId(String partnerId){
        int pId = Integer.parseInt(partnerId);
         DeliveryPartner partner = deliveryPartnerHashMap.get(pId);
         return partner.getNumberOfOrders();
    }

//    7
    public ArrayList<Order> getOrdersByPartnerId(String partnerId){
        int pId = Integer.parseInt(partnerId);
        ArrayList<Order> orders = new ArrayList<Order>();
        ArrayList<Integer> orderIds = deliveryPartnerOrderHashMap.get(pId);
        for (int orderId : orderIds){
            Order order = orderHashMap.get(orderId);
            order.getSetDeliveryTime(intToStringTime(order.getDeliveryTime()));
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
        int pId = Integer.parseInt(partnerId);
        ArrayList<Integer> orderIds = new ArrayList<Integer>();
        orderIds = deliveryPartnerOrderHashMap.get(pId);
        int leftCount = orderIds.size();
        for (Integer oId : orderIds) {
            Order order = orderHashMap.get(oId);
            if(timeT >= order.getDeliveryTime()){
                orderAssignedHashMap.put(order.getId(),true);
                leftCount--;
            }
        }
        return leftCount;
    }

//    11
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int pId = Integer.parseInt(partnerId);
        ArrayList<Integer> orderIds = new ArrayList<>();
        orderIds = deliveryPartnerOrderHashMap.get(pId);
        int time  = 0 ;
        for(Integer oId : orderIds){
            if(orderAssignedHashMap.get(oId)){
                Order order = orderHashMap.get(oId);
                if(time < order.getDeliveryTime())
                    time = order.getDeliveryTime();
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
        int pId = Integer.parseInt(partnerId);
        if(deliveryPartnerHashMap.containsKey(pId)) {
            if (deliveryPartnerOrderHashMap.containsKey(pId)) {
                ArrayList<Integer> orders = new ArrayList<>();
                orders = deliveryPartnerOrderHashMap.get(pId);
                for (Integer oId : orders) {
                    if (orderAssignedHashMap.containsKey(oId)) {
                        orderAssignedHashMap.remove(oId);
                    }
                }
                deliveryPartnerOrderHashMap.remove(pId);
            }
            deliveryPartnerHashMap.remove(pId);
        }
    }

//    13
    public void deleteOrderById(String orderId){
        int oId = Integer.parseInt(orderId);
        if(orderHashMap.containsKey(oId)){
            if(orderAssignedHashMap.containsKey(oId)){
                ArrayList<Integer> orders = new ArrayList<>();
                int pId = orderDeliveryPartnermap.get(oId);
                orders = deliveryPartnerOrderHashMap.get(pId);
                orders.remove(oId);
                deliveryPartnerOrderHashMap.put(pId,orders);
                orderDeliveryPartnermap.remove(oId);
                orderAssignedHashMap.remove(oId);
            }
            orderHashMap.remove(oId);
        }
    }
}
