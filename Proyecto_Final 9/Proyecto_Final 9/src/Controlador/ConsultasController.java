package Controlador;

import Modelo.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ConsultasController implements Initializable {

    @FXML
    private Button btnBuscarCuenta;

    @FXML
    private Button btnBuscarGeneral;

    @FXML
    private Button btnBuscarPersonal;

    @FXML
    private Pane pnGeneral;

    @FXML
    private Pane pnPersonal;

    @FXML
    private ToggleButton tbtnGeneral;

    @FXML
    private ToggleButton tbtnPersonal;

    @FXML
    private TableView<Cliente> tbvCuentasActivas;

    @FXML
    private TableView<Transaccion> tbvTransaccionesGenerales;

    @FXML
    private TableView<Transaccion> tbvTransaccionesPersonales;

    @FXML
    private TextField txtBuscarCuenta;

    @FXML
    private TextField txtBuscarGeneral;

    @FXML
    private TextField txtBuscarPersonal;


    @FXML
    private TableColumn<Transaccion, TransactionType> coltipotrans;
    @FXML
    private TableColumn colemisor;
    @FXML
    private TableColumn colmonto;
    @FXML
    private TableColumn colreceptor;
    @FXML
    private TableColumn coldescripcion;
    @FXML
    private TableColumn colfecha;
    @FXML
    private TableColumn<Cliente, String> colnombre;
    @FXML
    private TableColumn colapellido;
    @FXML
    private TableColumn colusuario;
    @FXML
    private TableColumn coledad;
    @FXML
    private TableColumn colsexo;
    @FXML
    private TableColumn coltipo2;
    @FXML
    private TableColumn colemisor2;
    @FXML
    private TableColumn colmonto2;
    @FXML
    private TableColumn colreceptor2;
    @FXML
    private TableColumn coldescripcion2;
    @FXML
    private TableColumn colfecha2;
    
    private TableColumn colFCreacion;

    private ObservableList<Transaccion> mistransacciones;
    private ObservableList<Transaccion> todastransacciones;
    private ObservableList<Cliente> CuentasActivas;
    @FXML
    private TableColumn <Cliente, LocalDate> colsaldo;
    @FXML
    private TableColumn<Cliente, LocalDate> colcreacion;
DateTimeFormatter formattercito = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        
        
        CuentasActivas = FXCollections.observableArrayList();
        todastransacciones = FXCollections.observableArrayList();
        mistransacciones = FXCollections.observableArrayList();
        pnGeneral.setVisible(false);
               
        
        ToggleGroup tbnsConsultas = new ToggleGroup();

        tbtnPersonal.setToggleGroup(tbnsConsultas);
        tbtnGeneral.setToggleGroup(tbnsConsultas);
      
        tbtnPersonal.setSelected(true);
        
        //tabla de transacciones personales
        columnas(coltipotrans, "Tipo");
        columnas(colemisor, "UsuarioEmisor");
        columnas(colmonto, "Montico");
        columnas(colreceptor, "UsuarioReceptor");
        columnas(coldescripcion, "NumMovimiento");
        columnas(colfecha, "Fechitas");
        //TABLA DE TRANSACCIONES GENERALES
       
        columnas(coltipo2, "Tipo");
        columnas(colemisor2, "UsuarioEmisor");
        columnas(colmonto2, "Montico");
        columnas(colreceptor2, "UsuarioReceptor");
       coldescripcion2.setCellValueFactory(new PropertyValueFactory<>("NumMovimiento"));
        columnas(colfecha2, "Fechitas");

        //TABLA DE CUENTAS ACTIVAS  
        columnas(colnombre,"Nombre");
        columnas(colapellido, "Apellido");
        columnas(colusuario, "Usuario");
        columnas(coledad, "Edad");
        columnas(colsexo, "Sexo");
        columnas(colsaldo, "Montico");
        columnas(colcreacion, "Fechitas");
        colcreacion.setSortType(TableColumn.SortType.ASCENDING);
        colfecha2.setSortType(TableColumn.SortType.ASCENDING);
        colfecha.setSortType(TableColumn.SortType.ASCENDING);
        
        
        tbnsConsultas.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });
     
    
         tbtnPersonal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnPersonal.isSelected()) {
                    pnGeneral.setVisible(false);
                    pnPersonal.setVisible(true);
          
                    tbvTransaccionesPersonales.refresh();
                    tbvTransaccionesPersonales.setItems(mistransacciones);
                 tbvTransaccionesPersonales.getSortOrder().add(colfecha);
                 

                }
            }
        });

        tbtnGeneral.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnGeneral.isSelected()) {
                    pnPersonal.setVisible(false);
                    pnGeneral.setVisible(true);

                    tbvCuentasActivas.setItems(CuentasActivas);
                    tbvTransaccionesGenerales.setItems(todastransacciones);
                     tbvCuentasActivas.getSortOrder().add(colcreacion);
                      tbvTransaccionesGenerales.getSortOrder().add(colfecha2);
                }

            }
        });
        
        txtBuscarCuenta.textProperty().addListener((observable, oldValue, newValue) -> {
        filtrarCuentasActivas(newValue);
    });
        
          txtBuscarCuenta.textProperty().addListener((observable, oldValue, newValue) -> {
        filtrarCuentasActivas(newValue);
    });
        
        txtBuscarGeneral.textProperty().addListener((observable, oldValue, newValue) -> {
    filtrarTransaccionesGenerales(newValue);
});

