package lk.ads.app.greenelec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

import lk.ads.app.greenelec.adapter.ProductAdapter;
import lk.ads.app.greenelec.model.Cart;
import lk.ads.app.greenelec.model.Product;

public class ProductViewActivity extends AppCompatActivity {
    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private ArrayList<Product> items;
    private ImageView goToCart,goToFavorite;

//    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

//        goToCart = findViewById(R.id.imageView3);
//        goToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        items = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.productViewLayout);

        ProductAdapter productAdapter = new ProductAdapter(items,ProductViewActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(productAdapter);

        fireStore.collection("product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                items.clear();
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    Product product = snapshot.toObject(Product.class);
                    items.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }
        });

        fireStore.collection("product").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange change : value.getDocumentChanges()){
                    Product product = change.getDocument().toObject(Product.class);
                    switch (change.getType()){
                        case ADDED:
                            items.add(product);
                        case MODIFIED:
                            Product old = items.stream().filter(p->p.getName().equals(product.getName())).findFirst().orElse(null);
                            if (old != null){
                                old.setDescription(product.getDescription());
                                old.setPrice(product.getPrice());
                                old.setImage(product.getImage());
                            }
                            //break;
                        case REMOVED:
                            items.remove(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }
        });
    }
}