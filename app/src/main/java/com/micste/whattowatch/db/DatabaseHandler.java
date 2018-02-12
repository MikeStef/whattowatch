package com.micste.whattowatch.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.micste.whattowatch.model.MovieLight;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "watchListDatabase";
    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_POSTER_PATH = "poster_path";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_MOVIE_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT," + KEY_RELEASE_DATE + " TEXT,"
                + KEY_VOTE_AVERAGE + " TEXT," + KEY_POSTER_PATH + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        // Create tables again
        onCreate(db);
    }

    public void addMovie(MovieLight movie) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE_ID, movie.getMovieId());
        values.put(KEY_POSTER_PATH, movie.getPosterPath());
        values.put(KEY_RELEASE_DATE, movie.getReleaseDate());
        values.put(KEY_TITLE, movie.getTitle());
        values.put(KEY_VOTE_AVERAGE, movie.getVoteAverage());

        database.insert(TABLE_MOVIES, null, values);
        database.close();
    }

    public List<MovieLight> getAllMovies() {
        List<MovieLight> movieList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MOVIES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MovieLight movie = new MovieLight();
                movie.setMovieId(Integer.parseInt(cursor.getString(0)));
                movie.setTitle(cursor.getString(1));
                movie.setReleaseDate(cursor.getString(2));
                movie.setVoteAverage(cursor.getString(3));
                movie.setPosterPath(cursor.getString(4));

                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return movieList;
    }

    public void deleteMovie(MovieLight movie) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_MOVIES, KEY_MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie.getMovieId()) });
        database.close();
    }

    public boolean checkIfMovieExists(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String[] columns = { KEY_MOVIE_ID };
        String selection = KEY_MOVIE_ID + " =?";
        String[] selectionArgs = { id };
        String limit = "1";

        Cursor cursor = database.query(TABLE_MOVIES, columns, selection, selectionArgs,
                null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        database.close();

        return exists;
    }
}
