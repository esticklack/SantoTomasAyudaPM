package gi.stomasayuda.pm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AuthActivity extends AppCompatActivity {

    EditText EditTextCorreo;
    EditText EditTextPassword;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    Button btnIniciarSesion;

    TextView btnRegistrarse;


    int intentos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio_sesion);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        EditTextCorreo = findViewById(R.id.correoInicioSesion);
        EditTextCorreo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                EditTextCorreo.setHintTextColor(Color.TRANSPARENT);
                EditTextCorreo.setTextColor(Color.BLACK);
            } else {
                EditTextCorreo.setHintTextColor(Color.BLACK);
            }
        });

        EditTextPassword = findViewById(R.id.passwordInicioSesion);
        EditTextPassword.setOnFocusChangeListener((v, hasFocus) ->{
           if (hasFocus) {
               EditTextPassword.setHintTextColor(Color.TRANSPARENT);
               EditTextPassword.setTextColor(Color.BLACK);
           } else {
               EditTextPassword.setHintTextColor(Color.BLACK);
           }
        });

        btnIniciarSesion = findViewById(R.id.boton_inicio_sesion);
        btnIniciarSesion.setOnClickListener(view -> {
            String correo = EditTextCorreo.getText().toString();
            String password = EditTextPassword.getText().toString();
            if (correo.isEmpty() || password.isEmpty()){
                Toast.makeText(getBaseContext(), "Debe ingresar correo y contraseña", Toast.LENGTH_SHORT).show();
            } else {

                mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    irAInterfaz();
                        } else {
                    intentos++;
                    if (intentos >= 5) {
                        btnIniciarSesion.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Ha excedido el número máximo de intentos", Toast.LENGTH_SHORT).show();
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            btnIniciarSesion.setEnabled(true);
                            intentos = 0;
                             }, 5000); // Estos serian en milisegundos
                        } else
                        Toast.makeText(getApplicationContext(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnRegistrarse = findViewById(R.id.RegistrarseButton);
        btnRegistrarse.setOnClickListener(view -> {
            Intent registrarse = new Intent(AuthActivity.this, RegistrarseActivity.class);
            startActivity(registrarse);
        });


    } //Fin oncreate

    private void irAInterfaz() {
        String correo = EditTextCorreo.getText().toString();

        CollectionReference usuariosRef = db.collection("usuarios");

        usuariosRef.whereEqualTo("correo", correo).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                        }
                    } else {
                        Log.d("TAG", "Error obteniendo documentos: ", task.getException());
                    }
                });

        Intent i = new Intent(AuthActivity.this, HomeActivity.class);
        i.putExtra("correo", EditTextCorreo.getText().toString());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //No deja al usuario volver hacia atrás
    }

    public void onStart() {
        super.onStart();
        // Si el usuario accedió con anterioridad, no sale hasta que cierre sesión.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            irAInterfaz();
        }
    }
}