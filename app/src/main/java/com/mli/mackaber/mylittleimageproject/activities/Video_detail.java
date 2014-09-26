package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.mli.mackaber.mylittleimageproject.models.Videos;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class Video_detail extends Activity {

    public static final String ARG_ITEM_ID = "Item_id";
    private Dao<Videos.Video, Integer> videoDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;
    private Videos.Video video;
    private VideoView videoView;
    private MediaController mediaController;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        Bundle extras = getIntent().getExtras();

        id = extras.getInt(ARG_ITEM_ID);

        Log.d("Seleccionado dentro ID: ", id + "");

        TextView text = (TextView) findViewById(R.id.textView);
        ImageView image = (ImageView) findViewById(R.id.displayImage);
        TextView albumtitle = (TextView) findViewById(R.id.albumtitle);

        try {
            videoDao = Aplication.getApplication().getVideoeDao();
            albumDao = Aplication.getApplication().getAlbumDao();
            video = videoDao.queryForId(id);

            albumDao.refresh(video.getAlbum());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Uri uri = Uri.parse(video.getUrl());
        videoView = (VideoView) findViewById(R.id.videoPreview);

        videoView.setVideoURI(uri);

        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.start();

        albumtitle.setText(video.getAlbum().getTitle());
        text.setText(video.getTitle());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
