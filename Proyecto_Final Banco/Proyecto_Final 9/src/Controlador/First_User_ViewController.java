package Controlador;

import Modelo.*;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class First_User_ViewController implements Initializable {

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnRegistrar;

    @FXML
    private ToggleButton tbtnHideContrasena;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private TextField txtContrasenaShow;

    @FXML
    private TextField txtUsuario;

    @FXML
    private Label lbError;

    private Stage stage; 
    private Scene scene;
    private Parent root;
    private ObservableList<Cliente> listaclientes;
    private ObservableList<Transaccion> transaccionespersonales;
    private ObservableList<Transaccion> transaccionesgenerales;

    private static Cliente C;
//    // Método para inyectar la referencia del Stage principal
//    public void setPrimaryStage(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//    }

    private First_User_ViewController instancia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        instancia = this;

        listaclientes = FXCollections.observableArrayList();
        transaccionespersonales = FXCollections.observableArrayList();
        transaccionesgenerales = FXCollections.observableArrayList();
        cargarDatos();
        cargarDatosTransacciones();
        lbError.setVisible(false);
        errorView();

        txtContrasenaShow.setVisible(false);

        btnRegistrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vista/View_registrarse.fxml"));
                    Parent root = cargador.load();
                    View_registrarseController controlador = cargador.getController();
                    controlador.setListaclientes(listaclientes);
                    controlador.setCliente(C);
                    controlador.setTransaccionesgenerales(transaccionesgenerales);
                    controlador.setTransaccionespersonales(transaccionespersonales);

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);

                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(First_User_ViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnIniciar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnHideContrasena.isSelected()) {
                    txtContraseña.setText(txtContrasenaShow.getText());
                } else {
                    txtContrasenaShow.setText(txtContraseña.getText());
                }

                if (IsEmpty()) {
                    errorMsg("Porfavor ingrese todas las casillas");
                    return;
                }
               
                
                
                Boolean entrar = false;
                for (Cliente c : listaclientes) {

                    if (txtUsuario.getText().equals(c.getUsuario().toLowerCase()) && txtContraseña.getText().equals(c.getContrasenna().toLowerCase())) {

                        entrar = true;
                        System.out.println("Esta corriendo");
                        C = c;
                        try {
                         transaccionesgenerales.clear();
                         transaccionespersonales.clear();
                         cargarDatosTransacciones();

                            // Cargo la vista
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/View_after_entrar.fxml"));

                            // Cargo el padre
                            Parent root = loader.load();

                            // Obtengo el controlador
                            View_after_entrarController controlador = loader.getController();
                            controlador.setListaclientes(listaclientes);
                            controlador.setMistransacciones(transaccionespersonales);
                            controlador.setTodastransacciones(transaccionesgenerales);
                            if (c.getUltimInteresFecha().equals(c.getFCreacion()) == false && c.getUltimInteres() != 0.0) {
                            c.generateInterest();
                            }
                            controlador.recibirUsuario(c);

                            // Creo la scene y el stage
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();

                            // Asocio el stage con el scene
                            stage.setScene(scene);
                            stage.show();
                            stage.setTitle("Menu Principal");
                       stage.getIcons().add(new Image("/Vista/imagenes/Logo Bank Blanco sin fondo.png"));

                            // Ciero la ventana donde estoy
                            Stage myStage = (Stage) btnIniciar.getScene().getWindow();
                            myStage.close();

//                            break;
                        } catch (IOException ex) {
                            Logger.getLogger(First_User_ViewController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

                if (!entrar) {
                    errorMsg("Verifique su usuario y contraseña");
//                    showAlert("Error", "Verifique su usuario y contraseña, Si no posee cuenta puede crearla en el apartado de registro");
                }

            }

        });

        tbtnHideContrasena.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnHideContrasena.isSelected()) {
                    txtContraseña.setVisible(false);
                    txtContrasenaShow.setText(txtContraseña.getText());
                    txtContrasenaShow.setVisible(true);
                } else {
                    txtContrasenaShow.setVisible(false);
                    txtContraseña.setText(txtContrasenaShow.getText());
                    txtContraseña.setVisible(true);
                }
            }
        });

    }

    private boolean IsEmpty() {
        boolean answer;
        answer = false;
        if (txtUsuario.getText().isEmpty() || txtContraseña.getText().isEmpty() || txtContrasenaShow.getText().isEmpty()) {
            answer = true;
        }

        return answer;
    }

    private void errorMsg(String msg) {
        lbError.setText(msg);
        lbError.setVisible(true);
    }

    private void errorView() {

        txtUsuario.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if (tbtnHideContrasena.isSelected()) {
                        txtContraseña.setText(txtContrasenaShow.getText());
                    } else {
                        txtContrasenaShow.setText(txtContraseña.getText());
                    }

                    if (txtContraseña.getText().isEmpty()) {
                        txtContraseña.requestFocus();
                    } else if (txtContrasenaShow.getText().isEmpty()) {
                        txtContrasenaShow.requestFocus();
                    } else {
                        btnIniciar.requestFocus();
                        btnIniciar.fire();
                    }
                }
            }
        });

        txtContraseña.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if (txtUsuario.getText().isEmpty()) {
                        txtUsuario.requestFocus();
                    } else {
                        btnIniciar.requestFocus();
                        btnIniciar.fire();
                    }
                }
            }
        });

        txtContrasenaShow.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    if (txtUsuario.getText().isEmpty()) {
                        txtUsuario.requestFocus();
                    } else {
                        btnIniciar.requestFocus();
                        btnIniciar.fire();
                    }
                }
            }
        });

        txtUsuario.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtContraseña.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtContrasenaShow.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });
    }

    public ObservableList<Cliente> getListaclientes() {
        return listaclientes;
    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void guardarDatos() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Clientes.csv"))) {
            for (Cliente cliente : listaclientes) {
                writer.write(cliente.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        listaclientes.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("Clientes.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Cliente cliente = Cliente.fromCSV(line);
                listaclientes.add(cliente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cliente getCliente() {
        return C;
    }

    public void setCliente(Cliente nvCliente) {
        C = nvCliente;
    }

    public void Actualizarlistaclientes(Cliente b) {
        Cliente a = null;
        C = a;
        if (a != null) {
            // Actualizar el registro existente
            int index = listaclientes.indexOf(a);
            if (index != -1) {
                listaclientes.set(index, b);

                showAlert("Información", "Cliente actualizado correctamente.");
            } else {
                showAlert("Error", "No se encontró el cliente seleccionado.");
            }

        }

    }

    public void actualizarDatos(ObservableList<Cliente> listaClientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Clientes.csv"))) {
            for (Cliente cliente : listaClientes) {
                writer.write(cliente.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Transaccion> getTransaccionespersonales() {
        return transaccionespersonales;
    }

    public void setTransaccionespersonales(ObservableList<Transaccion> transaccionespersonales) {
        this.transaccionespersonales = transaccionespersonales;
    }

    public ObservableList<Transaccion> getTransaccionesgenerales() {
        return transaccionesgenerales;
    }
    
    public void setTransaccionesgenerales(ObservableList<Transaccion> transaccionesgenerales) {
        this.transaccionesgenerales = transaccionesgenerales;
    }
  
   public void guardarTransacciones() {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("Transacciones.csv"))) {
        for (Transaccion c : transaccionesgenerales) {
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s%n",
                    c.getFehcaT(),
                    String.valueOf(c.getMonto()),
                    c.getDescripcion(),
                    String.valueOf(c.getTipo()),
                    c.getUsuarioEmisor(),
                    c.getUsuarioReceptor(),
                    c.getCedulaReceptor()
                    ));
        }
    } catch (IOException e) {
        System.out.println("Error No se pudo guardar el archivo de transacciones.");
    }
}
  
   
    public void cargarDatosTransacciones() {
    File archivo = new File("Transacciones.csv");
    if (archivo.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] campos = line.split(",");
                
                // Verifica que la cantidad de campos es la correcta (7 en este caso)
                if (campos.length == 7) {
                    try {
                        // Convertir cada campo a su tipo apropiado
                        LocalDate fecha = LocalDate.parse(campos[0]);
                        double monto = Double.parseDouble(campos[1]);
                        String descripcion = campos[2];
                        TransactionType tipo = TransactionType.valueOf(campos[3].toUpperCase());
                        String usuarioEmisor = campos[4];
                        String usuarioReceptor = campos[5];
                        String cedulaReceptor = campos[6];
                        
                        // Crear y agregar la transacción
                        Transaccion T = new Transaccion(fecha, monto, descripcion, tipo, usuarioEmisor, usuarioReceptor, cedulaReceptor);
                        transaccionesgenerales.add(T);
                        
                        // Agregar a las transacciones personales si corresponde
                        if (C != null && (C.getUsuario().equals(usuarioReceptor) || C.getUsuario().equals(usuarioEmisor))) {
                            transaccionespersonales.add(T);
                        }
                        
                        System.out.println("Cargado correctamente");
                        
                    } catch (NumberFormatException | DateTimeParseException e) {
                        // Manejo de errores de conversión
                        System.out.println("Error al procesar una línea del archivo: " + e.getMessage());
                    } catch (IllegalArgumentException e) {
                        // Manejo de errores de tipo de transacción
                        System.out.println("Error en el tipo de transacción: " + e.getMessage());
                    }
                } else {
                    System.out.println("Formato incorrecto en línea: " + line);
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de transacciones: " + e.getMessage());
        }
    } else {
        System.out.println("El archivo de transacciones no existe.");
    }
}

 public void cargartodo(){
 transaccionesgenerales.clear();
 transaccionespersonales.clear();
 listaclientes.clear();
 cargarDatos();
cargarDatosTransacciones();
 
 }
 
 public void guardartodo(){
 guardarDatos();
 guardarTransacciones();
 
 
 }
}

