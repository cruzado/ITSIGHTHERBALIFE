package com.desarrollo.herbalife.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.desarrollo.herbalife.HerbalifeApp;
import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseFragment;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.io.model.GeneralResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.adapter.DetailAdapter;
import com.desarrollo.herbalife.ui.adapter.HomeAdapter;
import com.desarrollo.herbalife.ui.view.MessageDialog;
import com.desarrollo.herbalife.ui.viewmodel.IRecyclerView;
import com.desarrollo.herbalife.ui.viewmodel.IShareView;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class DetailFragment extends BaseFragment implements IShareView{

    @Bind(R.id.txtTitleBar)
    TextView txtTitleBar;
    @Bind(R.id.recyclerHome)
    RecyclerView recyclerHome;
    DetailAdapter detailAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    ArrayList<Publicationtable> publications;
    String nameCategory;

    public static DetailFragment newInstance(ArrayList<Publicationtable> publications, String nameCategory) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.publications = publications;
        detailFragment.nameCategory = nameCategory;
        return detailFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_detail;
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

    public void loadData(){
        startLoading();
        if(nameCategory != null){
            txtTitleBar.setText(nameCategory);
            Typeface faceBold=Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Bold.ttf");
            txtTitleBar.setTypeface(faceBold);
        }
        recyclerHome.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerHome.setLayoutManager(mLayoutManager);
        if(publications != null){
            detailAdapter = new DetailAdapter(publications, getContext(), this);
            recyclerHome.setAdapter(detailAdapter);
        }
        stopLoading();
    }
    @Override
    public void onResume() {
        super.onResume();
        HerbalifeApp.viewTracking(nameCategory);
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
    public void onFacebookReciclerView(int position) {
        Publicationtable publicationtable = publications.get(position);
        HerbalifeApp.eventTracking(nameCategory, HerbalifeApp.EVENT_CLICK, "facebook[" + publicationtable.getPublicationTitle() + "]");

        ShareDialog shareDialog = new ShareDialog(this);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        Log.i("click","onClick");
        Log.i("click", "url =" + publicationtable.getPublicationUrl());
        if(publicationtable.getPublicationUrl() != null){
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentDescription(
                            publicationtable.getPublicationTitle())
                    .setContentUrl(Uri.parse(publicationtable.getPublicationUrl()))
                    .setImageUrl(Uri.parse(publicationtable.getPublicationPhoto()))
                    .build();
            shareDialog.show(content);
        }else{
            MessageDialog.showMessageDialog(getContext(), "No se puede compartir esta publicación");
        }

    }

    @Override
    public void onTwitterReciclerView(int position) {
        Log.i("click", "onClick");
        Log.i("click","onTwitterReciclerView");
        Publicationtable publicationtable = publications.get(position);
        HerbalifeApp.eventTracking(nameCategory, HerbalifeApp.EVENT_CLICK, "twitter[" + publicationtable.getPublicationTitle() + "]");
        URL urlPublication = null;
        try {
            urlPublication = new URL(publicationtable.getPublicationUrl());
            TweetComposer.Builder builder = new TweetComposer.Builder(getContext())
                    .text(publicationtable.getPublicationTitle())
                    .url(urlPublication);
            builder.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            MessageDialog.showMessageDialog(getContext(), "No se puede compartir esta publicación");
        }
    }

    @Override
    public void onWhatsappReciclerView(int position) {
        Publicationtable publicationtable = publications.get(position);
        HerbalifeApp.eventTracking(nameCategory, HerbalifeApp.EVENT_CLICK, "whatsapp[" + publicationtable.getPublicationTitle() + "]");
        if(publicationtable.getPublicationUrl() != null){
            try
            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, publicationtable.getPublicationTitle()+" " +publicationtable.getPublicationUrl());
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

    Publicationtable publicationSeleted;
    @Override
    public void onFavoriteView(int position) {
        startLoading();
        publicationSeleted = publications.get(position);
        HerbalifeApp.eventTracking(nameCategory, HerbalifeApp.EVENT_CLICK, "favoritos/[" + publicationSeleted.getPublicationTitle() + "]");

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
                                if(publicationSeleted.getPublicationFavourite().compareTo("0")==0){
                                    favoriteState = "1";
                                }
                                publicationSeleted.setPublicationFavourite(favoriteState);
                                publicationSeleted.save();
                                loadData();
                            }else {
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
    public void onDeleteView(int position) {

        publicationSeleted = publications.get(position);
        if(getLocalApiService().getID().compareTo(publicationSeleted.getPublicationId()) == 0){
            MessageDialog.showMessageDialog(getActivity(), "Esta publicación no se puede eliminar");
        }else{
            startLoading();
            getApiService().postDelete(getLocalApiService().getToken(), publicationSeleted.getPublicationId(),
                    new Callback<GeneralResponse>() {
                        @Override
                        public void success(GeneralResponse generalResponse, Response response) {
                            stopLoading();
                            if (generalResponse.getResult() != null) {
                                if (generalResponse.getResult().compareTo("1") == 0) {
                                    MessageDialog.showMessageDialog(getActivity(), generalResponse.getMessage());
                                    publications.remove(publicationSeleted);
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

    }

    @Override
    public void onWebReciclerView(int position) {
        try {
            Publicationtable publicationtable = publications.get(position);
            HerbalifeApp.eventTracking(nameCategory, HerbalifeApp.EVENT_CLICK, "vermas/[" + publicationtable.getPublicationTitle() + "]");
            getShowPublication(publicationtable);
            String url = publicationtable.getPublicationUrl();
            if(url != null){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }else{
                MessageDialog.showMessageDialog(getContext(), "No se puede ver el detalle de la publicación");
            }
        }catch (Exception e){
            MessageDialog.showMessageDialog(getContext(), "No se puede ver el detalle de la publicación");
        }

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

    @OnClick(R.id.btnBack)
    public void onBack(){
        getActivity().finish();
    }
}
