package lk.ads.app.greenelec;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AboutUsFragment extends Fragment {

    private ImageView imageView;
    private ImageButton Facebook, Instagram, Twitter, Youtube;
    private Button ContactUs, ShopLocations;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View aboutUs, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(aboutUs, savedInstanceState);

        imageView = aboutUs.findViewById(R.id.appLogo);
        Facebook = aboutUs.findViewById(R.id.FBbutton);
        Instagram = aboutUs.findViewById(R.id.Inbutton);
        Twitter = aboutUs.findViewById(R.id.Tbutton);
        Youtube = aboutUs.findViewById(R.id.Ybutton);

        Picasso.get().load(R.drawable.four_leaf_icon).resize(400,400).centerCrop().into(imageView);
        Picasso.get().load(R.drawable.facebook_icon).resize(90,90).centerCrop().into(Facebook);
        Picasso.get().load(R.drawable.instagram_icon).resize(90,90).centerCrop().into(Instagram);
        Picasso.get().load(R.drawable.twitter_icon).resize(90,90).centerCrop().into(Twitter);
        Picasso.get().load(R.drawable.youtube_icon).resize(90,90).centerCrop().into(Youtube);

        ContactUs = aboutUs.findViewById(R.id.contactUs);
        ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.DIAL",Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        ShopLocations = aboutUs.findViewById(R.id.shopLocations);
        ShopLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
            }
        });

        Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.WEB_SEARCH");
                intent.putExtra("query","https://www.youtube.com/channel/UCohnV_kpiK1rVdqJcJQuRWg");
                startActivity(intent);
            }
        });
    }
}