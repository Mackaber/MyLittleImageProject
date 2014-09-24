package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class New_album extends Activity {

    private Dao<Albums.Album, Integer> albumDao = null;
    private EditText mTitleText;
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private TypedFile typedFile;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        final Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_picture, menu);
        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void confirm() {
        Albums repre = Aplication.getApplication().getRestAdapter().create(Albums.class);
//        Pictures repre = Aplication.getApplication().getRestAdapter().create(Pictures.class);


        final Albums.Album album;
        album = new Albums.Album();

        mTitleText = (EditText) findViewById(R.id.album_title);

        album.setTitle(mTitleText.getText().toString());

        Callback<Albums.Album> callback;
        callback = new Callback<Albums.Album>() {
            @Override
            public void success (Albums.Album album, Response response){
                Toast.makeText(getApplicationContext(), album.getTitle(),
                        Toast.LENGTH_LONG).show();
                Log.d("Si jalo: ", album.getTitle());
                try {
                    albumDao = Aplication.getApplication().getAlbumDao();
                    albumDao.create(album);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure (RetrofitError error){
                Toast.makeText(getApplicationContext(), "FAIL!",
                        Toast.LENGTH_LONG).show();
            }
        };

        repre.createAlbum(album, callback);
        finish();
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
