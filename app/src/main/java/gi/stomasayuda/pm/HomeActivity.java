package gi.stomasayuda.pm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


public class HomeActivity extends AppCompatActivity {

    Button btnBuscarAula;
    Button btnAgendarSala;

    Button btnCancelarReserva;
    ImageView btnConfiguration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnBuscarAula = findViewById(R.id.btnBuscarAula);
        btnAgendarSala = findViewById(R.id.btnAgendarSala);
        btnConfiguration = findViewById(R.id.imgConfiguration);
        btnCancelarReserva = findViewById(R.id.btnCancelarReserva);
        btnConfiguration.setOnClickListener(view -> irAConfiguracion());
        btnAgendarSala = findViewById(R.id.btnAgendarSala);

        btnBuscarAula.setOnClickListener(view -> irABuscarAula());

        btnAgendarSala.setOnClickListener(view -> irAgendarSala());

        btnCancelarReserva.setOnClickListener(view -> irACancelarReserva());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    String correo = user.getEmail();
                    CollectionReference usuariosRef = db.collection("usuarios");
                    Query query = usuariosRef.whereEqualTo("correo", correo);
                    query.get().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            for (QueryDocumentSnapshot document : task1.getResult()){
                                document.getReference().update("token", token).addOnCompleteListener(task2 -> Log.w("TAG", "Token obtenido correctamente", task2.getException()));
                            }
                        } else {
                            Log.w("TAG", "Error actualizando el documento");
                        }
                    });
                });

        if (user != null) {
            String email = user.getEmail();

            // Detecta el nombre del usuario

            db.collection("usuarios")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombre = document.getString("nombre");
                                TextView textViewNombre = findViewById(R.id.BienvenidaNombreApellido);


                                if (nombre.toLowerCase().endsWith("o")) {
                                    textViewNombre.setText("Bienvenido " + nombre);
                                } else if (nombre.toLowerCase().endsWith("a")) {
                                    textViewNombre.setText("Bienvenida " + nombre);
                                } else {
                                    // Por defecto, si no termina en "o" ni en "a", simplemente decimos "Bienvenido/a" seguido del nombre.
                                    textViewNombre.setText("Bienvenido/a " + nombre);
                                }

                            }
                        } else {

                            Log.d("TAG", "Error obteniendo el documento.: ", task.getException());
                        }
                    });
        }

    }

    public void irABuscarAula() {
        Intent I = new Intent(HomeActivity.this, SeleccionEdificio.class);
        startActivity(I);

    }

    public void irAgendarSala() {
        Intent I = new Intent(HomeActivity.this, SalasDeEstudiosActivity.class);
        startActivity(I);
    }

    public void irAConfiguracion() {
        Intent I = new Intent(HomeActivity.this, ConfigActivity.class);
        startActivity(I);
    }

    private void irACancelarReserva(){
        Intent I = new Intent(HomeActivity.this, ListaReservasPersonalActivity.class);
        startActivity(I);
    }

    public void onBackPressed() {
        finishAffinity();
    }

}