package Controlador;

import Modelo.Cliente;
import Modelo.Transaccion;
import Modelo.TransactionType;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

public class View_registrarseController implements Initializable {

    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnRegistrar;

    @FXML
    private DatePicker dtpFechaNacimiento;

    @FXML
    private Label lbError;

    @FXML
    private ToggleButton tbtnFemenino;

    @FXML
    private ToggleButton tbtnHideContrasena;

    @FXML
    private ToggleButton tbtnMasculino;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtContrasenaShow;

    @FXML
    private PasswordField txtContraseña;

    @FXML
    private TextField txtDireccion;

    @FXML
    private TextField txtMontoInicial;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUsuario;

    @FXML
    private VBox vbFormRegistro;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Pane pane;
    private ObservableList<Cliente> listaclientes;
    private ObservableList<Transaccion> transaccionespersonales;
    private ObservableList<Transaccion> transaccionesgenerales;
        private Cliente x;

    String imagen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaclientes = FXCollections.observableArrayList();

        //estableciendo el label de error
        lbError.setVisible(false);
        errorView();
        verificarCampos();

        //estableciendo grupo de botones de sexo
        ToggleGroup tgSexButtons = new ToggleGroup();
        tbtnFemenino.setToggleGroup(tgSexButtons);
        tbtnMasculino.setToggleGroup(tgSexButtons);

