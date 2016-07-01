package com.desarrollo.herbalife.domain.orm;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by Desarrollo on 24/01/16.
 */

public class Publicationtable extends SugarRecord {
    @Unique
    String publicationId;
    String publicationTitle;
    String publicationDescription;
    String publicationUrl;
    String publicationPhoto;
    String publicationMore;
    String publicationFavourite;
    String publicationCategory;

    public Publicationtable() {
        super();
    }

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

    @Override
    public String toString() {
        return "Publicationtable{" +
                "publicationId='" + publicationId + '\'' +
                ", publicationTitle='" + publicationTitle + '\'' +
                ", publicationDescription='" + publicationDescription + '\'' +
                ", publicationUrl='" + publicationUrl + '\'' +
                ", publicationPhoto='" + publicationPhoto + '\'' +
                ", publicationMore='" + publicationMore + '\'' +
                ", publicationFavourite='" + publicationFavourite + '\'' +
                ", publicationCategory='" + publicationCategory + '\'' +
                '}';
    }

    public Publicationtable(String publicationId, String publicationTitle, String publicationDescription, String publicationUrl, String publicationPhoto, String publicationMore, String publicationFavourite, String publicationCategory) {
        this.publicationId = publicationId;
        this.publicationTitle = publicationTitle;
        this.publicationDescription = publicationDescription;
        this.publicationUrl = publicationUrl;
        this.publicationPhoto = publicationPhoto;
        this.publicationMore = publicationMore;
        this.publicationFavourite = publicationFavourite;
        this.publicationCategory = publicationCategory;
    }
}
