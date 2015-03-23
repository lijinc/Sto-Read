package com.lijin.kahani.sto_read;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class ShowStoryActivity extends ActionBarActivity {
    TextView titleText;
    TextView authorText;
    TextView descriptionText;
    TextView myRatingText;
    TextView ratingText;
    TextView voteTextView;
    ImageView posterImageView;
    ImageView backgroundImageView;
    ImageButton readImageButton;
    String story;
    String BookID;
    LinearLayout ratingButton;
    ParseObject rating;
    ParseUser currentUser;
    String ratingID=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story);
        ParseObject.registerSubclass(StoryContent.class);
        ParseObject.registerSubclass(Rating.class);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        currentUser=ParseUser.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        myRatingText=(TextView)findViewById(R.id.yourRatingTextView);
        titleText=(TextView)findViewById(R.id.title_textView);
        authorText=(TextView)findViewById(R.id.author_textView);
        descriptionText=(TextView)findViewById(R.id.description_textView);
        ratingText=(TextView)findViewById(R.id.ratingTextView);
        voteTextView=(TextView)findViewById(R.id.votesTextView);
        posterImageView=(ImageView)findViewById(R.id.poster_imageView);
        backgroundImageView=(ImageView)findViewById(R.id.backgroud_imageView);
        ratingButton=(LinearLayout)findViewById(R.id.ratingButton);
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        readImageButton=(ImageButton)findViewById(R.id.readImageButton);
        readImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ReaderActivity.class);
                intent.putExtra("STORY",story);
                startActivity(intent);
            }
        });
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=getIntent();
        BookID=intent.getStringExtra("BOOKID");
        titleText.setText(intent.getStringExtra("TITLE"));
        authorText.setText(intent.getStringExtra("AUTHOR"));
        query(BookID);
        getRating();
    }

    public void getRating(){
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Rating");
        query1.whereEqualTo("BOOKID",BookID);
        query1.whereEqualTo("USERID",currentUser.getObjectId());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null){
                    if(parseObjects.size()!=0){
                        rating=parseObjects.get(0);
                        ratingID=rating.getObjectId();
                        myRatingText.setText("Your Rating "+rating.getDouble("RATING")+"/10");
                    }

                }
            }
        });
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("BOOKID", BookID);
        ParseCloud.callFunctionInBackground("averageRating", params, new FunctionCallback<Object>() {
            public void done(Object ratings, ParseException e) {
                if (e == null) {
                    String rating=ratings.toString();
                    if(rating.compareTo("{}")==0){
                        rating="-";
                    }
                    else{
                        float rate=Float.valueOf(rating);
                        rating=String.format("%.1f", rate);
                    }
                    ratingText.setText(rating+"/10");
                }
            }
        });
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Rating");
        query2.whereEqualTo("BOOKID",BookID);
        query2.countInBackground(new CountCallback() {
            @Override
            public void done(int i, ParseException e) {
                voteTextView.setText(i+" votes");
            }
        });


    }

    public void query(String BookID){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("StoryContent");
        query.whereEqualTo("BOOKID",BookID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> storyContent, ParseException e) {
                if (e == null) {
                    if(storyContent.size()>0){
                        story=storyContent.get(0).getString("STORY");
                        descriptionText.setText(storyContent.get(0).getString("DESCRIPTION"));
                        displayImage(storyContent.get(0).getParseFile("POSTER"), posterImageView);
                        displayImage(storyContent.get(0).getParseFile("BACKGROUND"), backgroundImageView);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_story, menu);
        return true;
    }

    public void showDialog()
    {

        final Dialog d = new Dialog(this);
        d.setTitle("Rate Story");
        d.setContentView(R.layout.rating_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
/*
        np.setOnValueChangedListener(this);
*/
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(ratingID==null){
                    rating=new Rating();
                    rating.put("RATING",Double.valueOf(np.getValue()));
                    rating.put("BOOKID",BookID);
                    if(currentUser!=null){
                        rating.put("USERID",currentUser.getObjectId());
                    }
                    rating.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            getRating();
                        }
                    });
                }
                else{
                    rating.put("RATING",Double.valueOf(np.getValue()));
                    rating.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            myRatingText.setText("Your Rating "+String.valueOf(np.getValue())+"/10");
                        }
                    });
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void displayImage(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, com.parse.ParseException e) {

                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
                                data.length);

                        if (bmp != null) {

                            // img.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                            // (display.getWidth() / 5),
                            // (display.getWidth() /50), false));
                            img.setImageBitmap(bmp);
                            // img.setPadding(10, 10, 0, 0);



                        }
                    } else {
                        Log.e("paser after downloade", " null");
                    }

                }
            });
        } else {

            Log.e("parse file is", " null");

            // img.setImageResource(R.drawable.ic_launcher);
        }

    }
}
