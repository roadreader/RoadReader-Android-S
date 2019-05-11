package roadreader.roadreader_android;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {

    private static final String USER_AGENT = "Mozilla/5.0";

    public Request() {

    }

    public String sendGET(String s) throws IOException {
        URL url = new URL(s);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();

        Log.d( "GET request","GET Response Code :: " + responseCode);
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // log result
            Log.d("GET request" , response.toString());
            System.out.println(response.toString());
            con.disconnect();
            return response.toString();

        } else {
            Log.d( "GET request","GET request did not work");
            System.out.println("GET request did not work");
            con.disconnect();
            return "";
        }
    }

    public String sendPOST() throws IOException {

        return "";
    }
}
