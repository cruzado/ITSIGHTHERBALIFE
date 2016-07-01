package com.desarrollo.herbalife.io.model;

import com.desarrollo.herbalife.domain.Category;
import com.desarrollo.herbalife.domain.Publication;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Desarrollo on 24/01/16.
 */
public class PublicationResponse {
    @SerializedName("resultado")
    String result;
    @SerializedName("mensaje")
    String message;
    @SerializedName("data")
    DataResponse dataResponse;

    public class DataResponse {
        @SerializedName("categorias")
        ArrayList<Category> categorys;
        @SerializedName("publicacion")
        Publication publication;

        public ArrayList<Category> getCategorys() {
            return categorys;
        }

        public void setCategorys(ArrayList<Category> categorys) {
            this.categorys = categorys;
        }

        public Publication getPublication() {
            return publication;
        }

        public void setPublication(Publication publication) {
            this.publication = publication;
        }

        @Override
        public String toString() {
            String categorysString = "";
            for (Category category : categorys){
                categorysString = categorysString+"- category ="+category.toString();
            }
            return "DataResponse{" +
                    "categorys=" + categorysString +
                    ", publication=" + publication.toString() +
                    '}';
        }
    }

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

    public DataResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(DataResponse dataResponse) {
        this.dataResponse = dataResponse;
    }


}
