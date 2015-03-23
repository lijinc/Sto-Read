package com.lijin.kahani.sto_read;

import android.content.Intent;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by LIJIN on 3/22/2015.
 */
@ParseClassName("Rating")
public class Rating extends ParseObject {

    public Rating() {

    }
    public String getBookID(){
        return getString("BOOKID");
    }

    public void setBookID(String bookID){
        put("BOOKID", bookID);
    }

    public Double getRating(){
        return getDouble("RATING");
    }

    public void setRating(Double rating){
        put("RATING", rating);
    }
    public String getUserID(){
        return getString("USERID");
    }

    public void setUserID(String userID){
        put("USERID", userID);
    }
}
