package gi.stomasayuda.pm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;


public class HomeActivity extends AppCompatActivity {
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(getBaseContext(), "Ahora recibiras notificaciones", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "No podrás recibir notificaciones", Toast.LENGTH_SHORT).show();
                }
            });
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    Button btnBuscarAula;
    Button btnAgendarSala;

    Button btnCancelarReserva;
    ImageView btnConfiguration;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static final int RC_NOTIFICATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        askNotificationPermission();

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