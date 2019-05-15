package roadreader.roadreader_android;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trip implements Cloneable{

    List<Square> squareList;

    public Trip() {
        squareList = new ArrayList<>();
    }

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     * Creates new GPS instance and adds it to the trip. Adds the GPS location to
     * its corresponding square in trip's squareList. If square is not in suareList,
     * creates a new square and adds it to the list.
     * @param sensor_data Data from accelerometer and gyroscope since last recorded GPS location
     * @param latitude
     * @param longitude
     */
    public void addGPSPoint(HashMap<String, ArrayList<Float>> sensor_data,
                       double latitude, double longitude, long time) throws IOException {

        GPSPoint gps = new GPSPoint(sensor_data, latitude, longitude, time);

        String square_name = gps.getSquare();
        Boolean square_exists = false;
        int index;
        if (!squareList.isEmpty()) {
            for (int i = 0; i < squareList.size(); i++) {
                if(squareList.get(i).getName().equals(square_name)) {
                    square_exists = true;
                    Square square = squareList.get(i);
                    square.addGPSPoint(gps);
                    Log.d("trip", "Adding to square: " + square_name);
                    break;
                }
            }
        }
        if(!square_exists) {
            Square square = new Square(gps, square_name);
            squareList.add(square);
            Log.d("trip", "Creating new square: " + square_name);
        }
    }

}
