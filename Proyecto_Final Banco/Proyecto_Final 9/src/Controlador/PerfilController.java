package Controlador;

import Modelo.Cliente;
import Modelo.Transaccion;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PerfilController implements Initializable {

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnPerfil;

    @FXML
    private DatePicker dtpFechaNacimiento;

    @FXML
    private ImageView imgPerfil;

    @FXML
    private Label lbError;

    @FXML
    private Label lbSexo;

    @FXML
    private Line lnDivisionSexo;

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
    private TextField txtNombre;

    @FXML
    private TextField txtUsuario;

    @FXML
    private VBox vbFormRegistro;

    private String des = "field_deshabilitado";
    private String desDate = "date_deshabilitado";
    private String hab = "int_field";
    private String habDate = "int_date";

    private String imagen;
    private String imagenurl;

    private First_User_ViewController fuvc;
    private ObservableList<Cliente> listaclientes;
    private ObservableList<Transaccion> transacciones;
    private ObservableList<Transaccion> transaccionesgene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorView();
        verificarCampos();

        fuvc = new First_User_ViewController();
        Cliente clienteActual = fuvc.getCliente();

        btnAceptar.setVisible(false);
        btnCancelar.setVisible(false);
        lnDivisionSexo.setVisible(false);
        lbError.setVisible(false);
        btnPerfil.setDisable(true);

        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtCedula.setDisable(true);
        txtDireccion.setDisable(true);
        txtContraseña.setDisable(true);
        txtContrasenaShow.setDisable(true);
        txtUsuario.setDisable(true);
        dtpFechaNacimiento.setDisable(true);
        lbSexo.setDisable(true);

        //estableciendo el text field de la contraseña que no sea invisible
        txtContrasenaShow.setVisible(false);

        //estableciendo grupo de botones de sexo
        ToggleGroup tgSexButtons = new ToggleGroup();
        tbtnFemenino.setToggleGroup(tgSexButtons);
        tbtnMasculino.setToggleGroup(tgSexButtons);

        tbtnFemenino.setVisible(false);
        tbtnMasculino.setVisible(false);
        tbtnHideContrasena.setVisible(false);

        txtNombre.setText(fuvc.getCliente().getNombre());
        txtApellido.setText(fuvc.getCliente().getApellido());
        txtCedula.setText(fuvc.getCliente().getCedula());
        txtDireccion.setText(fuvc.getCliente().getDireccion());
        dtpFechaNacimiento.setValue(fuvc.getCliente().getFnacimiento());
        txtContraseña.setText(fuvc.getCliente().getContrasenna());
        txtUsuario.setText(fuvc.getCliente().getUsuario());
        if (fuvc.getCliente().getSexo() == 'M') {
            lbSexo.setText("Masculino");
            tbtnMasculino.setSelected(true);
        } else {
            lbSexo.setText("Femenino");
            tbtnFemenino.setSelected(true);
        }
        imagen = fuvc.getCliente().getPerfil();
        imagenurl = imagen;

        Image perfil = new Image(imagenurl);
        imgPerfil.setImage(perfil);

        //estableciendo estilos para el dtpicker de las celdas activas y de las inactivas desactivadas   
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

        btnEditar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnEditar.setVisible(false);
                btnAceptar.setVisible(true);
                btnCancelar.setVisible(true);
                if (fuvc.getCliente().getSexo() == 'M') {
                    tbtnMasculino.setSelected(true);
                } else {
                    tbtnFemenino.setSelected(true);
                }

                removerDeshabilitado();
                habilitar();

            }
        });

        btnAceptar.setOnAction(new EventHandler<ActionEvent>() {
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
                Double Montoinicial = fuvc.getCliente().getMontoC();
                Double MontoInteres = fuvc.getCliente().getUltimInteres();

                if (tbtnMasculino.isSelected()) {
                    Sexo = 'M';
                } else if (tbtnFemenino.isSelected()) {
                    Sexo = 'F';
                }
                
                LocalDate FInteres = fuvc.getCliente().getUltimInteresFecha();
                LocalDate Fnacimiento = dtpFechaNacimiento.getValue();
                LocalDate Fcreacion = fuvc.getCliente().getFCreacion();

                if (IsEmpty()) {
                    errorMsg("Porfavor ingrese todas las casillas");
                    return;
                }

                if (calcularEdad(dtpFechaNacimiento.getValue()) < 14) {
                    errorMsg("Debe tener minimo 14 años para poder registrarse");
                    return;
                }

                for (Cliente cliente : listaclientes) {
                    if (cliente.getUsuario().equals(clienteActual.getUsuario())) {
                        break;
                    }
                    if (cliente.getUsuario().equals(Usuario)) {
                        errorMsg("El usuario ya existe");
                        return;
                    }
                }

                String imageNueva = imagen;

                Cliente nuevocliente = new Cliente(Nombre, Apellido, Cedula, Direccion, Usuario, Contrasenna, Fnacimiento, Edad, Sexo, Fcreacion, Montoinicial, imageNueva, FInteres, MontoInteres);

                int indiceDelCliente = listaclientes.indexOf(clienteActual);
                if (indiceDelCliente != -1) {
                    listaclientes.set(indiceDelCliente, nuevocliente);
                } else {
                    System.out.println("El cliente no se encuentra en la lista");
                }

                actualizarDatos(listaclientes);

                fuvc.setCliente(nuevocliente);
                System.out.println("Cambio se ejecuto");
                btnEditar.setVisible(true);
                btnAceptar.setVisible(false);
                btnCancelar.setVisible(false);

                removerHabilitar();
                deshabilitar();

                alertMsg(event, 'u', "Cambios realizados con exito");

                try {
                    // Cargo la vista
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/View_after_entrar.fxml"));

                    // Cargo el padre
                    Parent root = loader.load();

                    // Obtengo el controlador
                    View_after_entrarController controlador = loader.getController();
                    controlador.setListaclientes(listaclientes);
                    controlador.recibirUsuario(nuevocliente);
                    controlador.setMistransacciones(transacciones);
                    controlador.setTodastransacciones(transaccionesgene);
                    // Creo la scene y el stage
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();

                    // Asocio el stage con el scene
                    stage.setScene(scene);
                    stage.show();
                    stage.setTitle("Menu Principal");

                    // Ciero la ventana donde estoy
                    Stage myStage = (Stage) btnAceptar.getScene().getWindow();
                    myStage.close();

//                            break;
                } catch (IOException ex) {
                    Logger.getLogger(First_User_ViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        btnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btnEditar.setVisible(true);
                btnAceptar.setVisible(false);
                btnCancelar.setVisible(false);
                lbError.setVisible(false);

                txtNombre.setText(fuvc.getCliente().getNombre());
                txtApellido.setText(fuvc.getCliente().getApellido());
                txtCedula.setText(fuvc.getCliente().getCedula());
                txtDireccion.setText(fuvc.getCliente().getDireccion());
                dtpFechaNacimiento.setValue(fuvc.getCliente().getFnacimiento());
                txtContraseña.setText(fuvc.getCliente().getContrasenna());
                txtUsuario.setText(fuvc.getCliente().getUsuario());
                if (fuvc.getCliente().getSexo() == 'M') {
                    lbSexo.setText("Masculino");
                    tbtnMasculino.setSelected(true);
                } else {
                    lbSexo.setText("Femenino");
                    tbtnFemenino.setSelected(true);
                }
                imagen = fuvc.getCliente().getPerfil();
                imagenurl = imagen;

                Image perfil = new Image(imagenurl);
                imgPerfil.setImage(perfil);

                removerHabilitar();
                deshabilitar();
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

        btnPerfil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cargaAvatares();
                Image nvImagen = new Image(imagen);
                imgPerfil.setImage(nvImagen);
            }
        });
        
        
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

    private void removerDeshabilitado() {
        txtNombre.getStyleClass().remove(des);
        txtApellido.getStyleClass().remove(des);
//        txtCedula.getStyleClass().remove(des);
        txtDireccion.getStyleClass().remove(des);
        txtContraseña.getStyleClass().remove(des);
        txtContrasenaShow.getStyleClass().remove(des);
//        txtUsuario.getStyleClass().remove(des);
        dtpFechaNacimiento.getStyleClass().remove(desDate);
        lbSexo.setVisible(false);
    }

    private void deshabilitar() {
        txtNombre.getStyleClass().add(des);
        txtApellido.getStyleClass().add(des);
//        txtCedula.getStyleClass().add(des);
        txtDireccion.getStyleClass().add(des);
        txtContraseña.getStyleClass().add(des);
        txtContrasenaShow.getStyleClass().add(des);
//        txtUsuario.getStyleClass().add(des);
        dtpFechaNacimiento.getStyleClass().add(desDate);
        lbSexo.setVisible(true);
    }

    private void habilitar() {
        txtNombre.getStyleClass().add(hab);
        txtApellido.getStyleClass().add(hab);
//        txtCedula.getStyleClass().add(hab);
        txtDireccion.getStyleClass().add(hab);
        txtContraseña.getStyleClass().add(hab);
        txtContrasenaShow.getStyleClass().add(hab);
//        txtUsuario.getStyleClass().add(hab);
        dtpFechaNacimiento.getStyleClass().add(habDate);
        tbtnHideContrasena.setVisible(true);
        tbtnFemenino.setVisible(true);
        tbtnMasculino.setVisible(true);
        lnDivisionSexo.setVisible(true);

        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
//        txtCedula.setDisable(false);
        txtDireccion.setDisable(false);
        txtContraseña.setDisable(false);
        txtContrasenaShow.setDisable(false);
//        txtUsuario.setDisable(false);
        btnPerfil.setDisable(false);
        dtpFechaNacimiento.setDisable(false);
        lbSexo.setDisable(false);
    }

    private void removerHabilitar() {
        txtNombre.getStyleClass().remove(hab);
        txtApellido.getStyleClass().remove(hab);
//        txtCedula.getStyleClass().remove(hab);
        txtDireccion.getStyleClass().remove(hab);
        txtContraseña.getStyleClass().remove(hab);
        txtContrasenaShow.getStyleClass().remove(hab);
//        txtUsuario.getStyleClass().remove(hab);
        dtpFechaNacimiento.getStyleClass().remove(habDate);
        tbtnHideContrasena.setVisible(false);
        tbtnFemenino.setVisible(false);
        tbtnMasculino.setVisible(false);
        lnDivisionSexo.setVisible(false);

        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
//        txtCedula.setDisable(true);
        txtDireccion.setDisable(true);
        txtContraseña.setDisable(true);
        txtContrasenaShow.setDisable(true);
//        txtUsuario.setDisable(true);
        btnPerfil.setDisable(true);
        dtpFechaNacimiento.setDisable(true);
        lbSexo.setDisable(true);
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

    private boolean IsEmpty() {
        boolean answer;
        answer = false;
        if (txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtCedula.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtUsuario.getText().isEmpty()
                || txtContraseña.getText().isEmpty() || txtContrasenaShow.getText().isEmpty() || dtpFechaNacimiento.getEditor().getText().isEmpty()
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

    public void setListaclient(ObservableList<Cliente> list) {
        listaclientes = list;
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

    public ObservableList<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(ObservableList<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public ObservableList<Transaccion> getTransaccionesgene() {
        return transaccionesgene;
    }

    public void setTransaccionesgene(ObservableList<Transaccion> transaccionesgene) {
        this.transaccionesgene = transaccionesgene;
    }

}
