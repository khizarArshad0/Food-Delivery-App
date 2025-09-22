package x21l_5388_com.example.peezious;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private RecyclerView recyclerView;
    private AddressAdapter addressAdapter;
    private List<String> addressList;
    private ImageButton addAddressButton;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Fix 1: Add import for ActivityResultLauncher
    // Fix 2: Modify the permission launcher declaration
    private ActivityResultLauncher<String> requestPermissionsLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Initialize the permission launcher
        requestPermissionsLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        fetchAddressFromLocation();
                    } else {
                        Toast.makeText(this, "Location permission is required to fetch address", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.burgerRV);
        addAddressButton = findViewById(R.id.addAddressButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch the saved addresses from the database
        addressList = dbHelper.getAllAddresses();
        addressAdapter = new AddressAdapter(this, addressList);
        recyclerView.setAdapter(addressAdapter);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // On clicking the add button, show the dialog for address input
        addAddressButton.setOnClickListener(v -> showAddressDialog());
    }

    private void showAddressDialog() {
        // Use AlertDialog.Builder for the dialog instead of MaterialAlertDialogBuilder
        new AlertDialog.Builder(this)
                .setTitle("Add Address")
                .setItems(new String[]{"Manually Enter Address", "Use My Location"}, (dialog, which) -> {
                    if (which == 0) {
                        // Show input dialog to manually enter the address
                        showManualAddressInput();
                    } else {
                        // Fetch address using location
                        fetchAddressFromLocation();
                    }
                })
                .show();
    }

    private void showManualAddressInput() {
        // Create an EditText to enter the address manually
        EditText addressInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Enter Address")
                .setView(addressInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String address = addressInput.getText().toString().trim();
                    if (!address.isEmpty()) {
                        dbHelper.addAddress(address); // Save to DB
                        updateRecyclerView(); // Refresh RecyclerView
                    } else {
                        Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void fetchAddressFromLocation() {
        // Request location permissions if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not already granted
            requestPermissionsLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        // Fetch location using FusedLocationProviderClient
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                // Use the fetched location to get the address
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String address = addresses.get(0).getAddressLine(0); // Get address in the common tongue
                        dbHelper.addAddress(address); // Save to DB
                        updateRecyclerView(); // Refresh RecyclerView
                    } else {
                        Toast.makeText(this, "Unable to fetch address", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to fetch location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateRecyclerView() {
        // Get all addresses from DB and update RecyclerView
        addressList = dbHelper.getAllAddresses();
        addressAdapter.updateAddressList(addressList); // Update RecyclerView adapter
    }
}
