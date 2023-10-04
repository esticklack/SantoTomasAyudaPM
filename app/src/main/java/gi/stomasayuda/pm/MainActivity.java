package gi.stomasayuda.pm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button BtnIniciarSesionBase;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnIniciarSesionBase = findViewById(R.id.boton_inicio_sesion_base);
        BtnIniciarSesionBase.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
        });



    }

    private void irAInterfaz() {
        Intent i = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(i);
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