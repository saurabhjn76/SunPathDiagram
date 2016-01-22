package iiitvstudents.com.sunpathdiagram;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import java.util.Arrays;

public class PlotActivity extends AppCompatActivity {
XYPlot sunpathplot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        sunpathplot=(XYPlot)findViewById(R.id.plot);
        Intent from=getIntent();
        Double latitudeValue=from.getDoubleExtra(InputActivity.LATITUDE, 0);
        int dayNumberValue=from.getIntExtra(InputActivity.DAYNUMBER, 0);
        Double[][] results=calculate_result_arrays(latitudeValue,dayNumberValue);
        Double[] elevation=new Double[results.length];
        Double[] azimuth=new Double[results.length];
        //results[0] has elevation angle
        //results[1] has azimuth angle
        for (int i=0;i<results.length;i++){
            elevation[i]=results[i][0];
            azimuth[i]=results[i][1];
        }
       XYSeries series = new SimpleXYSeries(Arrays.asList(azimuth),Arrays.asList(elevation),"Sun Path Diagram");
           LineAndPointFormatter seriesFormat = new LineAndPointFormatter(Color.rgb(0, 0, 0),null,null,null);
        sunpathplot.addSeries(series, seriesFormat);
        sunpathplot.setUserDomainOrigin(0);
        sunpathplot.setUserRangeOrigin(0);
        sunpathplot.setTicksPerDomainLabel(3);
        sunpathplot.setDomainStep(XYStepMode.INCREMENT_BY_VAL,10);
        sunpathplot.setRangeStep(XYStepMode.INCREMENT_BY_VAL,5);
        sunpathplot.getGraphWidget().setPadding(50,0, 0, 50);
    }

    Double[][]  calculate_result_arrays(double latitude,int day_number){
        double timeofday=5;
        int i=0;
        Double[][] result=new Double[72][2];
        while(timeofday<17){
            result[i]=calculate_elevation_angle(latitude,day_number,timeofday);
            timeofday=timeofday+0.1667;  // incrementing by 1/6
            i++;
        }
        return result;
    }

    Double[] calculate_elevation_angle(double latitude,int day_number,double time_of_day)
    {
        double solar_declination=23.45*(Math.sin(2.0*Math.PI*(day_number-81)/365));
        time_of_day=-15.0*(time_of_day-12.0);
        double elevation_angle=Math.asin(Math.cos(2.0*Math.PI*latitude/360)*Math.cos(2.0*Math.PI*solar_declination/360)*Math.cos(2.0*Math.PI*time_of_day/360) + Math.sin(2.0*Math.PI*latitude/360)*Math.sin(2.0*Math.PI*solar_declination/360));
        double azimuth_angle=Math.asin(Math.cos(2.0*Math.PI*solar_declination/360)*Math.sin(2.0*Math.PI*time_of_day/360)/Math.cos(elevation_angle));

        if(Math.cos(2.0*Math.PI*time_of_day/360)>=Math.tan(2.0*Math.PI*solar_declination/360)/Math.tan(2.0*Math.PI*latitude/360))
        {
            azimuth_angle=azimuth_angle*360/(2.0*Math.PI);
        }
        else
            azimuth_angle=180-azimuth_angle*360/(2.0*Math.PI);
        elevation_angle=elevation_angle*360/(2.0*Math.PI);
        return new Double[]{elevation_angle,azimuth_angle};
    }
}
