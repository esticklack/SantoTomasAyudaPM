package gi.stomasayuda.pm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class ReservaAdapter extends ArrayAdapter <Reserva> {

    public ReservaAdapter(Context context, List<Reserva> reservas) {
        super(context, 0, reservas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obtener el objeto reserva de la posición actual
        Reserva reserva = getItem(position);

        //Inflar el layout personalizado si es necesario
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reserva_item, parent, false);
        }

        //Obtener las referencias a los TextViews del layout
        TextView tvHora = (TextView) convertView.findViewById(R.id.tvHora);
        TextView tvSala = (TextView) convertView.findViewById(R.id.tvSala);
        TextView tvCorreo = (TextView) convertView.findViewById(R.id.tvCorreo);

        //Asignar los valores de los campos de la reserva a los TextViews
        tvHora.setText(reserva.getHora());
        tvSala.setText(reserva.getSala());
        tvCorreo.setText(reserva.getCorreo());

        //Devolver la vista inflada
        return convertView;
    }

}
