package x21l_5388_com.example.peezious;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class PastaFragment extends Fragment {
    RecyclerView recyclerView;
    ProductAdapter adapter; // Reusing the same adapter
    DatabaseReference databaseReference;
    public PastaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pasta, container, false);

        recyclerView = view.findViewById(R.id.pastaRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        List<Product> pastaList = new ArrayList<>();
        adapter = new ProductAdapter(pastaList,getContext()); // Reuse the same adapter
        recyclerView.setAdapter(adapter);

        fetchPastaProducts(pastaList);

        return view;
    }

    private void fetchPastaProducts(List<Product> pastaList) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pastaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null && "Pasta".equalsIgnoreCase(product.getType())) {
                        pastaList.add(product);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data change
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
}
