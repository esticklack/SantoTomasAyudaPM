package gi.stomasayuda.pm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AgendarSalaEstudio extends AppCompatActivity {

    Spinner comboDias;

    Spinner comboHoras;

    Spinner comboPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_sala_estudio);


        comboDias = (Spinner) findViewById(R.id.spinnerDias);
        comboHoras = (Spinner) findViewById(R.id.spinnerHoras);
        //comboPersonas = (Spinner) findViewById(R.id.spinnerPersonas);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.combo_dias, android.R.layout.simple_spinner_item);

        comboDias.setAdapter(adapter);



        comboDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Toast.makeText(parent.getContext(), "Seleccionado: " + parent.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> horasAdapter = ArrayAdapter.createFromResource(this,
                R.array.combo_horas, android.R.layout.simple_spinner_item);

        comboHoras.setAdapter(horasAdapter);


        //ArrayAdapter<CharSequence> PersonasAdapter = ArrayAdapter.createFromResource(this,
         //       R.array.combo_personas, android.R.layout.simple_spinner_item);

        //comboPersonas.setAdapter(PersonasAdapter);







    }
}