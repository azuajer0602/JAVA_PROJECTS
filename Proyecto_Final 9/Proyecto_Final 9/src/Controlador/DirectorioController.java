package Controlador;

import Modelo.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DirectorioController implements Initializable {

    @FXML
    private Button btnlimpiar;
    @FXML
    private Button btncancelar;
    @FXML
    private TableView<Cliente> tbvDirectorio;
    @FXML
    private TableColumn<Cliente, String> colusuario;
    @FXML
    private TableColumn<Cliente, String> colnombre;
    @FXML
    private TableColumn<Cliente, String> colcedula;
    @FXML
    private TextField txtbuscarr;

    private Cliente seleccionado;
    private ObservableList<Cliente> listaclientes;
    private Stage dialogStage;
    private First_User_ViewController fuvc;
    private Cliente clienteActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fuvc = new First_User_ViewController();
        clienteActual = fuvc.getCliente();
        
        colcedula.setCellValueFactory(new PropertyValueFactory<>("Cedula"));
        colusuario.setCellValueFactory(new PropertyValueFactory<>("Usuario"));
        colnombre.setCellValueFactory(new PropertyValueFactory<>("Nombre"));

        tbvDirectorio.setItems(listaclientes);

        btncancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });

        tbvDirectorio.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                seleccionado = newValue;
                dialogStage.close();
            }
        });
        
        txtbuscarr.textProperty().addListener((observable, oldValue, newValue) -> {
        filtrarClientes(newValue);
    });

    }

    public Cliente getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Cliente seleccionado) {
        this.seleccionado = seleccionado;
    }

    public ObservableList<Cliente> getListaclientes() {
        return listaclientes;
    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    private void seleccionar() {
        Cliente p = this.tbvDirectorio.getSelectionModel().getSelectedItem();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void Settabla() {
        tbvDirectorio.setItems(listaclientes);
    }
    
    private void filtrarClientes(String texto) {
    ObservableList<Cliente> listaFiltrada = FXCollections.observableArrayList();
    for (Cliente cliente : listaclientes) {
        if (cliente.getNombre().toLowerCase().contains(texto.toLowerCase()) 
                || cliente.getUsuario().toLowerCase().contains(texto.toLowerCase()) 
                || cliente.getCedula().toLowerCase().contains(texto.toLowerCase())) {
            if (!cliente.equals(clienteActual)) { // Omite el cliente actual
                listaFiltrada.add(cliente);
            }
        }
    }
    tbvDirectorio.setItems(listaFiltrada);
}
}
