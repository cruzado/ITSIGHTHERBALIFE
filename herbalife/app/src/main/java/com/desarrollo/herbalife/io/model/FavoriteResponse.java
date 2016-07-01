package com.desarrollo.herbalife.io.model;

import com.desarrollo.herbalife.domain.Publication;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Desarrollo on 24/01/16.
 */
public class FavoriteResponse {
    @SerializedName("resultado")
    String result;
    @SerializedName("mensaje")
    String message;
    @SerializedName("publicaciones")
    ArrayList<Publication> publications;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public void setPublications(ArrayList<Publication> publications) {
        this.publications = publications;
    }


}
