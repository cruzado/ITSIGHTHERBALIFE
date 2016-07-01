package com.desarrollo.herbalife.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.desarrollo.herbalife.HerbalifeApp;
import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseFragment;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.io.model.BannerResponse;
import com.desarrollo.herbalife.io.model.DetailResponse;
import com.desarrollo.herbalife.io.model.GeneralResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.activity.DetailActivity;
import com.desarrollo.herbalife.ui.activity.LoginActivity;
import com.desarrollo.herbalife.ui.activity.MenuActivity;
import com.desarrollo.herbalife.ui.adapter.HomeAdapter;
import com.desarrollo.herbalife.ui.view.MessageDialog;
import com.desarrollo.herbalife.ui.viewmodel.IRecyclerView;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class HomeFragment extends BaseFragment implements IRecyclerView{

    ArrayList<Categorytable>categories;
    Publicationtable publication;

    @Bind(R.id.recyclerHome)
    RecyclerView recyclerHome;
    HomeAdapter homeAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    public static HomeFragment newInstance(ArrayList<Categorytable>categories, Publicationtable publication) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.categories = categories;
        homeFragment.publication = publication;

        return homeFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public boolean hasOptionsMenuEnable() {
        return false;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void setupView(View view) {
        loadData();
    }
    @Override
    public void onResume() {
        super.onResume();
        HerbalifeApp.viewTracking("home");
    }
    public void loadData(){
        recyclerHome.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerHome.setLayoutManager(mLayoutManager);
        if(categories != null){
            homeAdapter = new HomeAdapter(categories, publication, getContext(), this);
            recyclerHome.setAdapter(homeAdapter);

        }
    }
    @Override
    protected void onRestoreView(Bundle savedInstanceState) {

    }


    private HerbalifeLocalApiService getLocalApiService(){
        if(herbalifeLocalApiService == null){
            herbalifeLocalApiService = new HerbalifeLocalApiService(getContext());
        }
        return herbalifeLocalApiService;
    }

    private HerbalifeApiService getApiService(){
        if(herbalifeApiService == null){
            herbalifeApiService = HerbalifeApiAdapter.getInstance().create(HerbalifeApiService.class);
        }
        return herbalifeApiService;
    }

    @Override
    public void onItemReciclerView(int position) {
        Log.i("item", "position = "+position);
        Categorytable categorytable;
        if(publication != null){
            categorytable = categories.get(position - 1);
        }else{
            categorytable = categories.get(position);
        }

        Log.i("item", "nameCategory = " + categorytable.getName());

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("idCategory",categorytable.getIdCategory());
        intent.putExtra("nameCategory", categorytable.getName());
        getActivity().startActivityForResult(intent, 700);
      //  startActivity(intent);
    }

    @Override
    public void onWebReciclerView() {
        try {
            HerbalifeApp.eventTracking("home", HerbalifeApp.EVENT_CLICK, "vermas/[" + publication.getPublicationTitle() + "]");
            getShowPublication(publication);
            String url = publication.getPublicationUrl();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }catch (Exception e){
            MessageDialog.showMessageDialog(getContext(), "No se puede ver el detalle de la publicación");

        }

    }

    @Override
    public void onFavoritePublication() {
        getFavoritePublication(publication);
    }

    public void getShowPublication(Publicationtable publication){
        getApiService().postShow(getLocalApiService().getToken(), publication.getPublicationId(), new Callback<GeneralResponse>() {
            @Override
            public void success(GeneralResponse generalResponse, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void getFavoritePublication(Publicationtable publicationSeleted){
        HerbalifeApp.eventTracking("home", HerbalifeApp.EVENT_CLICK, "favoritos/["+publication.getPublicationTitle()+"]");

        startLoading();
        String favoriteState = "0";
        if(publicationSeleted.getPublicationFavourite().compareTo("0")==0){
            favoriteState = "1";
        }
        getApiService().postFavorite(getLocalApiService().getToken(), publicationSeleted.getPublicationId(), favoriteState,
                new Callback<GeneralResponse>() {
                    @Override
                    public void success(GeneralResponse generalResponse, Response response) {
                        stopLoading();
                        if (generalResponse.getResult() != null) {
                            if (generalResponse.getResult().compareTo("1") == 0) {
                                MessageDialog.showMessageDialog(getActivity(), generalResponse.getMessage());
                                String favoriteState = "0";
                                getLocalApiService().saveId(publication.getPublicationId());
                                if (publication.getPublicationFavourite().compareTo("0") == 0) {
                                    favoriteState = "1";
                                }
                                publication.setPublicationFavourite(favoriteState);
                                publication.save();
                                loadData();
                            } else {
                                MessageDialog.showMessageDialog(getActivity(), generalResponse.getMessage());
                            }
                        } else {
                            MessageDialog.showMessageDialog(getActivity(), "Ocurrio un error con la conexión");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        stopLoading();
                        MessageDialog.showMessageDialog(getActivity(), "Ocurrio un error con la conexión");
                    }
                });

    }

    @Override
    public void onFacebookReciclerView() {
        HerbalifeApp.eventTracking("home", HerbalifeApp.EVENT_CLICK, "facebook["+publication.getPublicationTitle()+"]");
        ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        Log.i("click","onClick");
        Log.i("click", "url =" + publication.getPublicationUrl());
        if(publication.getPublicationUrl() != null){
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentDescription(
                            publication.getPublicationTitle())
                    .setContentUrl(Uri.parse(publication.getPublicationUrl()))
                    .setImageUrl(Uri.parse(publication.getPublicationPhoto()))
                    .build();
            shareDialog.show(content);
        }else{
            MessageDialog.showMessageDialog(getContext(), "No se puede compartir esta publicación");
        }

    }

    @Override
    public void onTwitterReciclerView() {
        HerbalifeApp.eventTracking("home", HerbalifeApp.EVENT_CLICK, "twitter["+publication.getPublicationTitle()+"]");
        Log.i("click", "onClick");
        Log.i("click","onTwitterReciclerView");
        URL urlPublication = null;
        try {
            urlPublication = new URL(publication.getPublicationUrl());
            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                    .text(publication.getPublicationTitle())
                    .url(urlPublication);
            builder.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            MessageDialog.showMessageDialog(getContext(), "No se puede compartir esta publicación");
        }
    }

    @Override
    public void onWhatsappReciclerView() {
        HerbalifeApp.eventTracking("home", HerbalifeApp.EVENT_CLICK, "whatsapp["+publication.getPublicationTitle()+"]");

        if(publication.getPublicationUrl() != null){
            try
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, publication.getPublicationTitle()+" " +publication.getPublicationUrl());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }
            catch ( ActivityNotFoundException ex  )
            {
                MessageDialog.showMessageDialog(getActivity(), "No tiene la aplicación de Whatsapp instalada");

            }
        }else{
            MessageDialog.showMessageDialog(getContext(), "No se puede compartir esta publicación");
        }

    }
}
