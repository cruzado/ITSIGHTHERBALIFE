package com.desarrollo.herbalife.ui.viewmodel;

/**
 * Created by oswaldo on 30/01/16.
 */
public interface IShareView {
    public void onWebReciclerView(int position);
    public void onFacebookReciclerView(int position);
    public void onTwitterReciclerView(int position);
    public void onWhatsappReciclerView(int position);
    public void onFavoriteView(int position);
    public void onDeleteView(int position);

}
