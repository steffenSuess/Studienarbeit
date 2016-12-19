package com.example.steffensuess.price48;

/**
 * Created by steffensuess on 19.12.16.
 */

public class Offer {
    String shop_Name;
    String price;
    String price_With_Shipping;
    String currency;
    String url;

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
