package com.anakinfoxe.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by xing on 4/13/16.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.anakinfoxe.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_VIDEO = "video";
    public static final String PATH_REVIEW = "review";


    public static final class MovieEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME               = "movie";

        // columns
        public static final String COLUMN_ADULT             = "adult";
        public static final String COLUMN_BACKDROP_PATH     = "backdrop_path";
        public static final String COLUMN_HOMEPAGE          = "homepage";
        public static final String COLUMN_MOVIE_ID          = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE    = "original_title";
        public static final String COLUMN_OVERVIEW          = "overview";
        public static final String COLUMN_POPULARITY        = "popularity";
        public static final String COLUMN_POSTER_PATH       = "poster_path";
        public static final String COLUMN_RELEASE_DATE      = "release_date";
        public static final String COLUMN_RUNTIME           = "runtime";
        public static final String COLUMN_TITLE             = "title";
        public static final String COLUMN_VOTE_AVERAGE      = "vote_average";
        public static final String COLUMN_VOTE_COUNT        = "vote_count";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class VideoEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME               = "video";

        // columns
        public static final String COLUMN_VIDEO_ID          = "video_id";
        public static final String COLUMN_KEY               = "key";
        public static final String COLUMN_NAME              = "name";
        public static final String COLUMN_SITE              = "site";
        public static final String COLUMN_SIZE              = "size";
        public static final String COLUMN_TYPE              = "type";
        public static final String COLUMN_MOVIE_KEY         = "movie_key";  // foreign key

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_VIDEO).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VIDEO;

        public static Uri buildVideoUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildVideoWithVideoId(String videoId) {
            return CONTENT_URI.buildUpon().appendPath(videoId).build();
        }

        public static String getVideoIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }

    public static final class ReviewEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME       = "review";

        // columns
        public static final String COLUMN_REVIEW_ID         = "review_id";
        public static final String COLUMN_AUTHOR            = "author";
        public static final String COLUMN_CONTENT           = "content";
        public static final String COLUMN_MOVIE_KEY         = "movie_key";  // foreign key

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewWithReviewId(String reviewId) {
            return CONTENT_URI.buildUpon().appendPath(reviewId).build();
        }

        public static String getReviewIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }
    }
}
