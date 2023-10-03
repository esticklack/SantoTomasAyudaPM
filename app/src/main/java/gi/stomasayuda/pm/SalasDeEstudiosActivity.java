package gi.stomasayuda.pm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SalasDeEstudiosActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    TextView txtPrueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salas_de_estudios);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String correoUsuario = user.getEmail();

            db.collection("usuarios")
                    .whereEqualTo("correo", correoUsuario)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String nombreUsuario = document.getString("nombre");
                                txtPrueba = findViewById(R.id.txtPrueba);
                                txtPrueba.setText("Hola" + nombreUsuario);
                            }

                        } else {

                        }
                    });



        }


    }
}