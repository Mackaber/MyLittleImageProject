package com.mli.mackaber.mylittleimageproject.models;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.databases.DatabaseOrm;
import com.mli.mackaber.mylittleimageproject.models.Pictures.Picture;

import java.sql.SQLException;
import java.util.List;

public class PictureRepository {

    private DatabaseOrm db;
    Dao<Picture, Integer> picturesDao;

    public PictureRepository(Context ctx)
    {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            picturesDao = db.getPicturesDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }

    }

    public int create(Picture picture)
    {
        try {
            return picturesDao.create(picture);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(Picture picture)
    {
        try {
            return picturesDao.update(picture);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Picture picture)
    {
        try {
            return picturesDao.delete(picture);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List<Picture> getAll()
    {
        try {
            return picturesDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
