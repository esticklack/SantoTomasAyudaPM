package gi.stomasayuda.pm;

public class Reserva {
    private String hora;
    private String sala;
    private String correo;

    public Reserva() {}
    public Reserva(String hora, String sala, String correo) {
        this.hora = hora;
        this.sala = sala;
        this.correo = correo;
    }

    public String getHora() {
        return hora;
    }

    public String getSala() {
        return sala;
    }

    public String getCorreo() {
        return correo;
    }

}
