package com.desarrollo.herbalife.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class BannerResponse {
    @SerializedName("resultado")
    String result;
    @SerializedName("mensaje")
    String message;
    @SerializedName("data")
    DataResponse data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public class DataResponse{
        @SerializedName("url")
        String url;
        @SerializedName("mostrar_detalle")
        String detail;
        @SerializedName("tipo")
        String type;
        @SerializedName("id")
        String id;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
}
