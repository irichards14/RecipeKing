package recipeking.uw.tacoma.edu.recipeking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CookingVideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking_videos);


    }

    public void openVideo(View view) {
        String url = "";

        if (view.getId() == R.id.cooking_video_1) {
            url = "https://www.youtube.com/watch?v=D4p0RFK3TbA";
        } else if (view.getId() == R.id.cooking_video_2) {
            url = "https://www.youtube.com/watch?v=xJydA4sqrXk";
        } else if (view.getId() == R.id.cooking_video_3) {
            url = "https://www.youtube.com/watch?v=eJFQb9fq0Ik";
        } else if (view.getId() == R.id.cooking_video_4) {
            url = "https://www.youtube.com/watch?v=A0d51_YovHY";
        } else if (view.getId() == R.id.cooking_video_5) {
            url = "https://www.youtube.com/watch?v=i944XxUXkzs";
        }

        startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)));
    }
}
