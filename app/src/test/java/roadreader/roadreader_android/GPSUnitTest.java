package roadreader.roadreader_android;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GPSUnitTest {

    @Test
    public void getSquare_test() throws IOException {

        String correct_name = "index.home.raft";

        double lat =  51.521251;
        double lng =  -0.203586;

        GPSPoint gps = new GPSPoint(lat, lng);
        String name = gps.getSquare();

        assertEquals(correct_name, name);
    }

    @Test
    public void getLatLng_test() throws IOException {
        String name = "filled.count.soap";
        double lat = 51.520847;
        double lng = -0.195521;

        Square square = new Square(name);
        System.out.println("lat: " + square.getLat());
        System.out.println("lng: " + square.getLng());

        assertEquals(lat, square.getLat(), 0.00001);
        assertEquals(lng, square.getLng(), 0.00001);
    }
}