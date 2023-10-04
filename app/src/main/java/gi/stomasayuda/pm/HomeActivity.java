package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
<<<<<<< Updated upstream
=======
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
>>>>>>> Stashed changes

public class HomeActivity extends AppCompatActivity {

    Button btnBuscarAula;
    Button btnAgendarSala;
<<<<<<< Updated upstream
=======
    ImageView btnConfiguration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      
        btnBuscarAula = findViewById(R.id.btnBuscarAula);
<<<<<<< Updated upstream
=======
        btnAgendarSala = findViewById(R.id.btnAgendarSala);
        btnConfiguration = findViewById(R.id.imgConfiguration);

        btnConfiguration.setOnClickListener(view -> irAConfiguracion());

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
                            Log.d("TAG", "Error obteniendo la presentación.: ", task.getException());
                        }
                    });
        }
>>>>>>> Stashed changes

        btnBuscarAula.setOnClickListener(view -> {
            irABuscarAula();
        });
    }

    public void irABuscarAula(){
        Intent I = new Intent(HomeActivity.this, AuthActivity.class);
        startActivity(I);

    }

    public void irAgendarSala(){
<<<<<<< Updated upstream
        Intent I = new Intent(HomeActivity.this, AuthActivity.class);
=======
        Intent I = new Intent(HomeActivity.this, SalasDeEstudiosActivity.class);
>>>>>>> Stashed changes
        startActivity(I);
    }
<<<<<<< Updated upstream
=======

    public void onBackPressed() {
        finish();
        System.exit(0);
    }

    public void irAConfiguracion(){
        Intent I = new Intent(HomeActivity.this, ConfigActivity.class);
        startActivity(I);
    }

>>>>>>> Stashed changes
}