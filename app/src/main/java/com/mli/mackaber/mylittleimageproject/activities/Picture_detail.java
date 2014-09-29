package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class Picture_detail extends Activity {

    public static final String ARG_ITEM_ID = "Item_id";
    private Dao<Pictures.Picture, Integer> pictureDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;
    private Pictures.Picture picture;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);
        Bundle extras = getIntent().getExtras();

        id = extras.getInt(ARG_ITEM_ID);

        Log.d("Seleccionado dentro ID: ", id + "");

        TextView text = (TextView) findViewById(R.id.textView);
        ImageView image = (ImageView) findViewById(R.id.displayImage);
        TextView albumtitle = (TextView) findViewById(R.id.albumtitle);
        TextView created_at = (TextView) findViewById(R.id.created_at);

        try {
            pictureDao = Aplication.getApplication().getPictureDao();
            albumDao = Aplication.getApplication().getAlbumDao();
            picture = pictureDao.queryForId(id);

            albumDao.refresh(picture.getAlbum());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        albumtitle.setText(picture.getAlbum().getTitle());
        text.setText(picture.getTitle());
        created_at.setText(picture.getCreated_at().toString());
        Picasso.with(Aplication.getApplication().getContext()).load(picture.getUrl()).resize(300, 300).into(image);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.picture_detail, menu);
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
