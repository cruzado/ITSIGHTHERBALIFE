package com.desarrollo.herbalife.domain.orm;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Desarrollo on 24/01/16.
 */
public class Categorytable extends SugarRecord {
    @Unique
    String idCategory;
    String name;
    String total;

    public Categorytable() {
        super();
    }

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

    public Categorytable(String idCategory, String name, String total) {
        this.idCategory = idCategory;
        this.name = name;
        this.total = total;
    }
}
