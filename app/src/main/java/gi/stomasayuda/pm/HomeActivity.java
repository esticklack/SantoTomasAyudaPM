package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    Button btnBuscarAula;
    Button btnAgendarSala;

    private TextView mensaje;

    String correo = getIntent().getStringExtra("correo");

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnBuscarAula = findViewById(R.id.btnBuscarAula);
        btnAgendarSala = findViewById(R.id.btnAgendarSala);

        btnBuscarAula.setOnClickListener(view -> {
            irABuscarAula();
        });

        btnAgendarSala.setOnClickListener(view -> {
            irAgendarSala();
        });

        //mensaje = findViewById(R.id.BienvenidaNombreApellido);
        mensaje = (TextView) findViewById(R.id.BienvenidaNombreApellido);




        db.collection("usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String nombre = document.getString("nombre");
                            mensaje.setText("Bienvenido, " + nombre);
                        }
                    } else {
                        // Manejo de errores en caso de que la consulta falle
                        mensaje.setText("Error al obtener datos: " + task.getException().getMessage());
                    }
                });



    }

    public void irABuscarAula(){
        Intent I = new Intent(HomeActivity.this, SeleccionEdificio.class);
        startActivity(I);

    }

    public void irAgendarSala(){
        Intent I = new Intent(HomeActivity.this, AgendarSalaEstudio.class);
        startActivity(I);

    }


}