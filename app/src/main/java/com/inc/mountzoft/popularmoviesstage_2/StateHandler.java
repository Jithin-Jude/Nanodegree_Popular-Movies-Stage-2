package com.inc.mountzoft.popularmoviesstage_2;

import android.content.Context;


/**
 * Helper class to deal with different error states
 * of the values for certain views to display. Returns
 * the required value for such variables depending on
 * the state. For instance, if there is a network error
 * intimate user to check if device is connected to the
 * Internet by setting the tagline text to the same.
 */
public class StateHandler {
    public static String handleMovieDetailState(Context context, int flag) {
        String tagLine;
        switch (flag) {
            case Constants.NETWORK_ERROR: {
                tagLine = "network_error";
                break;
            }

            case Constants.SERVER_ERROR: {
                tagLine = "server_error";
                break;
            }

            default: {
                tagLine = "";
            }
        }
        return tagLine;
    }

    public static MovieVideo handleMovieVideoState(Context context, int flag) {
        MovieVideo movieVideo = new MovieVideo();
        movieVideo.setKey("");
        switch (flag) {
            case Constants.NONE: {
                movieVideo.setName("no_video");
                break;
            }

            case Constants.NETWORK_ERROR: {
                movieVideo.setName("network_error_short");
                break;
            }

            case Constants.SERVER_ERROR: {
                movieVideo.setName("server_error_short");
                break;
            }
        }
        return movieVideo;
    }

    public static MovieReview handleMovieReviewState(Context context, int flag) {
        MovieReview review = new MovieReview();
        review.setAuthor(context.getString(R.string.no_review));
        switch (flag) {
            case Constants.NONE: {
                review.setContent(context.getString(R.string.no_reviews));
                break;
            }

            case Constants.NETWORK_ERROR: {
                review.setContent("network_error");
                break;
            }

            case Constants.SERVER_ERROR: {
                review.setContent("server_error");
                break;
            }
        }
        return review;
    }
}
