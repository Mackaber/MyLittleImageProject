package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.utils.*;

import java.sql.SQLException;

/*
    This is the List Activity Class
*/

public class MainActivity extends Activity {

//  Database Variables
    public static final int CLEAR_DB = Menu.FIRST;
    public static final int NEW_PONY = 2;
    static final int NEW_IMAGE = 1;

//  View Variables
    private ListView list;
    private Activity activity = this;
    private PicturesAdapter picturesAdapter;

//V----------------------------------------------- ACTIVITY METHODS ----------------------------------------------------------------V

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_activity);
        new DownloadTask(this,itemClickHandler).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        menu.add(0,CLEAR_DB,0,R.string.clear_database);
        menu.add(1,NEW_PONY,1,R.string.new_pony);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case CLEAR_DB:
                cleardatabase();
                return true;
            case NEW_PONY:
                new_picture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//V----------------------------------------------- CUSTOM METHODS ----------------------------------------------------------------V

    private AdapterView.OnItemClickListener itemClickHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            picturesAdapter = Aplication.getApplication().getPicturesAdapter();

            Intent intent = new Intent(getApplicationContext(), Picture_detail.class);
            intent.putExtra(Picture_detail.ARG_ITEM_ID, picturesAdapter.getPictureAt(position).getId());
            startActivity(intent);
        }
    };

    public void new_picture(){
        Intent intent = new Intent(getApplicationContext(), New_picture.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_IMAGE && resultCode == RESULT_OK) {

            activity.recreate();
        }
    }

    public void cleardatabase(){
        try {
            Aplication.getApplication().cleanPictures();
            activity.recreate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
