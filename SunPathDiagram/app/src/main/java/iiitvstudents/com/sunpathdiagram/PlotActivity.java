package iiitvstudents.com.sunpathdiagram;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Size;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.XLayoutStyle;
import com.androidplot.ui.YLayoutStyle;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;

import java.text.DecimalFormat;
import java.util.Arrays;

public class PlotActivity extends AppCompatActivity {
    XYPlot sunpathplot;
    PointF maxXY;
    PointF minXY;
    float zoomRatio = 2, leftBoundary, rightBoundary;
    SimpleXYSeries series;
    SimpleXYSeries series_summersolstice;
    SimpleXYSeries series_eqinox;
    SimpleXYSeries series_wintersolstice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        sunpathplot=(XYPlot)findViewById(R.id.plot);
//        sunpathplot.setOnTouchListener(this);
        Intent from=getIntent();
        Double latitudeValue=from.getDoubleExtra(InputActivity.LATITUDE, 0);
        int dayNumberValue=from.getIntExtra(InputActivity.DAYNUMBER, 0);
        Double[][] summersolstice_results=calculate_result_arrays(latitudeValue,172);
        Double[][] equinox_results=calculate_result_arrays(latitudeValue,81);
        Double[][] wintersolistice_results=calculate_result_arrays(latitudeValue,355);
        Double[] elevation_summersolstice=new Double[summersolstice_results.length];
        Double[] azimuth_summersolstice=new Double[summersolstice_results.length];
        Double[] elevation_equinox=new Double[equinox_results.length];
        Double[] azimuth_equinox=new Double[equinox_results.length];
        Double[] elevation_wintersolstice=new Double[wintersolistice_results.length];
        Double[] azimuth_wintersolstice=new Double[wintersolistice_results.length];
        Double[][] results=calculate_result_arrays(latitudeValue, dayNumberValue);
        Double[] elevation=new Double[results.length];
        Double[] azimuth=new Double[results.length];

