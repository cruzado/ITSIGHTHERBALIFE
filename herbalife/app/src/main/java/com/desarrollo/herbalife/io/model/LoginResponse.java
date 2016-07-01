package com.desarrollo.herbalife.io.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class LoginResponse {
    @SerializedName("resultado")
    String result;
    @SerializedName("mensaje")
    String message;
    @SerializedName("token")
    String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
