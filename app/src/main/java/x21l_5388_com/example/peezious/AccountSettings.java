package x21l_5388_com.example.peezious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSettings extends AppCompatActivity {

    LinearLayout orderViewButton, addressViewButton, favViewButton, changePasswordButton, logOutButton, deleteAccountButton;
    TextView userName, userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);

        // Apply WindowInsets (Edge to Edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to the views
        orderViewButton = findViewById(R.id.orderViewButton);
        addressViewButton = findViewById(R.id.addressViewButton);
        favViewButton = findViewById(R.id.favViewButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        logOutButton = findViewById(R.id.logOutButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);

        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);

        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("user_name", "Guest");
        String phone = sharedPreferences.getString("user_phone", "Not Available");

        // Set the name and phone in the TextViews
        userName.setText(name);
        userPhone.setText(phone);

        // Set onClickListeners for the buttons
        orderViewButton.setOnClickListener(v -> viewOrders());
        addressViewButton.setOnClickListener(v -> viewAddresses());
        favViewButton.setOnClickListener(v -> viewFavorites());
        changePasswordButton.setOnClickListener(v -> changePassword());
        logOutButton.setOnClickListener(v -> logout());
        deleteAccountButton.setOnClickListener(v -> deleteAccount());
    }

    private void viewOrders() {
        Intent I = new Intent(AccountSettings.this, OrderHistory.class);
        startActivity(I);
    }

    private void viewAddresses() {
        Intent I = new Intent(AccountSettings.this, AddressActivity.class);
        startActivity(I);
    }

    private void viewFavorites() {
        Intent I = new Intent(AccountSettings.this, Favorites.class);
        startActivity(I);
    }

    private void changePassword() {
        Intent I = new Intent(AccountSettings.this,ChangePassword.class);
        startActivity(I);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.clearAddresses();
        dbHelper.clearFavorites();
        editor.clear();  // This will clear all data in SharedPreferences
        editor.apply();  // Apply the changes
        Intent I = new Intent(AccountSettings.this,LogIn.class);
        startActivity(I);
    }

    private void deleteAccount() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userID = sharedPreferences.getString("user_id", "Nan");
        if (!userID.equals("Nan")) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(userID).removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AccountSettings.this, "Account deleted from Firebase", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountSettings.this, "Failed to delete account from Firebase", Toast.LENGTH_SHORT).show();
                        }
                    });
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.clearAddresses();
            dbHelper.clearFavorites();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(this, "Deleting Account", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AccountSettings.this, LogIn.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
        }
    }

}
