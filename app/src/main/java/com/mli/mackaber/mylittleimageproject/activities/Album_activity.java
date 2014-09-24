package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.AlbumsAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.utils.DownloadTask;

import java.sql.SQLException;

public class Album_activity extends Activity {

    //  Database Variables
    public static final int CLEAR_DB = Menu.FIRST;
    public static final int NEW_ALBUM = 2;

    private AlbumsAdapter albumsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_activity);

        new DownloadTask(this,itemClickHandler).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_activity, menu);
        menu.add(0,CLEAR_DB,0,R.string.clear_database);
        menu.add(1,NEW_ALBUM,1,R.string.new_album);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case CLEAR_DB:
                cleardatabase();
                return true;
            case NEW_ALBUM:
                new_album();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener itemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            albumsAdapter = Aplication.getApplication().getAlbumsAdapter();

            Intent intent = new Intent(getApplicationContext(), Album_detail.class);
            intent.putExtra(Album_detail.ARG_ITEM_ID, albumsAdapter.getAlbumAt(position).getId());
            startActivity(intent);
        }
    };

    public void new_album(){
        Intent intent = new Intent(getApplicationContext(), New_album.class);
        startActivity(intent);
    }

    public void cleardatabase(){
        try {
            Aplication.getApplication().cleanPictures();
            this.recreate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}