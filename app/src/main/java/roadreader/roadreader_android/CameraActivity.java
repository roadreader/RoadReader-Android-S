package roadreader.roadreader_android;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.VideoView;

public class CameraActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        videoView = (VideoView) findViewById(R.id.videoView);
        dispatchTakeVideoIntent();

    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, String.valueOf(intent.getData()), Toast.LENGTH_SHORT).show();
            Uri videoUri = intent.getData();
            System.out.println(videoUri);
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
        else{
            Toast.makeText(this, String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
        }
    }
}
