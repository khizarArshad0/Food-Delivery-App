package x21l_5388_com.example.peezious;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Spinner addressSpinner;
    private RecyclerView cartRecyclerView;
    private Button placeOrderButton;
    private TextView totalAmountTextView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private List<String> savedAddresses;
    private DatabaseReference databaseReference;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize Database Helper
        dbHelper = new DBHelper(this);

        // Initialize Views
        addressSpinner = findViewById(R.id.address_spinner);
        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        placeOrderButton = findViewById(R.id.place_order_button);
        totalAmountTextView = findViewById(R.id.total_amount); // Existing TextView for total amount

        // Load Data from Database
        cartItems = dbHelper.getAllCartItems();
        savedAddresses = dbHelper.getAllAddresses();

        // Set up Spinner
        AddressSpinnerAdapter addressAdapter = new AddressSpinnerAdapter(this, savedAddresses);
        addressSpinner.setAdapter(addressAdapter);

        // Set up RecyclerView
        cartAdapter = new CartAdapter(cartItems, dbHelper, this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Calculate and Update Total Price
        updateTotalPrice();

        // Place Order Button Click Listener
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the total price and update it
                updateTotalPrice();

                // Get the selected address from the spinner
                String selectedAddress = (String) addressSpinner.getSelectedItem();

                // Get the order details
                String orderDetails = getOrderDetails(selectedAddress);

                // Get user details from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", "No ID Found");
                String phone = sharedPreferences.getString("user_phone", "No");

                // Get Firebase Database reference
                databaseReference = FirebaseDatabase.getInstance().getReference().child("orders");

                // Create a new order object
                Order order = new Order(userId, phone, selectedAddress, getTotalPrice(), "pending", orderDetails);

                // Push the order to Firebase
                String orderId = databaseReference.push().getKey();
                if (orderId != null) {
                    databaseReference.child(orderId).setValue(order)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Order placed successfully
                                    Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                                    clearCart(); // Clear the cart after order is placed
                                } else {
                                    // Order placement failed
                                    Toast.makeText(CartActivity.this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    // Function to update the total price
    private void updateTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        // Update the text of the existing TextView
        totalAmountTextView.setText("Rs. " + total);
    }

    // Function to get the order details as a formatted string
    private String getOrderDetails(String address) {
        StringBuilder orderDetails = new StringBuilder("Order Details:\n");
        for (CartItem item : cartItems) {
            orderDetails.append(item.getName()).append(" x ")
                    .append(item.getQuantity()).append(" - Rs. ")
                    .append(item.getPrice() * item.getQuantity()).append("\n");
        }
        return orderDetails.toString();
    }

    // Function to get the total price as an integer
    private int getTotalPrice() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    // Function to clear the cart after order placement
    private void clearCart() {
        dbHelper.clearCart();
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
        totalAmountTextView.setText("Rs. 0.00");
    }
}
