package com.lijin.kahani.sto_read;

import com.parse.Parse;
import com.parse.ParseTwitterUtils;

/**
 * Created by LIJIN on 3/22/2015.
 */
public class Application extends android.app.Application {

    public void onCreate() {
        Parse.initialize(this, "xhCRajILo3dct0LgxWWhtP30Men8YqFLoR05Xv8g", "sxXqAaeytHZBHHB6CE7bQfmSvTtTgJrJPrGFWqYP");
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));
    }

}
