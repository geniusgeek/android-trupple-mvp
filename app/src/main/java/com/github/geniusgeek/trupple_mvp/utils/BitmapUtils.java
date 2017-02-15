package com.github.geniusgeek.trupple_mvp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by DOTECH on 16/02/2016.
 */
public final class BitmapUtils {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    private BitmapUtils() {
        throw new AssertionError("cannot instantiate this object");
    }


    public static void loadImagePicasso(final Context context, final String url, final @DrawableRes int defId, final ImageView destination
            , Callback callback) {
        if (url == null) {
            destination.setImageResource(defId);
            return;
        }
        //otherwise set the user avatar image
        Picasso.with(context)
                .load(url)
                .fit()
                .placeholder(defId)
                .into(destination, callback==null?new Callback.EmptyCallback():callback);


    }

    public static void loadSquareImagePicasso(final Context context, final int size, final String url, final @DrawableRes int defId, final ImageView destination) {
        if (url == null) {
            destination.setImageResource(defId);
            return;
        }
        //otherwise set the user avatar image

        Picasso.with(context)
                .load(url)
                .resize(size, size)
                .placeholder(defId)
                .into(destination);

    }
    public static void loadWidthHeightImagePicasso(final Context context, final String url,final int width,final int height, final @DrawableRes int defId, final ImageView destination) {
        if (url == null) {
            destination.setImageResource(defId);
            return;
        }
        //otherwise set the user avatar image

        Picasso.with(context)
                .load(url)
                .resize(width, height)
                .placeholder(defId)
                .into(destination);

    }

    public static Bitmap getBitmapFromUrl(final Context context, final String url) {

        try {
            return Picasso.with(context).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();

            return BitmapFactory.decodeFile(url);
        }

    }

    /**
     * @return the url of the saved image
     */
    public static String saveBitmapToSDCard(Context context, Bitmap image, String userName) {
        verifyStoragePermission(context);//verify permission

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        String path = Environment.getExternalStorageDirectory() + File.separator + userName + ".png";
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(outputStream.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    private static void verifyStoragePermission(Context activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED)
            return;
        ActivityCompat.requestPermissions((Activity) activity,
                PERMISSION_STORAGE, REQUEST_EXTERNAL_STORAGE
        );

    }

    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    public static String getUrlFromResource(final Context context, final int resource) {

        ExecutorService executors = Executors.newFixedThreadPool(2);
        Future<String> future = executors.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + context.getResources().getResourcePackageName(resource)
                        + '/' + context.getResources().getResourceTypeName(resource) +
                        '/' + context.getResources().getResourceEntryName(resource));
                return imageUri.toString();
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Uri getUriFromPath(String path) {
        File file = new File(path);
        return Uri.fromFile(file);
    }

    public static Bitmap putOverlay(Bitmap bmp1, Bitmap overlay) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bmp1, 0, 0, null);
        canvas.drawBitmap(overlay, 0, 0, null);

        return bmOverlay;
    }
}
