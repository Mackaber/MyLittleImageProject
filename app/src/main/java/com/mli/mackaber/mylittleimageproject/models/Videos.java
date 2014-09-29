package com.mli.mackaber.mylittleimageproject.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by mackaber on 26/09/14.
 */
public interface Videos {

    @GET("/videos")
    List<Video> getAllVideos();

    @Multipart
    @POST("/videos")
    void createVideo(@Part("title")String title, @Part("album_id")int album_id, @Part("video")TypedFile video, Callback<Video> cb);

    @DatabaseTable(tableName = "videos")
    public class Video {
        @DatabaseField(id = true, generatedId = false)
        private int id;
        @DatabaseField
        private String title;
        @DatabaseField
        private String url;
        @DatabaseField
        private String description;
        @DatabaseField
        private Date created_at;
        @DatabaseField
        private Date updated_at;
        @DatabaseField(foreign = true)
        private Albums.Album album;

        // Album ID only to get it from the API
        private int album_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public Albums.Album getAlbum() {
            return album;
        }

        public void setAlbum(Albums.Album album) {
            this.album = album;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }
    }
}
