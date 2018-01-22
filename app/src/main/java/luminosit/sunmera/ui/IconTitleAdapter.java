package luminosit.sunmera.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import luminosit.sunmera.R;
import luminosit.sunmera.util.PhotoHandler;

/**
 * Created by GaryTang on 5/9/15.
 */
public class IconTitleAdapter extends BaseAdapter{

    private Context activityContext;
    private String[] titles;
    private String[] icons;

    public IconTitleAdapter(Context context, String[] icons, String[] titles){
        activityContext = context;
        this.titles = titles;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return titles.length;
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
        LayoutInflater inflater = (LayoutInflater) activityContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate the view
        View elementView = inflater.inflate(R.layout.element_icon_title, parent, false);

        //display image and photo
        ((TextView)elementView.findViewById(R.id.element_icon_title_textView))
                .setText(titles[position]);
        ((ImageView)elementView.findViewById(R.id.element_icon_title_imageView))
                .setImageBitmap(handler.readImageThumbnail(icons[position]));

        return elementView;
    }
}
