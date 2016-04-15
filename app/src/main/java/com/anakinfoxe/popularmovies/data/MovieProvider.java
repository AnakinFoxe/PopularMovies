package com.anakinfoxe.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by xing on 4/13/16.
 */
public class MovieProvider extends ContentProvider {

    private MovieDbHelper mDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final int MOVIE                  = 100;
    static final int MOVIE_WITH_MOVIE_ID    = 101;
    static final int VIDEO                  = 200;
    static final int VIDEO_WITH_VIDEO_ID    = 201;
    static final int VIDEO_OF_MOVIE_ID      = 210;
    static final int REVIEW                 = 300;
    static final int REVIEW_WITH_REVIEW_ID  = 301;
    static final int REVIEW_OF_MOVIE_ID     = 310;

    private static final SQLiteQueryBuilder sVideoByMovieIdQueryBuilder;
    private static final SQLiteQueryBuilder sReviewByMovieIdQueryBuilder;

    static {
        sVideoByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        sVideoByMovieIdQueryBuilder.setTables(
                MovieContract.VideoEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.VideoEntry.TABLE_NAME +
                        "." + MovieContract.VideoEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID
        );

        sReviewByMovieIdQueryBuilder = new SQLiteQueryBuilder();
        sReviewByMovieIdQueryBuilder.setTables(
                MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_KEY +
                        " = " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID
        );
    }

    private static final String sMovieIdSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                    " = ? ";

    private Cursor getVideoByMovieId(SQLiteDatabase db, Uri uri, String[] projection,
                                     String sortOrder) {
        String movieId = MovieContract.VideoEntry.getMovieIdFromUri(uri);

        return sVideoByMovieIdQueryBuilder.query(
                db,
                projection,
                sMovieIdSelection,
                new String[] {movieId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewByMovieId(SQLiteDatabase db, Uri uri, String[] projection,
                                      String sortOrder) {
        String movieId = MovieContract.ReviewEntry.getMovieIdFromUri(uri);

        return sReviewByMovieIdQueryBuilder.query(
                db,
                projection,
                sMovieIdSelection,
                new String[] {movieId},
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/*", VIDEO_WITH_VIDEO_ID);
        matcher.addURI(authority, MovieContract.PATH_VIDEO + "/" +
                                MovieContract.PATH_MOVIE + "/*", VIDEO_OF_MOVIE_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", REVIEW_WITH_REVIEW_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/" +
                                MovieContract.PATH_MOVIE + "/*", REVIEW_OF_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case VIDEO:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            case VIDEO_WITH_VIDEO_ID:
                return MovieContract.VideoEntry.CONTENT_ITEM_TYPE;
            case VIDEO_OF_MOVIE_ID:
                return MovieContract.VideoEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_WITH_REVIEW_ID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case REVIEW_OF_MOVIE_ID:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final SQLiteDatabase rDb = mDbHelper.getReadableDatabase();

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = rDb.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_WITH_MOVIE_ID: {
                retCursor = rDb.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{MovieContract.MovieEntry.getMovieIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case VIDEO: {
                retCursor = rDb.query(
                        MovieContract.VideoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case VIDEO_WITH_VIDEO_ID: {
                retCursor = rDb.query(
                        MovieContract.VideoEntry.TABLE_NAME,
                        projection,
                        MovieContract.VideoEntry.COLUMN_VIDEO_ID + " = ?",
                        new String[]{MovieContract.VideoEntry.getVideoIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case VIDEO_OF_MOVIE_ID: {
                retCursor = getVideoByMovieId(rDb, uri, projection, sortOrder);
                break;
            }
            case REVIEW: {
                retCursor = rDb.query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW_WITH_REVIEW_ID: {
                retCursor = rDb.query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " = ?",
                        new String[]{MovieContract.ReviewEntry.getReviewIdFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW_OF_MOVIE_ID: {
                retCursor = getReviewByMovieId(rDb, uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // register a content observer to watch changes happened to this uri
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // end
        rDb.close();

        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase wDb = mDbHelper.getWritableDatabase();

        Uri retUri;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                long _id = wDb.insert(
                        MovieContract.MovieEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0)
                    retUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case VIDEO: {
                long _id = wDb.insert(
                        MovieContract.VideoEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0)
                    retUri = MovieContract.VideoEntry.buildVideoUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = wDb.insert(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        null,
                        values
                );
                if (_id > 0)
                    retUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // register a content observer to watch changes happened to this uri
        getContext().getContentResolver().notifyChange(uri, null);

        // end
        wDb.close();

        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase wDb = mDbHelper.getWritableDatabase();

        // selection = "1" will delete all the rows
        if (selection == null)
            selection = "1";

        int rowsDeleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                rowsDeleted = wDb.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case VIDEO: {
                rowsDeleted = wDb.delete(
                        MovieContract.VideoEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            case REVIEW: {
                rowsDeleted = wDb.delete(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        // notify the listener only when something is deleted
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);


        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase wDb = mDbHelper.getWritableDatabase();

        int rowsEffected;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                rowsEffected = wDb.update(
                        MovieContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }
            case VIDEO: {
                rowsEffected = wDb.update(
                        MovieContract.VideoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }
            case REVIEW: {
                rowsEffected = wDb.update(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (rowsEffected != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsEffected;
    }


}
