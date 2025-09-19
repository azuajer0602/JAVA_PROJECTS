package Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaccion {

    private LocalDate fehcaT;
    private double monto;
    private String NumMovimiento, UsuarioEmisor, UsuarioReceptor, cedulaReceptor;
    private TransactionType tipo;
    DateTimeFormatter formattercito = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public Transaccion(LocalDate fehcaT, double monto, String NumMovimiento, TransactionType tipo, String UsuarioEmisor, String UsuarioReceptor, String cedulaReceptor) {
        this.fehcaT = fehcaT;
        this.monto = monto;
        this.NumMovimiento = NumMovimiento;
        this.tipo = tipo;
        this.UsuarioEmisor = UsuarioEmisor;
        this.UsuarioReceptor = UsuarioReceptor;
        this.cedulaReceptor = cedulaReceptor;
    }

    public String getNumMovimiento() {
        return NumMovimiento;
    }

    public void setNumMovimiento(String NumMovimiento) {
        this.NumMovimiento = NumMovimiento;
    }

    public LocalDate getFehcaT() {
        return fehcaT;
    }

    public void setFehcaT(LocalDate fehcaT) {
        this.fehcaT = fehcaT;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return NumMovimiento;
    }

    public void setDescripcion(String descripcion) {
        this.NumMovimiento = descripcion;
    }

    public String getUsuarioEmisor() {
        return UsuarioEmisor;
    }

    public void setUsuarioEmisor(String UsuarioEmisor) {
        this.UsuarioEmisor = UsuarioEmisor;
    }

    public String getUsuarioReceptor() {
        return UsuarioReceptor;
    }

    public void setNombreReceptor(String UsuarioReceptor) {
        this.UsuarioReceptor = UsuarioReceptor;
    }

    public TransactionType getTipo() {
        return tipo;
    }

    public void setTipo(TransactionType tipo) {
        this.tipo = tipo;
    }

    public String getCedulaReceptor() {
        return cedulaReceptor;
    }

    public void setCedulaReceptor(String cedulaReceptor) {
        this.cedulaReceptor = cedulaReceptor;
    }
    public String getFechitas(){
     
        return String.valueOf(fehcaT.format(formattercito));
                  
    }
    
    public String getMontico(){
        
        
        return String.format("%.2f", monto);
    }
    
    public String toCsv() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return String.join(",", 
        fehcaT.format(formatter), 
        String.valueOf(monto), 
        NumMovimiento, 
        tipo.name(), 
        UsuarioEmisor, 
        UsuarioReceptor, 
        cedulaReceptor
    );
}
    public static Transaccion fromCsv(String csv) {
    String[] fields = csv.split(",");
    
    if (fields.length != 7) {
        throw new IllegalArgumentException("Invalid CSV format");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fechaT = LocalDate.parse(fields[0], formatter);
    double monto = Double.parseDouble(fields[1]);
    String NumMovimiento = fields[2];
    TransactionType tipo = TransactionType.valueOf(fields[3]);
    String UsuarioEmisor = fields[4];
    String UsuarioReceptor = fields[5];
    String cedulaReceptor = fields[6];

    return new Transaccion(fechaT, monto, NumMovimiento, tipo, UsuarioEmisor, UsuarioReceptor, cedulaReceptor);
}

    public void retiro(Cliente a,String Cedula,String Usuario){
    
    
    }
    
}