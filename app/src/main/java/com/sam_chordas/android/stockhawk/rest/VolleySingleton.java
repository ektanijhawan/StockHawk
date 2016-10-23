package com.sam_chordas.android.stockhawk.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Ekta on 23-10-2016.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);


        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {


            private LruCache<String, Bitmap> cache = new LruCache<>((int) Runtime.getRuntime().maxMemory() / 1024 / 8);


            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

                cache.put(url, bitmap);
            }
        });
    }


    public static VolleySingleton getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new VolleySingleton(context);

        }
        return sInstance;
    }

    public RequestQueue getmRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        return mImageLoader;
    }
}

