package com.anakinfoxe.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anakinfoxe.popularmovies.data.MovieContract.MovieEntry;
import com.anakinfoxe.popularmovies.data.MovieContract.ReviewEntry;
import com.anakinfoxe.popularmovies.data.MovieContract.VideoEntry;

/**
 * Created by xing on 4/13/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // remember to increment database version once database schema has been changed
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "popularmovies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // define movie table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME
                + " (" +

                // columns
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntry.COLUMN_ADULT + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_HOMEPAGE + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT UNIQUE NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_POPULARITY + " REAL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " + // TODO: TEXT or INTEGER?
                MovieEntry.COLUMN_RUNTIME + " INTEGER, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +

                // unique on movie_id
                " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE"

                // end
                + ");";

        // define video table
        final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE " + VideoEntry.TABLE_NAME
                + " (" +

                // columns
                VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VideoEntry.COLUMN_VIDEO_ID + " TEXT UNIQUE NOT NULL, " +
                VideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_NAME + " TEXT, " +
                VideoEntry.COLUMN_SITE + " TEXT, " +
                VideoEntry.COLUMN_SIZE + " INTEGER, " +
                VideoEntry.COLUMN_TYPE + " TEXT, " +
                VideoEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

                // foreign key
                " FOREIGN KEY (" + VideoEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +

                // unique on video_id
                " UNIQUE (" + VideoEntry.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE"

                // end
                + ");";

        // define review table
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME
                +" (" +

                // columns
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COLUMN_REVIEW_ID + " TEXT UNIQUE NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT, " +
                ReviewEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL, " +

                // foreign key
                " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +

                // unique on review_id
                " UNIQUE (" + ReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE"

                // end
                + ");";

        // create these tables
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
