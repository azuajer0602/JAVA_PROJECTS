package Modelo;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

public class Movimiento {

    private final SimpleObjectProperty<LocalDate> fecha;
    private final SimpleIntegerProperty codigo;
    private final SimpleStringProperty tipot;
    private final SimpleStringProperty tipoO;
    private final SimpleStringProperty descripcion;
    private final SimpleIntegerProperty referencia;
    private final SimpleDoubleProperty debe;
    private final SimpleDoubleProperty haber;
    private final SimpleDoubleProperty saldo;
  DateTimeFormatter formattercito = DateTimeFormatter.ofPattern("dd-MM-yyyy");
  
    // Constructor por defecto
    public Movimiento() {
        this.fecha = new SimpleObjectProperty<>();
        this.codigo = new SimpleIntegerProperty();
        this.tipot = new SimpleStringProperty();
        this.tipoO = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.referencia = new SimpleIntegerProperty();
        this.debe = new SimpleDoubleProperty();
        this.haber = new SimpleDoubleProperty();
        this.saldo = new SimpleDoubleProperty();
    }

    // Constructor con parámetros
    public Movimiento(LocalDate fecha, int codigo, String tipot, String tipoO, String descripcion, int referencia, Double debe, Double haber, Double saldo) {
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.codigo = new SimpleIntegerProperty(codigo);
        this.tipot = new SimpleStringProperty(tipot);
        this.tipoO = new SimpleStringProperty (tipoO);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.referencia = new SimpleIntegerProperty(referencia);
        this.debe = new SimpleDoubleProperty(debe);
        this.haber = new SimpleDoubleProperty(haber);
        this.saldo = new SimpleDoubleProperty(saldo);
    }

    // Getters para las propiedades (para la TableView de JavaFX)

    public SimpleObjectProperty<LocalDate> fechaProperty() {
        return fecha;
    }

    public SimpleIntegerProperty codigoProperty() {
        return codigo;
    }

    public SimpleStringProperty tipotProperty() {
        return tipot;
    }

    public SimpleStringProperty tipoOProperty() {
        return tipoO;
    }

    public SimpleStringProperty descripcionProperty() {
        return descripcion;
    }

    public SimpleIntegerProperty referenciaProperty() {
        return referencia;
    }

    public SimpleDoubleProperty debeProperty() {
        return debe;
    }

    public SimpleDoubleProperty haberProperty() {
        return haber;
    }

    public SimpleDoubleProperty saldoProperty() {
        return saldo;
    }

    // Getters tradicionales (en caso de necesitarlos)
    public LocalDate getFecha() {
        return fecha.get();
    }

    public void setFecha(LocalDate fecha) {
        this.fecha.set(fecha);
    }

    public Integer getCodigo() {
        return codigo.get();
    }

    public void setCodigo(int codigo) {
        this.codigo.set(codigo);
    }

    public String getTipot() {
        return tipot.get();
    }

    public void setTipot(String tipot) {
        this.tipot.set(tipot);
    }

    public String getTipoO() {
        return tipoO.get();
    }

    public void setTipoO(String tipoO) {
        this.tipoO.set(tipoO);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public Integer getReferencia() {
        return referencia.get();
    }

    public void setReferencia(int referencia) {
        this.referencia.set(referencia);
    }

    public Double getDebe() {
        return debe.get();
    }

    public void setDebe(double debe) {
        this.debe.set(debe);
    }

    public Double getHaber() {
        return haber.get();
    }

    public void setHaber(double haber) {
        this.haber.set(haber);
    }

    public Double getSaldo() {
        return saldo.get();
    }

    public void setSaldo(double saldo) {
        this.saldo.set(saldo);
    }
    

    @Override
    public String toString() {
        return "Movimiento{" +
                "fecha=" + fecha.get() +
                ", codigo=" + codigo.get() +
                ", tipot='" + tipot.get() + '\'' +
                ", tipoO='" + tipoO.get() + '\'' +
                ", descripcion='" + descripcion.get() + '\'' +
                ", referencia=" + referencia.get() +
                ", debe=" + debe.get() +
                ", haber=" + haber.get() +
                ", saldo=" + saldo.get() +
                '}';
    }
    
        

}