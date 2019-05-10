package roadreader.roadreader_android;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class GPSPoint {
    final String API_KEY = "YK9ZNUS6";

    HashMap<String, ArrayList<Float>> sensor_data;
    long time;
    double lat, lng;

    public GPSPoint (HashMap<String, ArrayList<Float>> sensorData, double latitude, double longitude, long timestamp) {

        sensor_data = new HashMap<>(sensorData);
        lat = latitude;
        lng = longitude;
        time = timestamp;
    }

    public GPSPoint(float latitude, float longitude) {
        lat = latitude;
        lng = longitude;
    }

    /**
     * Uses lat and lng to get the gps coordinate's corresponding square
     * through a get request to what3words
     * @return The name of the Square that the gps coordinates fall under
     */
    protected String getSquare() throws IOException {
        String req = "https://api.what3words.com/v3/convert-to-3wa?coordinates=" +
                lat +
                "%2C" +
                lng +
                "&key=" +
                API_KEY;
        Request request = new Request();
        String response_string = request.sendGET(req);

        if(response_string.isEmpty())
            return "failed";

        Map<String, Object> jsonMap;
        Gson gson = new Gson();
        Type outputType = new TypeToken<Map<String, Object>>(){}.getType();
        jsonMap = gson.fromJson(response_string, outputType);

        Log.d("GPS", jsonMap.toString());
        System.out.println(jsonMap.toString());

        String square_name = jsonMap.get("words").toString();

        return square_name;
    }
}
