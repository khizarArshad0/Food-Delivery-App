package x21l_5388_com.example.peezious;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText inputName, inputPhone, inputPassword;
    private Button signUpBtn;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Bind UI elements
        inputName = findViewById(R.id.input_name);
        inputPhone = findViewById(R.id.input_phone);
        inputPassword = findViewById(R.id.input_password);
        signUpBtn = findViewById(R.id.signUpBtn);

        // Set up button click listener
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (validateInputs(name, phone, password)) {
                    createUser(name, phone, password);
                }
            }
        });

    }

    private boolean validateInputs(String name, String phone, String password) {
        if (TextUtils.isEmpty(name)) {
            inputName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            inputPhone.setError("Phone number is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            inputPassword.setError("Password must be at least 6 characters");
            return false;
        }
        return true;
    }

    private void createUser(String name, String phone, String password) {
        // Create a unique key in Firebase
        String userId = databaseReference.push().getKey();

        if (userId == null) {
            Toast.makeText(this, "Error creating user. Try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a user object with the password included
        User user = new User(name, phone, password);

        // Save user to Firebase
        databaseReference.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    // Save user details in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_id", userId);
                    editor.putString("user_name", name);
                    editor.putString("user_phone", phone);
                    editor.apply();

                    // Notify user
                    Toast.makeText(SignUp.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                    Intent I = new Intent(SignUp.this, MainActivity.class);
                    startActivity(I);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SignUp.this, "Failed to create user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // User model class
    public static class User {
        public String name;
        public String phone;
        public String password;

        public User() {
            // Default constructor required for Firebase
        }

        public User(String name, String phone, String password) {
            this.name = name;
            this.phone = phone;
            this.password = password;
        }
    }
}
