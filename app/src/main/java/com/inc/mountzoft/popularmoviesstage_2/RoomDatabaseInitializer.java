package com.inc.mountzoft.popularmoviesstage_2;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class RoomDatabaseInitializer {


    public static List<RoomMovieEntity> roomMovieEntityList;

    private static final String TAG = "check";

    public static void populateAsync(@NonNull final RoomMovieDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final RoomMovieDatabase db) {
        populateWithTestData(db);
    }

    private static RoomMovieEntity addUser(final RoomMovieDatabase db, RoomMovieEntity roomMovieEntity) {
        db.roomMovieDao().addMovie(roomMovieEntity);
        return roomMovieEntity;
    }

    private static void populateWithTestData(RoomMovieDatabase db) {
        if(MainActivity.insertData) {
            RoomMovieEntity roomMovieEntity = new RoomMovieEntity();
            roomMovieEntity.setId(MovieDetails.movieId);
            roomMovieEntity.setMovieName(MovieDetails.movieName);
            roomMovieEntity.setMovieRating(MovieDetails.movieRating);
            roomMovieEntity.setMovieReleaseDate(MovieDetails.movieReleaseDate);
            roomMovieEntity.setMovieSynopsis(MovieDetails.movieSynopsis);
            roomMovieEntity.setPosterImageUrl(MovieDetails.moviePosterImageUrl);
            roomMovieEntity.setBackdropImageUrl(MovieDetails.movieBackbropImageUrl);
            addUser(db, roomMovieEntity);
        }

        roomMovieEntityList = db.roomMovieDao().getAllMovies();
        MovieDetails.movieTable = roomMovieEntityList;
        MovieDetails.totalMovies = RoomDatabaseInitializer.roomMovieEntityList.size();
        Log.d(RoomDatabaseInitializer.TAG, "Rows Count: " + roomMovieEntityList.size());

        if(MovieDetails.del) {
            db.roomMovieDao().deleteByMovieId(MovieDetails.deleteMovieId);
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RoomMovieDatabase mDb;

        PopulateDbAsync(RoomMovieDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
