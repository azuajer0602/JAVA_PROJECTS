package Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javafx.scene.image.Image;

public class Cliente {

    private String Nombre, Apellido, Cedula, Direccion, Usuario, Contrasenna;
    private Integer Edad;
    private Character Sexo;
    private LocalDate Fnacimiento, fCreacion, UltimInteresFecha;
    private Double MontoC, UltiInteres;
    private String Perfil;
    DateTimeFormatter formattercito = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Cliente(String Nombre, String Apellido, String Cedula, String Direccion, String Usuario, String Contrasenna, LocalDate Fnacimiento, Integer Edad, Character Sexo, LocalDate fCreacion, Double MontoC, String Perfil, LocalDate UltimInteresFecha, Double UltiInteres) {
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Cedula = Cedula;
        this.Direccion = Direccion;
        this.Usuario = Usuario;
        this.Contrasenna = Contrasenna;
        this.Fnacimiento = Fnacimiento;
        this.Edad = Edad;
        this.Sexo = Sexo;
        this.fCreacion = fCreacion;
        this.MontoC = MontoC;
        this.Perfil = Perfil;
        this.UltimInteresFecha = UltimInteresFecha;
        this.UltiInteres = UltiInteres;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getContrasenna() {
        return Contrasenna;
    }

    public void setContrasenna(String Contrasenna) {
        this.Contrasenna = Contrasenna;
    }

    public Integer getEdad() {
        return Edad;
    }

    public void setEdad(Integer Edad) {
        this.Edad = Edad;
    }

    public Character getSexo() {
        return Sexo;
    }

    public void setSexo(Character Sexo) {
        this.Sexo = Sexo;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String Cedula) {
        this.Cedula = Cedula;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public LocalDate getFnacimiento() {
        return Fnacimiento;
    }

    public void setFnacimiento(LocalDate Fnacimiento) {
        this.Fnacimiento = Fnacimiento;
    }

    public LocalDate getFCreacion() {
        return fCreacion;
    }

    public void setFCreacion(LocalDate fCreacion) {
        this.fCreacion = fCreacion;
    }

    public Double getMontoC() {
        return MontoC;
    }

    public void setMontoC(Double MontoC) {
        this.MontoC = MontoC;
    }

    public String getPerfil() {
        return Perfil;
    }

    public void setPerfil(String Perfil) {
        this.Perfil = Perfil;
    }

    public LocalDate getfCreacion() {
        return fCreacion;
    }

    public LocalDate getUltimInteresFecha() {
        return UltimInteresFecha;
    }

    public Double getUltimInteres() {
        return UltiInteres;
    }

    public void setfCreacion(LocalDate fCreacion) {
        this.fCreacion = fCreacion;
    }

    public void setUltimInteresFecha(LocalDate UltimInteresFecha) {
        this.UltimInteresFecha = UltimInteresFecha;
    }

    public void setUltimInteres(Double UltimInteres) {
        this.UltiInteres = UltimInteres;
    }

    public String getFechitas() {

        return String.valueOf(fCreacion.format(formattercito));

    }
    
    public String getMontico(){
        
        
        return String.format("%.2f", MontoC);
    }

    public void generateInterest() {
        if (canGenerateInterest()) {
            double interesPorcentaje = getPorcentajeInteres(MontoC);
            double montoInteres = MontoC * interesPorcentaje / 100;
            MontoC += montoInteres;
            UltimInteresFecha = LocalDate.now();
            UltiInteres = montoInteres;
        }
    }

    public boolean canGenerateInterest() {
        long thirtyDaysInMillis = 30 * 24 * 60 * 60 * 1000;
        long timeSinceLastInterest = ChronoUnit.DAYS.between(UltimInteresFecha, LocalDate.now());
        return timeSinceLastInterest >= 30;
    }

    public double getPorcentajeInteres(double balance) {
        if (balance >= 1000) {
            return 4.5;
        } else if (balance >= 100) {
            return 3.5;
        } else if (balance >= 10) {
            return 3;
        } else {
            return 0;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(Cedula);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cliente other = (Cliente) obj;
        return Objects.equals(Cedula, other.Cedula);
    }

    public static Cliente fromCSV(String csv) {
        String[] parts = csv.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Cliente(
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                LocalDate.parse(parts[6], formatter),
                Integer.parseInt(parts[7]),
                parts[8].charAt(0),
                LocalDate.parse(parts[9], formatter),
                Double.parseDouble(parts[10]),
                parts[11],
                LocalDate.parse(parts[12], formatter),
                Double.parseDouble(parts[13])
        );
    }

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.join(",",
                Nombre,
                Apellido,
                Cedula,
                Direccion,
                Usuario,
                Contrasenna,
                Fnacimiento.format(formatter),
                Edad.toString(),
                Sexo.toString(),
                fCreacion.format(formatter),
                MontoC.toString(),
                Perfil,
                UltimInteresFecha.format(formatter),
                UltiInteres.toString()
        );
    }
}
