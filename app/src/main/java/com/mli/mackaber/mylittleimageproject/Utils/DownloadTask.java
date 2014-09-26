package com.mli.mackaber.mylittleimageproject.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.AlbumsAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.VideosAdapter;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;
import com.mli.mackaber.mylittleimageproject.models.Videos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by mackaber on 23/09/14.
 */

public class DownloadTask extends AsyncTask<Void, Void, Void> {
    private ListView list;

    private Dao<Pictures.Picture, Integer> pictureDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;
    private Dao<Videos.Video, Integer> videoDao = null;
    private List<Albums.Album> albums;
    private List<Pictures.Picture> pictures;
    private List<Videos.Video> videos;


    private List<Albums.Album> albumsToInsert = new ArrayList<Albums.Album>();
    private List<Pictures.Picture> picturesToInsert = new ArrayList<Pictures.Picture>();
    private List<Videos.Video> videosToInsert = new ArrayList<Videos.Video>();

    private AlbumsAdapter albumsAdapter;
    private PicturesAdapter picturesAdapter;
    private VideosAdapter videosAdapter;

    private Activity activity;
    private AdapterView.OnItemClickListener handler;

    public DownloadTask(Activity activity, AdapterView.OnItemClickListener handler){
        this.activity = activity;
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(final Void... arg0) {

        Albums albumsAdapter = Aplication.getApplication().getRestAdapter().create(Albums.class);
        Pictures picturesAdapter = Aplication.getApplication().getRestAdapter().create(Pictures.class);
        Videos videosAdapter = Aplication.getApplication().getRestAdapter().create(Videos.class);
        try {
            albumDao = Aplication.getApplication().getAlbumDao();
            pictureDao = Aplication.getApplication().getPictureDao();
            videoDao = Aplication.getApplication().getVideoeDao();

            Log.d("The table exists: ", pictureDao.isTableExists() + "");
            Log.d("The size is more than 0 : ", (pictureDao.queryForAll().size()) + "");
            if(pictureDao.isTableExists() && pictureDao.queryForAll().size()>0) {
                Log.d("Using...", "DB");
                pictures = pictureDao.queryForAll();
                albums = albumDao.queryForAll();
                videos = videoDao.queryForAll();
            } else {

                Log.d("Using...", "Adapter");
                Log.d("Total Albums: ",albumsAdapter.getAllAlbums().size() + "");
                Log.d("Total Pictures: ",picturesAdapter.getAllPictures().size() + "");

                albumsToInsert = albumsAdapter.getAllAlbums();

                albumDao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws Exception {
                        for (Albums.Album album: albumsToInsert) {
                            Log.d("Album #" + album.getId(), album.getTitle());
                            albumDao.create(album);
                        }
                        return null;
                    }
                });

                picturesToInsert = picturesAdapter.getAllPictures();

                pictureDao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws Exception {
                        for (Pictures.Picture picture: picturesToInsert) {
                            picture.setAlbum(albumDao.queryForId(picture.getAlbum_id()));
                            Log.d("Picture #" + picture.getId(), picture.getTitle() + ", " +  picture.getDescription() + ", " + picture.getAlbum().getTitle() );
                            pictureDao.create(picture);
                        }
                        return null;
                    }
                });

                videosToInsert = videosAdapter.getAllVideos();

                videoDao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws Exception {
                        for (Videos.Video video: videosToInsert) {
                            video.setAlbum(albumDao.queryForId(video.getAlbum_id()));
                            Log.d("Video #" + video.getId(), video.getTitle() + ", " +  video.getDescription() + ", " + video.getAlbum().getTitle() );
                            videoDao.create(video);
                        }
                        return null;
                    }
                });

                pictures = pictureDao.queryForAll();
                albums = albumDao.queryForAll();
                videos = videoDao.queryForAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onPostExecute(Void result) {
        albumsAdapter = new AlbumsAdapter(activity,R.layout.list_item, albums);

        list = (ListView) activity.findViewById(R.id.albumListView);
        list.setAdapter(albumsAdapter);
        list.setOnItemClickListener(handler);

        Aplication.getApplication().setAlbumsAdapter(albumsAdapter);

        Toast.makeText(Aplication.getApplication().getContext(),"DONE!",Toast.LENGTH_LONG).show();

//        try {
//            Albums.Album test = albumDao.queryForId(1);
//            Log.d("TESTING... ",test.getPictures().iterator().next().getTitle());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}