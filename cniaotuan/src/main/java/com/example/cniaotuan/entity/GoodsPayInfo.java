package com.example.cniaotuan.entity;

/**
 * Created by hongkl on 16/9/26.
 */
public class GoodsPayInfo extends BaseModel {
    private String orderID;
    private String product;
    private String imageUrl;
    private boolean orderStatus;
    private String price;

    public GoodsPayInfo(String orderID, String product, String imageUrl, boolean orderStatus, String price) {
        this.orderID = orderID;
        this.product = product;
        this.imageUrl = imageUrl;
        this.orderStatus = orderStatus;
        this.price = price;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
