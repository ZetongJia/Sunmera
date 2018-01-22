package luminosit.sunmera.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import luminosit.sunmera.R;
import luminosit.sunmera.activity.GalleryActivity;
import luminosit.sunmera.activity.MainActivity;
import luminosit.sunmera.activity.SpecificViewActivity;
import luminosit.sunmera.util.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    //this fragment's view
    View fragmentView;

    //navigation to categories
    public int navigation;
    public static final String EXTRA_NAVIGATION = "navi";

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final DatabaseHelper helper = new DatabaseHelper(getActivity());
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_category, container, false);

        //build the intent
        final Intent openGalleryIntent = new Intent(getActivity(), GalleryActivity.class);

        //listeners...
        (fragmentView.findViewById(R.id.category_button_grateful))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 5));
                        startActivity(openGalleryIntent);
                    }
                });
        (fragmentView.findViewById(R.id.category_button_excellent))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 4));
                        startActivity(openGalleryIntent);
                    }
                });

        (fragmentView.findViewById(R.id.category_button_excited))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 3));
                        startActivity(openGalleryIntent);
                    }
                });

        (fragmentView.findViewById(R.id.category_button_happy))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 2));
                        startActivity(openGalleryIntent);
                    }
                });

        (fragmentView.findViewById(R.id.category_button_bored))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 1));
                        startActivity(openGalleryIntent);
                    }
                });

        (fragmentView.findViewById(R.id.category_button_sad))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalleryIntent.putExtra(GalleryActivity.EXTRA_PHOTO_LIST,
                                helper.getUIDListByEmotion(MainActivity.username, 0));
                        startActivity(openGalleryIntent);
                    }
                });
        return fragmentView;
    }
}
