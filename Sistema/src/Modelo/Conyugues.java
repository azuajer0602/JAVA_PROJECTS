/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author Programación Reimil
 */
public class Conyugues {
    private Integer libro,folio,edadesposa,edadesposo;
    private String nombreesposo,nombreesposa,sacerdote,testigos;
    private LocalDate fechamatrimonio;

    public Conyugues(Integer libro, Integer folio, Integer edadesposa, Integer edadesposo, String nombreesposo, String nombreesposa, String sacerdote, String testigos, LocalDate fechamatrimonio) {
        this.libro = libro;
        this.folio = folio;
        this.edadesposa = edadesposa;
        this.edadesposo = edadesposo;
        this.nombreesposo = nombreesposo;
        this.nombreesposa = nombreesposa;
        this.sacerdote = sacerdote;
        this.testigos = testigos;
        this.fechamatrimonio = fechamatrimonio;
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

    public Integer getEdadesposa() {
        return edadesposa;
    }

    public void setEdadesposa(Integer edadesposa) {
        this.edadesposa = edadesposa;
    }

    public Integer getEdadesposo() {
        return edadesposo;
    }

    public void setEdadesposo(Integer edadesposo) {
        this.edadesposo = edadesposo;
    }

    public String getNombreesposo() {
        return nombreesposo;
    }

    public void setNombreesposo(String nombreesposo) {
        this.nombreesposo = nombreesposo;
    }

    public String getNombreesposa() {
        return nombreesposa;
    }

    public void setNombreesposa(String nombreesposa) {
        this.nombreesposa = nombreesposa;
    }

    public String getSacerdote() {
        return sacerdote;
    }

    public void setSacerdote(String sacerdote) {
        this.sacerdote = sacerdote;
    }

    public String getTestigos() {
        return testigos;
    }

    public void setTestigos(String testigos) {
        this.testigos = testigos;
    }

    public LocalDate getFechamatrimonio() {
        return fechamatrimonio;
    }

    public void setFechamatrimonio(LocalDate fechamatrimonio) {
        this.fechamatrimonio = fechamatrimonio;
    }
    @Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Conyugues conyugues = (Conyugues) obj;
    return libro.equals(conyugues.libro) &&
           folio.equals(conyugues.folio) &&
           edadesposa.equals(conyugues.edadesposa) &&
           edadesposo.equals(conyugues.edadesposo) &&
           nombreesposo.equals(conyugues.nombreesposo) &&
           nombreesposa.equals(conyugues.nombreesposa) &&
           sacerdote.equals(conyugues.sacerdote) &&
           testigos.equals(conyugues.testigos) &&
           fechamatrimonio.equals(conyugues.fechamatrimonio);
}
@Override
public int hashCode() {
    return Objects.hash(libro, folio, edadesposa, edadesposo, nombreesposo, nombreesposa, sacerdote, testigos, fechamatrimonio);
}
public String toCSV() {
    return libro + "," +
           folio + "," +
           edadesposa + "," +
           edadesposo + "," +
           nombreesposo + "," +
           nombreesposa + "," +
           sacerdote + "," +
           testigos + "," +
           fechamatrimonio.toString();
}
public static Conyugues fromCSV(String csv) {
    String[] values = csv.split(",");
    Integer libro = Integer.parseInt(values[0]);
    Integer folio = Integer.parseInt(values[1]);
    Integer edadesposa = Integer.parseInt(values[2]);
    Integer edadesposo = Integer.parseInt(values[3]);
    String nombreesposo = values[4];
    String nombreesposa = values[5];
    String sacerdote = values[6];
    String testigos = values[7];
    LocalDate fechamatrimonio = LocalDate.parse(values[8]);
    
    return new Conyugues(libro, folio, edadesposa, edadesposo, nombreesposo, nombreesposa, sacerdote, testigos, fechamatrimonio);
}

}
