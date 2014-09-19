package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.models.PictureRepository;
import com.mli.mackaber.mylittleimageproject.models.Pictures;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/*
    This is the List Activity Class
*/

public class MainActivity extends Activity {

//  Object Variables
    private PictureRepository repo;
    private List<Pictures.Picture> pictures;

//  Database Variables
    private Dao<Pictures.Picture, Integer> pictureDao = null;
    public static final int CLEAR_DB = Menu.FIRST;
    public static final int NEW_PONY = 2;

//  View Variables
    private ListView list;
    private Activity activity = this;
    private PicturesAdapter adapter;
    private List<Pictures.Picture> picturesToInsert = new ArrayList<Pictures.Picture>();

//V----------------------------------------------- ACTIVITY METHODS ----------------------------------------------------------------V

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        new DownloadNewsTask().execute();
        list = (ListView) findViewById(R.id.listView);

        repo = new PictureRepository(Aplication.getApplication().getContext());

        list.setOnItemClickListener(itemClickHandler);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        menu.add(0,CLEAR_DB,0,R.string.clear_database);
        menu.add(1,NEW_PONY,1,R.string.new_pony);
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
            case NEW_PONY:
                new_picture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//V----------------------------------------------- CUSTOM METHODS ----------------------------------------------------------------V

    private AdapterView.OnItemClickListener itemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), Picture_detail.class);
            intent.putExtra(Picture_detail.ARG_ITEM_ID, adapter.getPictureAt(position).getId());
            startActivity(intent);
        }
    };

    public void new_picture(){

        Pictures repre = Aplication.getApplication().getRestAdapter().create(Pictures.class);
        Pictures.Picture picture;
        picture = new Pictures.Picture();

        picture.setTitle("Test");

        Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.fluttershy);

        File imageFileFolder = new File(getCacheDir(),"Avatar");
        if( !imageFileFolder.exists() ){
            imageFileFolder.mkdir();
        }

        FileOutputStream out = null;

        File imageFileName = new File(imageFileFolder, "avatar-" + System.currentTimeMillis() + ".jpg");
        try {
            out = new FileOutputStream(imageFileName);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            Log.e("TAG", "Failed to convert image to JPEG", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e("TAG", "Failed to close output stream", e);
            }
        }

        TypedFile typedFile = new TypedFile("image/jpeg", imageFileName);

        Callback<Pictures.Picture> callback;
        callback = new Callback<Pictures.Picture>() {
            @Override
            public void success(Pictures.Picture picture, Response response) {
                Toast.makeText(getApplicationContext(), picture.getTitle(),
                        Toast.LENGTH_LONG).show();
                Log.d("Si jalo: ", picture.getTitle());
                try {
                    pictureDao = Aplication.getApplication().getPictureeDao();
                    pictureDao.create(picture);
                    activity.recreate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "FAIL!",
                        Toast.LENGTH_LONG).show();
            }
        };


       repre.createPicture(picture.getTitle(),typedFile,callback);
    }

    public void cleardatabase(){
        try {
            Aplication.getApplication().cleanPictures();
            activity.recreate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class DownloadNewsTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            activity.setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Void doInBackground(final Void... arg0) {

            Pictures repre = Aplication.getApplication().getRestAdapter().create(Pictures.class);
            try {
                pictureDao = Aplication.getApplication().getPictureeDao();
                Log.d("The table exists: ", pictureDao.isTableExists() + "");
                Log.d("The size is more than 0 : ", (pictureDao.queryForAll().size()) + "");
                if(pictureDao.isTableExists() && pictureDao.queryForAll().size()>0) {
                    Log.d("Using...", "DB");
                    pictures = pictureDao.queryForAll();
                } else {

                    Log.d("Using...", "Adapter");
                    Log.d("Número de Articulos",repre.getAllPictures().size() + "");

                    picturesToInsert = repre.getAllPictures();
                    pictureDao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws Exception {
                            for (Pictures.Picture picture: picturesToInsert) {
                                Log.d("Artículo #" + picture.getId(), picture.getTitle());
                                pictureDao.create(picture);
                            }
                            return null;
                        }
                    });

                    pictures = repre.getAllPictures();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            adapter = new PicturesAdapter(activity,R.layout.list_item, pictures);
            list.setAdapter(adapter);
            activity.setProgressBarIndeterminateVisibility(false);
        }
    }
}
