package gi.stomasayuda.pm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SalaAdapter extends ArrayAdapter <Sala> {

    public SalaAdapter(Context context, List<Sala> salas) {
        super(context, 0, salas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obtener el objeto reserva de la posición actual
        Sala sala = getItem(position);

        //Inflar el layout personalizado si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sala_item, parent, false);
        }

        //Obtener las referencias a los TextViews del layout
        TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);


        //Asignar los valores de los campos de la reserva a los TextViews
        tvNombre.setText(sala.getNombre());

        //Devolver la vista inflada
        return convertView;
    }
}
