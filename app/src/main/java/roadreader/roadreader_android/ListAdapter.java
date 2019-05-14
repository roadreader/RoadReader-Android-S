package roadreader.roadreader_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by chadlohrli on 5/13/19.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.VideoViewHolder> {

    private File[] videos;
    private Context context;

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView videoName, videoTimestamp, videoLength;
        ImageView videoSnapshot;

        VideoViewHolder(View item) {
            super(item);
            videoName = item.findViewById(R.id.videoName);
            videoTimestamp = item.findViewById(R.id.videoTimeStamp);
            videoLength = item.findViewById(R.id.videoLength);
            videoSnapshot = item.findViewById(R.id.videoImageView);
        }


    }


    public ListAdapter(Context c) {

        //grab all videos in folder
        File media = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES + File.separator + "RoadReader");
        videos = media.listFiles();
        context = c;

    }

    @Override
    public int getItemCount() {
        return videos.length;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_list_item, viewGroup, false);
        VideoViewHolder viewHolder = new VideoViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(VideoViewHolder videoViewHolder, final int i) {

        //get snapshot
        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(videos[i].getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        videoViewHolder.videoSnapshot.setImageBitmap(bMap);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videos[i].getAbsolutePath());
        String str_duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        int duration = (Integer.valueOf(str_duration) / 1000);
        str_duration = String.valueOf(duration) + "s";
        videoViewHolder.videoLength.setText(str_duration);

        String videoName = videos[i].getName();
        videoViewHolder.videoName.setText(videoName);

        //parse out VID_
        int startIndex = 4;
        int endIndex = videoName.indexOf(".mp4");
        videoName = videoName.substring(startIndex,endIndex);
        long unixTime = Integer.valueOf(videoName);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' hh:mm:ss a", Locale.US);
        String readableDate = sdf.format(unixTime*1000L);
        videoViewHolder.videoTimestamp.setText(readableDate);

        //transition click
        videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put intent code here
            }
        });


    }


}
