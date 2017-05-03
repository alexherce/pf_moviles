package com.herce.pf_moviles.Objects;

/**
 * Created by Herce on 02/05/2017.
 */

public class Order {
    private Integer orderID;
    private Integer customerID;
    private Integer sellerID;
    private Double total;
    private String dateCreated;
    private Integer status;

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setCustomerID(Integer sellerID) {
        this.sellerID = sellerID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setSellerID(Integer sellerID) {
        this.sellerID = sellerID;
    }

    public Integer getSellerID() {
        return sellerID;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
