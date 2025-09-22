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

public class PizzaFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter adapter; // Reusing the same adapter
    DatabaseReference databaseReference;

    public PizzaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza, container, false);

        recyclerView = view.findViewById(R.id.pizzaRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("products");

        List<Product> pizzaList = new ArrayList<>();
        adapter = new ProductAdapter(pizzaList,getContext()); // Reuse the same adapter
        recyclerView.setAdapter(adapter);

        fetchPizzaProducts(pizzaList);

        return view;
    }

    private void fetchPizzaProducts(List<Product> pizzaList) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pizzaList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null && "Pizza".equalsIgnoreCase(product.getType())) {
                        pizzaList.add(product);
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
