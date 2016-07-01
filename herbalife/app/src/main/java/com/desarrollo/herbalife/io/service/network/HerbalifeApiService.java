package com.desarrollo.herbalife.io.service.network;

import com.desarrollo.herbalife.io.model.BannerResponse;
import com.desarrollo.herbalife.io.model.DetailResponse;
import com.desarrollo.herbalife.io.model.FavoriteResponse;
import com.desarrollo.herbalife.io.model.GeneralResponse;
import com.desarrollo.herbalife.io.model.LoginResponse;
import com.desarrollo.herbalife.io.model.PublicationResponse;
import com.desarrollo.herbalife.io.model.SearhResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Desarrollo on 23/01/16.
 */
public interface HerbalifeApiService {

    @FormUrlEncoded
    @POST(HerbalifeApiConstants.LOGIN_PATH)
    void postRegisterTicket(@Field("imei")String imei,
                            @Field("plataforma")String plataforma, @Field("so")String so,
                            @Field("version_so")String version_so, @Field("marca")String marca,
                            @Field("modelo")String modelo, @Field("operador")String operador,
                            @Field("tipo_imei")String tipo_imei, @Field("id_herbalife")String id_herbalife,
                            Callback<LoginResponse> loginResponse);

    @GET(HerbalifeApiConstants.BANNER_PATH)
    void getBanner(@Header("token") String contentRange,
                            Callback<BannerResponse> bannerResponse);

    @GET(HerbalifeApiConstants.PUBLICATION_PATH)
    void getPublication(@Header("token") String contentRange,
                   Callback<PublicationResponse> bannerResponse);

    @GET(HerbalifeApiConstants.DETAIL_PATH)
    void getDetailCategory(@Header("token") String contentRange, @Query("categoria") String category,
                        Callback<DetailResponse> detailResponse);


    @GET(HerbalifeApiConstants.SEARCH_PATH)
    void getSearhCategory(@Header("token") String contentRange, @Query("busqueda") String category,
                           Callback<SearhResponse> searhResponse);

    @GET(HerbalifeApiConstants.FAVORITE_PATH)
    void getFavorite(@Header("token") String contentRange,
                          Callback<FavoriteResponse> favoriteResponse);
    @FormUrlEncoded
    @POST(HerbalifeApiConstants.ADD_FAVORITE_PATH)
    void postFavorite(@Header("token") String contentRange, @Field("id") String idPublication,
                      @Field("favorito") String favorito,
                     Callback<GeneralResponse> generalResponse);
    @FormUrlEncoded
    @POST(HerbalifeApiConstants.DELETE_PATH)
    void postDelete(@Header("token") String contentRange, @Field("id") String idPublication,
                    Callback<GeneralResponse> generalResponse);

    @FormUrlEncoded
    @POST(HerbalifeApiConstants.SHOW_PATH)
    void postShow(@Header("token") String contentRange, @Field("id") String idPublication,
                    Callback<GeneralResponse> generalResponse);
}
