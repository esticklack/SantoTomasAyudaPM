package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    Button btnBuscarAula;
    Button btnAgendarSala;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnBuscarAula = findViewById(R.id.btnBuscarAula);

        btnBuscarAula.setOnClickListener(view -> {
            irABuscarAula();
        });
    }

    public void irABuscarAula(){
        Intent I = new Intent(HomeActivity.this, AuthActivity.class);
        startActivity(I);

    }

    public void irAgendarSala(){
        Intent I = new Intent(HomeActivity.this, AuthActivity.class);
        startActivity(I);

    }
}