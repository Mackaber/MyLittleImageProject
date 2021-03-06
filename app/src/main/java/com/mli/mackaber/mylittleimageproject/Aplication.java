package com.mli.mackaber.mylittleimageproject;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mli.mackaber.mylittleimageproject.adapters.AlbumsAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.ListAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.VideosAdapter;
import com.mli.mackaber.mylittleimageproject.databases.DatabaseOrm;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.models.Videos;

import java.sql.SQLException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by mackaber on 17/09/14.
 */
public class Aplication extends Application {

    private static Aplication instance;
    private SharedPreferences preferences;
    private DatabaseOrm databaseHelper = null;

    private Dao<Albums.Album, Integer> album = null;
    private Dao<Pictures.Picture, Integer> picture = null;
    private Dao<Videos.Video, Integer> video = null;

    private RestAdapter restAdapter;
    private PicturesAdapter picturesAdapter;
    private AlbumsAdapter albumsAdapter;
    private VideosAdapter videosAdapter;
    private ListAdapter listAdapter;

    private String user_email;
    private String auth_token;


    public Aplication() { instance = this; }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        databaseHelper = new DatabaseOrm(this);

        instance=this;

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        user_email = sharedPref.getString(getString(R.string.user_email),"");
        auth_token = sharedPref.getString(getString(R.string.auth_token),"");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Log.d("Credentials :",user_email + ", " + auth_token);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("X-User-Email", user_email);
                request.addHeader("X-User-Token", auth_token);
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.server))
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    public Context getContext(){ return this; }
    public RestAdapter getRestAdapter() {
        return this.restAdapter;
    }

    public AlbumsAdapter getAlbumsAdapter() {
        return albumsAdapter;
    }

    public void setAlbumsAdapter(AlbumsAdapter albumsAdapter) {
        this.albumsAdapter = albumsAdapter;
    }

    public PicturesAdapter getPicturesAdapter() {
        return picturesAdapter;
    }
    public void setPicturesAdapter(PicturesAdapter picturesAdapter) {
        this.picturesAdapter = picturesAdapter;
    }

    public VideosAdapter getVideosAdapter() {
        return videosAdapter;
    }
    public void setVideosAdapter(VideosAdapter videosAdapter) {
        this.videosAdapter = videosAdapter;
    }


    public ListAdapter getListAdapter() {
        return listAdapter;
    }
    public void setListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public Dao<Albums.Album, Integer> getAlbumDao() throws SQLException {
        if (album == null) {
            album = databaseHelper.getDao(Albums.Album.class);
        }
        return album;
    }

    public Dao<Pictures.Picture, Integer> getPictureDao() throws SQLException {
        if (picture == null) {
            picture = databaseHelper.getDao(Pictures.Picture.class);
        }
        return picture;
    }

    public Dao<Videos.Video, Integer> getVideoeDao() throws SQLException {
        if (video == null) {
            video = databaseHelper.getDao(Videos.Video.class);
        }
        return video;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
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
