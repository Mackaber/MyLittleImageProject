package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.ListAdapter;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.mli.mackaber.mylittleimageproject.models.Videos;

import java.io.File;
import java.sql.SQLException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class New_video extends Activity {

    private Dao<Videos.Video, Integer> videoDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;

    private ListAdapter listAdapter;

    public static final String ALBUM_ID = "Album_id";
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private VideoView videoView;
    private MediaController mediaController;
    private int albumid;
    private TypedFile typedFile;


    private Videos.Video video;

    private EditText mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_video);

        Bundle extras = getIntent().getExtras();
        albumid = extras.getInt(ALBUM_ID);

        videoView = (VideoView) findViewById(R.id.videoPreview);
        listAdapter = Aplication.getApplication().getListAdapter();

        final Button take_picture = (Button) findViewById(R.id.take_video);
        take_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakeVideoIntent();
            }
        });

        final Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void confirm() {
        Videos repre = Aplication.getApplication().getRestAdapter().create(Videos.class);
        Pictures.Picture picture;
        video = new Videos.Video();

        mTitleText = (EditText) findViewById(R.id.video_title);

        video.setTitle(mTitleText.getText().toString());


        Callback<Videos.Video> callback;
        callback = new Callback<Videos.Video>() {
            @Override
            public void success (Videos.Video video, Response response){
                Toast.makeText(getApplicationContext(), video.getTitle(),
                        Toast.LENGTH_LONG).show();
                Log.d("Si jalo: ", video.getTitle());
                try {
                    videoDao = Aplication.getApplication().getVideoeDao();
                    albumDao = Aplication.getApplication().getAlbumDao();
                    video.setAlbum(albumDao.queryForId(albumid));
                    videoDao.create(video);

                    Videos.Video new_video = videoDao.queryForId(video.getId());
                    listAdapter.addItem(new_video);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure (RetrofitError error){
                Toast.makeText(getApplicationContext(), "FAIL!",
                        Toast.LENGTH_LONG).show();
            }
        };

        repre.createVideo(video.getTitle(), albumid, typedFile, callback);

//        Intent mIntent = new Intent();
//        mIntent.putExtras(bundle);
//        setResult(RESULT_OK, mIntent);

        finish();
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();

            Log.d("Uririririri :", videoUri.toString());
            videoView.setVideoURI(videoUri);

            mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();

            String tempPath = getPath(videoUri, New_video.this);
            File file = new File(tempPath);
            typedFile = new TypedFile("video/mp4", file);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
