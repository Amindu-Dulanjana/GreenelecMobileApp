package lk.ads.app.greenelec;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.ads.app.greenelec.adapter.CartAdapter;
import lk.ads.app.greenelec.adapter.ProductAdapter;
import lk.ads.app.greenelec.model.Cart;
import lk.ads.app.greenelec.model.Product;
import lk.ads.app.greenelec.model.User;

public class CartActivity extends AppCompatActivity {

    private FirebaseFirestore fireStore;
    private FirebaseStorage storage;
    private ArrayList<Cart> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        fireStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        cartItems = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.cartRecycle);

        CartAdapter cartAdapter = new CartAdapter(cartItems,CartActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(cartAdapter);

        User user = new User();
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String email = preferences.getString("email", user.getEmail());

        fireStore.collection("cartItem").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot snapshot : task.getResult()){
                    Cart cart = snapshot.toObject(Cart.class);
                    cartItems.add(cart);
                }
                cartAdapter.notifyDataSetChanged();
            }
        });

        fireStore.collection("cartItem").whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange change : value.getDocumentChanges()){
                    Cart cart = change.getDocument().toObject(Cart.class);
                    switch (change.getType()){
                        case ADDED:
                            cartItems.add(cart);
                        case MODIFIED:
                            Cart old = cartItems.stream().filter(c -> c.getProduct().equals(cart.getProduct())).findFirst().orElse(null);
                            if (old != null){
                                old.setPrice(cart.getPrice());
                                old.setQuantity(cart.getQuantity());
                                old.setImage(cart.getImage());
                            }
                        case REMOVED:
                            cartItems.remove(cart);
                    }
                }
            }
        });

        CollectionReference orderCollection = FirebaseFirestore.getInstance().collection("cartItem");

        Query userOrderQuery = orderCollection.whereEqualTo("email",email);

        userOrderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    double total = 0.0;
                    int totalItems = 0;

                    for (QueryDocumentSnapshot document : querySnapshot){
                        double price = document.getDouble("price");
                        Long itemCount = document.getLong("quantity");

                        total += price;
                        totalItems += itemCount;

                        TextView textView = findViewById(R.id.totalPrice);
                        textView.setText(String.valueOf(total));

                        TextView textView1 = findViewById(R.id.itemCount);
                        textView1.setText(String.valueOf(totalItems));
                    }
                }
            }
        });
    }
}