        //stableciendo estilos para el dtpicker de las celdas activas y de las inactivas desactivadas   
        dtpFechaNacimiento.setDayCellFactory(dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item.isAfter(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #F08080;");
                    }
                }
            }
        });
        //estableciendo valor de iniciio del dtpicker
        dtpFechaNacimiento.setValue(LocalDate.now().minusYears(14));

        //estableciendo el text field de la contraseña que no sea invisible
        txtContrasenaShow.setVisible(false);

        //acciones
        btnIniciar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader cargador = new FXMLLoader(getClass().getResource("/Vista/First_User_View.fxml"));
                    Parent root = cargador.load();
                    First_User_ViewController controlador = cargador.getController();

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(First_User_ViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnRegistrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (tbtnHideContrasena.isSelected()) {
                    txtContraseña.setText(txtContrasenaShow.getText());
                } else {
                    txtContrasenaShow.setText(txtContraseña.getText());
                }

                String Nombre = txtNombre.getText();
                String Apellido = txtApellido.getText();
                String Cedula = txtCedula.getText();
                String Direccion = txtDireccion.getText();
                String Usuario = txtUsuario.getText();
                String Contrasenna = txtContrasenaShow.getText();
                Integer Edad = calcularEdad(dtpFechaNacimiento.getValue());
                Character Sexo = null;
                Double Montoinicial = Double.parseDouble(txtMontoInicial.getText());

                if (tbtnMasculino.isSelected()) {
                    Sexo = 'M';
                } else if (tbtnFemenino.isSelected()) {
                    Sexo = 'F';
                }

                LocalDate Fnacimiento = dtpFechaNacimiento.getValue();
                LocalDate Fcreacion = LocalDate.now();

                if (IsEmpty()) {
                    errorMsg("Porfavor ingrese todas las casillas");
                    return;
                }

                if (calcularEdad(dtpFechaNacimiento.getValue()) < 14) {
                    errorMsg("Debe tener minimo 14 años para poder registrarse");
                    return;
                }

                if (Double.parseDouble(txtMontoInicial.getText()) < 5 || Double.parseDouble(txtMontoInicial.getText()) > 5000) {
                    errorMsg("El monto inicial debe ser mayor a 5$ y maximo 5000$");
                    return;
                }

                for (Cliente cliente : listaclientes) {
                    if (cliente.getUsuario().equals(Usuario)) {
                        errorMsg("El usuario ya existe");
                        return;
                    }else if (cliente.getCedula().equals(Cedula)) {
                        errorMsg("La cedula ya existe");
                        return;
                    }
                }

                cargaAvatares();

                Cliente nuevocliente = new Cliente(Nombre, Apellido, Cedula, Direccion, Usuario, Contrasenna, Fnacimiento, Edad, Sexo, Fcreacion, Montoinicial, imagen, Fcreacion, 0.0);
                  Transaccion transaccion = new Transaccion(LocalDate.now(), Montoinicial, "000", TransactionType.DEPOSITO , Usuario, Usuario,Cedula);
                 transaccionesgenerales.add(transaccion);
                     transaccionespersonales.add(transaccion);
                    guardarTransacciones();
                   
                  if (!listaclientes.contains(nuevocliente)) {
                    listaclientes.add(nuevocliente);
                   
                    alertMsg(event, 'u', "Registro exitoso");
                   for (Transaccion x : transaccionesgenerales){System.out.println(x.getUsuarioReceptor());}
                    try {
                        // Cargo la vista
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/First_User_View.fxml"));

                        // Cargo el padre
                        Parent root = loader.load();

                        // Obtengo el controlador
                        First_User_ViewController controlador = loader.getController();
                        controlador.setListaclientes(listaclientes);
                        controlador.guardarDatos();
                     
                        controlador.setTransaccionespersonales(transaccionespersonales);
                        controlador.setTransaccionesgenerales(transaccionesgenerales);
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

                    } catch (IOException ex) {
                        System.out.println("ERROR");
                        Logger.getLogger(First_User_ViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtCedula.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtUsuario.getText().isEmpty()
                || txtContraseña.getText().isEmpty() || txtContrasenaShow.getText().isEmpty() || txtMontoInicial.getText().isEmpty() || dtpFechaNacimiento.getEditor().getText().isEmpty()
                || !tbtnFemenino.isSelected() && !tbtnMasculino.isSelected()) {
            answer = true;
        }

        return answer;
    }

    private void errorMsg(String msg) {
        lbError.setText(msg);
        lbError.setVisible(true);
    }

    private void errorView() {
        txtNombre.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtApellido.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtCedula.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtDireccion.setOnKeyTyped(event -> {
            lbError.setVisible(false);
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

        txtMontoInicial.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        dtpFechaNacimiento.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
    }

    private void verificarCampos() {

        txtCedula.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            String texto = txtCedula.getText();
            if (!Character.isDigit(caracter)) {
                event.consume();
                errorMsg("No se permite caracteres en el campo de cedula");
            } else if (texto.length() > 8) {
                 event.consume();
                errorMsg("Se excedieron los 8 caracteres en el campo de cedula");
            }else {
                lbError.setVisible(false);
            }
        });

        txtMontoInicial.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (!Character.isDigit(caracter)) {
                if (caracter == '.' && txtMontoInicial.getText().contains(".")) {
                    event.consume();
                    errorMsg("No se permiten más de un punto en el campo del monto inicial");
                } else if (caracter != '.') {
                    event.consume();
                    errorMsg("No se permiten caracteres en el campo del monto inicial");
                }
            } else {
                lbError.setVisible(false);
            }
        });

        txtNombre.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            String texto = txtNombre.getText();
            if (Character.isDigit(caracter)) {
                event.consume();
                errorMsg("No se permite numeros en el campo de nombre");
            } else if (texto.length() > 15) {
                 event.consume();
                errorMsg("Se excedieron los 15 caracteres en el campo de nombre");
            } else {
                lbError.setVisible(false);
            }
        });

        txtApellido.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            String texto = txtApellido.getText();
            if (Character.isDigit(caracter)) {
                event.consume();
                errorMsg("No se permite numeros en el campo de apellido");
            } else if (texto.length() > 15) {
                 event.consume();
                errorMsg("Se excedieron los 15 caracteres en el campo de apellido");
            }else {
                lbError.setVisible(false);
            }
        });
        
        txtDireccion.setOnKeyTyped(event -> {
            String texto = txtDireccion.getText();
            if (texto.length() > 30) {
                 event.consume();
                errorMsg("Se excedieron los 30 caracteres en el campo de dirección");
            }else {
                lbError.setVisible(false);
            }
        });

        txtUsuario.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            String texto = txtUsuario.getText();
            if (Character.isWhitespace(caracter)) {
                event.consume();
                errorMsg("No se permiten espacios en blanco en el campo del usuario");
            } else if (texto.length() > 20) {
                 event.consume();
                errorMsg("Se excedieron los 20 caracteres en el campo de usuario");
            }else {
                lbError.setVisible(false);
            }
        });

        txtContraseña.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (Character.isWhitespace(caracter)) {
                event.consume();
                errorMsg("No se permiten espacios en blanco en el campo del contraseña");
            } else {
                lbError.setVisible(false);
            }
        });
        
        txtContrasenaShow.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (Character.isWhitespace(caracter)) {
                event.consume();
                errorMsg("No se permiten espacios en blanco en el campo del contraseña");
            } else {
                lbError.setVisible(false);
            }
        });
    }

    private Integer calcularEdad(LocalDate fechaNacimiento) {

        LocalDate fechaActual = LocalDate.now();

        int edad = fechaActual.getYear() - fechaNacimiento.getYear();

        LocalDate cumpleanos = LocalDate.of(fechaActual.getYear(), fechaNacimiento.getMonth(), fechaNacimiento.getDayOfMonth());

        if (cumpleanos.isAfter(fechaActual)) {
            edad--;
        } else if (cumpleanos.isEqual(fechaActual)) {
            // es igual no hacer naada
        } else {
            // ya pso no hacer nada
        }
        return edad;
    }

    void closeWindows() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/First_User_View.fxml"));

            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.show();
            stage.setTitle("Inicio de sesion");
            Stage myStage = (Stage) this.btnRegistrar.getScene().getWindow();
            myStage.close();

        } catch (IOException ex) {

        }

    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void alertMsg(ActionEvent event, Character img, String msg) {
        Stage alertStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double x = alertStage.getX();
        double y = alertStage.getY();

        try {
            FXMLLoader carga = new FXMLLoader(getClass().getResource("/Vista/Alert_View.fxml"));
            Parent root = carga.load();
            Alert_ViewController controller = carga.getController();
            controller.cambiarImg(img);
            controller.setText(msg);

            Stage stage = new Stage();
            stage.setTitle("Alert");
            Scene scene = new Scene(root, 420, 190);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setX(x + 450);
            stage.setY(y + 270);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargaAvatares() {

        try {
            FXMLLoader carga = new FXMLLoader(getClass().getResource("/Vista/Paginas/Avatares.fxml"));
            Parent root = carga.load();
            AvataresController controller = carga.getController();

            Stage stage = new Stage();
            stage.setTitle("xdxd");
            Scene scene = new Scene(root, 600, 400);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setX(400);
            stage.setY(200);
            stage.showAndWait();

            String sImagen = controller.getImagenSeleccionada();
            if (sImagen != null) {
                guardarImagen(sImagen);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void guardarImagen(String img) {
        imagen = img;
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
 public void cargarDatosTransaccion() throws IOException {
     transaccionesgenerales.clear();
     transaccionespersonales.clear();
     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader("Transacciones.csv"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Convertir la línea CSV en una instancia de Transaccion
                Transaccion transaccion = Transaccion.fromCsv(linea);

                // Añadir la transacción a la lista general
                transaccionesgenerales.add(transaccion);

                // Verificar si el emisor o receptor está en la lista de transacciones
                if (x.getUsuario().equals(transaccion.getUsuarioReceptor()) || x.getUsuario().equals(transaccion.getUsuarioEmisor())) {
                    transaccionespersonales.add(transaccion);
                }
            }
        }
    } 
 
 public void setCliente(Cliente nvCliente) {
        x = nvCliente;
    }
}
