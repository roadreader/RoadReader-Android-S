package roadreader.roadreader_android;

import java.util.ArrayList;
import java.util.List;

public class Square {

    /**
     * Array of GPS coords recorded that correspond to this Square
     */
    private List<GPSPoint> gpsList;

    /**
     * Name assigned to square
     */
    private String name;

    public Square (GPSPoint gps, String sName) {
        gpsList = new ArrayList<>();
        gpsList.add(gps);
        name = sName;
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
}
