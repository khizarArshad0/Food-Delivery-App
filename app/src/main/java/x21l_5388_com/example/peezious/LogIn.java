package x21l_5388_com.example.peezious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private EditText inputPhone, inputPassword;
    private Button loginBtn;
    private TextView signUpLink;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", null);
        if (userId != null) {
            goToMainActivity();
        }

        inputPhone = findViewById(R.id.input_phone);
        inputPassword = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.loginBtn);
        signUpLink = findViewById(R.id.signUpLink);

        loginBtn.setOnClickListener(v -> loginUser());

        signUpLink.setOnClickListener(v -> {
            // Navigate to SignUp Activity
            startActivity(new Intent(LogIn.this, SignUp.class));
        });

    }
    private void goToMainActivity() {
        // Navigate to the MainActivity if the user is already logged in or after successful login
        Intent intent = new Intent(LogIn.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the login activity so the user can't go back to it
    }


    private void loginUser() {
        String phone = inputPhone.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(LogIn.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the users node in Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Fetch the data for all users
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userFound = false;

                // Loop through all users in the database
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userPhone = userSnapshot.child("phone").getValue(String.class);
                    String userPassword = userSnapshot.child("password").getValue(String.class);
                    String userId = userSnapshot.getKey();
                    String userName = userSnapshot.child("name").getValue(String.class);

                    // Check if the phone and password match
                    if (userPhone.equals(phone) && userPassword.equals(password)) {
                        userFound = true;

                        // Save user data to SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_id", userId);
                        editor.putString("user_name", userName);
                        editor.putString("user_phone", userPhone);
                        editor.apply();

                        // Navigate to the next screen (MainActivity)
                        startActivity(new Intent(LogIn.this, MainActivity.class));
                        finish();
                        break;
                    }
                }

                if (!userFound) {
                    Toast.makeText(LogIn.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LogIn.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
