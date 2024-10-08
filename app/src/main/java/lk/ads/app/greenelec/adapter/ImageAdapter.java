package lk.ads.app.greenelec.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.ads.app.greenelec.HomeFragment;
import lk.ads.app.greenelec.R;

public class ImageAdapter extends PagerAdapter {
    private HomeFragment context;
    private List<Integer> imageList;

    private ViewPager viewPager;
    private Handler handler;

    public ImageAdapter(HomeFragment context, List<Integer> imageList, ViewPager viewPager) {
        this.context = context;
        this.imageList = imageList;
        this.viewPager = viewPager;
        this.handler = new Handler(Looper.getMainLooper());
        startAutoScroll();
    }

    private void startAutoScroll(){
        handler.postDelayed(autoScrollRunnable, 3000);
    }

    private final Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            int nextItem = (currentItem + 1) % imageList.size();
            viewPager.setCurrentItem(nextItem, true);
            handler.postDelayed(this, 3000);
        }
    };

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //LayoutInflater inflater = LayoutInflater.from(container.getContext());
        //View view = inflater.inflate(R.layout.image_item, null);
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.image_item, container, false);

        ImageView imageView = view.findViewById(R.id.sliderImage);

        Picasso.get()
                .load(imageList.get(position))
                .fit()
                .centerCrop()
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
