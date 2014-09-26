package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.AlbumsAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
    This is the List Activity Class
*/

public class Album_detail extends Activity {

//  Bundle Variables
    public static final int NEW_IMAGE = Menu.FIRST;
    public static final int NEW_VIDEO = 2;
    public static final String ARG_ITEM_ID = "Item_id";

//  View Variables
    private ListView list;
    private Activity activity = this;
    private PicturesAdapter picturesAdapter;
    private int id;
    private Dao<Albums.Album, Integer> albumDao = null;
    private List<Pictures.Picture> pictures;


//V----------------------------------------------- ACTIVITY METHODS ----------------------------------------------------------------V

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt(ARG_ITEM_ID);

        Albums.Album album = null;

        try {
            albumDao = Aplication.getApplication().getAlbumDao();
            album = albumDao.queryForId(id);
            pictures = new ArrayList<Pictures.Picture>(album.getPictures());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        picturesAdapter = new PicturesAdapter(activity, R.layout.list_item, pictures);

        Aplication.getApplication().setPicturesAdapter(picturesAdapter);

        list = (ListView) activity.findViewById(R.id.pictureListView);
        list.setAdapter(picturesAdapter);
        list.setOnItemClickListener(itemClickHandler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_detail, menu);
        menu.add(1,NEW_IMAGE,1,R.string.new_picture);
        menu.add(2,NEW_VIDEO,2,R.string.new_video);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case NEW_IMAGE:
                new_picture();
                return true;
            case NEW_VIDEO:
                new_video();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//V----------------------------------------------- CUSTOM METHODS ----------------------------------------------------------------V

    private AdapterView.OnItemClickListener itemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), Picture_detail.class);

            intent.putExtra(Picture_detail.ARG_ITEM_ID, picturesAdapter.getPictureAt(position).getId());
            startActivity(intent);
        }
    };

    public void new_picture(){
        Intent intent = new Intent(getApplicationContext(), New_picture.class);
        intent.putExtra(New_picture.ALBUM_ID, id);
        startActivity(intent);
    }

    public void new_video(){
        Intent intent = new Intent(getApplicationContext(), New_video.class);
        intent.putExtra(New_video.ALBUM_ID, id);
        startActivity(intent);
    }
}
