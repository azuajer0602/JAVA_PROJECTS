package Controlador;

import Modelo.*;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TransaccionesController implements Initializable {

    @FXML
    private Button btnDepositar;

    @FXML
    private Button btnRetirar;

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnlimpiar;

    @FXML
    private Button btnpagar;

    @FXML
    private Label lbError;

    @FXML
    private Label lbError2;

    @FXML
    private Label lbError3;

    @FXML
    private Label lbSaldoC;

    @FXML
    private Label lbSaldoCRetiro;

    @FXML
    private Pane pnDeposito;

    @FXML
    private Pane pnPago;

    @FXML
    private Pane pnRetiro;

    @FXML
    private Button tbnLimpiarDeposito;

    @FXML
    private Button btnLimpiarRetiro;

    @FXML
    private ToggleButton tbtnDeposito;

    @FXML
    private ToggleButton tbtnPago;

    @FXML
    private ToggleButton tbtnRetiro;

    @FXML
    private TextField txtCedula;

    @FXML
    private TextField txtMonto;

    @FXML
    private TextField txtMontoDeposito;

    @FXML
    private TextField txtMontoRetiro;

    @FXML
    private TextField txtNumMovimientoRetiro;

    @FXML
    private TextField txtNunMovimiento;

    @FXML
    private TextField txtNunMovimientoDeposito;

    @FXML
    private TextField txtUsuario;


    private Cliente x;

    private ObservableList<Cliente> listaclientes;
    private ObservableList<Transaccion> trasaccionespersonales;
    private ObservableList<Transaccion> trasaccionesgenerales;
    private ObservableList<Cliente> Directorio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lbError.setVisible(false);
        lbError2.setVisible(false);
        lbError3.setVisible(false);
        
        
        
        
        errorView();
        verificarCampos();

        pnDeposito.setVisible(false);
        pnRetiro.setVisible(false);
        Directorio = FXCollections.observableArrayList();
        ToggleGroup tbnsTransacciones = new ToggleGroup();
        tbtnRetiro.setToggleGroup(tbnsTransacciones);
        tbtnDeposito.setToggleGroup(tbnsTransacciones);
        tbtnPago.setToggleGroup(tbnsTransacciones);

        tbtnPago.setSelected(true);
        
        tbnsTransacciones.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });

        
        tbtnPago.setOnAction(event -> {
            if (tbtnPago.isSelected()) {
                pnDeposito.setVisible(false);
                pnRetiro.setVisible(false);
                pnPago.setVisible(true);
            }
        });

       
        tbtnDeposito.setOnAction(event -> {
            if (tbtnDeposito.isSelected()) {
                pnRetiro.setVisible(false);
                pnPago.setVisible(false);
                pnDeposito.setVisible(true);
                lbSaldoC.setText(String.format("%.2f", x.getMontoC()));
            }
        });
        
        tbtnRetiro.setOnAction(event ->{
        if (tbtnRetiro.isSelected()) {
                
                pnPago.setVisible(false);
                pnDeposito.setVisible(false);
                pnRetiro.setVisible(true);
                lbSaldoCRetiro.setText(String.format("%.2f", x.getMontoC()));
            }
        });

        
        btnpagar.setOnAction(event -> {
            try {
                double monto = Double.parseDouble(txtMonto.getText());
                String descripcion = txtNunMovimiento.getText();
                TransactionType tipo = TransactionType.PAGO;
                String UsuarioEmisor = x.getUsuario();
                String UsuarioReceptor = txtUsuario.getText();
                String cedulaReceptor = txtCedula.getText();

                Cliente auxiliar = null;

                // Buscar el receptor en la lista de clientes
                for (Cliente receptor : listaclientes) {
                    if (receptor.getCedula().equals(cedulaReceptor) && receptor.getUsuario().equals(UsuarioReceptor)) {
                        auxiliar = receptor;
                        break;
                    }
                }

                // Verificar si se encontró el receptor
                if (auxiliar == null) {
                    errorMsg("No se encontró el receptor.");
                    return;
                }
                
                if (monto > x.getMontoC()) {
                    errorMsg("Saldo inficiente para realizar el pago");
                    return;
                }
                
                if (monto < 0) {
                    errorMsg("No puede introducir un numero negativo");
                    return;
                }
                
                if (Integer.parseInt(txtNunMovimiento.getText()) < 0) {
                    errorMsg("No puede introducir un numero negativo");
                    return;
                }
                
                for (Transaccion transaccion : trasaccionespersonales) {
                    if (descripcion.equals(transaccion.getNumMovimiento())) {
                        errorMsg("Ya existe este numero de movimiento");
                        return;
                    }
                }

                // Crear la transacción
                Transaccion transaccion = new Transaccion(LocalDate.now(), monto, descripcion, tipo, UsuarioEmisor, UsuarioReceptor, cedulaReceptor);
                trasaccionesgenerales.add(transaccion);
                trasaccionespersonales.add(transaccion);

                // Actualizar los montos
                double montofinal = monto + auxiliar.getMontoC();
                auxiliar.setMontoC(montofinal);

                double montofinalemisor = x.getMontoC() - monto;
                x.setMontoC(montofinalemisor);

                // Mostrar mensaje de éxito
                alertMsg(event, 'c', "Pago realizado con éxito");

                // Cargar la nueva vista
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/View_after_entrar.fxml"));
                Parent root = loader.load();
                View_after_entrarController controlador = loader.getController();
                controlador.setListaclientes(listaclientes);
                controlador.setTodastransacciones(trasaccionesgenerales);
                controlador.setMistransacciones(trasaccionespersonales);
                controlador.recibirUsuario(x);

                // Mostrar la nueva ventana
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Menu Principal");
                stage.show();
                       stage.getIcons().add(new Image("/Vista/imagenes/Logo Bank Blanco sin fondo.png"));

                // Cerrar la ventana actual
                Stage myStage = (Stage) btnpagar.getScene().getWindow();
                myStage.close();

            } catch (NumberFormatException e) {
                errorMsg("El monto ingresado no es válido.");
            } catch (IOException ex) {
                Logger.getLogger(TransaccionesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

       
        btnDepositar.setOnAction(event -> {
            try {
                double monto = Double.parseDouble(txtMontoDeposito.getText());
                String descripcion = txtNunMovimientoDeposito.getText();
                TransactionType tipo = TransactionType.DEPOSITO;
                String UsuarioEmisor = "Deposito";
                String UsuarioReceptor = x.getUsuario(); // Parece que siempre es el mismo usuario
                String cedulaReceptor = x.getCedula(); // Parece que siempre es el mismo usuario
                
                if (Integer.parseInt(txtNunMovimientoDeposito.getText()) < 0) {
                    errorMsg2("No puede introducir un numero negativo");
                    return;
                }
                
                if (monto < 0) {
                    errorMsg2("No puede introducir un numero negativo");
                    return;
                }
                
                for (Transaccion transaccion : trasaccionespersonales) {
                    if (descripcion.equals(transaccion.getNumMovimiento())) {
                        errorMsg2("Ya existe este numero de movimiento");
                        return;
                    }
                }

                Transaccion transaccion = new Transaccion(LocalDate.now(), monto, descripcion, tipo, UsuarioEmisor, UsuarioReceptor, cedulaReceptor);
                trasaccionesgenerales.add(transaccion);
                trasaccionespersonales.add(transaccion);

                double saldo = x.getMontoC();
                x.setMontoC(saldo + monto);

                // Mostrar mensaje de éxito
                alertMsg(event, 'c', "Depósito Realizado con éxito");

                // Cargar la nueva vista
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/View_after_entrar.fxml"));
                Parent root = loader.load();
                View_after_entrarController controlador = loader.getController();
                controlador.setListaclientes(listaclientes);
                controlador.setTodastransacciones(trasaccionesgenerales);
                controlador.setMistransacciones(trasaccionespersonales);
                controlador.recibirUsuario(x);

                // Mostrar la nueva ventana
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Menu Principal");
                stage.show();

                // Cerrar la ventana actual
                Stage myStage = (Stage) btnDepositar.getScene().getWindow();
                myStage.close();

            } catch (NumberFormatException e) {
                errorMsg2("El monto ingresado no es válido.");
            } catch (IOException ex) {
                Logger.getLogger(TransaccionesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Configurar el evento para el botón Limpiar en Depósito
        tbnLimpiarDeposito.setOnAction(event -> {
            txtMontoDeposito.clear();
            txtNunMovimientoDeposito.clear();
        });

        // Configurar el evento para el botón Limpiar en Retiro
        btnlimpiar.setOnAction(event -> {
            txtMonto.clear();
            txtNunMovimiento.clear();
            txtCedula.clear();
            txtUsuario.clear();
        });
        
        btnLimpiarRetiro.setOnAction(event ->{
        txtMontoRetiro.clear();
        txtNumMovimientoRetiro.clear();
        });

        btnUsuarios.setOnAction(event -> {
            Directorio.clear();
            for (Cliente c : listaclientes) {
                if (!c.getCedula().equals(x.getCedula()) && !c.getCedula().equals(x.getUsuario())) {
                    Directorio.add(c);
                    System.out.println("Anadido correctamente");
                }

            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Paginas/Directorio.fxml"));
                Parent root = loader.load();

                DirectorioController controller = loader.getController();
                controller.setListaclientes(Directorio);
                controller.Settabla();
                Stage dialogStage = new Stage();
                Scene scene = new Scene(root);
                dialogStage.setTitle("Seleccionar Cliente");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initStyle(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);
                dialogStage.setResizable(false);

                dialogStage.setScene(scene);
                controller.setDialogStage(dialogStage);
                dialogStage.showAndWait();

                Cliente selectedCliente = controller.getSeleccionado();
                if (selectedCliente != null) {
                    txtCedula.setText(selectedCliente.getCedula());
                    txtUsuario.setText(selectedCliente.getUsuario());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        
           btnRetirar.setOnAction(event -> {
            try {
                double monto = Double.parseDouble(txtMontoRetiro.getText());
                String descripcion = txtNumMovimientoRetiro.getText();
                TransactionType tipo = TransactionType.RETIRO;
                String UsuarioEmisor = "Retiro";
                String UsuarioReceptor = x.getUsuario(); // Parece que siempre es el mismo usuario
                String cedulaReceptor = x.getCedula(); // Parece que siempre es el mismo usuario
                
                if (monto> x.getMontoC()) {
                    errorMsg3("Saldo insuficiente");
                    return;
                }
                
                if (monto < 0) {
                    errorMsg3("No puede introducir un numero negativo");
                    return;
                }
                
                for (Transaccion transaccion : trasaccionespersonales) {
                    if (descripcion.equals(transaccion.getNumMovimiento())) {
                        errorMsg("Ya existe este numero de movimiento");
                        return;
                    }
                }

                Transaccion transaccion = new Transaccion(LocalDate.now(), monto, descripcion, tipo, UsuarioEmisor, UsuarioReceptor, cedulaReceptor);
                trasaccionesgenerales.add(transaccion);
                trasaccionespersonales.add(transaccion);

                double saldo = x.getMontoC();
                x.setMontoC(saldo - monto);

                // Mostrar mensaje de éxito
                alertMsg(event, 'c', "Retiro Realizado con éxito");

                // Cargar la nueva vista
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/View_after_entrar.fxml"));
                Parent root = loader.load();
                View_after_entrarController controlador = loader.getController();
                controlador.setListaclientes(listaclientes);
                controlador.setTodastransacciones(trasaccionesgenerales);
                controlador.setMistransacciones(trasaccionespersonales);
                controlador.recibirUsuario(x);

                // Mostrar la nueva ventana
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Menu Principal");
                stage.show();

                // Cerrar la ventana actual
                Stage myStage = (Stage) btnDepositar.getScene().getWindow();
                myStage.close();

            } catch (NumberFormatException e) {
                errorMsg2("El monto ingresado no es válido.");
            } catch (IOException ex) {
                Logger.getLogger(TransaccionesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            

    
       
                   }
    public Cliente getX() {
        return x;
    }

    public void setX(Cliente x) {
        this.x = x;
    }

    public ObservableList<Cliente> getListaclientes() {
        return listaclientes;
    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    public ObservableList<Transaccion> getTrasaccionespersonales() {
        return trasaccionespersonales;
    }

    public void setTrasaccionespersonales(ObservableList<Transaccion> trasaccionespersonales) {
        this.trasaccionespersonales = trasaccionespersonales;
    }

    public ObservableList<Transaccion> getTrasaccionesgenerales() {
        return trasaccionesgenerales;
    }

    public void setTrasaccionesgenerales(ObservableList<Transaccion> trasaccionesgenerales) {
        this.trasaccionesgenerales = trasaccionesgenerales;
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

    private void errorMsg(String msg) {
        lbError.setText(msg);
        lbError.setVisible(true);
    }

    private void errorMsg2(String msg) {
        lbError2.setText(msg);
        lbError2.setVisible(true);
    }
    
    private void errorMsg3(String msg) {
        lbError3.setText(msg);
        lbError3.setVisible(true);
    }
    
    private void errorView() {
        txtCedula.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtNunMovimiento.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtNunMovimientoDeposito.setOnKeyTyped(event -> {
            lbError2.setVisible(false);
        });

        txtMonto.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtUsuario.setOnKeyTyped(event -> {
            lbError.setVisible(false);
        });

        txtMontoDeposito.setOnKeyTyped(event -> {
            lbError2.setVisible(false);
        });
        
        txtMontoRetiro.setOnAction(event -> {
            lbError3.setVisible(false);
        });
        
        txtNumMovimientoRetiro.setOnAction(event ->{
            lbError3.setVisible(false);
        });

    }
    
    private void verificarCampos() {

        txtCedula.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (!Character.isDigit(caracter)) {
                event.consume();
                errorMsg("No se permite caracteres en el campo de cedula");
            } else {
                lbError.setVisible(false);
            }
        });

        txtMonto.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (!Character.isDigit(caracter)) {
                if (caracter == '.' && txtMonto.getText().contains(".")) {
                    event.consume();
                    errorMsg("No se permiten más de un punto en el campo del monto");
                } else if (caracter != '.') {
                    event.consume();
                    errorMsg("No se permiten caracteres en el campo del monto");
                }
            } else {
                lbError.setVisible(false);
            }
        });
        
        txtMontoDeposito.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (!Character.isDigit(caracter)) {
                if (caracter == '.' && txtMontoDeposito.getText().contains(".")) {
                    event.consume();
                    errorMsg2("No se permiten más de un punto en el campo del monto");
                } else if (caracter != '.') {
                    event.consume();
                    errorMsg2("No se permiten caracteres en el campo del monto");
                }
            } else {
                lbError2.setVisible(false);
            }
        });
        
        txtMontoRetiro.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (!Character.isDigit(caracter)) {
                if (caracter == '.' && txtMontoDeposito.getText().contains(".")) {
                    event.consume();
                    errorMsg3("No se permiten más de un punto en el campo del monto");
                } else if (caracter != '.') {
                    event.consume();
                    errorMsg3("No se permiten caracteres en el campo del monto");
                }
            } else {
                lbError3.setVisible(false);
            }
        });

        txtUsuario.setOnKeyTyped(event -> {
            char caracter = event.getCharacter().charAt(0);
            if (Character.isWhitespace(caracter)) {
                event.consume();
                errorMsg("No se permiten espacios en blanco en el campo del usuario");
            } else {
                lbError.setVisible(false);
            }
        });
        
        txtNumMovimientoRetiro.setOnKeyTyped(event ->{
            char caracter = event.getCharacter().charAt(0);
            String texto = txtNumMovimientoRetiro.getText();
            if (!Character.isDigit(caracter)) {
                event.consume();
                errorMsg3("No se permite caracteres en el numero del movimiento");
            } else if (texto.length() > 15) {
                event.consume();
                errorMsg3("Se excedieron los 15 caracteres en el campo de movimiento");
            }else {
                lbError3.setVisible(false);
            }
        });
        
        txtNunMovimientoDeposito.setOnKeyTyped(event ->{
            char caracter = event.getCharacter().charAt(0);
            String texto = txtNunMovimientoDeposito.getText();
            if (!Character.isDigit(caracter)) {
                event.consume();
                errorMsg2("No se permite caracteres en el numero del movimiento");
            } else if (texto.length() > 15) {
                event.consume();
                errorMsg2("Se excedieron los 15 caracteres en el campo de movimiento");
            }else {
                lbError2.setVisible(false);
            }
        });
        
        txtNunMovimiento.setOnKeyTyped(event ->{
            char caracter = event.getCharacter().charAt(0);
            String texto = txtNunMovimiento.getText();
            if (!Character.isDigit(caracter)) {
                event.consume();
                errorMsg("No se permite caracteres en el numero del movimiento");
            } else if (texto.length() > 15) {
                event.consume();
                errorMsg("Se excedieron los 15 caracteres en el campo de movimiento");
            } else {
                lbError.setVisible(false);
            }
        });
        
        

    }


}
