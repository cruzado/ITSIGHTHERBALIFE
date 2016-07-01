package com.desarrollo.herbalife.ui.adapter;

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
import com.desarrollo.herbalife.domain.Publication;
import com.desarrollo.herbalife.domain.orm.Categorytable;
import com.desarrollo.herbalife.domain.orm.Publicationtable;
import com.desarrollo.herbalife.ui.viewmodel.IRecyclerView;
import com.desarrollo.herbalife.ui.viewmodel.IShareView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Desarrollo on 17/01/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_PUBLICATIONS = 0;
    private ArrayList<Publicationtable> publications;
    private Context context;
    private IShareView iShareView;
    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        int holderId;
        ImageView imgNew;
        TextView txtTitle, txtCompartir, txtFavoritos, txtEliminar;
        TextView txtDescription;
        Button btnMore;
        LinearLayout lnyShareOption, lnyShare, lnyFb, lnyTwt, lnyWhatsapp;
        LinearLayout lnyFavorite,lnyDelete;
        ImageView imgFavorite;

        public DetailViewHolder(View v, int viewType) {
            super(v);
            holderId = viewType;
            imgNew = (ImageView)v.findViewById(R.id.imgNew);
            txtTitle = (TextView)v.findViewById(R.id.txtTitle);
            txtCompartir = (TextView)v.findViewById(R.id.txtCompartir);
            txtFavoritos = (TextView)v.findViewById(R.id.txtFavoritos);
            txtEliminar = (TextView)v.findViewById(R.id.txtEliminar);

            txtDescription = (TextView)v.findViewById(R.id.txtDescription);
            btnMore = (Button)v.findViewById(R.id.btnMore);
            lnyShareOption = (LinearLayout)v.findViewById(R.id.lnyShareOption);
            lnyShare = (LinearLayout)v.findViewById(R.id.lnyShare);
            lnyFb = (LinearLayout)v.findViewById(R.id.lnyFb);
            lnyTwt = (LinearLayout)v.findViewById(R.id.lnyTwt);
            lnyWhatsapp = (LinearLayout)v.findViewById(R.id.lnyWhatsapp);
            lnyFavorite = (LinearLayout)v.findViewById(R.id.lnyFavorite);
            lnyDelete = (LinearLayout)v.findViewById(R.id.lnyDelete);
            imgFavorite = (ImageView)v.findViewById(R.id.imgFavorite);
        }
    }



    public DetailAdapter(ArrayList<Publicationtable> publications, Context context, IShareView iShareView) {
        this.publications = publications;
        this.context = context;
        this.iShareView = iShareView;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return TYPE_PUBLICATIONS;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);
        DetailViewHolder viewHolder = new DetailViewHolder(v, viewType);
        return viewHolder;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Typeface faceBold=Typeface.createFromAsset(context.getAssets(),"fonts/Gotham-Bold.ttf");
        Typeface faceLigth=Typeface.createFromAsset(context.getAssets(), "fonts/Gotham-Light.ttf");
        Typeface faceCurve=Typeface.createFromAsset(context.getAssets(), "fonts/Gotham-BookItalic.ttf");

        DetailViewHolder viewHolder = (DetailViewHolder) holder;
        Publicationtable publication = publications.get(position);
        viewHolder.txtTitle.setTypeface(faceBold);
        viewHolder.txtDescription.setTypeface(faceLigth);
        viewHolder.btnMore.setTypeface(faceBold);
        viewHolder.txtCompartir.setTypeface(faceBold);
        viewHolder.txtFavoritos.setTypeface(faceBold);
        viewHolder.txtEliminar.setTypeface(faceBold);

        if(viewHolder == null){
            Log.i("viewHolder", "HeaderViewHolder null");
        }
        viewHolder.txtTitle.setText(publication.getPublicationTitle());
        viewHolder.txtDescription.setText(publication.getPublicationDescription());
        Picasso.with(context).load(publication.getPublicationPhoto()).into(viewHolder.imgNew);
        if(publication.getPublicationFavourite() != null && publication.getPublicationFavourite().compareTo("0") == 0){
            viewHolder.imgFavorite.setImageResource(R.drawable.ico_like);
        }else{
            viewHolder.imgFavorite.setImageResource(R.drawable.ico_like_check);
        }

        viewHolder.lnyShare.setVisibility(View.GONE);
        viewHolder.lnyShareOption.setTag(viewHolder);
        viewHolder.lnyShareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailViewHolder viewHolder = (DetailViewHolder) v.getTag();
                if (viewHolder.lnyShare.getVisibility() == View.GONE) {
                    viewHolder.lnyShare.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.lnyShare.setVisibility(View.GONE);
                }
            }
        });

        viewHolder.btnMore.setText(publication.getPublicationMore());
        viewHolder.btnMore.setTag(Integer.toString(position));
        viewHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onWebReciclerView(position);
            }
        });
        viewHolder.lnyFb.setTag(Integer.toString(position));
        viewHolder.lnyFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "onClick");
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onFacebookReciclerView(position);
            }
        });
        viewHolder.lnyTwt.setTag(Integer.toString(position));
        viewHolder.lnyTwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "onClick");
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onTwitterReciclerView(position);
            }
        });

        viewHolder.lnyWhatsapp.setTag(Integer.toString(position));
        viewHolder.lnyWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click", "onClick");
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onWhatsappReciclerView(position);
            }
        });
        viewHolder.lnyFavorite.setTag(Integer.toString(position));
        viewHolder.lnyFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onFavoriteView(position);
            }
        });
        viewHolder.lnyDelete.setTag(Integer.toString(position));
        viewHolder.lnyDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt((String) v.getTag());
                iShareView.onDeleteView(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return publications.size();
    }
}
