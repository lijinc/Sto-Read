package com.lijin.kahani.sto_read;

/**
 * Created by LIJIN on 3/20/2015.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by LIJIN on 3/20/2015.
 */
public class DataAdapter extends ArrayAdapter<ParseObject> {

    Activity activity;
    int resource;
    List<ParseObject> datas;

    public DataAdapter(Activity activity, int resource, List<ParseObject> objects) {
        super(activity, resource, objects);

        this.activity = activity;
        this.resource = resource;
        this.datas = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DealHolder();
            holder.image = (DynamicHeightImageView)row.findViewById(R.id.image);
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.description = (TextView)row.findViewById(R.id.author);

            row.setTag(holder);
        }
        else {
            holder = (DealHolder) row.getTag();
        }

        final ParseObject data = datas.get(position);
        ParseFile image = data.getParseFile("ICON");
        holder.image.setImageResource(R.drawable.ic_launcher);
        holder.image.setHeightRatio(1.0);
        holder.title.setText(data.getString("TITLE"));
        holder.description.setText(data.getString("AUTHOR"));
        displayImage(image,holder.image);
        return row;
    }

    static class DealHolder {
        DynamicHeightImageView image;
        TextView title;
        TextView description;
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
