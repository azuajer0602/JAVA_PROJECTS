/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 *
 * @author Programación Reimil
 */
public class Catecumeno {
    protected String nombres,apellidos,madre,padre,padrinos,presbitero;
    protected Integer libro,folio;
    protected Character sexo;
    protected LocalDate FechaNacimiento,Fechabautismo;

    public Catecumeno(String nombres, String apellidos, String madre, String padre, String padrinos, String presbitero, Integer libro, Integer folio, Character sexo, LocalDate FechaNacimiento, LocalDate Fechabautismo) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.madre = madre;
        this.padre = padre;
        this.padrinos = padrinos;
        this.presbitero = presbitero;
        this.libro = libro;
        this.folio = folio;
        this.sexo = sexo;
        this.FechaNacimiento = FechaNacimiento;
        this.Fechabautismo = Fechabautismo;
    }
      public Catecumeno() {
        this.nombres = null;
        this.apellidos = null;
        this.madre = null;
        this.padre = null;
        this.padrinos = null;
        this.presbitero = null;
        this.libro = null;
        this.folio = null;
        this.sexo = null;
        this.FechaNacimiento = null;
        this.Fechabautismo = null;
    }
    
    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getMadre() {
        return madre;
    }

    public void setMadre(String madre) {
        this.madre = madre;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getPadrinos() {
        return padrinos;
    }

    public void setPadrinos(String padrinos) {
        this.padrinos = padrinos;
    }

    public String getPresbitero() {
        return presbitero;
    }

    public void setPresbitero(String presbitero) {
        this.presbitero = presbitero;
    }

    public Integer getLibro() {
        return libro;
    }

    public void setLibro(Integer libro) {
        this.libro = libro;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public LocalDate getFechabautismo() {
        return Fechabautismo;
    }

    public void setFechabautismo(LocalDate Fechabautismo) {
        this.Fechabautismo = Fechabautismo;
    }

   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Catecumeno that = (Catecumeno) o;
        return libro == that.libro &&
                folio == that.folio &&
                Objects.equals(nombres, that.nombres) &&
                Objects.equals(apellidos, that.apellidos) &&
                Objects.equals(madre, that.madre) &&
                Objects.equals(padre, that.padre) &&
                Objects.equals(padrinos, that.padrinos) &&
                Objects.equals(presbitero, that.presbitero) &&
                sexo == that.sexo &&
                Objects.equals(FechaNacimiento, that.FechaNacimiento) &&
                Objects.equals(Fechabautismo, that.Fechabautismo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombres, apellidos, madre, padre, padrinos, presbitero, libro, folio, sexo, FechaNacimiento, Fechabautismo);
    }

 public String toCSV() {
       
          DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    return String.join(",",
        nombres,
        apellidos,
        madre,
        padre,
        padrinos,
        presbitero,
        Integer.toString(libro),
        Integer.toString(folio),
        Character.toString(sexo),
        FechaNacimiento.format(formatter),
        Fechabautismo.format(formatter)
    );
    }

   public static Catecumeno fromCSV(String csv) {
     String[] parts = csv.split(",");

    // Verificar la longitud del array
    if (parts.length < 11) {
        throw new IllegalArgumentException("Línea de CSV con datos insuficientes: " + csv);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    return new Catecumeno(
        parts[0],
        parts[1],
        parts[2],
        parts[3],
        parts[4],
        parts[5],
        Integer.valueOf(parts[6]),
        Integer.parseInt(parts[7]),
        parts[8].charAt(0),
        LocalDate.parse(parts[9], formatter),
        LocalDate.parse(parts[10], formatter)
    );
}

    
    
}
