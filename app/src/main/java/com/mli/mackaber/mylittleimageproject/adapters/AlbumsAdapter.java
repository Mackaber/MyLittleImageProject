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
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mackaber on 17/09/14.
 */
public class AlbumsAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Albums.Album> albums;
    private AlbumsHolder holder;

    public AlbumsAdapter(Activity a, int textViewResourceId, List<Albums.Album> items) {
        super(a, textViewResourceId);
        this.activity = a;
        this.albums = items;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Albums.Album getAlbumAt(int position){  return albums.get(position); }

    public int getCount() { return albums.size(); }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = new AlbumsHolder();
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.title = (TextView)convertView.findViewById(R.id.title);
        holder.imageUrl = (ImageView)convertView.findViewById(R.id.imageUrl);

        convertView.setTag(holder);

        Albums.Album album = albums.get(position);

        holder.title.setText(album.getTitle());

        String url;

        if (album.getPictures().size() > 0) url = album.getPictures().iterator().next().getUrl();
        else url = "http://cumbrianrun.co.uk/wp-content/uploads/2014/02/default-placeholder.png";

        Picasso.with(Aplication.getApplication().getContext()).load(url).resize(90, 90).into(holder.imageUrl);

        return convertView;
    }

    // Add a single Album
    public void addAlbum(Albums.Album album){
        this.albums.add(album);
        this.notifyDataSetChanged();
    }

    // Add a album collection
    public void addAlbums(List<Albums.Album> albums){
        this.albums.addAll(albums);
        this.notifyDataSetChanged();
    }

    // Change the current album collection with another
    public void changeAlbums(List<Albums.Album> albums){
        this.albums = albums;
        this.notifyDataSetChanged();
    }

    private static class AlbumsHolder {
        public TextView title;
        public ImageView imageUrl;
    }

}
