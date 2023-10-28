package gi.stomasayuda.pm;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgregarSalasActivity extends AppCompatActivity {

    private static final String TAG = "AgregarSalasActivity";
    FirebaseFirestore db;

    TextView btnAgregar;
    TextView btnEliminar;

    TextView btnMantenimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_salas);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(view -> agregarSala());

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(view -> eliminarSala());

        btnMantenimiento = findViewById(R.id.btnMantinimiento);
        btnMantenimiento.setOnClickListener(view -> mantenimientoSala());
    }



    private void agregarSala(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar sala");

        // Configurar los campos de entrada
        final EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre de la sala");
        final EditText inputCapacidad = new EditText(this);
        inputCapacidad.setHint("Capacidad de la sala");
        inputCapacidad.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputNombre);
        layout.addView(inputCapacidad);
        builder.setView(layout);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nombre = inputNombre.getText().toString();
            String capacidad = inputCapacidad.getText().toString();

            // Crear el mapa de horarios con todos los valores en true
            Map<String, Boolean> horarios = new HashMap<>();
            for (int i = 8; i <= 19; i++) {
                String hora = String.format("%02d:00", i); // Asegurarse de que la hora tenga el formato "00:00"
                horarios.put(hora, true);
            }

            Map<String, Object> sala = new HashMap<>();
            sala.put("nombre", nombre);
            sala.put("capacidad", capacidad);
            sala.put("horarios", horarios);

            db = FirebaseFirestore.getInstance();
            db.collection("salas").add(sala)
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "Documento añadido con ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error añadiendo documento", e));

            Toast.makeText(getApplicationContext(), "Documento agregado correctamente", Toast.LENGTH_SHORT).show();

        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void eliminarSala(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar sala");

        final List<String> nombresSalas = new ArrayList<>();
        final List<DocumentSnapshot> documentosSalas = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("salas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nombresSalas.add(document.getString("nombre"));
                            documentosSalas.add(document);
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarSalasActivity.this,
                                android.R.layout.simple_selectable_list_item, nombresSalas);
                        builder.setAdapter(adapter, (dialog, which) -> {
                            // Confirmar la eliminación
                            new AlertDialog.Builder(AgregarSalasActivity.this)
                                    .setTitle("Confirmar eliminación")
                                    .setMessage("¿Estás seguro de que quieres eliminar la sala " + nombresSalas.get(which) + "?")
                                    .setPositiveButton("Eliminar", (dialog2, which2) -> {
                                        documentosSalas.get(which).getReference().delete()
                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Documento eliminado con éxito"))
                                                .addOnFailureListener(e -> Log.w(TAG, "Error eliminando documento", e));
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        });

                        builder.show();
                    } else {
                        Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                    }
                });

    }

    private void mantenimientoSala(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mantenimiento de sala");

        final List<String> nombresSalas = new ArrayList<>();
        final List<DocumentSnapshot> documentosSalas = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("salas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            nombresSalas.add(document.getString("nombre"));
                            documentosSalas.add(document);
                        }

                        final ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarSalasActivity.this,
                                android.R.layout.simple_selectable_list_item, nombresSalas);
                        builder.setAdapter(adapter, (dialog, which) -> {
                            new AlertDialog.Builder(AgregarSalasActivity.this)
                                    .setTitle("Selecciona una opción")
                                    .setSingleChoiceItems(new String[]{"Dar mantenimiento", "Quitar mantenimiento"}, -1, null)
                                    .setPositiveButton("Confirmar", (dialog2, which2) -> {
                                        int selectedOption = ((AlertDialog) dialog2).getListView().getCheckedItemPosition();
                                        DocumentReference salaRef = documentosSalas.get(which).getReference();
                                        salaRef.update("mantenimiento", selectedOption == 0)
                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Documento actualizado con éxito"))
                                                .addOnFailureListener(e -> Log.w(TAG, "Error actualizando documento", e));
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        });

                        builder.show();
                    } else {
                        Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                    }
                });
    }
}