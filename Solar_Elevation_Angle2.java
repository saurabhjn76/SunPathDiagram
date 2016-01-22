import java.util.*;

public class Solar_Elevation_Angle2{
	public static void main(String[] args){
		Scanner input =new Scanner(System.in);


		System.out.println("Enter the Latitude Value");
		double latitude =input.nextDouble();

		System.out.println("Enter the Day number");
		int day_number =input.nextInt();

		System.out.println("Enter the time of the day(0-24)");
		double time_of_day =input.nextDouble();
		double elevation_angle=calculate_elevation_angle(latitude,day_number,time_of_day);
	}
	public static double calculate_elevation_angle(double latitude,int day_number,double time_of_day)
	{
		double solar_declination=23.45*(Math.sin(2.0*Math.PI*(day_number-81)/365));
		System.out.println("The solar_declination is:" + solar_declination);
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

		System.out.println("azimuth_angle,elevation_angle:"+azimuth_angle+","+elevation_angle);
		return elevation_angle;
	}

}
