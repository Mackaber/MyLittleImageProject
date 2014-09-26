package com.mli.mackaber.mylittleimageproject.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.mli.mackaber.mylittleimageproject.Aplication;
import com.mli.mackaber.mylittleimageproject.R;
import com.mli.mackaber.mylittleimageproject.adapters.AlbumsAdapter;
import com.mli.mackaber.mylittleimageproject.adapters.PicturesAdapter;
import com.mli.mackaber.mylittleimageproject.models.Albums;
import com.mli.mackaber.mylittleimageproject.models.Pictures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class New_picture extends Activity {

    private Dao<Pictures.Picture, Integer> pictureDao = null;
    private Dao<Albums.Album, Integer> albumDao = null;

    private PicturesAdapter picturesAdapter;

    private EditText mTitleText;
    public static final String ALBUM_ID = "Album_id";
    static final int REQUEST_CAMERA = 1;
    static final int SELECT_FILE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private TypedFile typedFile;
    private ImageView ivImage;
    private int albumid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_picture);

        Bundle extras = getIntent().getExtras();
        albumid = extras.getInt(ALBUM_ID);

        ivImage = (ImageView) findViewById(R.id.imagePreview);

        picturesAdapter = Aplication.getApplication().getPicturesAdapter();

        final Button confirm = (Button) findViewById(R.id.confirm_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                confirm();
            }
        });

        final Button take_picture = (Button) findViewById(R.id.take_picture);
        take_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(New_picture.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);

                    // bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    ivImage.setImageBitmap(bm);

                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Photos";

                    File mFolder = new File(path);
                    if (!mFolder.exists()) {
                        mFolder.mkdir();
                    }

                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                        fOut.flush();
                        fOut.close();

                        typedFile = new TypedFile("image/jpeg", file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                Log.d("El URI: ", selectedImageUri.toString());

                String tempPath = getPath(selectedImageUri, New_picture.this);
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                ivImage.setImageBitmap(bm);

                File file = new File(tempPath);

                typedFile = new TypedFile("image/jpeg", file);
            }


        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_picture, menu);
        return true;
    }

    private void confirm() {
        Pictures repre = Aplication.getApplication().getRestAdapter().create(Pictures.class);
        Pictures.Picture picture;
        picture = new Pictures.Picture();

        mTitleText = (EditText) findViewById(R.id.picture_title);

        picture.setTitle(mTitleText.getText().toString());


        Callback<Pictures.Picture> callback;
        callback = new Callback<Pictures.Picture>() {
            @Override
            public void success (Pictures.Picture picture, Response response){
                Toast.makeText(getApplicationContext(), picture.getTitle(),
                        Toast.LENGTH_LONG).show();
                Log.d("Si jalo: ", picture.getTitle());
                try {
                    pictureDao = Aplication.getApplication().getPictureDao();
                    albumDao = Aplication.getApplication().getAlbumDao();
                    picture.setAlbum(albumDao.queryForId(albumid));
                    pictureDao.create(picture);

                    Pictures.Picture new_picture = pictureDao.queryForId(picture.getId());
                    picturesAdapter.addPicture(new_picture);


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

        repre.createPicture(picture.getTitle(),albumid,typedFile,callback);

//        Intent mIntent = new Intent();
//        mIntent.putExtras(bundle);
//        setResult(RESULT_OK, mIntent);

        finish();
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
