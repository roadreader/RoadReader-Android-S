package roadreader.roadreader_android;

import java.util.ArrayList;
import java.util.List;

public class Square {

    /**
     * Array of GPS coords recorded that correspond to this Square
     */
    private List<GPS> gpsList;
    private String name;

    public Square (GPS gps, String sName) {
        gpsList = new ArrayList<>();
        gpsList.add(gps);
        name = sName;
    }

    public String getName() {
        return name;
    }

    public void addGPS(GPS gps) {
        gpsList.add(gps);
    }
}