        for (int i=0;i<results.length;i++){
            elevation[i]=results[i][0];
            azimuth[i]=results[i][1];
        }
        for (int i = 0; i <summersolstice_results.length ; i++) {
            elevation_summersolstice[i]=summersolstice_results[i][0];
            azimuth_summersolstice[i]=summersolstice_results[i][1];
        }
        for (int i = 0; i < wintersolistice_results.length; i++) {
            elevation_wintersolstice[i]=wintersolistice_results[i][0];
            azimuth_wintersolstice[i]=wintersolistice_results[i][1];
        }
        for (int i = 0; i < equinox_results.length; i++) {
            elevation_equinox[i]=equinox_results[i][0];
            azimuth_equinox[i]=equinox_results[i][1];
        }
        double minimumxy,sunrise,sunset;
        int temp_day;
        if(latitudeValue<0)
        {
            temp_day=355;

        }
        else
        {
            temp_day=172;
        }
        minimumxy=calculate_time_of_the_day(latitudeValue,temp_day);
        sunrise=12.0-minimumxy;
        sunset=12.0+minimumxy;
        int max_bound=(int)(calculate_elevation_angle(latitudeValue,temp_day,sunrise)[1]+5);
        int min_bound=(int)(calculate_elevation_angle(latitudeValue,temp_day,sunset)[1]-5);
        Log.e("min_bound",min_bound +"");
        Log.e("max_bound",max_bound +"");
       series = new SimpleXYSeries((Arrays.asList(azimuth)),Arrays.asList(elevation),"User");
        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(Color.rgb(0, 0, 0),null,null,null);
        seriesFormat.configure(this,R.xml.line_point_formatter_with_labels);
        series_summersolstice = new SimpleXYSeries((Arrays.asList(azimuth_summersolstice)),Arrays.asList(elevation_summersolstice),"Summer Solstice");
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.rgb(0, 0, 255),null,null,null);
        series_wintersolstice = new SimpleXYSeries((Arrays.asList(azimuth_wintersolstice)),Arrays.asList(elevation_wintersolstice),"Winter Solstice");
        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.rgb(0, 255, 0),null,null,null);
        series_eqinox = new SimpleXYSeries((Arrays.asList(azimuth_equinox)),Arrays.asList(elevation_equinox),"Equinox");
        LineAndPointFormatter series3Format = new LineAndPointFormatter(Color.rgb(255, 0, 0),null,null,null);
        XYGraphWidget graphWidget=sunpathplot.getGraphWidget();
        sunpathplot.addSeries(series, seriesFormat);
        sunpathplot.addSeries(series_eqinox, series3Format);
        sunpathplot.addSeries(series_summersolstice, series1Format);
        sunpathplot.addSeries(series_wintersolstice, series2Format);
        sunpathplot.setDomainBoundaries(-150, 150, BoundaryMode.FIXED);
        sunpathplot.setRangeBoundaries(0, 90, BoundaryMode.FIXED);
        sunpathplot.setTicksPerDomainLabel(3);
        sunpathplot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 10);
        sunpathplot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 5);
        
        graphWidget.getGridBackgroundPaint().setARGB(255, 255, 255, 255);
        graphWidget.getBackgroundPaint().setARGB(255, 255, 255, 255);
        graphWidget.getDomainTickLabelPaint().setARGB(255,0,0,0);
        graphWidget.getRangeTickLabelPaint().setARGB(255,0,0,0);
        graphWidget.getDomainOriginLinePaint().setARGB(255,0,0,0);
        graphWidget.getRangeOriginLinePaint().setARGB(255,0,0,0);
        sunpathplot.getBackgroundPaint().setARGB(255, 255, 255, 255);
        graphWidget.setPadding(50, 25, 30, 50);
        graphWidget.setSize(new com.androidplot.ui.Size(0.90f, SizeLayoutType.RELATIVE, 1.0f, SizeLayoutType.RELATIVE));
        graphWidget.position(0.01f, XLayoutStyle.RELATIVE_TO_LEFT, -0.09f, YLayoutStyle.RELATIVE_TO_BOTTOM, AnchorPosition.LEFT_BOTTOM);
        Paint paint=new Paint();
        paint.setARGB(255,0,0,0);
        paint.setTextSize(25);
        sunpathplot.getLegendWidget().setTextPaint(paint);
        graphWidget.setRangeValueFormat(new DecimalFormat("#####.##"));
        graphWidget.setDomainValueFormat(new DecimalFormat("#####.##"));
        sunpathplot.setRangeLabel("Elevation Angle(Degrees)");
        sunpathplot.setDomainLabel("Azimuth Angle(Degrees)");
        sunpathplot.getDomainLabelWidget().setLabelPaint(paint);
        sunpathplot.getDomainLabelWidget().position(0.0f, XLayoutStyle.RELATIVE_TO_LEFT, -0.075f, YLayoutStyle.RELATIVE_TO_BOTTOM, AnchorPosition.LEFT_BOTTOM);
        sunpathplot.getDomainLabelWidget().setSize(new com.androidplot.ui.Size(0.05f, SizeLayoutType.RELATIVE, 1.0f, SizeLayoutType.RELATIVE));
        sunpathplot.getRangeLabelWidget().setLabelPaint(paint);
        sunpathplot.getRangeLabelWidget().position(0.02f, XLayoutStyle.RELATIVE_TO_LEFT, 0.0f, YLayoutStyle.RELATIVE_TO_BOTTOM, AnchorPosition.LEFT_BOTTOM);
        sunpathplot.getRangeLabelWidget().setSize(new com.androidplot.ui.Size(1.0f, SizeLayoutType.RELATIVE, 0.05f, SizeLayoutType.RELATIVE));
        sunpathplot.getLegendWidget().setTableModel(new DynamicTableModel(2, 2));
        sunpathplot.getLegendWidget().setSize(new com.androidplot.ui.Size(0.075f, SizeLayoutType.RELATIVE, 1.0f, SizeLayoutType.RELATIVE));
        sunpathplot.getLegendWidget().position(0.1f, XLayoutStyle.RELATIVE_TO_LEFT, -0.008f, YLayoutStyle.RELATIVE_TO_BOTTOM, AnchorPosition.LEFT_BOTTOM);
//        sunpathplot.setUserDomainOrigin(0);
//        sunpathplot.setUserRangeOrigin(0);
//        sunpathplot.setTicksPerDomainLabel(3);
        sunpathplot.canScrollHorizontally(1);
        sunpathplot.canScrollVertically(1);
        sunpathplot.isHorizontalScrollBarEnabled();

//        sunpathplot.redraw();
        //Set of internal variables for keeping track of the boundaries
