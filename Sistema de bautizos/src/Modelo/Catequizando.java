package Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Clase que representa a un catequizando.
 */
public class Catequizando {
    private String nombres, apellidos, padrinos, obispo, padres;
    private Integer folio, libro;
    private LocalDate fechasacramento;
    private Character sexo;

    public Catequizando(String nombres, String apellidos, String padrinos, String obispo, String padres, Integer libro, Integer folio, Character sexo, LocalDate fechasacramento) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.padrinos = padrinos;
        this.obispo = obispo;
        this.padres = padres;
        this.folio = folio;
        this.libro = libro;
        this.fechasacramento = fechasacramento;
        this.sexo = sexo;
    }

    public Catequizando() {
        // Constructor por defecto
    }

    // Getters y Setters

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

    public String getPadrinos() {
        return padrinos;
    }

    public void setPadrinos(String padrinos) {
        this.padrinos = padrinos;
    }

    public String getObispo() {
        return obispo;
    }

    public void setObispo(String obispo) {
        this.obispo = obispo;
    }

    public String getPadres() {
        return padres;
    }

    public void setPadres(String padres) {
        this.padres = padres;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getLibro() {
        return libro;
    }

    public void setLibro(Integer libro) {
        this.libro = libro;
    }

    public LocalDate getFechasacramento() {
        return fechasacramento;
    }

    public void setFechasacramento(LocalDate fechasacramento) {
        this.fechasacramento = fechasacramento;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombres, apellidos, padrinos, obispo, padres, folio, libro, fechasacramento, sexo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Catequizando other = (Catequizando) obj;
        return Objects.equals(nombres, other.nombres) &&
               Objects.equals(apellidos, other.apellidos) &&
               Objects.equals(padrinos, other.padrinos) &&
               Objects.equals(obispo, other.obispo) &&
               Objects.equals(padres, other.padres) &&
               Objects.equals(folio, other.folio) &&
               Objects.equals(libro, other.libro) &&
               Objects.equals(fechasacramento, other.fechasacramento) &&
               Objects.equals(sexo, other.sexo);
    }

 public String toCSV() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    return String.join(",",
        nombres != null ? nombres : "",
        apellidos != null ? apellidos : "",
        padrinos != null ? padrinos : "",
        obispo != null ? obispo : "",
        padres != null ? padres : "",
        libro != null ? libro.toString() : "",
        folio != null ? folio.toString() : "",
        sexo != null ? sexo.toString() : "",
        fechasacramento != null ? fechasacramento.format(formatter) : ""
    );
}

public static Catequizando fromCSV(String csv) {
    try {
        String[] parts = csv.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        String nombres = parts.length > 0 && !parts[0].isEmpty() ? parts[0] : null;
        String apellidos = parts.length > 1 && !parts[1].isEmpty() ? parts[1] : null;
        String padrinos = parts.length > 2 && !parts[2].isEmpty() ? parts[2] : null;
        String padres = parts.length > 3 && !parts[3].isEmpty() ? parts[3] : null;
        String obispo = parts.length > 4 && !parts[4].isEmpty() ? parts[4] : null;
        Integer libro = parts.length > 5 && !parts[5].isEmpty() ? Integer.valueOf(parts[5]) : null;
        Integer folio = parts.length > 6 && !parts[6].isEmpty() ? Integer.parseInt(parts[6]) : null;
        Character sexo = parts.length > 7 && !parts[7].isEmpty() ? parts[7].charAt(0) : null;
        LocalDate fechasacramento = parts.length > 8 && !parts[8].isEmpty() ? LocalDate.parse(parts[8], formatter) : null;

        return new Catequizando(nombres, apellidos, padrinos, padres, obispo, libro, folio, sexo, fechasacramento);
    } catch (NumberFormatException | DateTimeParseException e) {
        e.printStackTrace(); // Considera manejar los errores de forma más robusta
    }
    return null;
}
}
