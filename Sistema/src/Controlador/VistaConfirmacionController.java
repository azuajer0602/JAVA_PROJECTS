package Controlador;

import Modelo.Catequizando;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class VistaConfirmacionController implements Initializable {

    @FXML
    private Button btneliminar;
    @FXML
    private Button btnguardar;
    @FXML
    private TextField txtfiltro;
    @FXML
    private TableView<Catequizando> tablaconfirmandos;
    @FXML
    private TableColumn<Catequizando,String> colibro;
    @FXML
    private TableColumn<Catequizando,String> colfolio;
    @FXML
    private TableColumn<Catequizando,String> colnombres;
    @FXML
    private TableColumn<Catequizando,String> colapellidos;
    @FXML
    private TableColumn<Catequizando,Character> colsexo;
    @FXML
    private TableColumn<Catequizando,LocalDate> colfechaconfirmacion;
    @FXML
    private TableColumn<Catequizando,String> colpadrinos;
    @FXML
    private TableColumn<Catequizando, String> colobispo;
    @FXML
    private TableColumn<Catequizando, String> colpadres;
    @FXML
    private TextField txtnombre;
    @FXML
    private TextField txtapellido;
    @FXML
    private RadioButton rdbmasculino;
    @FXML
    private RadioButton rdbfemenino;
    @FXML
    private DatePicker dtpkfechaconfirmacion;
    @FXML
    private TextField txtlibro;
    @FXML
    private TextField txtfolio;
    @FXML
    private TextArea txtarpadrinos;
    @FXML
    private TextArea txtarpadres;
    @FXML
    private TextField txtobispo;

    private ObservableList<Catequizando> listaCatequizandos;
    private ObservableList<Catequizando> listaCatequizandosfiltro;
    @FXML
    private AnchorPane anchorpane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      listaCatequizandos = FXCollections.observableArrayList();
        listaCatequizandosfiltro = FXCollections.observableArrayList();

        setupEnterKeyHandlers();
        tablaconfirmandos.setItems(listaCatequizandos);
        inicializarTabla();
        setupRadioButtons();
        setupListeners();
        inicializarvalidaciones();
    }

    private void inicializarvalidaciones(){
    aplicarValidacionTexto(txtnombre);
    aplicarValidacionTexto(txtapellido);
    aplicarValidacionTexto(txtobispo);
    aplicarValidacionTextArea(txtarpadrinos);
    aplicarValidacionTextArea(txtarpadres);
    aplicarValidacionNumero(txtfolio);
    aplicarValidacionNumero(txtlibro);
    }
    
    
    
    private void inicializarTabla() {
        colnombres.setCellValueFactory(new PropertyValueFactory<>("Nombres"));
        colapellidos.setCellValueFactory(new PropertyValueFactory<>("Apellidos"));
        colfechaconfirmacion.setCellValueFactory(new PropertyValueFactory<>("Fechasacramento"));
        colpadres.setCellValueFactory(new PropertyValueFactory<>("Padres"));
        colsexo.setCellValueFactory(new PropertyValueFactory<>("Sexo"));
        colpadrinos.setCellValueFactory(new PropertyValueFactory<>("Padrinos"));
        colobispo.setCellValueFactory(new PropertyValueFactory<>("Obispo"));
        colibro.setCellValueFactory(new PropertyValueFactory<>("Libro"));
        colfolio.setCellValueFactory(new PropertyValueFactory<>("Folio"));

        tablaconfirmandos.setItems(listaCatequizandos);
    }

    private void setupRadioButtons() {
        ToggleGroup grupoSexo = new ToggleGroup();
        rdbmasculino.setToggleGroup(grupoSexo);
        rdbfemenino.setToggleGroup(grupoSexo);
    }

    private void setupListeners() {
        btnguardar.setOnAction(event -> handleGuardar());
        btneliminar.setOnAction(event -> handleEliminar());
        txtfiltro.setOnKeyReleased(event -> handleBuscar());
        tablaconfirmandos.setOnMouseClicked(event -> seleccionar());
    }

    private void setupEnterKeyHandlers() {
        Control[] allControls = {
            txtlibro, txtfolio,  txtnombre, txtapellido, rdbmasculino, rdbfemenino,
            txtarpadres,  dtpkfechaconfirmacion, txtarpadrinos,  txtobispo, 
        };

        for (Control control : allControls) {
            control.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleEnterKey(allControls, control);
                }
            });
        }
    }

    private void handleEnterKey(Control[] allControls, Control currentControl) {
        if (currentControl instanceof TextInputControl) {
            TextInputControl inputControl = (TextInputControl) currentControl;
            if (inputControl.getText().isEmpty()) {
                inputControl.requestFocus();
                return;
            }
        } else if (currentControl instanceof DatePicker) {
            DatePicker datePicker = (DatePicker) currentControl;
            if (datePicker.getValue() == null) {
                datePicker.requestFocus();
                return;
            }
        } else if (currentControl instanceof RadioButton) {
            if (!rdbmasculino.isSelected() && !rdbfemenino.isSelected()) {
                rdbmasculino.requestFocus();
                return;
            }
        }

        if (areAllFieldsFilled(allControls)) {
            btnguardar.fire();
        } else {
            moveToNextControl(allControls, currentControl);
        }
    }

    private boolean areAllFieldsFilled(Control[] allControls) {
        for (Control control : allControls) {
            if (control instanceof TextInputControl) {
                TextInputControl inputControl = (TextInputControl) control;
                if (inputControl.getText().isEmpty()) {
                    return false;
                }
            } else if (control instanceof DatePicker) {
                DatePicker datePicker = (DatePicker) control;
                if (datePicker.getValue() == null) {
                    return false;
                }
            } else if (control instanceof RadioButton) {
                if (!rdbmasculino.isSelected() && !rdbfemenino.isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void moveToNextControl(Control[] allControls, Control currentControl) {
        int index = getCurrentFocusIndex(allControls, currentControl);
        if (index != -1) {
            int newIndex = (index + 1) % allControls.length;
            allControls[newIndex].requestFocus();
        }
    }

    private int getCurrentFocusIndex(Control[] controls, Control currentControl) {
        for (int i = 0; i < controls.length; i++) {
            if (controls[i].equals(currentControl)) {
                return i;
            }
        }
        return -1;
    }

    private void handleGuardar() {
        String nombres = txtnombre.getText();
        String apellidos = txtapellido.getText();
        String padrinos = txtarpadrinos.getText();
        String padres = txtarpadres.getText();
        String obispo = txtobispo.getText();
        String cantfolio = txtfolio.getText();
        String cantlibro = txtlibro.getText();
        LocalDate fechaConfirmacion = dtpkfechaconfirmacion.getValue();
        Character sexo = rdbmasculino.isSelected() ? 'M' : 'F';

        if (nombres.isEmpty() || apellidos.isEmpty() || padrinos.isEmpty() || padres.isEmpty() ||
                cantfolio.isEmpty() || cantlibro.isEmpty() || obispo.isEmpty() || fechaConfirmacion == null) {
            showAlert("Error", "Debe rellenar todos los campos.");
            return;
        }

        if (!cantfolio.matches("\\d+") || !cantlibro.matches("\\d+")) {
            showAlert("Error", "Los campos de Libro y Pagina deben contener números enteros.");
            return;
        }

        int folioInt = Integer.parseInt(cantfolio);
        int libroInt = Integer.parseInt(cantlibro);

        if (folioInt < 0 || folioInt > 50000) {
            showAlert("Error", "Introduzca una cifra real en el campo de Pagina (menor o igual a 50.000).");
            return;
        }

        if (libroInt < 0 || libroInt > 50) {
            showAlert("Error", "Introduzca una cifra real en el campo del libro (menor o igual a 50).");
            return;
        }

        if (fechaConfirmacion.isBefore(LocalDate.of(1940, 1, 1)) || fechaConfirmacion.isAfter(LocalDate.now())) {
            showAlert("Error", "La fecha de bautismo debe estar entre el 1 de enero de 1940 y la fecha actual.");
            return;
        }

        Catequizando nuevoCatequizando = new Catequizando(nombres, apellidos, padres, padrinos, obispo, libroInt, folioInt, sexo, fechaConfirmacion);
        Catequizando seleccionado = tablaconfirmandos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            int index = listaCatequizandos.indexOf(seleccionado);
            if (index != -1) {
                listaCatequizandos.set(index, nuevoCatequizando);
                tablaconfirmandos.setItems(listaCatequizandos);
                tablaconfirmandos.refresh();
                  guardarDatos();
                  
       
                showAlert("Información", "Confirmado actualizado correctamente.");
            } else {
                showAlert("Error", "No se encontró el confirmado seleccionado.");
            }
        } else {
            if (!listaCatequizandos.contains(nuevoCatequizando)) {
                listaCatequizandos.add(nuevoCatequizando);
                tablaconfirmandos.refresh();
                  guardarDatos();
         
                showAlert("Información", "Confirmado agregado correctamente.");
            } else {
                showAlert("Error", "El Confirmado ya existe.");
            }
        }

        limpiarCampos();
        tablaconfirmandos.getSelectionModel().clearSelection();
    }

    private void handleEliminar() {
        Catequizando seleccionado = tablaconfirmandos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaCatequizandos.remove(seleccionado);
            tablaconfirmandos.setItems(listaCatequizandos);
            guardarDatos();
            limpiarCampos();
            tablaconfirmandos.getSelectionModel().clearSelection();
            showAlert("Información", "Confirmado eliminado correctamente.");
        } else {
            showAlert("Error", "No se ha seleccionado ningún confirmado para eliminar.");
        }
    }

      public void setListaCatequizandos(ObservableList<Catequizando> lista) {
        this.listaCatequizandos = lista;
        tablaconfirmandos.setItems(lista);
    }
public void handleClose() {
        Stage stage = (Stage) btnguardar.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Principal.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            controller.setListaCatequizandos(listaCatequizandos); // Pasar la lista al controlador
            controller.cargartodo();
           
            Stage primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Menu Principal");
            primaryStage.setResizable(true);
            primaryStage.getIcons().add(new Image("/Modelo/pixelcut2.png"));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleBuscar() {
        String filtro = txtfiltro.getText().toLowerCase();
        listaCatequizandosfiltro.clear();
        for (Catequizando c : listaCatequizandos) {
            if (c.getNombres().toLowerCase().contains(filtro) ||
                c.getApellidos().toLowerCase().contains(filtro) ||
                c.getPadres().toLowerCase().contains(filtro) ||
                c.getPadrinos().toLowerCase().contains(filtro) ||
                c.getObispo().toLowerCase().contains(filtro)) {
                listaCatequizandosfiltro.add(c);
            }
        }
        tablaconfirmandos.setItems(listaCatequizandosfiltro);
    }

    private void seleccionar() {
        Catequizando seleccionado = tablaconfirmandos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            txtnombre.setText(seleccionado.getNombres());
            txtapellido.setText(seleccionado.getApellidos());
            txtarpadrinos.setText(seleccionado.getPadrinos());
            txtarpadres.setText(seleccionado.getPadres());
            txtobispo.setText(seleccionado.getObispo());
            txtlibro.setText(String.valueOf(seleccionado.getLibro()));
            txtfolio.setText(String.valueOf(seleccionado.getFolio()));
            dtpkfechaconfirmacion.setValue(seleccionado.getFechasacramento());
            if (seleccionado.getSexo() == 'M') {
                rdbmasculino.setSelected(true);
            } else {
                rdbfemenino.setSelected(true);
            }
        }
    }

    private void limpiarCampos() {
        txtnombre.clear();
        txtapellido.clear();
        txtarpadrinos.clear();
        txtarpadres.clear();
        txtobispo.clear();
        txtlibro.clear();
        txtfolio.clear();
        dtpkfechaconfirmacion.setValue(null);
        rdbmasculino.setSelected(false);
        rdbfemenino.setSelected(false);
    }

   

   private void guardarDatos() {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("catequizandos.csv"))) {
        for (Catequizando c : listaCatequizandos) {
            writer.write(String.format("%s,%s,%s,%s,%s,%d,%d,%c,%s%n",
                    c.getNombres(),
                    c.getApellidos(),
                    c.getPadres(),
                    c.getPadrinos(),
                    c.getObispo(),
                    c.getLibro(),
                    c.getFolio(),
                    c.getSexo(),
                    c.getFechasacramento().toString()
            ));
        }
    } catch (IOException e) {
        showAlert("Error", "No se pudo guardar el archivo de confirmandos.");
    }
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
        private void aplicarValidacionTexto(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z\\sñÑ ]*")) {
                textField.setText(oldValue);
            }
        });
    }

   private void aplicarValidacionTextArea(TextArea textArea) {
    textArea.textProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue.matches("[a-zA-Z\\s;ñÑ]*") || newValue.contains("\n") || newValue.contains("\r")) {
            textArea.setText(oldValue);
        }
    });
}


    private void aplicarValidacionNumero(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(oldValue);
            }
        });
    }

}
  