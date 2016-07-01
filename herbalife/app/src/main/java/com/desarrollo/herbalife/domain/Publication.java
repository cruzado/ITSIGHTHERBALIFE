package com.desarrollo.herbalife.domain;

import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Desarrollo on 24/01/16.
 */
public class Publication{
    @SerializedName("id")
    String publicationId;
    @SerializedName("titulo")
    String publicationTitle;
    @SerializedName("descripcion")
    String publicationDescription;
    @SerializedName("url")
    String publicationUrl;
    @SerializedName("foto")
    String publicationPhoto;
    @SerializedName("titulo_ver_mas")
    String publicationMore;
    @SerializedName("favorito")
    String publicationFavourite;

    String publicationCategory;

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    public String getPublicationDescription() {
        return publicationDescription;
    }

    public void setPublicationDescription(String publicationDescription) {
        this.publicationDescription = publicationDescription;
    }

    public String getPublicationUrl() {
        return publicationUrl;
    }

    public void setPublicationUrl(String publicationUrl) {
        this.publicationUrl = publicationUrl;
    }

    public String getPublicationPhoto() {
        return publicationPhoto;
    }

    public void setPublicationPhoto(String publicationPhoto) {
        this.publicationPhoto = publicationPhoto;
    }

    public String getPublicationMore() {
        return publicationMore;
    }

    public void setPublicationMore(String publicationMore) {
        this.publicationMore = publicationMore;
    }


    public String getPublicationFavourite() {
        return publicationFavourite;
    }

    public void setPublicationFavourite(String publicationFavourite) {
        this.publicationFavourite = publicationFavourite;
    }

    public String getPublicationCategory() {
        return publicationCategory;
    }

    public void setPublicationCategory(String publicationCategory) {
        this.publicationCategory = publicationCategory;
    }

    public Publication(String publicationId, String publicationTitle, String publicationDescription, String publicationUrl, String publicationPhoto, String publicationMore, String publicationFavourite) {
        this.publicationId = publicationId;
        this.publicationTitle = publicationTitle;
        this.publicationDescription = publicationDescription;
        this.publicationUrl = publicationUrl;
        this.publicationPhoto = publicationPhoto;
        this.publicationMore = publicationMore;
        this.publicationFavourite = publicationFavourite;
    }

    public Publicationtable getPublicationTable(){

        return new Publicationtable(publicationId, publicationTitle, publicationDescription, publicationUrl, publicationPhoto ,publicationMore, publicationFavourite, publicationCategory);
    }
}
