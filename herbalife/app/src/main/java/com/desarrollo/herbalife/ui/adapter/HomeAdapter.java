package com.desarrollo.herbalife.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desarrollo.herbalife.R;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.ui.viewmodel.IRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<Categorytable> categories;
    private Publicationtable publication;
    private Context context;
    private IRecyclerView iRecyclerView;
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        ImageView imgNew;
        TextView txtMasLeido,txtTitle, txtCompartir, txtFavoritos;
        TextView txtDescription;
        Button btnMore;
        LinearLayout lnyFavorite;
        ImageView imgFavorite;
        LinearLayout lnyShareOption, lnyShare, lnyFb, lnyTwt, lnyWhatsapp;
        public HeaderViewHolder(View v, int viewType) {
            super(v);
            holderId = viewType;
            imgNew = (ImageView)v.findViewById(R.id.imgNew);
            txtMasLeido = (TextView)v.findViewById(R.id.txtMasLeido);
            txtCompartir = (TextView)v.findViewById(R.id.txtCompartir);
            txtFavoritos = (TextView)v.findViewById(R.id.txtFavoritos);
            txtTitle = (TextView)v.findViewById(R.id.txtTitle);
            txtDescription = (TextView)v.findViewById(R.id.txtDescription);
            btnMore = (Button)v.findViewById(R.id.btnMore);
            lnyFavorite = (LinearLayout)v.findViewById(R.id.lnyFavorite);
            imgFavorite = (ImageView)v.findViewById(R.id.imgFavorite);
            lnyShareOption = (LinearLayout)v.findViewById(R.id.lnyShareOption);
            lnyShare = (LinearLayout)v.findViewById(R.id.lnyShare);
            lnyFb = (LinearLayout)v.findViewById(R.id.lnyFb);
            lnyTwt = (LinearLayout)v.findViewById(R.id.lnyTwt);
            lnyWhatsapp = (LinearLayout)v.findViewById(R.id.lnyWhatsapp);

        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        TextView txtCategory;
        TextView txtBadge;

        View view;
        public CategoryViewHolder(View v, int viewType) {
            super(v);
            holderId = viewType;
            txtCategory = (TextView)v.findViewById(R.id.txtCategory);
            txtBadge = (TextView)v.findViewById(R.id.txtBadge);
            view = v;
        }
    }


    public HomeAdapter(ArrayList<Categorytable> categories, Publicationtable publication, Context context, IRecyclerView iRecyclerView) {
        this.categories = categories;
        this.publication = publication;
        this.context = context;
        this.iRecyclerView = iRecyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(position == 0){
            if(publication != null){
                return TYPE_HEADER;
            }else{
                return TYPE_ITEM;
            }

        }else{
            return TYPE_ITEM;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_new, parent, false);
            HeaderViewHolder viewHolder = new HeaderViewHolder(v, viewType);
            return viewHolder;
        }else{
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            CategoryViewHolder viewHolder = new CategoryViewHolder(v, viewType);
            return viewHolder;
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Typeface faceBold=Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Bold.ttf");
        Typeface faceLigth=Typeface.createFromAsset(context.getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceCurve=Typeface.createFromAsset(context.getAssets(), "fonts/Gotham-BookItalic.ttf");
        if(publication != null && holder.getItemViewType() == TYPE_HEADER){
            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;


            viewHolder.txtMasLeido.setTypeface(faceLigth);
            viewHolder.txtTitle.setTypeface(faceBold);
            viewHolder.txtDescription.setTypeface(faceLigth);
            viewHolder.btnMore.setTypeface(faceBold);
            viewHolder.txtCompartir.setTypeface(faceBold);
            viewHolder.txtFavoritos.setTypeface(faceBold);

            viewHolder.txtTitle.setText(publication.getPublicationTitle());
            viewHolder.txtDescription.setText(publication.getPublicationDescription());
            if(publication.getPublicationFavourite().compareTo("0") == 0){
                viewHolder.imgFavorite.setImageResource(R.drawable.ico_like);
            }else{
                viewHolder.imgFavorite.setImageResource(R.drawable.ico_like_check);
            }
            viewHolder.btnMore.setText(publication.getPublicationMore());
            viewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerView.onWebReciclerView();
                }
            });
            viewHolder.lnyFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerView.onFavoritePublication();
                }
            });
            Picasso.with(context).load(publication.getPublicationPhoto()).into(viewHolder.imgNew);

            viewHolder.lnyShare.setVisibility(View.GONE);
            viewHolder.lnyShareOption.setTag(viewHolder);
            viewHolder.lnyShareOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HeaderViewHolder viewHolder = (HeaderViewHolder) v.getTag();
                    if (viewHolder.lnyShare.getVisibility() == View.GONE) {
                        viewHolder.lnyShare.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.lnyShare.setVisibility(View.GONE);
                    }
                }
            });

            viewHolder.lnyFb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerView.onFacebookReciclerView();
                }
            });
            viewHolder.lnyTwt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerView.onTwitterReciclerView();
                }
            });

            viewHolder.lnyWhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecyclerView.onWhatsappReciclerView();
                }
            });
        }else{
            CategoryViewHolder viewHolder = (CategoryViewHolder) holder;
            Categorytable category;
            if(publication != null){
                category = categories.get(position-1);
            }else{
                category = categories.get(position);
            }

            viewHolder.txtCategory.setTypeface(faceBold);
            viewHolder.txtBadge.setTypeface(faceLigth);

            viewHolder.txtCategory.setText(category.getName());
            if(category.getTotal().compareTo("0")==0){
                viewHolder.txtBadge.setVisibility(View.INVISIBLE);
            }else{
                viewHolder.txtBadge.setVisibility(View.VISIBLE);
            }
            viewHolder.txtBadge.setText(category.getTotal());
            viewHolder.view.setTag(Integer.toString(position));
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt((String) v.getTag());
                    iRecyclerView.onItemReciclerView(position);
                }
            });
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(publication != null){
            return 1+categories.size();
        }else{
            return categories.size();
        }
    }
}
