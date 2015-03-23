package com.lijin.kahani.sto_read;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.melnykov.fab.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AbsListView.OnItemClickListener {
    ArrayList<ParseObject> arrayList;
    DataAdapter dataAdapter;
    StaggeredGridView mGridView;
    private ParseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseObject.registerSubclass(Story.class);
        ParseObject.registerSubclass(StoryContent.class);
/*
        ParseUser.enableAutomaticUser();
*/
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),AddStoryActivity.class);
                startActivity(intent);
            }
        });
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        arrayList=new ArrayList<>();
        dataAdapter=new DataAdapter(this,R.layout.grid_item,arrayList);
        mGridView.setAdapter(dataAdapter);
        mGridView.setOnItemClickListener(this);
        query();
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    MainActivity.this);
            startActivityForResult(loginBuilder.build(), 0);
        }
    }


    public void query(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Story");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> storyList, com.parse.ParseException e) {
                if (e == null) {
                    for(ParseObject parseObject:storyList){
                        arrayList.add(parseObject);
                    }
                    dataAdapter.notifyDataSetChanged();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,ShowStoryActivity.class);
        intent.putExtra("BOOKID",arrayList.get(position).getObjectId());
        intent.putExtra("TITLE",arrayList.get(position).getString("TITLE"));
        intent.putExtra("AUTHOR",arrayList.get(position).getString("AUTHOR"));
        startActivity(intent);
    }
}
