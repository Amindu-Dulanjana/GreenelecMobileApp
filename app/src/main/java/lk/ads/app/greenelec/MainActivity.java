package lk.ads.app.greenelec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Greenelec_FullScreen);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.LogoimageView);
        Picasso.get().load(R.drawable.four_leaf_icon).resize(75,75).centerCrop().into(imageView);
        progressBar = findViewById(R.id.progressBar);
        handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}