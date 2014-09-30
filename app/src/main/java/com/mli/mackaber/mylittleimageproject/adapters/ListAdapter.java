package com.mli.mackaber.mylittleimageproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.mli.mackaber.mylittleimageproject.models.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mackaber on 17/09/14.
 */
public class ListAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Object> items;
    private ItemHolder holder;

    public ListAdapter(Activity a, int textViewResourceId, List<Object> items) {
        super(a, textViewResourceId);
        this.activity = a;
        this.items = items;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() { return items.size(); }

    public Object getItemAt(int position){  return items.get(position); }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = new ItemHolder();
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.title = (TextView)convertView.findViewById(R.id.title);
        holder.imageUrl = (ImageView)convertView.findViewById(R.id.imageUrl);

        convertView.setTag(holder);

        if(items.get(position).getClass().equals(Pictures.Picture.class)){
            Pictures.Picture picture = (Pictures.Picture) items.get(position);
            holder.title.setText(picture.getTitle());
            Picasso.with(Aplication.getApplication().getContext()).load(picture.getUrl()).resize(90, 90).into(holder.imageUrl);
        } else {
            Videos.Video video = (Videos.Video) items.get(position);
            holder.title.setText(video.getTitle());
            String url = "http://cumbrianrun.co.uk/wp-content/uploads/2014/02/default-placeholder.png";
            Picasso.with(Aplication.getApplication().getContext()).load(url).resize(90, 90).into(holder.imageUrl);
        }

        return convertView;
    }

    // Add a single Item
    public void addItem(Object item){
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    // Add an item collection
    public void addItems(List<Object> items){
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    // Change the current item collection with another
    public void changeItems(List<Object> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

    private static class ItemHolder {
        public TextView title;
//        public TextView url;
        public ImageView imageUrl;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
