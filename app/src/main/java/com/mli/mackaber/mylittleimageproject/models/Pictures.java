package com.mli.mackaber.mylittleimageproject.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mackaber on 17/09/14.
 */
public interface Pictures {
    @GET("/pictures")
    List<Picture> getAllPictures();

    @DatabaseTable(tableName = "pictures")
    public class Picture {
        @DatabaseField(id = true, generatedId = false)
        private int id;
        @DatabaseField
        private String title;
        @DatabaseField
        private String url;

        public int getId(){ return id; }
        public void setId(int id){ this.id = id; }

        public String getTitle(){ return title; }
        public void setTitle(String title){ this.title = title;}

        public String getUrl(){ return url; }
        public void setUrl(String url){ this.url = url; }
    }
}
