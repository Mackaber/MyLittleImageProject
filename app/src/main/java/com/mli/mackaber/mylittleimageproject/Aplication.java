package com.mli.mackaber.mylittleimageproject;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.mli.mackaber.mylittleimageproject.databases.DatabaseOrm;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.PictureRepository;
import com.mli.mackaber.mylittleimageproject.models.Pictures;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import retrofit.RestAdapter;

/**
 * Created by mackaber on 17/09/14.
 */
public class Aplication extends Application {

    private static Aplication instance;
    private SharedPreferences preferences;
    private DatabaseOrm databaseHelper = null;

    private Dao<Pictures.Picture, Integer> picture = null;
    private Dao<Albums.Album, Integer> album = null;

    private RestAdapter restAdapter;
    private PictureRepository repo;
    public Aplication() { instance = this; }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseOrm(this);
        instance=this;

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.server))
                .build();
    }

    public Context getContext(){ return this; }
    public RestAdapter getRestAdapter() {
        return this.restAdapter;
    }

    public Dao<Pictures.Picture, Integer> getPictureDao() throws SQLException {
        if (picture == null) {
            picture = databaseHelper.getDao(Pictures.Picture.class);
        }
        return picture;
    }

    public Dao<Albums.Album, Integer> getAlbumDao() throws SQLException {
        if (album == null) {
            album = databaseHelper.getDao(Albums.Album.class);
        }
        return album;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    public void cleanPictures() throws SQLException {
        databaseHelper.cleanAll();
    }

    public static Aplication getApplication() {
        return instance;
    }
}
