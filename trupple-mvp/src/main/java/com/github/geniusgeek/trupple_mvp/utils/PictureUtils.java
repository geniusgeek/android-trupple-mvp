package com.github.geniusgeek.trupple_mvp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/20/16.
 */
public final class PictureUtils {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public final static int SELECT_FILE_REQUEST = 1;
    public static final int SHARPEN_DEFAULT = 11;

    /**
     * @throws AssertionError when trying to create an instance of this class
     */
    private PictureUtils() {
        throw new AssertionError("cannot instantiate this class");
    }

    /**
     * Get a BitmapDrawable from a local file that is scaled down
     * to fit the current Window size.
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity a, String path) {
        Display display = a.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        // read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(a.getResources(), bitmap);
    }

    public static void cleanImageView(ImageView imageView) {
        if (!(imageView.getDrawable() instanceof BitmapDrawable))
            return;

        // clean up the view's image for the sake of memory
        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }


    /**
     * See more at: http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample#sthash.4Tqa0MeL.dpuf
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context, final Fragment fragment) {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    fragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            }
            return true;

        }
        return true;

    }

    /**
     * select image from the users phone
     * See more at: http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample#sthash.4Tqa0MeL.dpuf
     *
     * @param data, the {@link Bitmap data to return}
     */
    @SuppressWarnings("deprecation")
    public static Uri onSelectFromGalleryResult(Intent data) {

        return data.getData();
    }

    /**
     * select image from the users phone
     * See more at: http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample#sthash.4Tqa0MeL.dpuf
     *
     * @param data, the {@link Bitmap data to return}
     */
    @SuppressWarnings("deprecation")
    public static Bitmap onSelectFromGalleryResult(Context context, Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }


    /**
     * get Bitmap from a captured photo data
     * - See more at: http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample#sthash.4Tqa0MeL.dpuf
     *
     * @param data
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Bitmap onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fo = new FileOutputStream(destination)) {
            destination.createNewFile();
            fo.write(bytes.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (thumbnail);
    }


    /**
     * @param filename
     * @return
     * @throws IOException
     */
    public static Bitmap loadPrescaledBitmap(String filename) throws IOException {
        // Facebook image size
        final int IMAGE_MAX_SIZE = 630;

        File file = null;
        FileInputStream fis;

        BitmapFactory.Options opts;
        int resizeScale;
        Bitmap bmp;

        file = new File(filename);

        // This bit determines only the width/height of the bitmap without loading the contents
        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        fis = new FileInputStream(file);
        BitmapFactory.decodeStream(fis, null, opts);
        fis.close();

        // Find the correct scale value. It should be a power of 2
        resizeScale = 1;

        if (opts.outHeight > IMAGE_MAX_SIZE || opts.outWidth > IMAGE_MAX_SIZE) {
            resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
        }

        // Load pre-scaled bitmap
        opts = new BitmapFactory.Options();
        opts.inSampleSize = resizeScale;
        fis = new FileInputStream(file);
        bmp = BitmapFactory.decodeStream(fis, null, opts);

        fis.close();

        return bmp;
    }


    /**
     * NOTE: please create a new thread to carry out this operation, so as to avoid ANR dialog
     * NOTE: this has been discovered to have inconsistent result as such it is preferable to use
     *
     * @param context
     * @param contentUri
     * @return
     * @see StorageAccessUtils#getPath(Context, Uri)
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static final String getUrl(final Context context, final Uri uri) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return StorageAccessUtils.getPath(context, uri);
            }
        });
        return future.get();
    }
}
