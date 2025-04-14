/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controlador;

import Modelo.Movimiento;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Programación Reimil
 */
public class VistapagomovilController implements Initializable {

    private ObservableList<Movimiento> movimientos;
    private ObservableList<Movimiento> transferencias;
    private ObservableList<Movimiento> pagomovil;
    private ObservableList<Movimiento> compras;
    private ObservableList<Movimiento> comisiones;
    private ObservableList<Movimiento> nomina;
    private ObservableList<Movimiento> creditos;
    private ObservableList<Movimiento> divisas;
    public String Nombrecuenta = null;
    @FXML
    private Button btnregresar;
    public TableView<Movimiento> Tablamovpm;
    @FXML
    private TableColumn<Movimiento, LocalDate> colfecha;
    @FXML
    private TableColumn<Movimiento, Integer> colcodigo;
    @FXML
    private TableColumn<Movimiento, String> coltipotra;
    @FXML
    private TableColumn<Movimiento, String> coltipoopera;
    @FXML
    private TableColumn<Movimiento, String> coldescripcion;
    @FXML
    private TableColumn<Movimiento, Integer> colreferencia;
    @FXML
    private TableColumn<Movimiento, Double> coldebe;
    @FXML
    private TableColumn<Movimiento, Double> colhaber;
    @FXML
    private TableColumn<Movimiento, Double> collsado;
    @FXML
    private Label labelgasto;
    @FXML
    private Label labelingreso;
    @FXML
    private TextField txtbuscar;
    @FXML
    public Label labelcuenta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movimientos = FXCollections.observableArrayList(); // Inicializar la lista
        transferencias = FXCollections.observableArrayList(); // Inicializar la lista
        pagomovil = FXCollections.observableArrayList(); // Inicializar la lista
        compras = FXCollections.observableArrayList(); // Inicializar la lista
        comisiones = FXCollections.observableArrayList(); // Inicializar la lista
        nomina = FXCollections.observableArrayList(); // Inicializar la lista
        creditos = FXCollections.observableArrayList(); // Inicializar la lista
        divisas = FXCollections.observableArrayList(); // Inicializar la lista

        Tablamovpm.setItems(pagomovil); // Vincular la lista a la TableView

           // Configurar las columnas de la tabla
        colfecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());

        // Configurar el CellFactory para aplicar el formato de fecha
        colfecha.setCellFactory(col -> new TableCell<Movimiento, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter)); // Formatear la fecha
                }
            }
        });
       
            
// Configurar la columna de debe
coldebe.setCellValueFactory(cellData -> cellData.getValue().debeProperty().asObject());
coldebe.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("#,##0.00;'-'#,##0.00"); // Formato para mostrar negativos con signo de resta

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Si el valor es cero, mostrar "0.00" sin signo
                    if (item == 0) {
                        setText(df.format(0)); // Mostrar cero sin signo
                    } else {
                        setText(df.format(-Math.abs(item))); // Formatear el número como negativo
                    }
                }
            }
        };
    }
});

// Configurar la columna de haber
colhaber.setCellValueFactory(cellData -> cellData.getValue().haberProperty().asObject());
colhaber.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("+#,##0.00;'-'#,##0.00"); // Formato para mostrar positivos con signo de suma

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Si el valor es cero, mostrar "0.00" sin signo
                    if (item.equals(0.0)) {
                        setText("0.00"); // Mostrar cero sin signo
                    } else {
                        // Mostrar el valor normalmente, asegurando que sea positivo
                        setText(df.format(item)); // Formatear el número según el formato definido
                    }
                }
            }
        };
    }
});

