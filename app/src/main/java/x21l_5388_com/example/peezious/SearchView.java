package x21l_5388_com.example.peezious;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SearchView extends AppCompatActivity {
    RecyclerView recyclerView;
    ProductAdapter adapter; // Reusing the same adapter
    DatabaseReference databaseReference;
    EditText searchterm;
    ImageButton searchButton;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        searchButton = findViewById(R.id.searchButton);
        searchterm = findViewById(R.id.searchterm);
        recyclerView = findViewById(R.id.searchRV);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        // Set up the search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchterm.getText().toString().trim();
                if (!query.isEmpty()) {
                    fetchProducts(query);
                } else {
                    Toast.makeText(SearchView.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchProducts(String query) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear(); // Clear the previous data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null && product.getName().toLowerCase().contains(query.toLowerCase())) {
                        productList.add(product); // Add matching products
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchView.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
