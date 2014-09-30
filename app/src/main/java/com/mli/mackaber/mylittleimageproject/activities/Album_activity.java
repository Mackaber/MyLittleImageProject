package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
    public static final int LOG_OUT = 3;

    private AlbumsAdapter albumsAdapter;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_activity);
        if (user_logged_in()) {
            new DownloadTask(this,itemClickHandler).execute();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public boolean user_logged_in() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String user_email = sharedPref.getString(getString(R.string.user_email),"");
        String auth_token = sharedPref.getString(getString(R.string.auth_token),"");

        Log.d("El user Mail: ",user_email);
        Log.d("El auth Token: ",auth_token);

        if (user_email != "" && auth_token != "") return true;
        else return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.album_activity, menu);
        menu.add(0, CLEAR_DB, 0, R.string.clear_database);
        menu.add(1,NEW_ALBUM,1,R.string.new_album);
        menu.add(2,LOG_OUT,2,R.string.log_out);
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
            case LOG_OUT:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
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
