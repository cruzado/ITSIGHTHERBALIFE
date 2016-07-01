package com.desarrollo.herbalife.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseActivity;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.domain.Category;
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.io.model.BannerResponse;
import com.desarrollo.herbalife.io.model.PublicationResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.fragment.FavoriteFragment;
import com.desarrollo.herbalife.ui.fragment.HomeFragment;
import com.desarrollo.herbalife.ui.fragment.SearhFragment;
import com.desarrollo.herbalife.ui.fragment.WebViewFragment;
import com.desarrollo.herbalife.ui.view.MessageDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 23/01/16.
 */
public class MenuActivity extends BaseActivity {
    WebViewFragment webViewFragment;
    boolean bannerBool, bannerCall;
    boolean result;
    boolean rotate;
    boolean block;
    String bannerUrl;
    @Bind(R.id.lnyClose)
    LinearLayout lnyClose;
    @Bind(R.id.lnyBanner)
    LinearLayout lnyBanner;

    String menuHome = "Home";
    String menuFavorite = "Favorite";
    String menuSearh = "Searh";

    @Bind(R.id.lnyHome)
    LinearLayout lnyHome;
    @Bind(R.id.lnyFavorite)
    LinearLayout lnyFavorite;
    @Bind(R.id.lnySearh)
    LinearLayout lnySearh;

