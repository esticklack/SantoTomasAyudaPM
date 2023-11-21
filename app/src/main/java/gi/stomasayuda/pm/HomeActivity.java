package gi.stomasayuda.pm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Nullable;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    Button btnBuscarAula;
    Button btnAgendarSala;
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

        btnConfiguration.setOnClickListener(view -> irAConfiguracion());
        btnAgendarSala = findViewById(R.id.btnAgendarSala);

        btnBuscarAula.setOnClickListener(view -> irABuscarAula());

        btnAgendarSala.setOnClickListener(view -> irAgendarSala());


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

    public void onBackPressed() {
        finishAffinity();
    }

}