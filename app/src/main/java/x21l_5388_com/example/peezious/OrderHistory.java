package x21l_5388_com.example.peezious;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get userId from Intent
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "No ID Found");
        // Firebase reference to the orders node
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Initialize the order list
        orderList = new ArrayList<>();

        // Fetch orders from Firebase for this user
        fetchOrdersFromFirebase(userId);

        // Set the adapter for RecyclerView
        adapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(adapter);
    }

    // Function to fetch orders from Firebase Database based on userId
    private void fetchOrdersFromFirebase(String userId) {
        ordersRef.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear(); // Clear existing list

                // Loop through each order
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);  // Map Firebase data to Order object
                    if (order != null) {
                        orderList.add(order); // Add the order to the list
                    }
                }

                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(OrderHistory.this, "Failed to load orders: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
