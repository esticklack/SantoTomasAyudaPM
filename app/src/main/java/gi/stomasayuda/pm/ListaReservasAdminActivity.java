package gi.stomasayuda.pm;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaReservasAdminActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    //Lista de reservas que se usará como fuente de datos para el ListView
    private List<Reserva> listaReservas;

    //Referencia al ListView de la activity
    private ListView lvReservas;

    //Adaptador personalizado para el ListView
    private ReservaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas_admin);

        db = FirebaseFirestore.getInstance();

        //Inicializar la lista de reservas
        listaReservas = new ArrayList<>();

        //Obtener la referencia al ListView
        lvReservas = (ListView) findViewById(R.id.listaReservasGeneral);

        //Crear el adaptador con la lista de reservas
        adapter = new ReservaAdapter(ListaReservasAdminActivity.this, listaReservas);

        //Asignar el adaptador al ListView
        lvReservas.setAdapter(adapter);

        //Obtener todos los documentos de la colección de reservas
        db.collection("reservas").get().addOnCompleteListener(task -> {
            //Verificar si la tarea fue exitosa
            if (task.isSuccessful()) {
                //Iterar sobre los documentos obtenidos
                for (QueryDocumentSnapshot document : task.getResult()) {
                    //Obtener los campos de cada documento
                    String hora = document.getString("hora");
                    String sala = document.getString("sala");
                    String correo = document.getString("correo");

                    //Crear un objeto reserva con los valores de los campos
                    Reserva reserva = new Reserva(hora, sala, correo);

                    //Añadir el objeto reserva a la lista de reservas
                    listaReservas.add(reserva);
                }

                //Notificar al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            } else {
                //Mostrar un mensaje de error
                Toast.makeText(ListaReservasAdminActivity.this, "Error al obtener las reservas: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}