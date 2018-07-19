package com.inc.mountzoft.popularmoviesstage_2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class RoomMovieEntity {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "movie_name")
    private String movieName;

    @ColumnInfo(name = "movie_rating")
    private String movieRating;

    @ColumnInfo(name = "movie_release_date")
    private String movieReleaseDate;

    @ColumnInfo(name = "movie_synopsis")
    private String movieSynopsis;

    @ColumnInfo(name = "poster_image_url")
    private String posterImageUrl;

    @ColumnInfo(name = "backdrop_image_url")
    private String backdropImageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getBackdropImageUrl() {
        return backdropImageUrl;
    }

    public void setBackdropImageUrl(String backdropImageUrl) {
        this.backdropImageUrl = backdropImageUrl;
    }
}
