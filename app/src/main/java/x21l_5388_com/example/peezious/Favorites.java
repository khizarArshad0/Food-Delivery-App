package x21l_5388_com.example.peezious;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter; // Reusing the same adapter
    DatabaseReference databaseReference;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favRV);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        DBHelper dbHelper = new DBHelper(this);

        // Fetch favorite product names from SQLite
        List<String> favoriteNames = dbHelper.getAllFavorites();
        if (favoriteNames.isEmpty()) {
            Toast.makeText(this, "No favorites found.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");
        // Fetch favorite products from Firebase
        fetchFavoritesFromFirebase(favoriteNames);
    }

    private void fetchFavoritesFromFirebase(List<String> favoriteNames) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Clear the previous data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null && favoriteNames.contains(product.getName())) {
                        productList.add(product); // Add products in favorites
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Favorites.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
