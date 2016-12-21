package com.example.paper.paperinteractive.Library;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.paper.paperinteractive.Database.DBHandler;
import com.example.paper.paperinteractive.Picture_Utilities.PictureUtils;
import com.example.paper.paperinteractive.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpandableListChild extends AppCompatActivity {

    private int mID;
    private String mName;
    private GridView gridView;
    private CustomMediaAdapter adapter;
    private Button btnAddMedia;
    private TextView textName;
    private LinearLayout mLayout;
    private LruCache<String, Bitmap> mMemoryCache;

    private Uri imageUri;

    String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandablelistchild);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Lägg till media");
        setSupportActionBar(toolbar);

        int memClass = ((ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

        final int cacheSize = 1024*1024*memClass/8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return bitmap.getByteCount();
            }
        };

        Intent intent = getIntent();
        mID = (int)intent.getLongExtra("ID", 0);
        mName = intent.getStringExtra("NAME");

        mLayout = (LinearLayout) findViewById(R.id.emptyMediaLayout);
        gridView = (GridView) findViewById(R.id.mediagrid);
        try {
            adapter = new CustomMediaAdapter(this);
            gridView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnAddMedia = (Button) findViewById(R.id.btnAddMedia);
        textName = (TextView) findViewById(R.id.textName);
        textName.setText(mName);
        UpdateGrid();

        btnAddMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                }
                UpdateGrid();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_media, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add_media:
                dispatchTakePictureIntent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void UpdateGrid(){
        if (gridView.getAdapter().getCount() != 0){
            gridView.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
        } else {
            gridView.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg",        /* suffix */
                storageDir     /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){
                // Error occured while creating the file
            }

            // Continue only if the file was created successfully
            if (photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(this, "com.example.paper.paperinteractive",
                        photoFile);
                imageUri = photoUri;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            DBHandler.getInstance(this).addMedia(mID, imageUri.toString());
            adapter.notifyDataSetChanged();
            UpdateGrid();
        } else {
            getContentResolver().delete(imageUri, null, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    btnAddMedia.callOnClick();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null){
            outState.putString("cameraImageUri", imageUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")){
            imageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    private class CustomMediaAdapter extends BaseAdapter {

        Context mContext;
        List<Uri> bitmapLocations;

        CustomMediaAdapter(Context context) throws IOException {
            mContext = context;
            bitmapLocations = DBHandler.getInstance(getBaseContext()).getMediaUris(mID);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            bitmapLocations = DBHandler.getInstance(getBaseContext()).getMediaUris(mID);
        }


        @Override
        public int getCount() {
            return bitmapLocations.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View row = view;
            ViewHolder vh;

            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(R.layout.photo_gridview, viewGroup, false);
            } else {
                row = (View) view;
            }
            vh = new ViewHolder(row);
            vh.imageView = (ImageView) row.findViewById(R.id.pictureView);
            vh.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            loadBitmap(bitmapLocations.get(i), vh.imageView);
            return row;
        }

        public void loadBitmap(Uri uri, ImageView imageView) {
            final String imageKey = uri.getPath();

            final Bitmap bitmap = getBitmapFromMemCache(imageKey);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.mipmap.placeholder_thumbnail);
                BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                task.execute(uri);
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap>{
        private final WeakReference<ImageView> imageViewReference;
        private Uri uri;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        //Decode image in background
        @Override
        protected Bitmap doInBackground(Uri... params) {
            uri = params[0];
            final Bitmap bitmap = PictureUtils.getScaledBitmap(uri, getApplicationContext(), 300,250);
            addBitmapToMemoryCache(params[0].getPath(), bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(isCancelled()){
                bitmap = null;
            }

            if(imageViewReference != null && bitmap != null){
                final ImageView imageView = imageViewReference.get();
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }
}
