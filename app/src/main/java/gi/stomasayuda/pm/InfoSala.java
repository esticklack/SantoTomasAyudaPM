package gi.stomasayuda.pm;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoSala extends AppCompatActivity {

    private static final String TAG = "InfoSala";

    Spinner spinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button btnReserva;
    TextView txtEstado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_sala);

        spinner = findViewById(R.id.SpnHorarios102);

        txtEstado = findViewById(R.id.lblDispA);

        btnReserva = findViewById(R.id.btnRealizarReserva102);

        // Crear una lista con los valores desde las 08:00 hasta las 19:00
        List<String> horarios = new ArrayList<>();
        for (int i = 8; i <= 19; i++) {
            horarios.add(String.format("%02d:00", i));
        }

        // Crear un ArrayAdapter usando la lista de horarios
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        // BASE DE DATOS
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String horarioSeleccionado = (String) parent.getItemAtPosition(position);
                CollectionReference salasRef = db.collection("salas");
                Query query = salasRef.whereEqualTo("nombre", "Sala 102");

                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Boolean> horarios1 = (Map<String, Boolean>) document.get("horarios");

                            Boolean estaDisponible = horarios1.get(horarioSeleccionado);

                            if (estaDisponible) {
                                txtEstado.setText("Disponible");
                                txtEstado.setTextColor(Color.GREEN);
                            } else {
                                txtEstado.setText("No disponible");
                                txtEstado.setTextColor(Color.RED);
                            }
                        }
                    } else {
                        Log.d(TAG, "Ha fallado: ", task.getException());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });


        btnReserva.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(InfoSala.this);
            builder.setMessage("¿Estás seguro de que quieres reservar este horario?")
                    .setTitle("Confirmar reserva");

            builder.setPositiveButton("Sí", (dialog, id) -> {
                String horarioSeleccionado = spinner.getSelectedItem().toString();
                CollectionReference salasRef = db.collection("salas");
                Query query = salasRef.whereEqualTo("nombre", "Sala 102");
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> updates = new HashMap<>();
                            updates.put("horarios." + horarioSeleccionado, false);
                            document.getReference().update(updates).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
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
            });
            builder.setNegativeButton("No", (dialog, id) -> {
                //Aqui no se hace nada ya que el usuario niega
            });

            // Aqui se crea y se muestra el alertdialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });


    }
}