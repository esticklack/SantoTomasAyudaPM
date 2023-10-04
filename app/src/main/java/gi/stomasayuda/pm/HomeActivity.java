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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    Button btnBuscarAula;
    Button btnAgendarSala;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnBuscarAula = findViewById(R.id.btnBuscarAula);
        btnAgendarSala = findViewById(R.id.btnAgendarSala);

        btnBuscarAula.setOnClickListener(view -> irABuscarAula());

        btnAgendarSala.setOnClickListener(view -> irAgendarSala());




        if (user != null) {
            String email = user.getEmail();

            db.collection("usuarios")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                TextView textViewNombre = findViewById(R.id.BienvenidaNombreApellido);


                                if (nombre.toLowerCase().endsWith("o")) {
                                    textViewNombre.setText("Bienvenido "+ nombre);
                                } else if (nombre.toLowerCase().endsWith("a")) {
                                    textViewNombre.setText("Bienvenida "+ nombre);
                                } else {
                                    // Por defecto, si no termina en "o" ni en "a", simplemente decimos "Bienvenido/a" seguido del nombre.
                                    textViewNombre.setText("Bienvenido/a "+ nombre);
                                }

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    });
        }

    }

    public void irABuscarAula(){
        Intent I = new Intent(HomeActivity.this, SeleccionEdificio.class);
        startActivity(I);

    }

    public void irAgendarSala(){
        Intent I = new Intent(HomeActivity.this, AgendarSalaEstudio.class);
        startActivity(I);


    }

    public void onBackPressed() {
        finish();
        System.exit(0);
    }


}