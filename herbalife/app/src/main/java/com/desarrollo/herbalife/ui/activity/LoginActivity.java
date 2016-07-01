package com.desarrollo.herbalife.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.common.BaseActivity;
import com.desarrollo.herbalife.common.BasePresenter;
import com.desarrollo.herbalife.io.model.BannerResponse;
import com.desarrollo.herbalife.io.service.local.HerbalifeLocalApiService;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiAdapter;
import com.desarrollo.herbalife.io.service.network.HerbalifeApiService;
import com.desarrollo.herbalife.ui.fragment.LoginFragment;
import com.desarrollo.herbalife.ui.fragment.WebViewFragment;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView() {
        setupLoader();

        replaceContentFragment(LoginFragment.newInstance(), false);
    }

    @Override
    protected void onRestoreView(Bundle savedInstanceState) {
        setupLoader();
    }

    @Override
    protected void onTraslucentBackgroundUpdated(boolean backgroundEnable) {

    }


}

