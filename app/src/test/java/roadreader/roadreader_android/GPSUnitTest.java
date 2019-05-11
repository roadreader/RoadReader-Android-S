package roadreader.roadreader_android;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GPSUnitTest {

    @Test
    public void getSquare_test() throws IOException {

        String correct_name = "index.home.raft";

        float lat = (float) 51.521251;
        float lng = (float) -0.203586;

        GPS gps = new GPS(lat, lng);
        String name = gps.getSquare();

        assertEquals(correct_name, name);
    }
}