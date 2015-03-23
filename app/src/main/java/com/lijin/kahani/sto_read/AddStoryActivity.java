package com.lijin.kahani.sto_read;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class AddStoryActivity extends ActionBarActivity {
    EditText titleEditText;
    EditText storyEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        titleEditText=(EditText)findViewById(R.id.titleText);
        storyEditText=(EditText)findViewById(R.id.storyText);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            Intent intent =new Intent(this,PicUploadActivity.class);
            intent.putExtra("TITLE",titleEditText.getText().toString());
            intent.putExtra("STORY",storyEditText.getText().toString());
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
