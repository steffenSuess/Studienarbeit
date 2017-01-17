package com.example.steffensuess.price48.Models;

import java.io.Serializable;

/**
 * Created by steffensuess on 19.12.16.
 */

public class Offer implements Serializable {
    String shop_Name;
    String price;
    String price_With_Shipping;
    String cost_For_Shipping;
    String currency;
    String url;
    String productName;
    String productImage;
    String availability;

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }


    public String getShop_Name() {
        return shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        this.shop_Name = shop_Name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_With_Shipping() {
        return price_With_Shipping;
    }

    public void setPrice_With_Shipping(String price_With_Shipping) {
        this.price_With_Shipping = price_With_Shipping;
    }

    public String getCost_For_Shipping() {
        return cost_For_Shipping;
    }

    public void setCost_For_Shipping(String cost_For_Shipping) {
        this.cost_For_Shipping = cost_For_Shipping;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
