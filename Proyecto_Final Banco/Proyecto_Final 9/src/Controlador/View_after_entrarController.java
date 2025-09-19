package Controlador;

import Modelo.After_entrar_cargador;
import Modelo.Cliente;
import Modelo.Transaccion;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class View_after_entrarController implements Initializable {

    @FXML
    private BorderPane bpnBase;

    @FXML
    private Button btnSalir;

    @FXML
    private HBox hbxInf;

    @FXML
    private HBox hbxSup;

    @FXML
    private ImageView imgPerfil;

    @FXML
    private Label lbNUsuario;

    @FXML
    private ToggleButton tbtnConsultas;

    @FXML
    private ToggleButton tbtnHome;

    @FXML
    private ToggleButton tbtnIntereses;

    @FXML
    private ToggleButton tbtnPerfil;

    @FXML
    private ToggleButton tbtnTransaccion;

    private String n = "normal";
    private String ia = "izqArriba";
    private String ib = "izqAbajo";
    private String da = "dchArriba";
    private String db = "dchAbajo";
    private String sl = "selec";
    private ObservableList<Cliente> listaclientes;
    private Cliente C;

    private Stage stage;  // Referencia al Stage principal
    private Scene scene;

    private First_User_ViewController fuvc;
    private ObservableList<Transaccion> mistransacciones;
    private ObservableList<Transaccion> todastransacciones;
    
    private Boolean cerrar;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaclientes = FXCollections.observableArrayList();
        mistransacciones = FXCollections.observableArrayList();
        todastransacciones = FXCollections.observableArrayList();
        hbxSup.getStyleClass().add(n);
        tbtnHome.getStyleClass().add(n);
        tbtnTransaccion.getStyleClass().add(n);
        tbtnIntereses.getStyleClass().add(n);
        tbtnConsultas.getStyleClass().add(n);
        hbxInf.getStyleClass().add(n);

        ToggleGroup menu = new ToggleGroup();

        tbtnHome.setToggleGroup(menu);
        tbtnTransaccion.setToggleGroup(menu);
        tbtnIntereses.setToggleGroup(menu);
        tbtnConsultas.setToggleGroup(menu);
        tbtnPerfil.setToggleGroup(menu);

        tbtnHome.setSelected(true);
        applyEstiloSeleccionado(tbtnHome);

        cargaPantalla("Principal");
        
        cerrar = false;

//        cargaAvatares();
        menu.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            resetEstilos();
            if (newToggle != null) {
                ToggleButton selectedButton = (ToggleButton) newToggle;
                applyEstiloSeleccionado(selectedButton);
            } else {
                oldToggle.setSelected(true);
            }
        });

        btnSalir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cerrarSesion(event);
                
                if (cerrar) {
                    
                
                try {
                    // Carga el archivo FXML y obtiene el controlador
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/First_User_View.fxml"));
                    Parent root = loader.load();

                    // Obtén el controlador desde el loader
                    First_User_ViewController controlador = loader.getController();
                    controlador.setListaclientes(listaclientes);
                    controlador.setTransaccionesgenerales(todastransacciones);
                    
                    // Llama al método para guardar los datos
                    controlador.guardarDatos();
                    controlador.guardarTransacciones();
                    mistransacciones.clear();                   // Crea una nueva escena y la muestra en un nuevo escenario
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Inicio de sesion");
                    stage.show();

                    // Cierra la ventana actual
                    Stage myStage = (Stage) btnSalir.getScene().getWindow();
                    myStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace(); // Puedes añadir un manejo de errores más robusto aquí
                }}
            }
        });

        tbtnHome.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cargaPantalla("Principal");
            }
        });

        tbtnTransaccion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
         //       cargaPantalla("Transacciones");
                 try {
                    URL direccion = getClass().getResource("/Vista/Paginas/Transacciones.fxml");

                    if (direccion == null) {
                        throw new IOException("No se encontró el archivo FXML: Transacciones");
                    }

                    FXMLLoader cargador = new FXMLLoader(direccion);

                    Pane pantalla = cargador.load();
                    TransaccionesController controller = cargador.getController();
                    controller.setListaclientes(listaclientes);
                    controller.setTrasaccionespersonales(mistransacciones);
                    controller.setTrasaccionesgenerales(todastransacciones);
                    controller.setX(C);
                    
                    bpnBase.setCenter(pantalla);
                } catch (IOException e) {
                    System.err.println("Error al cargar el archivo FXML: transacciones");
                    e.printStackTrace();
                }
         
         
            }
        });

        tbtnIntereses.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                cargaPantalla("Intereses");
                try {
                    URL direccion = getClass().getResource("/Vista/Paginas/Intereses.fxml");

                    if (direccion == null) {
                        throw new IOException("No se encontró el archivo FXML: Intereses");
                    }

                    FXMLLoader cargador = new FXMLLoader(direccion);

                    Pane pantalla = cargador.load();
                    InteresesController controller = cargador.getController();
                    controller.setTrasaccionespersonales(mistransacciones);
                    controller.setTrasaccionesgenerales(todastransacciones);
                    controller.setListaclientes(listaclientes);
                    controller.setcliente(C);
                    controller.setUltimInteres(C.getUltimInteres());

                    bpnBase.setCenter(pantalla);
                } catch (IOException e) {
                    System.err.println("Error al cargar el archivo FXML: Intereses");
                    e.printStackTrace();
                }
            }
        });

        tbtnConsultas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
         //       cargaPantalla("Consultas");
         
         
                try {
                    URL direccion = getClass().getResource("/Vista/Paginas/Consultas.fxml");

                    if (direccion == null) {
                        throw new IOException("No se encontró el archivo FXML: Consultas");
                    }

                    FXMLLoader cargador = new FXMLLoader(direccion);

                    Pane pantalla = cargador.load();
                    ConsultasController controller = cargador.getController();
                    controller.setCuentasActivas(listaclientes);
                    controller.setMistransacciones(mistransacciones);
                    controller.setTodastransacciones(todastransacciones);
                   
                    controller.Settabla();
                    bpnBase.setCenter(pantalla);
                } catch (IOException e) {
                    System.err.println("Error al cargar el archivo FXML: consultas");
                    e.printStackTrace();
                }
         
         
            }
        });

        tbtnPerfil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                cargaPantalla("Perfil");

                try {
                    URL direccion = getClass().getResource("/Vista/Paginas/Perfil.fxml");

                    if (direccion == null) {
                        throw new IOException("No se encontró el archivo FXML: Perfil");
                    }

                    FXMLLoader cargador = new FXMLLoader(direccion);

                    Pane pantalla = cargador.load();
                    PerfilController controller = cargador.getController();
                    controller.setListaclient(listaclientes);
                    controller.setTransacciones(mistransacciones);
                    controller.setTransaccionesgene(todastransacciones);

                    bpnBase.setCenter(pantalla);
                } catch (IOException e) {
                    System.err.println("Error al cargar el archivo FXML: Perfil");
                    e.printStackTrace();
                }

            }
        });

    }

    private void resetEstilos() {
        tbtnHome.getStyleClass().removeAll(ia, ib, da, db, sl);
        tbtnTransaccion.getStyleClass().removeAll(ia, ib, da, db, sl);
        tbtnIntereses.getStyleClass().removeAll(ia, ib, da, db, sl);
        tbtnConsultas.getStyleClass().removeAll(ia, ib, da, db, sl);
        hbxSup.getStyleClass().removeAll(ia, ib, da, db);
        hbxInf.getStyleClass().removeAll(ia, ib, da, db);

        hbxSup.getStyleClass().remove(n);
        tbtnHome.getStyleClass().remove(n);
        tbtnTransaccion.getStyleClass().remove(n);
        tbtnIntereses.getStyleClass().remove(n);
        tbtnConsultas.getStyleClass().remove(n);
        hbxInf.getStyleClass().remove(n);
    }

    private void applyEstiloSeleccionado(ToggleButton selectedButton) {
        if (selectedButton == tbtnHome) {
            hbxSup.getStyleClass().remove(n);
            tbtnHome.getStyleClass().remove(n);
            tbtnTransaccion.getStyleClass().remove(n);

            hbxSup.getStyleClass().add(db);
            selectedButton.getStyleClass().add(sl);
            tbtnTransaccion.getStyleClass().add(da);

            tbtnIntereses.getStyleClass().add(n);
            tbtnConsultas.getStyleClass().add(n);
            hbxInf.getStyleClass().add(n);
        } else if (selectedButton == tbtnTransaccion) {
            tbtnHome.getStyleClass().remove(n);
            tbtnTransaccion.getStyleClass().remove(n);
            tbtnIntereses.getStyleClass().remove(n);

            tbtnHome.getStyleClass().add(db);
            selectedButton.getStyleClass().add(sl);
            tbtnIntereses.getStyleClass().add(da);

            tbtnConsultas.getStyleClass().add(n);
            hbxInf.getStyleClass().add(n);
            hbxSup.getStyleClass().add(n);
        } else if (selectedButton == tbtnIntereses) {
            tbtnTransaccion.getStyleClass().remove(n);
            tbtnIntereses.getStyleClass().remove(n);
            tbtnConsultas.getStyleClass().remove(n);

            tbtnTransaccion.getStyleClass().add(db);
            selectedButton.getStyleClass().add(sl);
            tbtnConsultas.getStyleClass().add(da);

            hbxInf.getStyleClass().add(n);
            hbxSup.getStyleClass().add(n);
            tbtnHome.getStyleClass().add(n);
        } else if (selectedButton == tbtnConsultas) {
            tbtnIntereses.getStyleClass().remove(n);
            tbtnConsultas.getStyleClass().remove(n);
            hbxInf.getStyleClass().remove(n);

            tbtnIntereses.getStyleClass().add(db);
            selectedButton.getStyleClass().add(sl);
            hbxInf.getStyleClass().add(da);

            hbxSup.getStyleClass().add(n);
            tbtnHome.getStyleClass().add(n);
            tbtnTransaccion.getStyleClass().add(n);
        } else if (selectedButton == tbtnPerfil) {
            hbxSup.getStyleClass().add(n);
            tbtnHome.getStyleClass().add(n);
            tbtnTransaccion.getStyleClass().add(n);
            tbtnIntereses.getStyleClass().add(n);
            tbtnConsultas.getStyleClass().add(n);
            hbxInf.getStyleClass().add(n);
        }
    }

    private void cargaPantalla(String n) {
        After_entrar_cargador carga = new After_entrar_cargador();
        Pane pantalla = carga.obtenerPantalla(n);
        bpnBase.setCenter(pantalla);
    }

    public void recibirUsuario(Cliente cliente) {
        C = cliente;
        lbNUsuario.setText(C.getUsuario());
        Image ftPerfil = new Image(C.getPerfil());
        imgPerfil.setImage(ftPerfil);
    }

    public ObservableList<Cliente> getListaclientes() {
        return listaclientes;
    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    public void refrescarVista() {
        bpnBase.requestLayout();
        // o
//    bpnBase.layout();
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
    
    private void cerrarSesion(ActionEvent event) {
        Stage alertStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double x = alertStage.getX();
        double y = alertStage.getY();

        try {
            FXMLLoader carga = new FXMLLoader(getClass().getResource("/Vista/CerrarSesion_Alert.fxml"));
            Parent root = carga.load();
            CerrarSesion_AlertController controller = carga.getController();

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
            cerrar = controller.getSeleccion();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
