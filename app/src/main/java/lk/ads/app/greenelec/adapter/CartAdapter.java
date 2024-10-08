package lk.ads.app.greenelec.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lk.ads.app.greenelec.R;
import lk.ads.app.greenelec.model.Cart;
import lk.ads.app.greenelec.model.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH>{
    private ArrayList<Cart> items;
    private FirebaseStorage storage;
    private FirebaseFirestore fireStore;
    private Context context;

    public CartAdapter(ArrayList<Cart> items, Context context){
        this.items = items;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_cart_row, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Cart cart = items.get(position);

        holder.cartProduct.setText(cart.getProduct());
        holder.cartPrice.setText(String.valueOf(cart.getPrice()));
        holder.cartQty.setText(String.valueOf(cart.getQuantity()));

        storage.getReference("item-images/" + cart.getImage())
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).resize(300, 300).centerCrop().into(holder.cartImage);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class VH extends RecyclerView.ViewHolder{
        TextView cartProduct,cartPrice,cartQty;
        ImageView cartImage;
        public VH(@NonNull View itemView) {
            super(itemView);
            cartProduct = itemView.findViewById(R.id.cartName);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartQty = itemView.findViewById(R.id.cartQty);
            cartImage = itemView.findViewById(R.id.cartImage);
        }
    }
}
