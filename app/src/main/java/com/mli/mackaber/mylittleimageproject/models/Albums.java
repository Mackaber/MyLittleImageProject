package com.mli.mackaber.mylittleimageproject.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by mackaber on 23/09/14.
 */
public interface Albums {

    @GET("/albums")
    List<Album> getAllAlbums();

    @POST("/albums")
    void createAlbum(@Body Album album, Callback<Album> cb);

    @DatabaseTable(tableName = "albums")
    public class Album {

        @DatabaseField(id = true, generatedId = false)
        private int id;
        @DatabaseField
        private String title;
        @ForeignCollectionField
        ForeignCollection<Pictures.Picture> pictures;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public ForeignCollection<Pictures.Picture> getPictures() {
            return pictures;
        }
        public void setPictures(ForeignCollection<Pictures.Picture> pictures) {
            this.pictures = pictures;
        }
    }
}
