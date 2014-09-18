package com.mli.mackaber.mylittleimageproject.models;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.mli.mackaber.mylittleimageproject.databases.DatabaseOrm;


/**
 * Created by alienware18 on 9-8-13.
 */
public class DatabaseManager {


    private DatabaseOrm databaseHelper = null;

    //gets a helper once one is created ensures it doesnt create a new one
    public DatabaseOrm getHelper(Context context)
    {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(context, DatabaseOrm.class);
        }
        return databaseHelper;
    }

    //releases the helper once usages has ended
    public void releaseHelper(DatabaseOrm helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

}
