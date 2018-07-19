package com.inc.mountzoft.popularmoviesstage_2;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RoomMovieDao {
    @Insert
    void addMovie(RoomMovieEntity roomMovieEntity);

    @Query("SELECT * FROM roommovieentity")
    List<RoomMovieEntity> getAllMovies();

    @Query("DELETE FROM roommovieentity WHERE id = :movieId")
    void deleteByMovieId(int movieId);
}
