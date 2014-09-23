package com.mli.mackaber.mylittleimageproject.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by mackaber on 23/09/14.
 */
public interface Albums {

    @GET("/albums")
    List<Album> getAllAlbums();

    @DatabaseTable(tableName = "albums")
    public class Album {

        @DatabaseField(id = true, generatedId = false)
        private int id;
        @DatabaseField
        private String title;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

    }
}
