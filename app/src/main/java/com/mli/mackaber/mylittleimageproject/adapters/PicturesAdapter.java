package com.mli.mackaber.mylittleimageproject.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mackaber on 17/09/14.
 */
public class PicturesAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Pictures.Picture> pictures;
    private PicturesHolder holder;

    public PicturesAdapter(Activity a, int textViewResourceId, List<Pictures.Picture> items) {
        super(a, textViewResourceId);
        this.activity = a;
        this.pictures = items;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Pictures.Picture getPictureAt(int position){  return pictures.get(position); }

    public int getCount() { return pictures.size(); }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = new PicturesHolder();
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.title = (TextView)convertView.findViewById(R.id.title);
        holder.imageUrl = (ImageView)convertView.findViewById(R.id.imageUrl);

        convertView.setTag(holder);

        Pictures.Picture picture = pictures.get(position);

        Log.d("ID del adapter:", position + "");

        holder.title.setText(picture.getTitle());
        Picasso.with(Aplication.getApplication().getContext()).load(picture.getUrl()).resize(90, 90).into(holder.imageUrl);

        return convertView;
    }

    private static class PicturesHolder {
        public TextView title;
        public TextView url;
        public ImageView imageUrl;
    }

}
