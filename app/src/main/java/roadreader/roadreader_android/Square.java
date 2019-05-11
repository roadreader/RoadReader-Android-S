package roadreader.roadreader_android;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Square {
    final String API_KEY = "YK9ZNUS6";

    /**
     * Array of GPS coords recorded that correspond to this Square
     */
    private List<GPSPoint> gpsList;
    private double lat, lng;

    /**
     * Name assigned to square
     */
    private String name;

    public Square (GPSPoint gps, String sName) {
        gpsList = new ArrayList<>();
        gpsList.add(gps);
        name = sName;

        try {
            double[] coords = getLatLng();
            lat = coords[0];
            lng = coords[1];
        } catch (Exception e) {
            lat = 0;
            lng = 0;
        };

    }

    /**
     * Constructor for testing
     * @param sName
     */
    public Square(String sName) {
        name = sName;
        try {
            double[] coords = getLatLng();
            lat = coords[0];
            lng = coords[1];
        } catch (Exception e) {
            lat = 0;
            lng = 0;
        };
    }

    /**
     * Getter for name
     * @return Name of square
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a GPS instance to gpsList
     * @param gps GPS instance to add to GPS List
     */
    public void addGPSPoint(GPSPoint gps) {
        gpsList.add(gps);
    }

    /**
     * Gets the coordinates that corresponds to the square's name
     * @return Array of type double with index 0 being latitude and index 1 being longitude,
     *          or {0,0} if error getting the name
     * @throws IOException For GET request
     */
    private double[] getLatLng() throws IOException {

        String req = "https://api.what3words.com/v3/convert-to-coordinates?words=" +
                name + "&key=" +
                API_KEY;

        Request request = new Request();
        String response = request.sendGET(req);
        if(response.isEmpty()) {
            return (new double[] {0,0});
        }
        double[] coords;

        //create hashmap out of response string
        Map<String, Object> jsonMap;
        Gson gson = new Gson();
        Type outputType = new TypeToken<Map<String, Object>>(){}.getType();
        jsonMap = gson.fromJson(response, outputType);
        Log.d("Square", jsonMap.toString());
        System.out.println(jsonMap.toString());
        System.out.println(jsonMap.get("coordinates").toString());

        //create a smaller map for just coordinates
        jsonMap = gson.fromJson(jsonMap.get("coordinates").toString(), outputType);

        double latitude = Double.parseDouble(jsonMap.get("lat").toString());
        double longitude = Double.parseDouble(jsonMap.get("lng").toString());
        coords = new double[] {latitude, longitude};

        return coords;
    }

    private void setName(String new_name) {
        name = new_name;
    }

    protected double getLat() {
        return lat;
    }

    protected double getLng() {
        return lng;
    }

}
