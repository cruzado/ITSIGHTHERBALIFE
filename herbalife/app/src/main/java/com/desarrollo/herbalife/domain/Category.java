package com.desarrollo.herbalife.domain;

import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Desarrollo on 24/01/16.
 */
public class Category{
    @SerializedName("id")
    String idCategory;
    @SerializedName("nombre")
    String name;
    @SerializedName("total")
    String total;


    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + idCategory + '\'' +
                ", name='" + name + '\'' +
                ", total='" + total + '\'' +
                '}';
    }

    public Categorytable getCategoryTable(){
            return new Categorytable(idCategory,name,total);
    }
}
