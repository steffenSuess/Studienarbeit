package com.example.steffensuess.price48;

/**
 * Created by steffensuess on 07.01.17.
 */

public class SearchQuery {

    int id;
    String imageURL;
    String productName;
    String searchText;
    String searchEANNumber;
    String date;
    String price;
    String shopName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchEANNumber() {
        return searchEANNumber;
    }

    public void setSearchEANNumber(String searchEANNumber) {
        this.searchEANNumber = searchEANNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
