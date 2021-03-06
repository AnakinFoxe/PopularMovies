package com.anakinfoxe.popularmovies;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

/**
 * Created by xing on 1/23/16.
 */
public class PopularMovieApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // init Fresco before everything else
        Fresco.initialize(this);

        Stetho.initializeWithDefaults(this);
    }
}
