package Controlador;

import Modelo.Catecumeno;
import Modelo.Conyugues;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class VistaBautizosmenuController implements Initializable {

    @FXML
    private Button btnguardar;
    @FXML
    private Button btneliminar;
    @FXML
    private TableView<Catecumeno> tablabautizados;

    @FXML
    private TableColumn<Catecumeno, String> collibro;
    @FXML
    private TableColumn<Catecumeno, String> colfolio;
    @FXML
    private TableColumn<Catecumeno, String> colnombre;
    @FXML
    private TableColumn<Catecumeno, String> colapellido;
    @FXML
    private TableColumn<Catecumeno, LocalDate> colfechanac;
    @FXML
    private TableColumn<Catecumeno, LocalDate> colfechabau;
    @FXML
    private TableColumn<Catecumeno, Character> colsexo;
    @FXML
    private TableColumn<Catecumeno, String> colpadrinos;
    @FXML
    private TableColumn<Catecumeno, String> colsacerdote;
    @FXML
    private TableColumn<Catecumeno, String> colpadre;
    @FXML
    private TableColumn<Catecumeno, String> colmadre;

    @FXML
    private TextField txtnombre;
    @FXML
    private TextField txtapellido;
    @FXML
    private TextField txtmadre;
    @FXML
    private TextField txtpadre;
    @FXML
    private TextArea txtpadrinoss;
    @FXML
    private TextField txtpresbitero;
    @FXML
    private TextField txtlibro;
    @FXML
    private TextField txtfolio;
    @FXML
    private DatePicker dtpkfechabautismo;
    @FXML
    private DatePicker dtpkfechanacimiento;
    @FXML
    private RadioButton rdbmasculino;
    @FXML
    private RadioButton rdbfemenino;

    private ObservableList<Catecumeno> listaBautizados;
    private ObservableList<Catecumeno> listaBautizadosfiltro;
    @FXML
    private TextField txtfiltro;
    @FXML
    private AnchorPane anchorpane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        //Inicializo las listas que utilizare
        
        listaBautizados = FXCollections.observableArrayList();
        listaBautizadosfiltro = FXCollections.observableArrayList();
     
        //Setteo las tablas para mostrar lo que hay en la lista principal
        tablabautizados.setItems(listaBautizados);

        //setteo las columnas para indicarles con los datos que van a trabajar
        colnombre.setCellValueFactory(new PropertyValueFactory<>("Nombres"));
        colapellido.setCellValueFactory(new PropertyValueFactory<>("Apellidos"));
        colfechanac.setCellValueFactory(new PropertyValueFactory<>("FechaNacimiento"));
        colfechabau.setCellValueFactory(new PropertyValueFactory<>("Fechabautismo"));
        colpadre.setCellValueFactory(new PropertyValueFactory<>("Padre"));
        colmadre.setCellValueFactory(new PropertyValueFactory<>("Madre"));
        colsexo.setCellValueFactory(new PropertyValueFactory<>("Sexo"));
        colpadrinos.setCellValueFactory(new PropertyValueFactory<>("Padrinos"));
        collibro.setCellValueFactory(new PropertyValueFactory<>("Libro"));
        colfolio.setCellValueFactory(new PropertyValueFactory<>("Folio"));
        colsacerdote.setCellValueFactory(new PropertyValueFactory<>("Presbitero"));
 
        //Hago un togglegroup para poder tener un mejor manejo de los radiobuttons
        ToggleGroup grupoSexo = new ToggleGroup();
        //setteo los dos radiobuttons con el togglegroup
        
        rdbmasculino.setToggleGroup(grupoSexo);
        rdbfemenino.setToggleGroup(grupoSexo);
        
        
        // Aplicar validaciones
    aplicarValidacionTexto(txtnombre);
    aplicarValidacionTexto(txtapellido);
    aplicarValidacionTexto(txtmadre);
    aplicarValidacionTexto(txtpadre);
    aplicarValidacionTextArea(txtpadrinoss);
    aplicarValidacionTexto(txtpresbitero);
    aplicarValidacionNumero(txtlibro);
    aplicarValidacionNumero(txtfolio);
    
    setupEnterKeyHandlers();
   
        
        
        //Uso una expresion lambda para asignarle un evento a cada boton 
        
        btnguardar.setOnAction(event -> handleGuardar());
        
        tablabautizados.setOnMouseClicked(event -> seleccionar());
        
        btneliminar.setOnAction(event -> handleEliminar());
        
        txtfiltro.setOnKeyReleased(event -> handleBuscar());
        
    }
    
