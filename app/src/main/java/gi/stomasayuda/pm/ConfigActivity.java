package gi.stomasayuda.pm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.UUID;

public class ConfigActivity extends AppCompatActivity {

    private static final String TAG = "ConfigActivity";
    TextView txtAdmin;
    Button btnLogout;

    Button btnAdministrar;
    FirebaseFirestore db;
    FirebaseUser user;

    private FirebaseStorage fs;
    private ImageView imgProfile;
    private Uri ImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ActivityResultLauncher<Intent> photoRequest = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result!=null) {
                        // Obtener el intent con la foto seleccionada
                        ImagePath = result.getData().getData();
                        // Hacer algo con la foto
                        getImageInTheImageView();
                    }
                });

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        fs = FirebaseStorage.getInstance();

        btnLogout = findViewById(R.id.btnLogout);
        txtAdmin = findViewById(R.id.txtAdmin);
        btnAdministrar = findViewById(R.id.btnAdministrarSalas);
        imgProfile = findViewById(R.id.imgProfile);

        btnAdministrar.setOnClickListener(view -> administrarSalas());

        btnLogout.setOnClickListener(view -> cerrarSesion());

        imgProfile.setOnClickListener(view -> {
            Intent photoIntent = new Intent(Intent.ACTION_PICK);
            photoIntent.setType("image/*");
            photoRequest.launch(photoIntent);
        });




        DocumentReference docRef = db.collection("usuarios").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String foto = documentSnapshot.getString("foto");
                    Glide.with(ConfigActivity.this)
                            .load(foto)
                            .into(imgProfile);
                } else {
                    // El documento no existe o no tiene datos
                    // Manejar el error
                }
            }
        }).addOnFailureListener(e -> {
            // No se pudo obtener el documento del usuario
            // Manejar el error
        });



        if (user != null) {
            String email = user.getEmail();

            CollectionReference usuariosRef = db.collection("usuarios");

            usuariosRef.whereEqualTo("correo", email).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Boolean isAdmin = document.getBoolean("admin");
                                if (isAdmin != null && isAdmin) {
                                    // Si el usuario es administrador, hacemos visible el botón
                                    btnAdministrar.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.d(TAG, "Error obteniendo documentos: ", task.getException());
                        }
                    });


            //Verifica si el usuario es administrador
            db.collection("usuarios")
                    .whereEqualTo("correo", email)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Boolean admin = document.getBoolean("admin");

                                if (admin != null && admin.equals(true)) {
                                    txtAdmin.setText("Usted es profesor");
                                } else {
                                    txtAdmin.setText("Usted es usuario");
                                }
                            }
                        } else {
                            Log.d("TAG", "Error obteniendo la presentación.: ", task.getException());
                        }
                    });
        }


    }

    private void uploadImage(){
        Toast.makeText(this,"Subiendo imagen...",
                Toast.LENGTH_SHORT).show();
        fs.getReference("images/" + UUID.randomUUID().toString()).putFile(ImagePath).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        updateProfilePicture(task1.getResult().toString());
                    }
            });

            Toast.makeText(this,"Imagen subida correctamente!",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Subido correctamente!");
            }else {
                Toast.makeText(this,"Error!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfilePicture(String url) {
        String uid = user.getUid();
        db.collection("usuarios")
                .document(uid)
                .update("foto", url);

    }

    private void getImageInTheImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ImagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imgProfile.setImageBitmap(bitmap);
        uploadImage();
    }

    private void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent I = new Intent(ConfigActivity.this, MainActivity.class);
        startActivity(I);
    }

    private void administrarSalas(){
        Intent I = new Intent(ConfigActivity.this, AgregarSalasActivity.class);
        startActivity(I);
    }

}