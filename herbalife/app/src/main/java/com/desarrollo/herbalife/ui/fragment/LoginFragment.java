package com.desarrollo.herbalife.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.desarrollo.herbalife.HerbalifeApp;
import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseActivity;
import com.desarrollo.herbalife.common.BaseFragment;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.io.model.BannerResponse;
import com.desarrollo.herbalife.io.model.LoginResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.activity.LoginActivity;
import com.desarrollo.herbalife.ui.activity.MenuActivity;
import com.desarrollo.herbalife.ui.view.BannerDialog;
import com.desarrollo.herbalife.ui.view.MessageDialog;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class LoginFragment extends BaseFragment {

    @Bind(R.id.currentID)
    EditText currentID;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.txtTerm)
    TextView txtTerm;
    HerbalifeLocalApiService herbalifeLocalApiService;
    HerbalifeApiService herbalifeApiService;
    Boolean bannerBool;
    String bannerUrl;

    public static LoginFragment newInstance() {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.bannerBool = false;
        return loginFragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_login;
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
        if(getLocalApiService().getToken().length()>0){
            Intent intent = new Intent(getContext(), MenuActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        Typeface faceBold=Typeface.createFromAsset(getContext().getAssets(),"fonts/Gotham-Bold.ttf");
        Typeface faceLigth=Typeface.createFromAsset(getContext().getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceCurve=Typeface.createFromAsset(getContext().getAssets(), "fonts/Gotham-BookItalic.ttf");
        currentID.setTypeface(faceCurve);
        btnLogin.setTypeface(faceBold);
        txtTerm.setTypeface(faceLigth);
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        bannerUrl = savedInstanceState.getString("bannerUrl");
    }

    @Override
    public void onResume() {
        super.onResume();
        HerbalifeApp.viewTracking("login");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(bannerUrl != null){
            Log.i("bannerUrl", "save bannerUrl ="+bannerUrl);
            savedInstanceState.putString("bannerUrl", bannerUrl);
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


    @OnClick(R.id.btnLogin)
    public void onLogin(){
        if(currentID.getText().toString().length()>0){
            ((BaseActivity)getActivity()).lockOrientation(true);
            startLoading();
            getApiService().postRegisterTicket(getLocalApiService().getImei(), getLocalApiService().getPlatform(),
                    getLocalApiService().getSO(), getLocalApiService().getAndroidVersion(), getLocalApiService().getManufacturer(),
                    getLocalApiService().getModel(), getLocalApiService().getOperator(), getLocalApiService().getImeiType(),
                    currentID.getText().toString(), new Callback<LoginResponse>() {
                        @Override
                        public void success(LoginResponse loginResponse, Response response) {
                            Log.i("Login", "LoginResponse = " + loginResponse.toString());
                            if (loginResponse.getResult() != null) {
                                if (loginResponse.getResult().compareTo("1") == 0) {
                                    ((BaseActivity)getActivity()).lockOrientation(false);
                                    getLocalApiService().saveToken(loginResponse.getToken());
                                    Intent intent = new Intent(getContext(), MenuActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    stopLoading();
                                    ((BaseActivity)getActivity()).lockOrientation(false);
                                    MessageDialog.showMessageDialog(getContext(), loginResponse.getMessage());
                                }
                            } else {
                                stopLoading();
                                ((BaseActivity)getActivity()).lockOrientation(false);
                                MessageDialog.showMessageDialog(getContext(), "Ocurrio un error con la conexión");
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("error", "errorResponse = " + error.toString());
                            stopLoading();
                            ((BaseActivity)getActivity()).lockOrientation(false);
                            MessageDialog.showMessageDialog(getContext(), "Ocurrio un error con la conexión");
                        }
                    });
        }else{
            MessageDialog.showMessageDialog(getContext(), "Ingrese su id de Herbalife");
        }

    }

}
