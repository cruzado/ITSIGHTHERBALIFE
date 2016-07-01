package com.desarrollo.herbalife.common;


public interface IBaseView {

    void startLoading();

    void stopLoading();

    void showErrorDialog(String message);
}
