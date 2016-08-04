package com.geval6.praymate.Utlis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.widget.ImageView;
import com.geval6.praymate.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {
    ExecutorService executorService;
    FileCache fileCache;
    Handler handler;
    private Map<ImageView, String> imageViews;
    MemoryCache memoryCache;
    final int stub_id;

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            this.bitmap = b;
            this.photoToLoad = p;
        }

        public void run() {
            if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                if (this.bitmap != null) {
                    this.photoToLoad.imageView.setImageBitmap(this.bitmap);
                } else {
                    this.photoToLoad.imageView.setImageResource(R.drawable.temp_img);
                }
            }
        }
    }

    private class PhotoToLoad {
        public ImageView imageView;
        public String url;

        public PhotoToLoad(String u, ImageView i) {
            this.url = u;
            this.imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        public void run() {
            try {
                if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                    Bitmap bmp = ImageLoader.this.getBitmap(this.photoToLoad.url);
                    ImageLoader.this.memoryCache.put(this.photoToLoad.url, bmp);
                    if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                        ImageLoader.this.handler.post(new BitmapDisplayer(bmp, this.photoToLoad));
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    public ImageLoader(Context context) {
        this.memoryCache = new MemoryCache();
        this.imageViews = Collections.synchronizedMap(new WeakHashMap());
        this.handler = new Handler();
        this.stub_id = R.drawable.temp_img;
        this.fileCache = new FileCache(context);
        this.executorService = Executors.newFixedThreadPool(5);
    }

    public void DisplayImage(String url, ImageView imageView) {
        this.imageViews.put(imageView, url);
        Bitmap bitmap = this.memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        queuePhoto(url, imageView);
        imageView.setImageResource(R.drawable.temp_img);
    }

    private void queuePhoto(String url, ImageView imageView) {
        this.executorService.submit(new PhotosLoader(new PhotoToLoad(url, imageView)));
    }

    public Bitmap getBitmap(String url) {
        File f = this.fileCache.getFile(url);
        Bitmap b = decodeFile(f);
        if (b != null) {
            return b;
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            return decodeFile(f);
        } catch (Throwable ex) {
            ex.printStackTrace();
            if (ex instanceof OutOfMemoryError) {
                this.memoryCache.clear();
            }
            return null;
        }
    }

    public Bitmap decodeFile(File f) {
        try {
            Options o = new Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();
            int width_tmp = o.outWidth;
            int height_tmp = o.outHeight;
            int scale = 1;
            while (width_tmp / 2 >= 70 && height_tmp / 2 >= 70) {
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = (String) this.imageViews.get(photoToLoad.imageView);
        return tag == null || !tag.equals(photoToLoad.url);
    }

    public void clearCache() {
        this.memoryCache.clear();
        this.fileCache.clear();
    }
}
