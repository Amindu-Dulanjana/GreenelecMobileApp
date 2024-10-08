package lk.ads.app.greenelec.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ads.app.greenelec.CartActivity;
import lk.ads.app.greenelec.HomeFragment;
import lk.ads.app.greenelec.LoginActivity;
import lk.ads.app.greenelec.ProductViewActivity;
import lk.ads.app.greenelec.R;
import lk.ads.app.greenelec.model.Cart;
import lk.ads.app.greenelec.model.Product;
import lk.ads.app.greenelec.model.User;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {
    private ArrayList<Product> items;
    private FirebaseStorage storage;
    private FirebaseFirestore fireStore;
    private Context context;

    public ProductAdapter(ArrayList<Product> items, Context context) {
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
        this.fireStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_product_row, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Product product = items.get(position);
        double total = product.getPrice()+product.getDeliverPrice();
        holder.NameText.setText(product.getName()+" | "+product.getBrand());
        holder.DesText.setText(product.getDescription());
        holder.PriceText.setText(String.valueOf(total));

        storage.getReference("item-images/" + product.getImage())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(200, 200).centerCrop().into(holder.image);
                    }
                });

        holder.Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product, 1);
            }
        });
    }

    private void addToCart(Product product, int qty) {

        String pName = product.getName()+" | "+product.getBrand();
        String pImage = product.getImage();
        double Total = product.getPrice()+product.getDeliverPrice();

        User user = new User();
        SharedPreferences preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String email = preferences.getString("email", user.getEmail());

        fireStore.collection("cartItem").whereEqualTo("product", pName).whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot result = task.getResult();
                    if (result.isEmpty()) {
                        Cart cartItems = new Cart(email, pName, qty, pImage, Total);

                        fireStore.collection("cartItem").add(cartItems).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Toast.makeText(context.getApplicationContext(), "This Product is a Add to the Cart",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        for (QueryDocumentSnapshot documentSnapshot : result) {
                            Long cartQty = documentSnapshot.getLong("quantity");
                            Double cartPrice = documentSnapshot.getDouble("price");
                            String docId = documentSnapshot.getId();

                            //cartQty = cartQty+(long) qty;
                            cartQty+=(long) qty;
                            cartPrice+=Total;


                            Map<String, Object> updates = new HashMap<>();
                            updates.put("quantity",cartQty);
                            updates.put("price",cartPrice);

                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("cartItem").document(docId);

                            docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context.getApplicationContext(), "This Product is a Add to the Cart",Toast.LENGTH_LONG).show();
                                }
                            });
                            break;
                        }
                    }
                }
            }
        });

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView NameText, DesText, PriceText;
        ImageView image, Cart;

        public VH(@NonNull View itemView) {
            super(itemView);
            NameText = itemView.findViewById(R.id.productNameView);
            DesText = itemView.findViewById(R.id.productDescriptionView);
            PriceText = itemView.findViewById(R.id.productPriceView);
            image = itemView.findViewById(R.id.productImageView);
            Cart = itemView.findViewById(R.id.imageViewCart);
        }
    }
}
