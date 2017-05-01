package com.herce.pf_moviles.Objects;

/**
 * Created by Herce on 30/04/2017.
 */

public class Product {
    private Integer ProductID;
    private String Name;
    private String Description;
    private String ImageURL;
    private Double Price;
    private String Brand;
    private Integer Category;

    public void setProductID(Integer ProductID) {
        this.ProductID = ProductID;
    }

    public Integer getProductID() {
        return ProductID;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getDescription() {
        return Description;
    }

    public void setImageURL(String ImageURL) {
        this.ImageURL = ImageURL;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setPrice(Double Price) {
        this.Price = Price;
    }

    public Double getPrice(){
        return Price;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getBrand(){
        return Brand;
    }

    public void setCategory(Integer Category) {
        this.Category = Category;
    }

    public Integer getCategory(){
        return Category;
    }

}
