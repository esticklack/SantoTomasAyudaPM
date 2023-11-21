package gi.stomasayuda.pm;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaReservasPersonalActivity extends AppCompatActivity {

    private static final String TAG = "ListaReservasPersonalActivity";
    FirebaseFirestore db;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas_personal);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
// Obtener el email del usuario actual
        String email = user.getEmail();

        List<String> horarios = new ArrayList<>();
        for (int i = 8; i <= 19; i++) {
            horarios.add(String.format("%02d:00", i));
        }

// Crear un ArrayList para almacenar los objetos Reserva
        ArrayList<Reserva> reservas = new ArrayList<>();

// Obtener una referencia al ListView de la actividad
        ListView lvReservas = findViewById(R.id.listaReservasPersonal);

// Crear un ReservaAdapter y asignarlo al ListView
        ReservaAdapter adapter = new ReservaAdapter(ListaReservasPersonalActivity.this, reservas);
        lvReservas.setAdapter(adapter);

// Obtener los datos de la colección reservas y filtrarlos por el email del usuario
        db.collection("reservas").whereEqualTo("correo", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Recorrer cada documento obtenido
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                // Convertir el documento en un objeto Reserva
                Reserva reserva = document.toObject(Reserva.class);
                // Añadir el objeto Reserva al ArrayList
                reservas.add(reserva);
            }
            // Notificar al adapter que los datos han cambiado
            adapter.notifyDataSetChanged();
        });

// Establecer un listener para los elementos del ListView
        lvReservas.setOnItemClickListener((parent, view, position, id) -> {
            // Obtener el objeto Reserva que corresponde al elemento seleccionado
            Reserva reserva = reservas.get(position);
            // Obtener los datos de la reserva
            String hora = reserva.getHora();
            String sala = reserva.getSala();
            String correo = reserva.getCorreo();
            // Crear un AlertDialog para confirmar la cancelación de la reserva
            AlertDialog.Builder builder = new AlertDialog.Builder(ListaReservasPersonalActivity.this);
            builder.setTitle("Cancelar reserva");
            builder.setMessage("¿Estás seguro de que quieres cancelar la reserva de la " + sala + " a las " + hora + "?");
            builder.setPositiveButton("Sí", (dialog, which) -> {
                // Si el usuario confirma la cancelación, eliminar el documento de la colección reservas
                Query query = db.collection("reservas").whereEqualTo("correo", correo).whereEqualTo("sala", sala).whereEqualTo("hora", hora);
// Ejecutar la Query y obtener el primer documento que coincida
                query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    // Verificar que la Query no esté vacía
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Obtener el primer documento que coincida
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        // Obtener el id del documento
                        String id1 = document.getId();
                        // Usar el id para eliminar el documento de la colección reservas
                        db.collection("reservas").document(id1).delete().addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "Se elimino el documento con exito");
                            // Si se elimina el documento con éxito, cambiar el campo hora de la colección salas a true
                            String horarioSeleccionado = hora;
                            CollectionReference salasRef = db.collection("salas");
                            Query query1 = salasRef.whereEqualTo("nombre", sala);
                            query1.get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task.getResult()) {
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("horarios." + horarioSeleccionado, true);
                                        document1.getReference().update(updates).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(this, "Se cancelado la reserva con exito!",
                                                        Toast.LENGTH_SHORT);
                                                Log.d(TAG, "El documento se ha actualizado correctamente!");
                                            } else {
                                                Log.d(TAG, "Erro al actualizar el documento", task1.getException());
                                            }
                                        });
                                    }
                                } else {
                                    Log.d(TAG, "Ha fallado:", task.getException());
                                }
                            });

                            //aqui termina el de salas xd

                        });
                    }
                });
            });
            builder.setNegativeButton("No", null);
            // Mostrar el AlertDialog
            builder.show();
        });
    }
}