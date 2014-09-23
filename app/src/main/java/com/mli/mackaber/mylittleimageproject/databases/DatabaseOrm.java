package com.mli.mackaber.mylittleimageproject.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
* Created by mackaber on 17/09/14.
*/

/** SQLite Adapter
 *
 *
 * Database Structure
 *
 *
 * */
public class DatabaseOrm extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ponies";
    private static final int DATABASE_VERSION = 10;
    private SQLiteDatabase db;

    // the DAO object we use to access the SimpleData table
    private Dao<Pictures.Picture, Integer> picturesDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;


    public DatabaseOrm(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            this.db = db;
            Log.i(DatabaseOrm.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, Albums.Album.class);
            TableUtils.createTable(connectionSource, Pictures.Picture.class);

        } catch (SQLException e) {
            Log.e(DatabaseOrm.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseOrm.class.getName(), "onUpgrade");

            TableUtils.dropTable(connectionSource, Albums.Album.class, true);
            TableUtils.dropTable(connectionSource, Pictures.Picture.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseOrm.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Pictures.Picture, Integer> getPicturesDao() throws SQLException {
        if (picturesDao == null) {
            picturesDao = getDao(Pictures.Picture.class);
        }
        return picturesDao;
    }

    public Dao<Albums.Album, Integer> getAlbumDao() throws SQLException {
        if (albumDao == null) {
            albumDao = getDao(Albums.Album.class);
        }
        return albumDao;
    }

    @Override
    public void close() {
        super.close();
    }

    public void cleanAll() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), Pictures.Picture.class);
        TableUtils.clearTable(getConnectionSource(), Albums.Album.class);
    }

}