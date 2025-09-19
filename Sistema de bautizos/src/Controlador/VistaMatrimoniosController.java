/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controlador;

import Modelo.Catecumeno;
import Modelo.Catequizando;
import Modelo.Conyugues;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class VistaMatrimoniosController implements Initializable {

    @FXML
    private TextField txtnombreesposa;
    @FXML
    private TextField txtnombreesposo;
    @FXML
    private DatePicker dtpfechaboda;
    @FXML
    private TextField txtedadesposa;
    @FXML
    private TextField txtedadesposo;
    @FXML
    private TextField txtlibro;
    @FXML
    private TextField txtfolio;
    @FXML
    private TextArea txtartestigos;
    @FXML
    private TextField txtpresbitero;
    @FXML
    private TableView<Conyugues> tablacasados;
    @FXML
    private TableColumn collibro;
    @FXML
    private TableColumn colfolio;
    @FXML
    private TableColumn colnombreesposa;
    @FXML
    private TableColumn  colnombreesposo;
    @FXML
    private TableColumn coledadesposa;
    @FXML
    private TableColumn coledadesposo;
    @FXML
    private TableColumn coltestigos;
    @FXML
    private TableColumn colfechadeboda;
    @FXML
    private TableColumn colpresbitero;
    @FXML
    private TextField txtfiltro;
    @FXML
    private Button btneliminar;
    @FXML
    private Button btnguardar;

    /**
     * Initializes the controller class.
     */
    
       private ObservableList<Conyugues> listaCasados;
    private ObservableList<Conyugues> listaCasadosfiltro;
    @FXML
    private AnchorPane anchorpane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Inicializo las listas que utilizare
        
        listaCasados = FXCollections.observableArrayList();
        listaCasadosfiltro = FXCollections.observableArrayList();
        
        //Setteo las tablas para mostrar lo que hay en la lista principal
        tablacasados.setItems(listaCasados);

        //setteo las columnas para indicarles con los datos que van a trabajar
        colnombreesposa.setCellValueFactory(new PropertyValueFactory<>("Nombreesposa"));
        colnombreesposo.setCellValueFactory(new PropertyValueFactory<>("Nombreesposo"));
        colfechadeboda.setCellValueFactory(new PropertyValueFactory<>("Fechamatrimonio"));
        coledadesposa.setCellValueFactory(new PropertyValueFactory<>("Edadesposa"));
        coledadesposo.setCellValueFactory(new PropertyValueFactory<>("Edadesposo"));
        coltestigos.setCellValueFactory(new PropertyValueFactory<>("Testigos"));
        colpresbitero.setCellValueFactory(new PropertyValueFactory<>("Sacerdote"));
       
        collibro.setCellValueFactory(new PropertyValueFactory<>("Libro"));
        colfolio.setCellValueFactory(new PropertyValueFactory<>("Folio"));
             
        // Aplicar validaciones
    aplicarValidacionTexto(txtnombreesposa);
    aplicarValidacionTexto(txtnombreesposo);
    aplicarValidacionNumero(txtedadesposo);
    aplicarValidacionNumero(txtedadesposa);
    aplicarValidacionTextArea(txtartestigos);
    aplicarValidacionTexto(txtpresbitero);
    aplicarValidacionNumero(txtlibro);
    aplicarValidacionNumero(txtfolio);
        setupEnterKeyHandlers();
      btnguardar.setOnAction(event -> handleGuardar());
     tablacasados.setOnMouseClicked(event -> seleccionar());
     btneliminar.setOnAction(event -> handleEliminar());
     txtfiltro.setOnKeyReleased(event -> handleBuscar());
     anchorpane.setOnMouseClicked(event -> handleDesseleccionar());
    }    
  
    
  private void handleBuscar() {
    String filtro = txtfiltro.getText().toLowerCase().trim(); // Convertir a minúsculas y eliminar espacios extra

    if (filtro.isEmpty()) {
        tablacasados.setItems(listaCasados);
        return;
    }

    listaCasadosfiltro.clear();

    for (Conyugues p : listaCasados) {
        // Convertir todos los campos relevantes a minúsculas para comparación
        String folioString = String.valueOf(p.getFolio()).toLowerCase();
        String nombreEsposa = p.getNombreesposa().toLowerCase();
        String nombreEsposo = p.getNombreesposo().toLowerCase();
        String testigos = p.getTestigos().toLowerCase();

        // Verificar si el filtro coincide con algún campo relevante
        if (folioString.contains(filtro) ||
            nombreEsposa.contains(filtro) ||
            nombreEsposo.contains(filtro) ||
            testigos.contains(filtro)) {
            listaCasadosfiltro.add(p);
        }
    }

    tablacasados.setItems(listaCasadosfiltro);
    tablacasados.refresh();
}

 private void handleGuardar() {
      
 
    // Verificar si los campos están inicializados
    if (txtnombreesposa == null || txtnombreesposo == null || txtedadesposa == null || 
        txtedadesposo == null || txtartestigos == null || txtpresbitero == null || 
        txtfolio == null || txtlibro == null || dtpfechaboda == null) {
        showAlert("Error", "Uno o más campos no están inicializados.");
        return;
    }

    // Obtener valores de los campos
    String nombreesposa = txtnombreesposa.getText();
    String nombreesposo = txtnombreesposo.getText();
    String edadesposa = txtedadesposa.getText();
    String edadesposo = txtedadesposo.getText();
    String testigos = txtartestigos.getText();
    String presbitero = txtpresbitero.getText();
    String cantfolio = txtfolio.getText();
    String cantlibro = txtlibro.getText();
    LocalDate fechamatrimonio = dtpfechaboda.getValue();

    // Validación de campos vacíos
    if (nombreesposa.isEmpty() || nombreesposo.isEmpty() || edadesposa.isEmpty() || 
        edadesposo.isEmpty() || cantfolio.isEmpty() || cantlibro.isEmpty() || 
        presbitero.isEmpty() || fechamatrimonio == null || testigos.isEmpty()) {
        showAlert("Error", "Debe rellenar todos los campos.");
        return;
    }

    // Validación de números
    if (!cantfolio.matches("\\d+") || !cantlibro.matches("\\d+")) {
        showAlert("Error", "Los campos de Libro y Folio deben contener números enteros.");
        return;
    }

    int folioInt;
    int libroInt;
    int edadesposaint;
    int edadesposoint;
    try {
        folioInt = Integer.parseInt(cantfolio);
        libroInt = Integer.parseInt(cantlibro);
        edadesposaint = Integer.parseInt(edadesposa);
        edadesposoint = Integer.parseInt(edadesposo);
    } catch (NumberFormatException e) {
        showAlert("Error", "Los campos de Libro y Folio deben contener números válidos.");
        return;
    }

    // Validación de rangos
    if (folioInt < 0 || folioInt > 50000) {
        showAlert("Error", "Introduzca una cifra real en el campo de folio (menor o igual a 50.000).");
        return;
    }
    if (edadesposaint < 18 || edadesposaint > 100) {
        showAlert("Error", "Introduzca una una edad real.");
        return;
    }
     if (edadesposoint < 18 || edadesposoint > 100) {
        showAlert("Error", "Introduzca una una edad real.");
        return;
    }
    

    if (libroInt < 0 || libroInt > 50) {
        showAlert("Error", "Introduzca una cifra real en el campo del libro (menor o igual a 50).");
        return;
    }

    if (fechamatrimonio.isBefore(LocalDate.of(1940, 1, 1)) || fechamatrimonio.isAfter(LocalDate.now())) {
        showAlert("Error", "La fecha de matrimonio debe estar entre el 1 de enero de 1940 y la fecha actual.");
        return;
    }

    // Verificar que el folio no esté ya en la lista
    Conyugues seleccionado = tablacasados.getSelectionModel().getSelectedItem();

   
    Conyugues nuevosConyugues = new Conyugues(libroInt, folioInt, edadesposaint, edadesposoint,
                                              nombreesposo, nombreesposa, presbitero, testigos,
                                              fechamatrimonio);

    if (seleccionado != null) {
        // Actualizar el registro existente
        int index = listaCasados.indexOf(seleccionado);
        if (index != -1) {
            listaCasados.set(index, nuevosConyugues);
            tablacasados.setItems(listaCasados);
            tablacasados.refresh();
             guardarDatos();
             limpiarcampos();
            showAlert("Información", "Conyugues actualizado correctamente.");
        } else {
            showAlert("Error", "No se encontraron los conyugues seleccionado.");
        }
    } else {
        // Añadir un nuevo registro si no hay uno seleccionado
        if (!listaCasados.contains(nuevosConyugues)) {
            listaCasados.add(nuevosConyugues);
            tablacasados.refresh();
             guardarDatos();
             limpiarcampos();
            showAlert("Información", "Conyugues agregados correctamente.");
        } else {
            showAlert("Error", "Los conyugues ya existen.");
        }
    }

  limpiarcampos();
     this.tablacasados.getSelectionModel().clearSelection();
}

 private void handleEliminar() {
        Conyugues seleccionado = tablacasados.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            showAlert("Error", "Debe seleccionar unos conyugues para eliminar.");
            return;
        }
  
            listaCasados.remove(seleccionado);
            tablacasados.setItems(listaCasados);
            limpiarcampos();
            guardarDatos();
            tablacasados.getSelectionModel().clearSelection();
            showAlert("Información", "Bautizado eliminado correctamente.");
       
       
        
    }
    
   
 
    public void handleClose() {
        Stage stage = (Stage) btnguardar.getScene().getWindow();
        stage.close();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Principal.fxml"));
            Parent root = loader.load();
            PrincipalController controller = loader.getController();
            controller.setListaCasados(listaCasados); // Pasar la lista al controlador
            controller.cargartodo(); // Guardar los datos antes de cerrar
            
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

     public void setListaCasados(ObservableList<Conyugues> lista) {
        this.listaCasados = lista;
        tablacasados.setItems(lista);
    }
      
     private void seleccionar() {
         Conyugues p = this.tablacasados.getSelectionModel().getSelectedItem();
    
    if (p != null){
     txtnombreesposa.setText(p.getNombreesposa());
          txtnombreesposo.setText(p.getNombreesposo());
       txtedadesposo.setText(p.getEdadesposo() + "");
       txtedadesposa.setText(p.getEdadesposa() + "");
       
        txtartestigos.setText(p.getTestigos());
        txtpresbitero.setText(p.getSacerdote());
        txtfolio.setText(p.getFolio() + "");
        txtlibro.setText(p.getLibro() + "");
       dtpfechaboda.setValue(p.getFechamatrimonio());
   
    
  
   
    }
  
        }
     
 // Método para aplicar validación de texto (solo letras)
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

   private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

public void handleDesseleccionar(){

 this.tablacasados.getSelectionModel().clearSelection();
}
    public void guardarDatos() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("matrimonios.csv"))) {
            for (Conyugues a : listaCasados) {
                writer.write(String.format("%d,%d,%d,%d,%s,%s,%s,%s,%s%n",
                        a.getLibro(),
                        a.getFolio(),
                        a.getEdadesposa(),
                        a.getEdadesposo(),
                        a.getNombreesposo(),
                        a.getNombreesposa(),
                        a.getSacerdote(),
                        a.getTestigos(),
                        a.getFechamatrimonio()
                ));
           
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void limpiarcampos(){
    
      txtnombreesposa.clear();
                txtlibro.clear();
                txtfolio.clear();
                txtnombreesposo.clear(); 
                dtpfechaboda.setValue(null);
               
                txtedadesposa.clear();
                txtedadesposo.clear();
                txtartestigos.clear();
                txtpresbitero.clear();
                
                txtfiltro.clear();
    
    }
    
    
  private void setupEnterKeyHandlers() {
        Control[] allControls = {
            txtlibro, txtfolio,  txtnombreesposa, txtnombreesposo,txtedadesposa,txtedadesposo,dtpfechaboda,txtartestigos, txtpresbitero, 
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
     
}

