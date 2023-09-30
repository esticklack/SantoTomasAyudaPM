package gi.stomasayuda.pm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {

    EditText EditTextCorreo;
    EditText EditTextPassword;
    FirebaseAuth mAuth;

    Button btnIniciarSesion;

    TextView btnRegistrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio_sesion);

        mAuth = FirebaseAuth.getInstance();

        EditTextCorreo = findViewById(R.id.correoInicioSesion);
        EditTextCorreo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                EditTextCorreo.setHintTextColor(Color.TRANSPARENT);
            } else {
                EditTextCorreo.setHintTextColor(Color.BLACK);
            }
        });

        EditTextPassword = findViewById(R.id.passwordInicioSesion);
        EditTextPassword.setOnFocusChangeListener((v, hasFocus) ->{
           if (hasFocus) {
               EditTextPassword.setHintTextColor(Color.TRANSPARENT);
           } else {
               EditTextPassword.setHintTextColor(Color.BLACK);
           }
        });

        btnIniciarSesion = findViewById(R.id.boton_inicio_sesion);
        btnIniciarSesion.setOnClickListener(view -> {
            String correo = EditTextCorreo.getText().toString();
            String password = EditTextPassword.getText().toString();

            mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    irAInterfaz();
                } else {

                }
            });

        });

        btnRegistrarse = findViewById(R.id.RegistrarseButton);
        btnRegistrarse.setOnClickListener(view -> {
            Intent registrarse = new Intent(AuthActivity.this, RegistrarseActivity.class);
            startActivity(registrarse);
        });


    } //Fin oncreate

    private void irAInterfaz() {
        Intent i = new Intent(AuthActivity.this, HomeActivity.class);
        i.putExtra("correo", EditTextCorreo.getText().toString());
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //No deja al usuario volver hacia atrás
    }
}