package com.mli.mackaber.mylittleimageproject.models;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.databases.DatabaseOrm;
import com.mli.mackaber.mylittleimageproject.models.Albums.Album;

import java.sql.SQLException;
import java.util.List;

public class AlbumRepository {

    private DatabaseOrm db;
    Dao<Albums.Album, Integer> albumsDao;

    public AlbumRepository(Context ctx)
    {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            albumsDao = db.getAlbumDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }

    }

    public int create(Albums.Album album)
    {
        try {
            return albumsDao.create(album);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(Album album)
    {
        try {
            return albumsDao.update(album);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Album album)
    {
        try {
            return albumsDao.delete(album);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List<Album> getAll()
    {
        try {
            return albumsDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
