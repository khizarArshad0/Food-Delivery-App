package x21l_5388_com.example.peezious;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private DBHelper dbHelper;
    private Context context;

    public CartAdapter(List<CartItem> cartItems, DBHelper dbHelper, Context context) {
        this.cartItems = cartItems;
        this.dbHelper = dbHelper;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.name.setText(item.getName());
        holder.quantity.setText("Quantity: " + item.getQuantity());
        holder.price.setText("Price: Rs. " + (item.getPrice() * item.getQuantity()));

        holder.deleteButton.setOnClickListener(v -> {
            int itemId = item.getId();
            dbHelper.deleteFromCart(itemId); // Delete from database
            cartItems.remove(position);         // Remove from the list
            notifyItemRemoved(position);        // Notify the adapter
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, price;
        Button deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_item_name);
            quantity = itemView.findViewById(R.id.cart_item_quantity);
            price = itemView.findViewById(R.id.cart_item_price);
            deleteButton = itemView.findViewById(R.id.cart_item_delete_button);
        }
    }
}
