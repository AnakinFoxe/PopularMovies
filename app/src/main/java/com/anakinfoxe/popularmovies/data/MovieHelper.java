package com.anakinfoxe.popularmovies.data;

/**
 * Created by xing on 4/18/16.
 */
public class MovieHelper {
    
    
    // These indices are tied to MovieEntry columns.  
    // If MovieEntry column changes, these must change.
    public static final int COL__ID             = 0;
    public static final int COL_ADULT           = 1;
    public static final int COL_BACKDROP_PATH   = 2;
    public static final int COL_HOMEPAGE        = 3;
    public static final int COL_MOVIE_ID        = 4;
    public static final int COL_ORIGINAL_TITLE  = 5;
    public static final int COL_OVERVIEW        = 6;
    public static final int COL_POPULARITY      = 7;
    public static final int COL_POSTER_PATH     = 8;
    public static final int COL_RELEASE_DATE    = 9;
    public static final int COL_RUNTIME         = 10;
    public static final int COL_TITLE           = 11;
    public static final int COL_VOTE_AVERAGE    = 12;
    public static final int COL_VOTE_COUNT      = 13;
    
    public static final String[] MOVIE_ENTRY_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_ADULT,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_HOMEPAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_POPULARITY,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RUNTIME,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_VOTE_COUNT
    };
    
}
