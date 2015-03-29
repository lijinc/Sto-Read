package com.lijin.kahani.sto_read;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * Created by LIJIN on 3/21/2015.
 */
@ParseClassName("Story")
public class Story extends ParseObject {

    public Story(){

    }

    public String getTitle(){
        return getString("TITLE");
    }

    public void setTitle(String title){
        put("TITLE", title);
    }

    public String getAuthor(){
        return getString("AUTHOR");
    }

    public void setAuthor(String author){
        put("AUTHOR", author);
    }

    public ParseFile getIconPhotoFile() {
        return getParseFile("ICON");
    }

    public void setIconPhotoFile(ParseFile file) {
        put("ICON", file);
    }

    public String getType(){
        return getString("TYPE");
    }

    public void setType(String title){
        put("TYPE", title);
    }


}
