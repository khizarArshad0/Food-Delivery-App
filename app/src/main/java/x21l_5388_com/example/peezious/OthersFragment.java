package x21l_5388_com.example.peezious;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OthersFragment extends Fragment {

    private RecyclerView rv;
    private ProductAdapter adapter;
    private DatabaseReference databaseReference;
    private List<Product> productList = new ArrayList<>();

    public OthersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_others, container, false);

        rv = v.findViewById(R.id.otherRV);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        // Configure RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with an empty list
        adapter = new ProductAdapter(productList, getContext());
        rv.setAdapter(adapter);

        // Fetch products of type "Others"
        fetchOthersProducts();

        return v;
    }

    private void fetchOthersProducts() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null && "Others".equalsIgnoreCase(product.getType())) {
                        productList.add(product);
                    }
                }
                adapter.updateProducts(productList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