private void handleGuardar() {
        String nombres = txtnombre.getText();
        String apellidos = txtapellido.getText();
        String madre = txtmadre.getText();
        String padre = txtpadre.getText();
        String padrinos = txtpadrinoss.getText();
        String presbitero = txtpresbitero.getText();
        String cantfolio = txtfolio.getText();
        String cantlibro = txtlibro.getText();
        LocalDate fechaNacimiento = dtpkfechanacimiento.getValue();
        LocalDate fechaBautismo = dtpkfechabautismo.getValue();
        Character sexo = rdbmasculino.isSelected() ? 'M' : 'F';

        if (nombres.isEmpty() || apellidos.isEmpty() || madre.isEmpty() || padre.isEmpty() || padrinos.isEmpty() ||
            cantfolio.isEmpty() || cantlibro.isEmpty() || presbitero.isEmpty() || fechaNacimiento == null || fechaBautismo == null) {
            showAlert("Error", "Debe rellenar todos los campos.");
            return;
        }
        if(!rdbmasculino.isSelected() && !rdbfemenino.isSelected()) {
            showAlert("Error", "Debe seleccionar el sexo del bautizado.");
            return;
        }
        if (!cantfolio.matches("\\d+") || !cantlibro.matches("\\d+")) {
            showAlert("Error", "Los campos de Libro y Pagina deben contener números enteros.");
            return;
        }

        int folioInt;
        int libroInt;
        try {
            folioInt = Integer.parseInt(cantfolio);
            libroInt = Integer.parseInt(cantlibro);
        } catch (NumberFormatException e) {
            showAlert("Error", "Los campos de Libro y Pagina deben contener números válidos.");
            return;
        }

        if (folioInt < 0 || folioInt > 50000) {
            showAlert("Error", "Introduzca una cifra real en el campo de Pagina (menor o igual a 50.000).");
            return;
        }

        if (libroInt < 0 || libroInt > 50) {
            showAlert("Error", "Introduzca una cifra real en el campo del libro (menor o igual a 50).");
            return;
        }

        if (fechaNacimiento.isBefore(LocalDate.of(1940, 1, 1)) || fechaNacimiento.isAfter(LocalDate.now())) {
            showAlert("Error", "La fecha de nacimiento debe estar entre el 1 de enero de 1940 y la fecha actual.");
            return;
        }

        if (fechaBautismo.isBefore(LocalDate.of(1940, 1, 1)) || fechaBautismo.isAfter(LocalDate.now())) {
            showAlert("Error", "La fecha de bautismo debe estar entre el 1 de enero de 1940 y la fecha actual.");
            return;
        }

        if (fechaBautismo.isBefore(fechaNacimiento)) {
            showAlert("Error", "La fecha de bautismo no puede ser anterior a la fecha de nacimiento.");
            return;
        }

        Catecumeno seleccionado = tablabautizados.getSelectionModel().getSelectedItem();
        Catecumeno nuevoCatecumeno = new Catecumeno(nombres, apellidos, madre, padre, padrinos, presbitero, libroInt, folioInt, sexo, fechaNacimiento, fechaBautismo);

        if (seleccionado != null) {
            int index = listaBautizados.indexOf(seleccionado);
            if (index != -1) {
                listaBautizados.set(index, nuevoCatecumeno);
                tablabautizados.setItems(listaBautizados);
                tablabautizados.refresh();
                guardarDatos();
                Limpiarcampos();
                showAlert("Información", "Bautizado actualizado correctamente.");
            }
        } else {
            if (!listaBautizados.contains(nuevoCatecumeno)) {
                listaBautizados.add(nuevoCatecumeno);
                tablabautizados.setItems(listaBautizados);
                guardarDatos();
                Limpiarcampos();
                
                showAlert("Información", "Bautizado guardado correctamente.");
            } else {
                showAlert("Error", "El bautizado ya existe.");
            }
        }
        tablabautizados.getSelectionModel().clearSelection();
    }

    private void handleEliminar() {
        Catecumeno seleccionado = tablabautizados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaBautizados.remove(seleccionado);
            tablabautizados.setItems(listaBautizados);
            Limpiarcampos();
            guardarDatos();
            tablabautizados.getSelectionModel().clearSelection();
            showAlert("Información", "Bautizado eliminado correctamente.");
        } else {
            showAlert("Error", "Debe seleccionar un bautizado para eliminar.");
        }
    }

    private void handleBuscar() {
       String filtro = txtfiltro.getText().toLowerCase().trim(); // Convertir a minúsculas y eliminar espacios extra



    if (filtro.isEmpty()) {
        // Si el filtro está vacío, mostrar todos los elementos
        tablabautizados.setItems(listaBautizados);
        return;
    }
    listaBautizadosfiltro.clear();
 // Filtrar elementos
    for (Catecumeno p : listaBautizados) {
        String folioString = String.valueOf(p.getFolio()).toLowerCase();
        String nombreyapellido = p.getNombres().toLowerCase() + " " + p.getApellidos().toLowerCase();
        String padres = p.getMadre().toLowerCase() + " " + p.getPadre().toLowerCase();
        String testigos = p.getPadrinos().toLowerCase();

        if (folioString.contains(filtro) ||
            nombreyapellido.contains(filtro) ||
            padres.contains(filtro) ||
            testigos.contains(filtro)) {
            listaBautizadosfiltro.add(p);
        }
    }

    // Actualizar la tabla con los resultados filtrados
    tablabautizados.setItems(listaBautizadosfiltro);
}


    

    private void seleccionar() {
        Catecumeno seleccionado = tablabautizados.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            txtnombre.setText(seleccionado.getNombres());
            txtapellido.setText(seleccionado.getApellidos());
            txtmadre.setText(seleccionado.getMadre());
            txtpadre.setText(seleccionado.getPadre());
            txtpadrinoss.setText(seleccionado.getPadrinos());
            txtpresbitero.setText(seleccionado.getPresbitero());
            txtlibro.setText(Integer.toString(seleccionado.getLibro()));
            txtfolio.setText(Integer.toString(seleccionado.getFolio()));
            dtpkfechanacimiento.setValue(seleccionado.getFechaNacimiento());
            dtpkfechabautismo.setValue(seleccionado.getFechabautismo());
            if (seleccionado.getSexo() == 'M') {
                rdbmasculino.setSelected(true);
            } else {
                rdbfemenino.setSelected(true);
            }
        }
    }

    

    private void guardarDatos() {

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("catecumenos.csv"))) {
            for (Catecumeno c : listaBautizados) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%d,%d,%c,%s,%s%n",
                        c.getNombres(), c.getApellidos(), c.getMadre(), c.getPadre(), c.getPadrinos(),
                        c.getPresbitero(), c.getLibro(), c.getFolio(), c.getSexo(),
                        c.getFechaNacimiento(), c.getFechabautismo()));
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo guardar el archivo de datos.");
        }
    
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
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


    public void handleClose() {
        Stage stage = (Stage) btnguardar.getScene().getWindow();
        stage.close();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Principal.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            controller.setListaCatecumenos(listaBautizados);
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

   

  private void setupEnterKeyHandlers() {
        Control[] allControls = {
            txtlibro, txtfolio,  txtnombre, txtapellido, rdbmasculino, rdbfemenino,
             dtpkfechanacimiento, dtpkfechabautismo,txtpadre,txtmadre,txtpadrinoss, txtpresbitero, 
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
        public void Limpiarcampos(){
                txtnombre.clear();
                txtlibro.clear();
                txtfolio.clear();
                txtapellido.clear(); 
                dtpkfechanacimiento.setValue(null);
                dtpkfechabautismo.setValue(null);
                txtpadre.clear();
                txtmadre.clear();
                txtpadrinoss.clear();
                txtpresbitero.clear();
                rdbmasculino.setSelected(false);
                rdbfemenino.setSelected(false);
                txtfiltro.clear();
   }
  
 public void setListaBautizados(ObservableList<Catecumeno> lista) {
        this.listaBautizados = lista;
        tablabautizados.setItems(lista);
    }
  
   }
