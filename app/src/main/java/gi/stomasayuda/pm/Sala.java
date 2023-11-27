package gi.stomasayuda.pm;

public class Sala {
    private String nombre;
    private String capacidad;
    private Boolean mantenimiento;
    private String ubicacion;

    public Sala(String nombre, String capacidad, Boolean mantenimiento, String ubicacion) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.mantenimiento = mantenimiento;
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public Boolean getMantenimiento() {
        return mantenimiento;
    }

    public String getUbicacion() {
        return ubicacion;
    }
}
