package com.desarrollo.herbalife.io.service.network;

import retrofit.RestAdapter;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class HerbalifeApiAdapter {
    private static RestAdapter API_ADAPTER;
    public static RestAdapter getInstance(){
        //The adapter will be a singleton
        if(API_ADAPTER == null)
            API_ADAPTER = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.BASIC)
                    .setEndpoint(HerbalifeApiConstants.BASE_URL)
                    .build();


        return API_ADAPTER;
    }
}
