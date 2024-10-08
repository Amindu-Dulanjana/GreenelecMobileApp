package lk.ads.app.greenelec;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import lk.ads.app.greenelec.adapter.ImageAdapter;
import lk.ads.app.greenelec.adapter.ProductAdapter;
import lk.ads.app.greenelec.model.Product;

public class HomeFragment extends Fragment {

    private ViewPager viewPager;

    private Button startShopping;

    private FirebaseStorage storage;
    private FirebaseFirestore fireStore;
    private ArrayList<Product> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View home, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(home, savedInstanceState);

        viewPager = home.findViewById(R.id.slider);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.slider_image1);
        imageList.add(R.drawable.slider_image2);
        imageList.add(R.drawable.slider_image3);

        ImageAdapter adapter = new ImageAdapter(this, imageList,viewPager);
        viewPager.setAdapter(adapter);

        startShopping = home.findViewById(R.id.startShopping);
        startShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                if (preferences.contains("email")){
                    Intent intent = new Intent(getActivity(),ProductViewActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Warning").setIcon(R.drawable.baseline_warning_24).setMessage("Please Login First in Greenelec !");

                    builder.setPositiveButton("Go to Sign In", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        ///list
//        fireStore = FirebaseFirestore.getInstance();
//        storage = FirebaseStorage.getInstance();
////
//        items = new ArrayList<>();
////
//        RecyclerView recyclerView1 = home.findViewById(R.id.homeRecycle);
////
//        ProductAdapter HomeProductAdapter = new ProductAdapter(items,getActivity());
//       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//       recyclerView1.setLayoutManager(linearLayoutManager);
//     recyclerView1.setAdapter(HomeProductAdapter);
//////
//        fireStore.collection("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                items.clear();
//                for (QueryDocumentSnapshot snapshot : task.getResult()){
//                    Product product = snapshot.toObject(Product.class);
//                    items.add(product);
//                }
//                HomeProductAdapter.notifyDataSetChanged();
//            }
//        });
////////
//        fireStore.collection("product").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                for (DocumentChange change : value.getDocumentChanges()){
//                    Product product = change.getDocument().toObject(Product.class);
//                    switch (change.getType()){
//                        case ADDED:
//                            items.add(product);
//                        case MODIFIED:
//                            Product old = items.stream().filter(p->p.getName().equals(product.getName())).findFirst().orElse(null);
//                            if (old != null){
//                                old.setPrice(product.getPrice());
//                                old.setImage(product.getImage());
//                            }
//                            break;
//                        case REMOVED:
//                            items.remove(product);
//                    }
//                }
//                HomeProductAdapter.notifyDataSetChanged();
//            }
//        });
    }

}