package com.example.paper.paperinteractive.Picture_Utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PictureUtils {

    public static Bitmap getScaledBitmap(Uri uri, Context context, int destWidth, int destHeight){
        // Read in dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(in, null, options);
        } catch (FileNotFoundException e){
            Log.e("Error: ", e.toString());
        }


        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;
        String imageType = options.outMimeType;

        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth){
            if (srcHeight > destHeight || srcWidth > destWidth) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        // Read in and create final Bitmap
        try {
            in = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(in, null, options);
        } catch (FileNotFoundException e){
            Log.e("Error: ", e.toString());
            return null;
        }
    }
}
