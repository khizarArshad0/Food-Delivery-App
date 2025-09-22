package x21l_5388_com.example.peezious;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    // Constructor
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the product card layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        DBHelper dbHelper = new DBHelper(context);
        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(String.format("Rs. %.2f", product.getPrice())); // Format price
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAdded = dbHelper.toggleFavorite(holder.name.getText().toString().strip());
                if (isAdded) {
                    Toast.makeText(context, "Item Added To Favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Item Removed From Favorites.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Decode Base64 image and set it to the ImageView using Glide
        if (product.getimageBase64() != null && !product.getimageBase64().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(product.getimageBase64(), Base64.DEFAULT);
                Glide.with(holder.image.getContext())
                        .asBitmap()
                        .load(decodedString)
                        .placeholder(R.drawable.baseline_fireplace_24)
                        .error(R.drawable.baseline_fireplace_24)
                        .into(holder.image);
            } catch (Exception e) {
                holder.image.setImageResource(R.drawable.baseline_fireplace_24);
            }
        } else {
            holder.image.setImageResource(R.drawable.baseline_fireplace_24);
        }

        holder.addToCart.setOnClickListener(v -> {
            // Inflate the popup layout
            View popupView = LayoutInflater.from(context).inflate(R.layout.add_to_cart_popup, null);
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setView(popupView);

            // Create and show the dialog
            android.app.AlertDialog dialog = builder.create();
            dialog.show();

            // Populate popup fields
            ImageView popupImage = popupView.findViewById(R.id.popup_item_image);
            TextView popupName = popupView.findViewById(R.id.popup_item_name);
            TextView popupDescription = popupView.findViewById(R.id.popup_item_description);
            TextView popupPrice = popupView.findViewById(R.id.popup_item_price);
            TextView popupQuantity = popupView.findViewById(R.id.popup_item_quantity);
            Button quantityIncrease = popupView.findViewById(R.id.quantity_increase);
            Button quantityDecrease = popupView.findViewById(R.id.quantity_decrease);
            Button addToCartButton = popupView.findViewById(R.id.popup_add_to_cart);

            popupName.setText(product.getName());
            popupDescription.setText(product.getDescription());
            popupPrice.setText(String.format("Rs. %.2f", product.getPrice()));

            // Load image using Glide
            if (product.getimageBase64() != null && !product.getimageBase64().isEmpty()) {
                try {
                    byte[] decodedString = Base64.decode(product.getimageBase64(), Base64.DEFAULT);
                    Glide.with(context)
                            .asBitmap()
                            .load(decodedString)
                            .placeholder(R.drawable.baseline_fireplace_24)
                            .error(R.drawable.baseline_fireplace_24)
                            .into(popupImage);
                } catch (Exception e) {
                    popupImage.setImageResource(R.drawable.baseline_fireplace_24);
                }
            }

            // Quantity change logic
            final int[] quantity = {1};
            quantityIncrease.setOnClickListener(v1 -> {
                quantity[0]++;
                popupQuantity.setText(String.valueOf(quantity[0]));
            });
            quantityDecrease.setOnClickListener(v1 -> {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    popupQuantity.setText(String.valueOf(quantity[0]));
                }
            });

            // Add to cart action
            addToCartButton.setOnClickListener(v2 -> {
                dbHelper.addToCart(product.getName(), product.getPrice(), quantity[0]);
                Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    // Update the product list and refresh the RecyclerView
    public void updateProducts(List<Product> newProducts) {
        productList = newProducts;
        notifyDataSetChanged();
    }

    // ViewHolder class for the Product items
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price;
        ImageView image;
        ImageView fav;
        Button addToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            name = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            price = itemView.findViewById(R.id.item_price);
            image = itemView.findViewById(R.id.item_image);
            fav = itemView.findViewById(R.id.item_favorite_icon);
            addToCart = itemView.findViewById(R.id.item_add_button);
        }
    }
}
