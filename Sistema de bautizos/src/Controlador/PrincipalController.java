package Controlador;

import Modelo.Catecumeno;
import Modelo.Catequizando;
import Modelo.Conyugues;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.Initializable;


public class PrincipalController implements Initializable {

    @FXML
    private Button btnbautizos;
    @FXML
    private Button btnmatrimonios;
    @FXML
    private Button btnconfirmaciones;

    private ObservableList<Catecumeno> listaCatecumenos;
    private ObservableList<Catequizando> listaCatequizandos;
    private ObservableList<Conyugues> listaCasados;
    
 
    public PrincipalController() {
        listaCatecumenos = FXCollections.observableArrayList();
        listaCatequizandos = FXCollections.observableArrayList();
        listaCasados = FXCollections.observableArrayList();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
       
        cargarDatosmatrimonios();
        configurarAccionesBotones();
    }

    private void configurarAccionesBotones() {
        if (btnbautizos != null) {
            btnbautizos.setOnAction(event -> handleBautizos());
        }
        if (btnconfirmaciones != null) {
            btnconfirmaciones.setOnAction(event -> handleConfirmacion());
        }
    
     if (btnmatrimonios != null) {
            btnmatrimonios.setOnAction(event -> handleMatrimonios());
        }
    }

    private void handleBautizos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/VistaBautizosmenu.fxml"));
            Parent root = loader.load();
            
            VistaBautizosmenuController controlador = loader.getController();
            controlador.setListaBautizados(listaCatecumenos);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Bautizos");
            stage.setResizable(false);
            stage.getIcons().add(new Image("/Modelo/bautizo2.png"));
            stage.show();

