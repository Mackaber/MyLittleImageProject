package com.mli.mackaber.mylittleimageproject.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
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
        @DatabaseField
        private Date created_at;
        @DatabaseField
        private Date updated_at;
        @ForeignCollectionField
        ForeignCollection<Pictures.Picture> pictures;
        @ForeignCollectionField
        ForeignCollection<Videos.Video> videos;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public Date getCreated_at() {
            return created_at;
        }
        public void setCreated_at(Date created_at) {
            this.created_at = created_at;
        }

        public Date getUpdated_at() {
            return updated_at;
        }
        public void setUpdated_at(Date updated_at) {
            this.updated_at = updated_at;
        }

        public ForeignCollection<Pictures.Picture> getPictures() {
            return pictures;
        }
        public void setPictures(ForeignCollection<Pictures.Picture> pictures) {
            this.pictures = pictures;
        }

        public ForeignCollection<Videos.Video> getVideos() {
            return videos;
        }
        public void setVideos(ForeignCollection<Videos.Video> videos) {
            this.videos = videos;
        }
    }
}
