package luminosit.sunmera.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.Calendar;

import luminosit.sunmera.R;
import luminosit.sunmera.activity.MainActivity;
import luminosit.sunmera.activity.SpecificViewActivity;
import luminosit.sunmera.ui.IconTitleAdapter;
import luminosit.sunmera.util.CalendarManager;
import luminosit.sunmera.util.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    View fragmentView;
    CalendarView calendarView;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment, and get the returned view
        fragmentView = inflater.inflate(R.layout.fragment_calendar, container, false);

        //set up the listener for the calendar
        calendarView = (CalendarView)fragmentView.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                //+1 because Jan = 0
                updateList(String.valueOf(year) + formatDate(month + 1) + formatDate(dayOfMonth));
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //update the list once in order to display it properly
        CalendarManager manager = new CalendarManager();
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());
        updateList(manager.getToday());
    }

    // the list is updated by the uid to show photos on a specific day
    public void updateList(String date) {
        final DatabaseHelper helper = new DatabaseHelper(getActivity());
        CalendarManager manager = new CalendarManager();
        ListView dailyList = ((ListView)fragmentView.findViewById(R.id.list_daily));

        //get user's list of photos taken today
        final String[] todayUIDList = helper.getUIDListByUser(MainActivity.username
                + DatabaseHelper.UID_NAME_SEPARATOR + date);

        //convert uid into readable time
        String[] readableTimeList = new String[todayUIDList.length];
        for (int i = 0; i < readableTimeList.length; i++){
            readableTimeList[i] = manager.convertTimeCodeToNormalFormat(todayUIDList[i]);
        }

        //apply the adapter to display timeline
        dailyList.setAdapter(new IconTitleAdapter(getActivity(), todayUIDList, readableTimeList));

        //listener
        dailyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent specificViewIntent = new Intent(getActivity(), SpecificViewActivity.class);
                specificViewIntent.putExtra(SpecificViewActivity.EXTRA_UID, todayUIDList[position]);
                startActivity(specificViewIntent);
            }
        });
    }

    //add zero in front of any number less than 10
    public String formatDate(int date) {
        String stringDate;
        if (date < 10) {
            stringDate = "0" + String.valueOf(date);
        } else {
            stringDate = String.valueOf(date);
        }
        return stringDate;
    }
}
