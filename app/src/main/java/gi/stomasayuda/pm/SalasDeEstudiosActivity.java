package gi.stomasayuda.pm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;

public class SalasDeEstudiosActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView txtPrueba;

    Calendar obtenerHora = Calendar.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_de_estudios);

        mAuth = FirebaseAuth.getInstance();

        int hora = obtenerHora.get(Calendar.HOUR_OF_DAY);
        int minuto = obtenerHora.get(Calendar.MINUTE);
        String horaFormateada = String.format("%02d", hora);
        String minutoFormateado = String.format("%02d", minuto);

        txtPrueba = findViewById(R.id.txtPrueba2);
        txtPrueba.setText("Hora Actual: " + horaFormateada + ":" + minutoFormateado);



    }
}