package iiitvstudents.com.sunpathdiagram;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends AppCompatActivity {
    public static final String LATITUDE="iiitvstudents.com.sunpathdidagram.latitudemessage";
    public static final String DAYNUMBER="iiitvstudents.com.sunpathdidagram.daynumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        final EditText latitudeText=(EditText)findViewById(R.id.latitude);
        final EditText dayNumberText=(EditText)findViewById(R.id.daynumber);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent plotActivity = new Intent(getApplicationContext(), PlotActivity.class);
                double latitudeValue = Double.parseDouble(latitudeText.getText().toString());
                int dayNumberValue = Integer.parseInt(dayNumberText.getText().toString());
                plotActivity.putExtra(LATITUDE, latitudeValue);
                plotActivity.putExtra(DAYNUMBER, dayNumberValue);
                startActivity(plotActivity);
            }
        });

    }
}
