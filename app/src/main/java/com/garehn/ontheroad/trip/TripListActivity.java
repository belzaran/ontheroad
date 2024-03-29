package com.garehn.ontheroad.trip;
import static android.os.Build.VERSION_CODES.O;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.garehn.ontheroad.MainActivity;
import com.garehn.ontheroad.R;
import com.garehn.ontheroad.graphics.CostCellAdapter;
import com.garehn.ontheroad.graphics.TripCellAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TripListActivity extends CostBaseActivity implements View.OnClickListener {

    private static final int GAME_ACTIVITY_REQUEST_CODE = 23;

    //GRAPHICS
    private ListView listView;
    private TripCellAdapter adapter;
    private ImageView[] button = new ImageView[2];

    //STRINGS
    private static String TXT_DIALOG = "What do you want to do with this trip ?";
    private static String TXT_DIALOG_SELECT = "select";
    private static String TXT_DIALOG_DELETE = "delete";
    private static String TXT_DIALOG_MODIFY = "modify";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        createCategories();
        createDatabase();
        getTripId();
        adapter = new TripCellAdapter(TripListActivity.this, getTrips());
        createGraphics();
    }

    public void createGraphics() {

        //List of categories
        listView = findViewById(R.id.trip_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                sendDeleteMessage(getTrips().get(position).getId(), position);
            }
        });

        button[0] = findViewById(R.id.trip_list_button_return);
        button[1] = findViewById(R.id.trip_list_button_add);
        for(int i = 0; i < button.length; i++) {
            button[i].setOnClickListener(this);
        }


    }

    /*public List<String> getTrips(){
        int n = tripDao.getCount();
        Log.i("ONTHEROAD_LISTTRIP", String.format("Number of trips : " + n));
        for (int i = 0 ; i < n ; i++){
            trips.add(tripDao.getTrip(i).getName());
            Log.i("ONTHEROAD_LISTTRIP", String.format(trips.get(i)));
        }
        return trips;
    }*/

    public List<Trip> getTrips(){
        return tripDao.getTrips();
    }

    // OPEN A MESSAGE BOX
    public void sendDeleteMessage(long l,int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getTrips().get(position).getName())
                .setMessage(TXT_DIALOG)
                .setPositiveButton(TXT_DIALOG_SELECT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int itemId = (int) adapter.getTrips().get(position).getId();

                        Intent activity = new Intent(TripListActivity.this, MainActivity.class);
                        activity.putExtra("Categories", categories);
                        //activity.putExtra("id", position);
                        activity.putExtra("TripId", itemId);
                        setResult(RESULT_OK, activity);
                        startActivityForResult(activity, GAME_ACTIVITY_REQUEST_CODE);
                        finish();
                    }
                })
                .setNegativeButton(TXT_DIALOG_DELETE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int itemId = (int) adapter.getTrips().get(position).getId();
                        tripDao.deleteTrip(itemId);
                        adapter.setTrips(getTrips());
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNeutralButton(TXT_DIALOG_MODIFY, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int itemId = (int) adapter.getTrips().get(position).getId();

                        Intent activity = new Intent(TripListActivity.this, TripModifyActivity.class);
                        activity.putExtra("Categories", categories);;
                        activity.putExtra("TripId", itemId);
                        setResult(RESULT_OK, activity);
                        startActivityForResult(activity, GAME_ACTIVITY_REQUEST_CODE);
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onClick(View v) {
        if(v == button[0]){
            Intent activity = new Intent(TripListActivity.this, MainActivity.class);
            activity.putExtra("Categories", categories);
            activity.putExtra("TripId", tripId);
            setResult(RESULT_OK, activity);
            startActivityForResult(activity, GAME_ACTIVITY_REQUEST_CODE);
            finish();
        } else if (v == button[1]){
            Intent activity = new Intent(TripListActivity.this, TripAddActivity.class);
            activity.putExtra("Categories", categories);
            activity.putExtra("TripId", tripId);
            setResult(RESULT_OK, activity);
            startActivityForResult(activity, GAME_ACTIVITY_REQUEST_CODE);
            finish();
        }

    }
}