            stage.setOnCloseRequest(e -> controlador.handleClose());
            closeStage(btnbautizos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConfirmacion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/VistaConfirmacion.fxml"));
            Parent root = loader.load();
            
            VistaConfirmacionController controlador = loader.getController();
            controlador.setListaCatequizandos(listaCatequizandos);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Confirmaciónes");
            stage.setResizable(false);
            stage.getIcons().add(new Image("/Modelo/confirmacion.png"));
            stage.show();

            stage.setOnCloseRequest(e -> controlador.handleClose());
            closeStage(btnconfirmaciones);

        } catch (IOException e) {
            e.printStackTrace();
        }
       
         
    }

    private void handleMatrimonios(){
            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/VistaMatrimonios.fxml"));
            Parent root = loader.load();
            
            VistaMatrimoniosController controlador = loader.getController();
            controlador.setListaCasados(listaCasados);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu Matrimonios");
            stage.setResizable(false);
            stage.getIcons().add(new Image("/Modelo/anillos.png"));
            stage.show();

            stage.setOnCloseRequest(e -> controlador.handleClose());
            closeStage(btnmatrimonios);

        } catch (IOException e) {
            e.printStackTrace();
        }
       
         
    }
    
    private void closeStage(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Catecumeno> getListaCatecumenos() {
        return listaCatecumenos;
    }

    public void setListaCatecumenos(ObservableList<Catecumeno> lista) {
        this.listaCatecumenos = lista;
    }

    public ObservableList<Catequizando> getListaCatequizandos() {
        return listaCatequizandos;
    }

    public void setListaCatequizandos(ObservableList<Catequizando> listaCatequizandos) {
        this.listaCatequizandos = listaCatequizandos;
    }
    public ObservableList<Conyugues> getListaCasados() {
        return listaCasados;
    }

    public void setListaCasados(ObservableList<Conyugues> listaCasados) {
        this.listaCasados = listaCasados;
    }

      public void guardarDatosmatrimonios() {
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

  private void guardarDatosconfirmacion() {
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
        System.out.println("Error No se pudo guardar el archivo de confirmandos.");
    }
}
  
  private void guardarDatosBautizos() {
     try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("catecumenos.csv"))) {
        // Crea un conjunto para detectar duplicados
        Set<Catecumeno> uniqueBautizados = new HashSet<>(listaCatecumenos);
        
        // Escribe solo elementos únicos en el archivo
        for (Catecumeno c : uniqueBautizados) {
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%d,%d,%c,%s,%s%n",
                    c.getNombres(), c.getApellidos(), c.getMadre(), c.getPadre(), c.getPadrinos(),
                    c.getPresbitero(), c.getLibro(), c.getFolio(), c.getSexo(),
                    c.getFechaNacimiento(), c.getFechabautismo()));
        }
    } catch (IOException e) {
         System.out.println("Error No se pudo guardar el archivo de datos.");
    }
    }
       //CAARGA DE DATOS BAUTIZOS
    public void cargarDatosBautizos() 
    {
    Path path = Paths.get("catecumenos.csv");

    if (!Files.exists(path)) {
        System.out.println("El archivo no existe.");
        return;
    }

    try (BufferedReader reader = Files.newBufferedReader(path)) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] datos = line.split(",");
            
            // Asegúrate de que hay 11 columnas en la línea
            if (datos.length == 11) {
                try {
                    String nombres = datos[0];
                    String apellidos = datos[1];
                    String madre = datos[2];
                    String padre = datos[3];
                    String padrinos = datos[4];
                    String presbitero = datos[5];
                    int libro = Integer.parseInt(datos[6]);
                    int folio = Integer.parseInt(datos[7]);
                    char sexo = datos[8].charAt(0);
                    LocalDate fechaNacimiento = LocalDate.parse(datos[9]);
                    LocalDate fechaBautismo = LocalDate.parse(datos[10]);
                    
                    // Añade el nuevo Catecumeno a la lista
                    listaCatecumenos.add(new Catecumeno(nombres, apellidos, madre, padre, padrinos, presbitero, libro, folio, sexo, fechaNacimiento, fechaBautismo));
                    System.out.println("Cargado correctamente: " + nombres + " " + apellidos);
                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico en la línea: " + line);
                    e.printStackTrace();
                } catch (DateTimeParseException e) {
                    System.err.println("Error de formato de fecha en la línea: " + line);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Línea ignorada (cantidad incorrecta de datos): " + line);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("No se pudo cargar el archivo de datos.");
    }
}
    //CARGA DE DATOS CONFIRMACION
    public void cargarDatosConfirmacion()
    {
        File archivo = new File("catequizandos.csv");
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] campos = line.split(",");
                    if (campos.length == 9) {
                        Catequizando catequizando = new Catequizando(
                                campos[0], campos[1], campos[2], campos[3], campos[4],
                                Integer.parseInt(campos[5]), Integer.parseInt(campos[6]),
                                campos[7].charAt(0), LocalDate.parse(campos[8])
                        );
                       
                        listaCatequizandos.add(catequizando);
                        System.out.println("Cargado correctamente");
                    }
                }
              
            } catch (IOException e) {
                System.out.println("Error No se pudo cargar el archivo de confirmandos.");
            }
        }
    }
    //CARGA DE DATOS MATRIMONIOS
    public void cargarDatosmatrimonios() 
    {
       File archivo = new File("matrimonios.csv");
    if (archivo.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] campos = line.split(",");
                if (campos.length == 9) {  // Ajusta el número de campos según tu archivo CSV
                    Conyugues conyugue;
                    conyugue = new Conyugues(
                            Integer.valueOf(campos[0]), // Ajusta el índice según el orden de los campos en tu CSV
                            Integer.valueOf(campos[1]), // Ajusta el índice según el orden de los campos en tu CSV
                            Integer.valueOf(campos[2]), // Ajusta el índice según el orden de los campos en tu CSV
                            Integer.valueOf(campos[3]), // Ajusta el índice según el orden de los campos en tu CSV
                            campos[4],
                            campos[5],
                            campos[6],
                            campos[7],
                            LocalDate.parse(campos[8])
                            // Ajusta si es necesario
                            // Agrega más campos según tu estructura de datos
                    );
                    listaCasados.add(conyugue);
                    System.out.println("Cargado correctamente" + " " + campos[4] + " y " + campos[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: No se pudo cargar el archivo de matrimonios.");
        }
    }
    }
    //METODO PARA GUARDAR TODAS LAS LISTAS
    public void guardartodo()
    {
    guardarDatosBautizos();
    guardarDatosconfirmacion();
    guardarDatosmatrimonios();
    }
   //METODO PARA CARGAR TODO AL MOMENTO EN EL QUE SE INICIALIZA EL PROGRAMA 
    public void cargartodo()
    {
    listaCatecumenos.clear(); // Limpiar la lista antes de cargar datos
    listaCatequizandos.clear(); // Limpiar la lista antes de cargar datos
    listaCasados.clear(); // Limpiar la lista antes de cargar datos
    cargarDatosBautizos();
     cargarDatosmatrimonios();
    cargarDatosConfirmacion();
  
    }
    
    
}
