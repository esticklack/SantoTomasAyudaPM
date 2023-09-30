package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnRegistrarse;
    EditText etCorreo;
    EditText etPassword;
    EditText etNombre;
    EditText etApellido;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        etCorreo = findViewById(R.id.etCorreoRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);


        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(view -> {

            String correo = etCorreo.getText().toString();
            String nombre = etNombre.getText().toString();
            String apellido = etApellido.getText().toString();
            String password = etPassword.getText().toString();

            mAuth.createUserWithEmailAndPassword(correo, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Map<String, Object> usuario = new HashMap<>();
                            usuario.put("nombre", nombre);
                            usuario.put("apellido", apellido);
                            usuario.put("correo", correo);
                            usuario.put("reservas", "");

                            db.collection("usuarios")
                                    .add(usuario)
                                    .addOnSuccessListener(documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId()))
                                    .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));

                            IrAHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Correo ya registrado",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void IrAHome(){
        Toast.makeText(this, "Registro exitoso.",
                Toast.LENGTH_SHORT).show();
        Intent irAHome = new Intent(RegistrarseActivity.this, AuthActivity.class);
        startActivity(irAHome);
    }
}