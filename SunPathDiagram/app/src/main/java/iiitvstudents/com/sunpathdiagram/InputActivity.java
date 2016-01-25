package iiitvstudents.com.sunpathdiagram;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.location.*;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity {
    public static final String LATITUDE = "iiitvstudents.com.sunpathdidagram.latitudemessage";
    public static final String DAYNUMBER = "iiitvstudents.com.sunpathdidagram.daynumber";
    static int num;
    static EditText dayNumberText;
    static EditText latitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        latitudeText = (EditText) findViewById(R.id.latitude);
        dayNumberText = (EditText) findViewById(R.id.daynumber);
        ImageView getlocation = (ImageView) findViewById(R.id.locationButton);
        getlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        Log.e("im","here");
                        // Called when a new location is found by the network location provider.
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        Location l = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        double latitude=l.getLatitude();
                        Log.e("loc",""+latitude);
                        latitudeText.setText(""+latitude);

                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }
                    Log.e("sfas","SAfas");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100000, 1000, locationListener);
                    Location l = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    double latitude=l.getLatitude();
                    Log.e("loc",""+latitude);
                    latitudeText.setText("" + latitude);

                }
                else {
                    IssueDialog issueDialog =new IssueDialog();
                    issueDialog.show(getFragmentManager(),"Location");
                }
            }
        });

// Register the listener with the Location Manager to receive location updates
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plotActivity = new Intent(getApplicationContext(), PlotActivity.class);
                double latitudeValue;
                try
                {latitudeValue = Double.parseDouble(latitudeText.getText().toString());

                if (latitudeValue<90&&latitudeValue>-90){
                    int dayNumberValue = Integer.parseInt(dayNumberText.getText().toString());
                    if(dayNumberValue<365&&dayNumberValue>0) {
                        plotActivity.putExtra(LATITUDE, latitudeValue);
                        plotActivity.putExtra(DAYNUMBER, dayNumberValue);
                        startActivity(plotActivity);
                    }
                    else {
                        Snackbar.make(v,"Number of days must be between 0 to 365",Snackbar.LENGTH_INDEFINITE).show();
                    }
                }else {
                    Snackbar.make(v,"Latitude must be between -90 to 90",Snackbar.LENGTH_INDEFINITE).show();
                }}
                catch (NumberFormatException e){Snackbar.make(v, "Invalid Input", Snackbar.LENGTH_INDEFINITE).show();}
            }
        });
    }
    public void datepick(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            DatePicker datePicker= view;
            Calendar cal=Calendar.getInstance();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            int month_=datePicker.getMonth();
            int date=datePicker.getDayOfMonth();
            int number;
            int mnth=1;
             num=0;
            while(mnth<month_) {
                if (mnth == 1 || mnth == 3 || mnth == 5 || mnth == 7 || mnth == 8 || mnth == 10 || mnth == 12) {
                    num += 31;
                } else if (mnth == 2) {
                    num += 28;
                } else {
                    num += 30;
                }
                mnth++;
            }
            num+=date;
            dayNumberText.setText(""+num);
        }

    }
    public static class  IssueDialog extends android.app.DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("GPS is not Active")
                    .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent locationSetting=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(locationSetting);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
