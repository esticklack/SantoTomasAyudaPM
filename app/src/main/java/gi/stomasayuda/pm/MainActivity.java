package gi.stomasayuda.pm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button BtnIniciarSesionBase;

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
}