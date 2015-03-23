package com.lijin.kahani.sto_read;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class PicUploadActivity extends ActionBarActivity implements View.OnClickListener {
    Intent intent;
    Story story;
    StoryContent storyContent;
    ImageButton iconImageButton;
    ImageButton posterImageButton;
    ImageButton backgroundImageButton;
    PicUploadActivity picUploadActivity;
    EditText discriptionEditText;
    String storyText;
    int clicked;
    ParseUser currentUser;
    ParseFile photoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_upload);
        ParseObject.registerSubclass(Story.class);
        ParseObject.registerSubclass(StoryContent.class);
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        intent=getIntent();
        currentUser=ParseUser.getCurrentUser();
        storyText=intent.getStringExtra("STORY");
        discriptionEditText=(EditText)findViewById(R.id.description_editText);
        story=new Story();
        story.setTitle(intent.getStringExtra("TITLE"));
        if(currentUser!=null){
            story.setAuthor(currentUser.getString("name"));
        }


        storyContent=new StoryContent();
        storyContent.setStory(intent.getStringExtra("STORY"));
        picUploadActivity=this;
        iconImageButton=(ImageButton)findViewById(R.id.icon_imgButton);
        iconImageButton.setOnClickListener(this);
        posterImageButton=(ImageButton)findViewById(R.id.poster_imageButton);
        posterImageButton.setOnClickListener(this);
        backgroundImageButton=(ImageButton)findViewById(R.id.background_imgButton);
        backgroundImageButton.setOnClickListener(this);
        photoFile= null;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.show();
            story.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    storyContent.setBookID(story.getObjectId());
                    storyContent.setDescription(discriptionEditText.getText().toString());
                    storyContent.setStory(storyText);
                    storyContent.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressDialog.dismiss();
                        }
                    });
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            switch (clicked){
                case 0:
                    iconImageButton.setImageURI(Crop.getOutput(result));
                    photoFile=new ParseFile("thumbnail_photo.jpg",uriToBytes(Crop.getOutput(result)));
                    final ProgressDialog progressDialog=new ProgressDialog(this);
                    progressDialog.show();
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            progressDialog.dismiss();
                            if (e != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                story.setIconPhotoFile(photoFile);
                            }
                        }
                    });
                    break;
                case 1:
                    posterImageButton.setImageURI(Crop.getOutput(result));
                    photoFile=new ParseFile("thumbnail_photo.jpg",uriToBytes(Crop.getOutput(result)));
                    final ProgressDialog progressDialog1=new ProgressDialog(this);
                    progressDialog1.show();
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            progressDialog1.dismiss();
                            if (e != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                storyContent.setPosterPhotoFile(photoFile);
                            }
                        }
                    });
                    break;
                case 2:
                    backgroundImageButton.setImageURI(Crop.getOutput(result));
                    photoFile=new ParseFile("thumbnail_photo.jpg",uriToBytes(Crop.getOutput(result)));
                    final ProgressDialog progressDialog2=new ProgressDialog(this);
                    progressDialog2.show();
                    photoFile.saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            progressDialog2.dismiss();
                            if (e != null) {
                                Toast.makeText(getApplicationContext(),
                                        "Error saving: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } else {
                                storyContent.setBackgroundPhotoFile(photoFile);
                            }
                        }
                    });
                    break;
                default:
                    break;
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_imgButton:
                clicked=0;
                iconImageButton.setImageURI(null);
                Crop.pickImage(picUploadActivity);
                break;
            case R.id.poster_imageButton:
                clicked=1;
                posterImageButton.setImageURI(null);
                Crop.pickImage(picUploadActivity);
                break;
            case R.id.background_imgButton:
                clicked=2;
                backgroundImageButton.setImageURI(null);
                Crop.pickImage(picUploadActivity);
                break;
            default:
                clicked=-1;
        }
    }
    public byte[] uriToBytes(Uri uri){
        InputStream iStream = null;
        try {
            iStream=getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] inputData=null;
        try {
            inputData = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
