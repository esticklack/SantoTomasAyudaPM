package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ConfigActivity extends AppCompatActivity {

    TextView txtAdmin;
    Button btnLogout;
    FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnLogout = findViewById(R.id.btnLogout);
        txtAdmin = findViewById(R.id.txtAdmin);

        btnLogout.setOnClickListener(view -> cerrarSesion());

        if (user != null) {
            String email = user.getEmail();

            db.collection("usuarios")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String admin = document.getString("admin");

                                if (admin != null && admin.equals("si")) {
                                    txtAdmin.setText("Usted es profesor");
                                } else {
                                    txtAdmin.setText("Usted es usuario");
                                }
                            }
                        } else {
                            Log.d("TAG", "Error obteniendo la presentación.: ", task.getException());
                        }
                    });
        }


    }

    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent I = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(I);
    }
}