txtBuscarPersonal.textProperty().addListener((observable, oldValue, newValue) -> {
    filtrarTransaccionesPersonales(newValue);
});
    }

    private void columnas(TableColumn x, String Valor) {
        x.setCellValueFactory(new PropertyValueFactory<>(Valor));
    }

    public ObservableList<Transaccion> getMistransacciones() {
        return mistransacciones;
    }

    public void setMistransacciones(ObservableList<Transaccion> mistransacciones) {
        this.mistransacciones = mistransacciones;
    }

    public ObservableList<Transaccion> getTodastransacciones() {
        return todastransacciones;
    }

    public void setTodastransacciones(ObservableList<Transaccion> todastransacciones) {
        this.todastransacciones = todastransacciones;
    }

    public ObservableList<Cliente> getCuentasActivas() {
        return CuentasActivas;
    }

    public void setCuentasActivas(ObservableList<Cliente> CuentasActivas) {
        this.CuentasActivas = CuentasActivas;
    }
public void Settabla(){
 tbvTransaccionesPersonales.setItems(mistransacciones);
}

private void filtrarCuentasActivas(String texto) {
    ObservableList<Cliente> listaFiltrada = FXCollections.observableArrayList();
    for (Cliente cliente : CuentasActivas) {
        if (cliente.getNombre().toLowerCase().contains(texto.toLowerCase()) 
                || cliente.getApellido().toLowerCase().contains(texto.toLowerCase()) 
                || cliente.getUsuario().toLowerCase().contains(texto.toLowerCase())) {
            listaFiltrada.add(cliente);
        }
    }
    tbvCuentasActivas.setItems(listaFiltrada);
}
private void filtrarTransaccionesGenerales(String texto) {
    ObservableList<Transaccion> listaFiltrada = FXCollections.observableArrayList();
    for (Transaccion transaccion : todastransacciones) {
        if (transaccion.getUsuarioEmisor().toLowerCase().contains(texto.toLowerCase()) 
                || transaccion.getUsuarioReceptor().toLowerCase().contains(texto.toLowerCase()) 
                || transaccion.getNumMovimiento().toLowerCase().contains(texto.toLowerCase())) {
            listaFiltrada.add(transaccion);
        }
    }
    tbvTransaccionesGenerales.setItems(listaFiltrada);
}

private void filtrarTransaccionesPersonales(String texto) {
    ObservableList<Transaccion> listaFiltrada = FXCollections.observableArrayList();
    for (Transaccion transaccion : mistransacciones) {
        if (transaccion.getUsuarioEmisor().toLowerCase().contains(texto.toLowerCase()) 
                || transaccion.getUsuarioReceptor().toLowerCase().contains(texto.toLowerCase()) 
                || transaccion.getNumMovimiento().toLowerCase().contains(texto.toLowerCase())) {
            listaFiltrada.add(transaccion);
        }
    }
    tbvTransaccionesPersonales.setItems(listaFiltrada);
}
}