// Configurar la columna de saldo (sin cambios en el formato)
collsado.setCellValueFactory(cellData -> cellData.getValue().saldoProperty().asObject());
collsado.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("#,##0.00"); // Formato estándar

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(df.format(item)); // Formatear el número normalmente
                }
            }
        };
    }
});
    
        // Configurar las columnas de la tabla
        colfecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        colcodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty().asObject());
        coltipotra.setCellValueFactory(cellData -> cellData.getValue().tipotProperty());
        coltipoopera.setCellValueFactory(cellData -> cellData.getValue().tipoOProperty());
        coldescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        colreferencia.setCellValueFactory(cellData -> cellData.getValue().referenciaProperty().asObject());
        coldebe.setCellValueFactory(cellData -> cellData.getValue().debeProperty().asObject());
        colhaber.setCellValueFactory(cellData -> cellData.getValue().haberProperty().asObject());
        collsado.setCellValueFactory(cellData -> cellData.getValue().saldoProperty().asObject());

        txtbuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarMovimientos(newValue);
        });

        btnregresar.setOnAction(event -> {

            try {
                // Cargar la ventana principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistaone.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                VistaoneController controlador = loader.getController();

                controlador.setMovimientos(movimientos);
                controlador.setTransferencias(transferencias);
                controlador.setPagomovil(pagomovil);
                controlador.setCompras(compras);
                controlador.setComisiones(comisiones);
                controlador.setNomina(nomina);
                controlador.setCreditos(creditos);
                controlador.setDivisas(divisas);
                controlador.setNombrecuenta(Nombrecuenta);
                controlador.labelcuenta.setText(Nombrecuenta);
                for (Movimiento x : transferencias) {
                    System.out.println(x.toString());
                }
                for (Movimiento c : pagomovil) {
                    System.out.println("Listo el pago :" + c.toString());
                }

                controlador.refrescar();
                // Obtener el stage de la ventana principal
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Ventana Principal");
                stage.show();

                // Cerrar la ventana actual
                Stage myStage = (Stage) btnregresar.getScene().getWindow();
                myStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    public void setMovimientos(ObservableList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void setTransferencias(ObservableList<Movimiento> transferencias) {
        this.transferencias = transferencias;
    }

    public void setPagomovil(ObservableList<Movimiento> pagomovil) {
        this.pagomovil = pagomovil;
    }

    public void setCompras(ObservableList<Movimiento> compras) {
        this.compras = compras;
    }

    public void setComisiones(ObservableList<Movimiento> comisiones) {
        this.comisiones = comisiones;
    }

    public void setNomina(ObservableList<Movimiento> nomina) {
        this.nomina = nomina;
    }

    public void setCreditos(ObservableList<Movimiento> creditos) {
        this.creditos = creditos;
    }

    public void setDivisas(ObservableList<Movimiento> divisas) {
        this.divisas = divisas;
    }

    public void refrescar() {
        Tablamovpm.setItems(transferencias);
        Tablamovpm.refresh();
    }
    // Método para buscar movimientos

    private void buscarMovimientos(String searchText) {
        // Crear una lista filtrada a partir de la lista original
        FilteredList<Movimiento> filteredData = new FilteredList<>(movimientos, p -> true);

        // Filtrar la lista según el texto ingresado
        filteredData.setPredicate(movimiento -> {
            // Si el texto de búsqueda está vacío, se muestran todos los elementos
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            // Convertir el texto de búsqueda a minúsculas para una comparación no sensible a mayúsculas
            String lowerCaseFilter = searchText.toLowerCase();

            // Comprobar si alguno de los atributos coincide con el texto de búsqueda
            return movimiento.getDescripcion().toLowerCase().contains(lowerCaseFilter)
                    || movimiento.getTipot().toLowerCase().contains(lowerCaseFilter)
                    || movimiento.getTipoO().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getCodigo()).contains(lowerCaseFilter)
                    ||   String.valueOf(movimiento.getReferencia()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getDebe()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getHaber()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getSaldo()).contains(lowerCaseFilter);
        });

        // Actualizar la TableView con los resultados filtrados
        Tablamovpm.setItems(filteredData);
    }

    public void setNombrecuenta(String Nombrecuenta) {
        this.Nombrecuenta = Nombrecuenta;
    }
}