    @Bind(R.id.imgHome)
    ImageView imgHome;
    @Bind(R.id.imgFavorite)
    ImageView imgFavorite;
    @Bind(R.id.imgSearh)
    ImageView imgSearh;

    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    ArrayList<Categorytable>categories;
    Publicationtable publication;
    String menuType, idPublication;

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume","onResume");
        if(menuType.compareTo(menuHome)==0 && !block && !rotate){
            getCategory();
        }else if(result){
            if(menuType.compareTo(menuFavorite)==0){
                replaceContentFragment(FavoriteFragment.newInstance(), false);
                menuType = menuFavorite;
                loadViewMenu();
            }else if(menuType.compareTo(menuSearh)==0){
                replaceContentFragment(SearhFragment.newInstance(idPublication), false);
                menuType = menuSearh;
                loadViewMenu();
            }
            result = false;
        }
        rotate = false;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_menu;
    }

    @Override
    protected void setupView() {
        menuType = menuHome;
        bannerBool = false;
        lnyBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupLoader();
        getCategory();
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        setupLoader();
        rotate = true;
        menuType = savedInstanceState.getString("menuType");
        idPublication = savedInstanceState.getString("idPublication");
        lnyBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bannerBool = savedInstanceState.getBoolean("bannerBool");
        bannerCall = savedInstanceState.getBoolean("bannerCall");

        if(bannerBool){
            lnyBanner.setVisibility(View.VISIBLE);
        }
        Log.i("menuType", "onRestoreView menuType = " + menuType);
        loadData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("menuType", menuType);
        savedInstanceState.putString("idPublication", idPublication);
        savedInstanceState.putBoolean("bannerBool", bannerBool);
        savedInstanceState.putBoolean("bannerCall", bannerCall);

        Log.i("menuType", "onSaveInstanceState menuType = " + menuType);
    }

    public void saveData(ArrayList<Category>categoriesAux, Publication publicationAux){
        Log.i("saveData","saveData menu");
        Categorytable.deleteAll(Categorytable.class);
        Publicationtable.deleteAll(Publicationtable.class);
        categories = new ArrayList<>();
        for(Category category : categoriesAux){
            Categorytable categorytable = category.getCategoryTable();
            categorytable.save();
            categories.add(categorytable);
        }

        if(publicationAux != null){
            publication = publicationAux.getPublicationTable();
            publication.save();
            idPublication = publication.getPublicationId();
        }else{
            publication = null;
            idPublication = "";
        }
    }

    public void loadData(){
        List<Publicationtable> publications;
        Log.i("loadData", "idPublication = " + idPublication);
        if(idPublication != null){
            publications = Publicationtable.find(Publicationtable.class, "publication_id = ?", idPublication);
        }else{
            publications = Publicationtable.find(Publicationtable.class, "publication_category = ?", "H");
        }
        List<Categorytable> categoriesAux = Categorytable.listAll(Categorytable.class);
        Log.i("loadData", "loadData");
        Log.i("loadData", "publications =" + publications.size());
        Log.i("loadData", "categoriesAux =" + categoriesAux.size());

        if(categoriesAux.size()>0){
            if(publications.size()>0){
                categories = new ArrayList<>();
                for(Categorytable category : categoriesAux){
                    categories.add(category);
                }
                publication = publications.get(0);
                if(menuType.compareTo(menuHome)==0){
                    replaceContentFragment(HomeFragment.newInstance(categories,publication), false);
                }
            }else {
                getCategory();
            }
        }else{
            getCategory();
        }
        loadViewMenu();
    }
    @Override
    protected void onTraslucentBackgroundUpdated(boolean backgroundEnable) {

    }


    public void getCategory(){
        lockOrientation(true);
        block = true;
        startLoading();
        getApiService().getPublication(getLocalApiService().getToken(), new Callback<PublicationResponse>() {
            @Override
            public void success(PublicationResponse publicationResponse, Response response) {

                if (publicationResponse.getResult() != null) {
                    if (publicationResponse.getResult().compareTo("1") == 0) {
                        Log.i("plub", "publicationResponse =" + publicationResponse.toString());
                        ArrayList<Category> categoriesAux = publicationResponse.getDataResponse().getCategorys();
                        Publication publicationAux = publicationResponse.getDataResponse().getPublication();
                        if(publicationAux != null){
                            publicationAux.setPublicationCategory("H");
                            getLocalApiService().saveId(publicationAux.getPublicationId());
                        }
                        saveData(categoriesAux, publicationAux);
                        replaceContentFragment(HomeFragment.newInstance(categories, publication), false);
                        if(bannerCall){
                            stopLoading();
                            block = false;
                            lockOrientation(false);
                        }else{
                            loadBannerService();
                        }

                    } else {
                        stopLoading();
                        MessageDialog.showMessageDialog(MenuActivity.this, publicationResponse.getMessage(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                    }
                } else {
                    stopLoading();
                    MessageDialog.showMessageDialog(MenuActivity.this, "Ocurrio un error con la conexión", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                block = false;
                stopLoading();
                MessageDialog.showMessageDialog(MenuActivity.this, "Ocurrio un error con la conexión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

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
        if(menuType.compareTo(menuSearh)!=0){
            replaceContentFragment(SearhFragment.newInstance(idPublication), false);
            menuType = menuSearh;
            loadViewMenu();
        }

    }

    @OnClick(R.id.lnyHome)
    public void onHome(){
        if(menuType.compareTo(menuHome)!=0){
            replaceContentFragment(HomeFragment.newInstance(categories,publication), false);
            menuType = menuHome;
            loadViewMenu();
            getCategory();
        }
    }

    @OnClick(R.id.lnyFavorite)
    public void onFavorite(){
        if(menuType.compareTo(menuFavorite)!=0){
            replaceContentFragment(FavoriteFragment.newInstance(), false);
            menuType = menuFavorite;
            loadViewMenu();
        }
    }
    @OnClick(R.id.lnyClose)
    public void onClose(){
        closeBanner();
    }

    void loadViewMenu(){
        LinearLayout lnySeleted, lnyOff1, lnyOff2;
        if(menuType.compareTo(menuHome)==0){
            lnySeleted = lnyHome;
            imgHome.setImageResource(R.drawable.ico_home_tab_green);
            imgFavorite.setImageResource(R.drawable.ico_like_tab_black);
            imgSearh.setImageResource(R.drawable.ico_searh_tab_black);
            lnyOff1 = lnyFavorite;
            lnyOff2 = lnySearh;
        }else if(menuType.compareTo(menuFavorite)==0){
            lnySeleted = lnyFavorite;
            imgHome.setImageResource(R.drawable.ico_home_tab_off);
            imgFavorite.setImageResource(R.drawable.ico_like_tab);
            imgSearh.setImageResource(R.drawable.ico_searh_tab_off);
            lnyOff1 = lnyHome;
            lnyOff2 = lnySearh;
        }else{
            lnySeleted = lnySearh;
            imgHome.setImageResource(R.drawable.ico_home_tab_off);
            imgFavorite.setImageResource(R.drawable.ico_like_tab_off);
            imgSearh.setImageResource(R.drawable.ico_searh_tab);
            lnyOff1 = lnyFavorite;
            lnyOff2 = lnyHome;
        }
        final int version = Build.VERSION.SDK_INT;
        if(menuType.compareTo(menuHome)==0){
            if (version >= 23) {
                lnySeleted.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_herbalife_background));
                lnyOff1.setBackgroundColor(ContextCompat.getColor(this, R.color.green_herbalife_banner));
                lnyOff2.setBackgroundColor(ContextCompat.getColor(this, R.color.green_herbalife_banner));
            } else {
                lnySeleted.setBackgroundColor(this.getResources().getColor(R.color.gray_herbalife_background));
                lnyOff1.setBackgroundColor(this.getResources().getColor(R.color.green_herbalife_banner));
                lnyOff2.setBackgroundColor(this.getResources().getColor(R.color.green_herbalife_banner));
            }
        }else{
            if (version >= 23) {
                lnySeleted.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_herbalife_background));
                lnyOff1.setBackgroundColor(ContextCompat.getColor(this, R.color.clear));
                lnyOff2.setBackgroundColor(ContextCompat.getColor(this, R.color.clear));

            } else {
                lnySeleted.setBackgroundColor(this.getResources().getColor(R.color.gray_herbalife_background));
                lnyOff1.setBackgroundColor(this.getResources().getColor(R.color.clear));
                lnyOff2.setBackgroundColor(this.getResources().getColor(R.color.clear));
            }
        }
    }


    public void loadBanner(String url){
        bannerBool = true;
        lnyBanner.setVisibility(View.VISIBLE);
        Log.i("url", "url =" + url);
        webViewFragment = WebViewFragment.newInstance(url);
        replaceFragment(R.id.contentBanner, webViewFragment, false);

    }

    public void closeBanner(){
        bannerBool = false;
        lnyBanner.setVisibility(View.GONE);
    }

    void loadBannerService(){
        getApiService().getBanner(getLocalApiService().getToken(), new Callback<BannerResponse>() {
            @Override
            public void success(BannerResponse bannerResponse, Response response) {
                Log.i("Login", "BannerResponse = " + bannerResponse.toString());
                bannerCall = true;
                block = false;
                stopLoading();
                if (bannerResponse.getResult() != null) {
                    if (bannerResponse.getResult().compareTo("1") == 0) {
                        bannerUrl = bannerResponse.getData().getUrl();
                        loadBanner(bannerUrl);
                    }
                }
                lockOrientation(false);
            }

            @Override
            public void failure(RetrofitError error) {
                block = false;
                bannerCall = true;
                stopLoading();
                lockOrientation(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(!bannerBool){
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Result", "onActivityResult");
        Log.i("Result", "requestCode ="+requestCode);
        Log.i("Result", "resultCode ="+resultCode);

        if (requestCode == 700) {
            if(resultCode == Activity.RESULT_OK){
                result = true;
                String result=data.getStringExtra("result");
                if(result.compareTo(menuFavorite)==0){
                    menuType = menuFavorite;
                }else if(result.compareTo(menuSearh)==0){
                    menuType = menuSearh;
                }
            }

        }
    }
}