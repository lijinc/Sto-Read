package com.lijin.kahani.sto_read;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by LIJIN on 3/21/2015.
 */
@ParseClassName("StoryContent")
public class StoryContent extends ParseObject {

    public StoryContent(){

    }

    public String getBookID(){
        return getString("BOOKID");
    }

    public void setBookID(String bookID){
        put("BOOKID", bookID);
    }

    public String getStory(){
        return getString("STORY");
    }

    public void setStory(String story){
        put("STORY", story);
    }

    public String getDescription(){
        return getString("DESCRIPTION");
    }

    public void setDescription(String description){
        put("DESCRIPTION", description);
    }

    public ParseFile getPosterPhotoFile() {
        return getParseFile("POSTER");
    }

    public void setPosterPhotoFile(ParseFile file) {
        put("POSTER", file);
    }

    public ParseFile getBackgroundPhotoFile() {
        return getParseFile("BACKGROUND");
    }

    public void setBackgroundPhotoFile(ParseFile file) {
        put("BACKGROUND", file);
    }

}
