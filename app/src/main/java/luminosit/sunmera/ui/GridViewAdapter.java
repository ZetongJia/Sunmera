package luminosit.sunmera.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import luminosit.sunmera.R;
import luminosit.sunmera.util.PhotoHandler;

/**
 * Created by GaryTang on 5/9/15.
 */
public class GridViewAdapter extends BaseAdapter {

    private Context activityContext;
    private String[] uidList;

    public GridViewAdapter(Context context, String[] uidList){
        activityContext = context;
        this.uidList = uidList;
    }

    @Override
    public int getCount() {
        return uidList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoHandler handler = new PhotoHandler(activityContext);

        //get the inflater
        LayoutInflater inflater = (LayoutInflater)activityContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate the view
        View elementView = inflater.inflate(R.layout.element_thumbnail, parent, false);

        //set the image to each element
        ((ImageView)elementView.findViewById(R.id.element_thumbnail_imageView))
                .setImageBitmap(handler.readImageThumbnail(uidList[position]));
        return elementView;
    }
}
