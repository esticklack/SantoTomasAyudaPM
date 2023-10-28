package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = "ConfigActivity";
    TextView txtAdmin;
    Button btnLogout;

    Button btnAdministrar;
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
        btnAdministrar = findViewById(R.id.btnAdministrarSalas);

        btnAdministrar.setOnClickListener(view -> administrarSalas());

        btnLogout.setOnClickListener(view -> cerrarSesion());

        if (user != null) {
            String email = user.getEmail();

            CollectionReference usuariosRef = db.collection("usuarios");

            usuariosRef.whereEqualTo("correo", email).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Boolean isAdmin = document.getBoolean("admin");
                                if (isAdmin != null && isAdmin) {
                                    // Si el usuario es administrador, hacemos visible el botón
                                    btnAdministrar.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                        }
                    });


            //Verifica si el usuario es administrador
            db.collection("usuarios")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Boolean admin = document.getBoolean("admin");

                                if (admin != null && admin.equals(true)) {
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

    private void administrarSalas(){
        Intent I = new Intent(ConfigActivity.this, AgregarSalasActivity.class);
        startActivity(I);
    }
}