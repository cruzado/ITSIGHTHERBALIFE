package com.desarrollo.herbalife.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseActivity;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.domain.Category;
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.io.model.DetailResponse;
import com.desarrollo.herbalife.io.model.PublicationResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.fragment.DetailFragment;
import com.desarrollo.herbalife.ui.fragment.FavoriteFragment;
import com.desarrollo.herbalife.ui.fragment.HomeFragment;
import com.desarrollo.herbalife.ui.fragment.SearhFragment;
import com.desarrollo.herbalife.ui.view.MessageDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class DetailActivity extends BaseActivity {
    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    PublicationResponse mPublicationResponse;
    ArrayList<Publicationtable>publications;
    String idCategory;
    String nameCategory;
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void setupView() {
        idCategory = getIntent().getStringExtra("idCategory");
        nameCategory = getIntent().getStringExtra("nameCategory");
        Log.i("nameCategory", "nameCategory ="+nameCategory);
        setupLoader();
        getDetailCategory();
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        idCategory = savedInstanceState.getString("idCategory");
        nameCategory = savedInstanceState.getString("nameCategory");

        setupLoader();
        if(idCategory == null){
            finish();
        }else{
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("idCategory", idCategory);
        savedInstanceState.putString("nameCategory", nameCategory);

    }

    public void saveData(ArrayList<Publication>publicationsAux){
        Publicationtable.deleteAll(Publicationtable.class, "publication_category = ?", idCategory);
        publications = new ArrayList<>();
        for(Publication publication : publicationsAux){
            publication.setPublicationCategory(idCategory);
            Publicationtable publicationtable = publication.getPublicationTable();
            publicationtable.save();
            publications.add(publicationtable);
        }
    }

    public void loadData(){
        List<Publicationtable> publicationsAux = Publicationtable.find(Publicationtable.class, "publication_category = ?", idCategory);
        Log.i("loadData", "loadData");
        Log.i("loadData", "publications =" + publicationsAux.size());

        if(publicationsAux.size()>0){
            publications = new ArrayList<>();
            for(Publicationtable publication : publicationsAux){
                publications.add(publication);
            }
            replaceContentFragment(DetailFragment.newInstance(publications, nameCategory), false);
        }else {
            getDetailCategory();
        }
    }
    @Override
    protected void onTraslucentBackgroundUpdated(boolean backgroundEnable) {

    }


    public void getDetailCategory(){
        startLoading();
        getApiService().getDetailCategory(getLocalApiService().getToken(), idCategory, new Callback<DetailResponse>() {
            @Override
            public void success(DetailResponse detailResponse, Response response) {
                Log.i("response", "response = "+response.toString());
                stopLoading();
                if (detailResponse.getResult() != null) {
                    if (detailResponse.getResult().compareTo("1") == 0) {
                        Log.i("plub", "publicationResponse =" + detailResponse.toString());
                        ArrayList<Publication> publicationsAux = detailResponse.getPublications();
                        if(publicationsAux != null){
                            saveData(publicationsAux);
                            replaceContentFragment(DetailFragment.newInstance(publications, nameCategory), false);
                        }else{
                            MessageDialog.showMessageDialog(DetailActivity.this, "No cuenta con publicaciones disponibles", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

                        }

                    } else {
                        MessageDialog.showMessageDialog(DetailActivity.this, detailResponse.getMessage());
                    }
                } else {
                    MessageDialog.showMessageDialog(DetailActivity.this, "Ocurrio un error con la conexión");

                }
            }

            @Override
            public void failure(RetrofitError error) {
                stopLoading();
                MessageDialog.showMessageDialog(DetailActivity.this, "Ocurrio un error con la conexión");

            }
        });

    }

    private HerbalifeLocalApiService getLocalApiService(){
        if(herbalifeLocalApiService == null){
            herbalifeLocalApiService = new HerbalifeLocalApiService(this);
        }
        return herbalifeLocalApiService;
    }

    private HerbalifeApiService getApiService(){
        if(herbalifeApiService == null){
            herbalifeApiService = HerbalifeApiAdapter.getInstance().create(HerbalifeApiService.class);
        }
        return herbalifeApiService;
    }

    @OnClick(R.id.lnySearh)
    public void onSearh(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "Searh");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }



    @OnClick(R.id.lnyFavorite)
    public void onFavorite(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "Favorite");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}