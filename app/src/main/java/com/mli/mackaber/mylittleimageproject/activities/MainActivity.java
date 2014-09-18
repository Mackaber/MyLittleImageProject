package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.models.PictureRepository;
import com.mli.mackaber.mylittleimageproject.models.Pictures;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class MainActivity extends Activity {

    private PictureRepository repo;
    private List<Pictures.Picture> pictures;
    private ListView list;
    private Dao<Pictures.Picture, Integer> pictureDao = null;
    private Activity activity = this;
    private PicturesAdapter adapter;

    private List<Pictures.Picture> picturesToInsert = new ArrayList<Pictures.Picture>();

    public static final int CLEAR_DB = Menu.FIRST;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener itemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), Picture_detail.class);
            intent.putExtra(Picture_detail.ARG_ITEM_ID, adapter.getPictureAt(position).getId());
            startActivity(intent);
        }
    };

    public void cleardatabase(){
        try {
            Aplication.getApplication().cleanPictures();
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

//          ESTO NO HACE NADA XP
//            try {
//                pictureDao = Aplication.getApplication().getPictureeDao();
//
//                for(Pictures.Picture p :  pictures) {
//                    pictureDao.queryForAll();
//                }
////                articleDao.c(news);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

            activity.setProgressBarIndeterminateVisibility(false);
        }
    }
}