//        sunpathplot.calculateMinMaxVals();
//        minXY=new PointF(sunpathplot.getCalculatedMinX().floatValue(),sunpathplot.getCalculatedMinY().floatValue());
//        maxXY=new PointF(sunpathplot.getCalculatedMaxX().floatValue(),sunpathplot.getCalculatedMaxY().floatValue());
    }


    Double[][]  calculate_result_arrays(double latitude,int day_number){
        double timeofday=calculate_time_of_the_day(latitude,day_number);
        double sunrise=12.0-timeofday;
        double sunset=12.0+timeofday;
        double currentTime=sunrise;
        Log.e("timeofday",""+timeofday);
        int a=(int)(timeofday*24);
        int i=0;
        Double[][] result=new Double[a+2][2];
        while(currentTime<sunset+1.0/12.0){
            result[i]=calculate_elevation_angle(latitude,day_number,currentTime);
            currentTime=currentTime+1.0/12.0;  // incrementing by 1/6
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
        if(azimuth_angle>180)
            azimuth_angle-=360;
        else if(azimuth_angle<-180)
            azimuth_angle+=360;
        elevation_angle=elevation_angle*360/(2.0*Math.PI);
        return new Double[]{elevation_angle,azimuth_angle};
    }
    public double calculate_time_of_the_day(double latitude,int day_number)
    {
        double solar_declination=23.45*(Math.sin(2.0*Math.PI*(day_number-81)/365));
        double time_of_day=Math.acos(-1.0 * Math.tan(2.0 * Math.PI * latitude / 360) * Math.tan(2.0 * Math.PI * solar_declination / 360));
        time_of_day=time_of_day*180.0/Math.PI;
        time_of_day/=15;
        return  time_of_day;
    }


    //*****************
    // Definition of the touch states
    /*static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float distBetweenFingers;
    boolean stopThread = false;

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // Start gesture
                Log.e("mode","ACTION_DOWN");
                firstFinger = new PointF(event.getX(), event.getY());
                mode = ONE_FINGER_DRAG;
                stopThread = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                Log.e("mode","Action_pointer_up&&action_up&NONE)");
                mode = NONE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // second finger
                Log.e("mode","ACTION_POINTER_DOWN");
                distBetweenFingers = spacing(event);
                // the distance check is done to avoid false alarms
                if (distBetweenFingers > 5f) {
                    mode = TWO_FINGERS_DRAG;
                    Log.e("mode","TWO_FINGERS_DRAG");
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == ONE_FINGER_DRAG) {
                    Log.e("mode","Action_move & mode=ONE_FINGER_DRAG");
                    PointF oldFirstFinger = firstFinger;
                    firstFinger = new PointF(event.getX(), event.getY());
                    Float dist=oldFirstFinger.x - firstFinger.x;
                    Log.e("dist",""+dist);
                    if(dist>35)
                    {
                        scroll(dist);
                        sunpathplot.setDomainBoundaries(minXY.x, maxXY.x,
                                BoundaryMode.FIXED);
                        sunpathplot.redraw();
                    }
                } else if (mode == TWO_FINGERS_DRAG) {
                    Log.e("mode","Action_move & mode=TWO_fingers_DRAG");
                    float oldDist = distBetweenFingers;
                    distBetweenFingers = spacing(event);
                    zoom(oldDist / distBetweenFingers);
                    sunpathplot.setDomainBoundaries(minXY.x, maxXY.x,
                            BoundaryMode.FIXED);
                    sunpathplot.redraw();
                }
                break;
        }
        return true;
    }

    private void zoom(float scale) {
        float domainSpan = maxXY.x - minXY.x;
        float oldMax = maxXY.x;
        float oldMin = minXY.x;
        float domainMidPoint = maxXY.x - (domainSpan / 2.0f);
        float offset = domainSpan * scale / 2.0f;
        minXY.x = domainMidPoint - offset;
        maxXY.x = domainMidPoint + offset;
        float newSpan = maxXY.x - minXY.x;
        if (newSpan < (float)5) {
            minXY.x = oldMin;
            maxXY.x = oldMax;
        }

        if (minXY.x < leftBoundary) {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan * zoomRatio;

           try {
               if (maxXY.x > series.getX(series.size() - 1).floatValue())
                   maxXY.x = rightBoundary;
           }catch (NullPointerException e){
               Log.e("error",e.toString());
               e.printStackTrace();
           }
        }
        Log.e("size",series.size()+"");
        if (maxXY.x > series.getX(series.size() - 1).floatValue()) {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan * zoomRatio;
            if (minXY.x < leftBoundary) minXY.x = leftBoundary;
        }
    }

    private void scroll(float pan) {
        float domainSpan = maxXY.x - minXY.x;
        float step = domainSpan / sunpathplot.getWidth();
        float offset = pan * step;
        minXY.x = minXY.x + offset;
        maxXY.x = maxXY.x + offset;

        if (minXY.x < leftBoundary) {
            minXY.x = leftBoundary;
            maxXY.x = leftBoundary + domainSpan;
        }
        Log.e("size",series.size()+"");
        Log.e("a","afsa");
        if (maxXY.x > series.getX(series.size() - 1).floatValue()) {
            maxXY.x = rightBoundary;
            minXY.x = rightBoundary - domainSpan;
        }
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
*/

}
