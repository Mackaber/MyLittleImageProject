package com.mli.mackaber.mylittleimageproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Videos;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mackaber on 17/09/14.
 */
public class VideosAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Videos.Video> videos;
    private VideosHolder holder;

    public VideosAdapter(Activity a, int textViewResourceId, List<Videos.Video> items) {
        super(a, textViewResourceId);
        this.activity = a;
        this.videos = items;

        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Videos.Video getVideoAt(int position){  return videos.get(position); }

    public int getCount() { return videos.size(); }

    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        holder = new VideosHolder();
        convertView = inflater.inflate(R.layout.list_item, parent, false);
        holder.title = (TextView)convertView.findViewById(R.id.title);
//        holder.imageUrl = (ImageView)convertView.findViewById(R.id.imageUrl);

        convertView.setTag(holder);

        Videos.Video video = videos.get(position);
        holder.title.setText(video.getTitle());
//        Picasso.with(Aplication.getApplication().getContext()).load(video.getUrl()).resize(90, 90).into(holder.imageUrl);

        return convertView;
    }

    // Add a single Video
    public void addVideo(Videos.Video video){
        this.videos.add(video);
        this.notifyDataSetChanged();
    }

    // Add a video collection
    public void addVideos(List<Videos.Video> videos){
        this.videos.addAll(videos);
        this.notifyDataSetChanged();
    }

    // Change the current video collection with another
    public void changeVideos(List<Videos.Video> videos){
        this.videos = videos;
        this.notifyDataSetChanged();
    }

    private static class VideosHolder {
        public TextView title;
        public TextView url;
//        public ImageView imageUrl;
    }

}
