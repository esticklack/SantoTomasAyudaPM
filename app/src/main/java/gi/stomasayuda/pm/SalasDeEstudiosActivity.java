package gi.stomasayuda.pm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalasDeEstudiosActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView txtPrueba;

    private ListView lvSalas;

    private List<Sala> listaSalas;

    private  SalaAdapter adapter;
    Calendar obtenerHora = Calendar.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_de_estudios);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        int hora = obtenerHora.get(Calendar.HOUR_OF_DAY);
        int minuto = obtenerHora.get(Calendar.MINUTE);
        String horaFormateada = String.format("%02d", hora);
        String minutoFormateado = String.format("%02d", minuto);

        txtPrueba = findViewById(R.id.txtPrueba2);
        txtPrueba.setText("Hora Actual: " + horaFormateada + ":" + minutoFormateado);

        // Lista Salas
        listaSalas = new ArrayList<>();
        lvSalas = (ListView) findViewById(R.id.lvSalas);
        adapter = new SalaAdapter(SalasDeEstudiosActivity.this, listaSalas);
        lvSalas.setAdapter(adapter);


        db.collection("salas").get().addOnCompleteListener(task -> {
            //Verificar si la tarea fue exitosa
            if (task.isSuccessful()) {
                //Iterar sobre los documentos obtenidos
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //Obtener los campos de cada documento
                    String nombre = document.getString("nombre");
                    String capacidad = document.getString("capacidad");
                    Boolean mantenimiento = document.getBoolean("mantenimiento");
                    String ubicacion = document.getString("ubicacion");
                    //Crear un objeto reserva con los valores de los campos
                    Sala sala = new Sala(nombre, capacidad, mantenimiento, ubicacion);

                    //Añadir el objeto reserva a la lista de reservas
                    listaSalas.add(sala);
                }
                //Notificar al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            } else {
                //Mostrar un mensaje de error
                Toast.makeText(SalasDeEstudiosActivity.this, "Error al obtener las reservas: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        lvSalas.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener el objeto Reserva que corresponde al elemento seleccionado
            Sala sala = listaSalas.get(position);
            // Obtener los datos de la reserva
            String nombre = sala.getNombre();
            String capacidad = sala.getCapacidad();
            Boolean mantenimiento = sala.getMantenimiento();
            String ubicacion = sala.getUbicacion();

            Intent parametros = new Intent(SalasDeEstudiosActivity.this, InfoSala.class);
            parametros.putExtra("nombre", nombre);
            parametros.putExtra("capacidad", capacidad);
            parametros.putExtra("mantenimiento", mantenimiento);
            parametros.putExtra("ubicacion", ubicacion);
            startActivity(parametros);

        });


    }
}