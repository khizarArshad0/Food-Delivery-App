package x21l_5388_com.example.peezious;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends AppCompatActivity {

    private TextInputEditText currentPasswordField, newPasswordField, reEnterPasswordField;
    private MaterialButton updatePasswordButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Get references to UI elements
        currentPasswordField = findViewById(R.id.currentPassword);
        newPasswordField = findViewById(R.id.newPassword);
        reEnterPasswordField = findViewById(R.id.reEnterPassword);
        updatePasswordButton = findViewById(R.id.updatePasswordButton);

        // Set OnClickListener for the update password button
        updatePasswordButton.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        // Get the user ID from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userID = sharedPreferences.getString("user_id", "Nan");

        // Get the current and new passwords entered by the user
        String currentPassword = currentPasswordField.getText().toString().trim();
        String newPassword = newPasswordField.getText().toString().trim();
        String reEnteredPassword = reEnterPasswordField.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(currentPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(reEnteredPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the new password and re-entered password match
        if (!newPassword.equals(reEnteredPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate the new password length (minimum 6 characters)
        if (newPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the stored current password from Firebase Realtime Database
        databaseReference.child(userID).child("password").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String storedCurrentPassword = task.getResult().getValue(String.class);

                // Check if the entered current password matches the stored password
                if (storedCurrentPassword != null && storedCurrentPassword.equals(currentPassword)) {
                    // If current password matches, update the password
                    databaseReference.child(userID).child("password").setValue(newPassword)
                            .addOnCompleteListener(updateTask -> {
                                if (updateTask.isSuccessful()) {
                                    // Show success message
                                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Failed to update password
                                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // Current password does not match
                    Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Failed to get the current password from the database
                Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }
}
