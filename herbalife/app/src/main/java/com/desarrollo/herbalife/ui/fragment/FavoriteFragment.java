package com.desarrollo.herbalife.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.desarrollo.herbalife.HerbalifeApp;
import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseFragment;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.io.model.FavoriteResponse;
import com.desarrollo.herbalife.io.model.GeneralResponse;
import com.desarrollo.herbalife.io.model.SearhResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.activity.MenuActivity;
import com.desarrollo.herbalife.ui.adapter.DetailAdapter;
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
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class FavoriteFragment extends BaseFragment implements IShareView{

    @Bind(R.id.txtTitleBar)
    TextView txtTitleBar;
    @Bind(R.id.recyclerHome)
    RecyclerView recyclerHome;
    DetailAdapter detailAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    ArrayList<Publicationtable> publications;

    public static FavoriteFragment newInstance() {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        return favoriteFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_favorite;
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
        loadView();
        startLoading();
        getApiService().getFavorite(getLocalApiService().getToken(), new Callback<FavoriteResponse>() {
            @Override
            public void success(FavoriteResponse favoriteResponse, Response response) {
                Log.i("response", "response = " + response.toString());
                stopLoading();
                if (favoriteResponse.getResult() != null) {
                    if (favoriteResponse.getResult().compareTo("1") == 0) {
                        Log.i("plub", "searhResponse =" + favoriteResponse.toString());
                        ArrayList<Publication> publicationsAux = favoriteResponse.getPublications();
                        if (publicationsAux != null) {
                            saveData(publicationsAux);
                        } else {
                            MessageDialog.showMessageDialog(getActivity(), "No se encontraron publicaciones");
                        }

                    } else {
                        MessageDialog.showMessageDialog(getActivity(), favoriteResponse.getMessage());
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
    public void loadView(){
        recyclerHome.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerHome.setLayoutManager(mLayoutManager);
        txtTitleBar.setText("FAVORITOS");
        Typeface faceBold=Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Bold.ttf");
        txtTitleBar.setTypeface(faceBold);
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        loadView();
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    public void loadData(){
        List<Publicationtable> publicationsAux = Publicationtable.find(Publicationtable.class, "publication_favourite = ?", "1");
        Log.i("loadData", "loadData");
        Log.i("loadData", "publications =" + publicationsAux.size());
        publications = new ArrayList<>();
        for(Publicationtable publication : publicationsAux){
            publications.add(publication);
        }
        reloadRecycleView();
    }
    @Override
    public void onResume() {
        super.onResume();
        HerbalifeApp.viewTracking("favoritos");
    }
    private void reloadRecycleView(){
        if(publications != null){
            detailAdapter = new DetailAdapter(publications, getContext(), this);
            recyclerHome.setAdapter(detailAdapter);
        }

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

    public void saveData(ArrayList<Publication>publicationsAux){
        List<Publicationtable> publicationsTemp = Publicationtable.find(Publicationtable.class, "publication_favourite = ?", "1");
        for(Publicationtable publication : publicationsTemp){
            publication.setPublicationFavourite("0");
            publication.save();
        }
        Log.i("saveData", "publicationsTemp count =" + publicationsTemp.size());
        publications = new ArrayList<>();
        for(Publication publication : publicationsAux){
            Publicationtable publicationtable;
            publicationtable = Publicationtable.findById(Publicationtable.class, Integer.parseInt(publication.getPublicationId()));
            if(publicationtable == null){
                publicationtable = publication.getPublicationTable();
            }else{
                publicationtable.setPublicationFavourite("1");
            }
            publicationtable.save();
            publications.add(publicationtable);
        }
        reloadRecycleView();
    }


    @Override
    public void onFacebookReciclerView(int position) {
        Publicationtable publicationtable = publications.get(position);
        HerbalifeApp.eventTracking("favoritos", HerbalifeApp.EVENT_CLICK, "facebook[" + publicationtable.getPublicationTitle() + "]");
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
        Publicationtable publicationtable = publications.get(position);
        HerbalifeApp.eventTracking("favoritos", HerbalifeApp.EVENT_CLICK, "twitter[" + publicationtable.getPublicationTitle() + "]");
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
        HerbalifeApp.eventTracking("favoritos", HerbalifeApp.EVENT_CLICK, "whatsapp[" + publicationtable.getPublicationTitle() + "]");
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
        HerbalifeApp.eventTracking("favoritos", HerbalifeApp.EVENT_CLICK, "favorito/[" + publicationSeleted.getPublicationTitle() + "]");
        String favoriteState = "0";
        if(publicationSeleted.getPublicationFavourite()!= null && publicationSeleted.getPublicationFavourite().compareTo("0")==0){
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
                                if(publicationSeleted.getPublicationFavourite()!= null && publicationSeleted.getPublicationFavourite().compareTo("0")==0){
                                    favoriteState = "1";
                                }
                                publicationSeleted.setPublicationFavourite(favoriteState);
                                publicationSeleted.save();
                                /*
                                if(favoriteState.compareTo("0")==0){
                                    publications.remove(publicationSeleted);
                                }*/
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
    public void onWebReciclerView(int position) {
        try {
            Publicationtable publicationtable = publications.get(position);
            HerbalifeApp.eventTracking("favoritos", HerbalifeApp.EVENT_CLICK, "vermas/[" + publicationtable.getPublicationTitle() + "]");
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
                                    publicationSeleted.setPublicationFavourite("0");
                                    publicationSeleted.save();
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
        MenuActivity menuActivity = (MenuActivity)getActivity();
        menuActivity.onHome();
    }
}
