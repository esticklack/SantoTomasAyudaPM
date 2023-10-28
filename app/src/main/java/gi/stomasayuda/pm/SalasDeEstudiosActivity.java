package gi.stomasayuda.pm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class SalasDeEstudiosActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView txtPrueba;

    ListView listaDeSalas;

    Calendar obtenerHora = Calendar.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_de_estudios);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        listaDeSalas = findViewById(R.id.ListaSalas);

        int hora = obtenerHora.get(Calendar.HOUR_OF_DAY);
        int minuto = obtenerHora.get(Calendar.MINUTE);
        String horaFormateada = String.format("%02d", hora);
        String minutoFormateado = String.format("%02d", minuto);

        txtPrueba = findViewById(R.id.txtPrueba2);
        txtPrueba.setText("Hora Actual: " + horaFormateada + ":" + minutoFormateado);

        ArrayList<String> listaSalas = new ArrayList<>();

        db.collection("salas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Agrega el nombre de la sala al ArrayList
                            listaSalas.add(document.getString("nombre"));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_layout, R.id.textItem, listaSalas);
                        listaDeSalas.setAdapter(adapter);
                    } else {


                    }
                });

        listaDeSalas.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getBaseContext(), InfoSala.class);
            startActivity(intent);
        });



    }
}