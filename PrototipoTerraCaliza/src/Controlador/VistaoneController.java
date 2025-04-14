package Controlador;

import Modelo.Movimiento; // Asegúrate de importar tu clase Movimiento
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook; // Para archivos .xls
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Para archivos .xlsx
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.util.Callback;

public class VistaoneController implements Initializable {

    @FXML
    private Button Openbutton;
    @FXML
    private TableView<Movimiento> Tablamov;
    @FXML
    private TableColumn<Movimiento, LocalDate> colfecha;
    @FXML
    private TableColumn<Movimiento, Integer> colcodigo;
    @FXML
    private TableColumn<Movimiento, String> coltipotra;
    @FXML
    private TableColumn<Movimiento, String> coltipoopera;
    @FXML
    private TableColumn<Movimiento, String> coldescripcion;
    @FXML
    private TableColumn<Movimiento, Integer> colreferencia;
    @FXML
    private TableColumn<Movimiento, Double> coldebe;
    @FXML
    private TableColumn<Movimiento, Double> colhaber;
    @FXML
    private TableColumn<Movimiento, Double> collsado;

      private ObservableList<Movimiento> movimientos;
      private ObservableList<Movimiento> transferencias;
      private ObservableList<Movimiento> pagomovil;
      private ObservableList<Movimiento> compras;
      private ObservableList<Movimiento> comisiones;
      private ObservableList<Movimiento> nomina;
      private ObservableList<Movimiento> creditos;
      private ObservableList<Movimiento> divisas;
    @FXML
    private Button btntransferencias;
    @FXML
    private Button btnpagomovil;
    @FXML
    private Button btncompra;
    @FXML
    private Button btncomisiones;
    @FXML
    private Button btnnomina;
    @FXML
    private Button btncreditos;
    @FXML
    private Button btndivisas;
    @FXML
    private Button btnexportar;
    @FXML
    private Button btnlimpiardatos;
    @FXML
    private TextField txtbuscar;
    @FXML
    private Label labelingresos;
    @FXML
    private Label labelegresos;
    @FXML
    private Label labelsaldo;
    
    public  String Nombrecuenta = null;
    @FXML
    public Label labelcuenta;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movimientos = FXCollections.observableArrayList(); // Inicializar la lista
        transferencias = FXCollections.observableArrayList(); // Inicializar la lista
        pagomovil = FXCollections.observableArrayList(); // Inicializar la lista
        compras = FXCollections.observableArrayList(); // Inicializar la lista
        comisiones = FXCollections.observableArrayList(); // Inicializar la lista
        nomina = FXCollections.observableArrayList(); // Inicializar la lista
        creditos = FXCollections.observableArrayList(); // Inicializar la lista
        divisas= FXCollections.observableArrayList(); // Inicializar la lista

                
        Tablamov.setItems(movimientos); // Vincular la lista a la TableView
 // Configurar las columnas de la tabla
    colfecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
    
    // Configurar el CellFactory para aplicar el formato de fecha
    colfecha.setCellFactory(col -> new TableCell<Movimiento, LocalDate>() {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        @Override
        protected void updateItem(LocalDate item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.format(formatter)); // Formatear la fecha
            }
        }
    });
      // Configurar las columnas de la tabla
    colfecha.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
    colcodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty().asObject());
    coltipotra.setCellValueFactory(cellData -> cellData.getValue().tipotProperty());
    coltipoopera.setCellValueFactory(cellData -> cellData.getValue().tipoOProperty());
    coldescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
     colreferencia.setCellValueFactory(cellData -> cellData.getValue().referenciaProperty().asObject());
    coldebe.setCellValueFactory(cellData -> cellData.getValue().debeProperty().asObject());
    colhaber.setCellValueFactory(cellData -> cellData.getValue().haberProperty().asObject());
    collsado.setCellValueFactory(cellData -> cellData.getValue().saldoProperty().asObject());

    
    
    
// Configurar la columna de debe
coldebe.setCellValueFactory(cellData -> cellData.getValue().debeProperty().asObject());
coldebe.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("#,##0.00;'-'#,##0.00"); // Formato para mostrar negativos con signo de resta

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Si el valor es cero, mostrar "0.00" sin signo
                    if (item == 0) {
                        setText(df.format(0)); // Mostrar cero sin signo
                    } else {
                        setText(df.format(-Math.abs(item))); // Formatear el número como negativo
                    }
                }
            }
        };
    }
});

// Configurar la columna de haber
colhaber.setCellValueFactory(cellData -> cellData.getValue().haberProperty().asObject());
colhaber.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("+#,##0.00;'-'#,##0.00"); // Formato para mostrar positivos con signo de suma

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Si el valor es cero, mostrar "0.00" sin signo
                    if (item.equals(0.0)) {
                        setText("0.00"); // Mostrar cero sin signo
                    } else {
                        // Mostrar el valor normalmente, asegurando que sea positivo
                        setText(df.format(item)); // Formatear el número según el formato definido
                    }
                }
            }
        };
    }
});
// Configurar la columna de saldo (sin cambios en el formato)
collsado.setCellValueFactory(cellData -> cellData.getValue().saldoProperty().asObject());
collsado.setCellFactory(new Callback<TableColumn<Movimiento, Double>, TableCell<Movimiento, Double>>() {
    private final DecimalFormat df = new DecimalFormat("#,##0.00"); // Formato estándar

    @Override
    public TableCell<Movimiento, Double> call(TableColumn<Movimiento, Double> param) {
        return new TableCell<Movimiento, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(df.format(item)); // Formatear el número normalmente
                }
            }
        };
    }
});
    
    
    
//el boton que abre el archivo de excel
        Openbutton.setOnAction(event -> {
            try {
                openFile();
                
                Agregartransferencias();
                Agregarpagomovil();
                AgregarCompras();
                AgregarComisiones();
                AgregarNomina();
                AgregarCredito();
                AgregarDivisas();
                for(Movimiento c : pagomovil){System.out.println(c.toString());}
            } catch (IOException ex) {
                Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Boton que abre la seccion de transferencias.
        btntransferencias.setOnAction(event ->{
       if(transferencias.isEmpty()){
          Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Transferencias en el archivo");
        alerta.showAndWait();
       }else{
       
            
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistatransferencias.fxml"));
    Parent root = loader.load();
    VistatransferenciasController controller = loader.getController();
  
        controller.setNombrecuenta(Nombrecuenta);
//Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamov.setItems(transferencias);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
    controller.setComisiones(comisiones);
    controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
   
    controller.labelcuenta.setText(Nombrecuenta);
    for(Movimiento c : pagomovil){System.out.println("listo el pago movil"+c.toString());}
   
    if (controller != null) {
        System.out.println("Controlador VistatransferenciasController cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistatransferencias.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btntransferencias.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


        }
        
        });
        
        
        
        //Boton que abre la seccion de pago movil
        btnpagomovil.setOnAction(event -> {
         if(pagomovil.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo pago movil en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistapagomovil.fxml"));
    Parent root = loader.load();
    VistapagomovilController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovpm.setItems(pagomovil);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
    controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
    controller.setNombrecuenta(Nombrecuenta);
        controller.labelcuenta.setText(Nombrecuenta);

    for(Movimiento c : pagomovil){System.out.println("listo el pago movil"+c.toString());}
   
    if (controller != null) {
        System.out.println("Controlador Vistapagomovil cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistatransferencias.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btntransferencias.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        
        });
        
        
        btncompra.setOnAction(event ->{
        
         if(compras.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Compras Maestro en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistacompra.fxml"));
    Parent root = loader.load();
    VistacompraController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovcompra.setItems(compras);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
    controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
    controller.setNombrecuenta(Nombrecuenta);
       controller.labelcuenta.setText(Nombrecuenta);

    if (controller != null) {
        System.out.println("Controlador Vistacompra cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistatransferencias.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btntransferencias.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        
        });
        
        
        btncomisiones.setOnAction(event -> {
        
           if(comisiones.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Comision en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistacomisiones.fxml"));
    Parent root = loader.load();
    VistacomisionesController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovcomisiones.setItems(comisiones);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
   controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
  controller.setNombrecuenta(Nombrecuenta);
   
    if (controller != null) {
        System.out.println("Controlador Vistacomisiones cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistacomisiones.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btncomisiones.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        
        });
        
        
        btnnomina.setOnAction(event -> {
        
           if(nomina.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Nomina en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistanomina.fxml"));
    Parent root = loader.load();
    VistanominaController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovnomina.setItems(nomina);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
   controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
  controller.setNombrecuenta(Nombrecuenta);
       controller.labelcuenta.setText(Nombrecuenta);

    if (controller != null) {
        System.out.println("Controlador Vistanomina cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistacomisiones.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btncomisiones.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        
        });
        
        
        btncreditos.setOnAction(event -> {
        
                if(creditos.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Credito en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistacredito.fxml"));
    Parent root = loader.load();
    VistacreditoController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovcredito.setItems(creditos);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
   controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
  controller.setNombrecuenta(Nombrecuenta);
       controller.labelcuenta.setText(Nombrecuenta);

    if (controller != null) {
        System.out.println("Controlador Vistacredito cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistacomisiones.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btncomisiones.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        
        });
        
        btndivisas.setOnAction(event -> {
        
         
                if(divisas.isEmpty()){
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("No se encontraron movimientos de tipo Divisas en el archivo");
        alerta.showAndWait();
    } else {
            try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistadivisas.fxml"));
    Parent root = loader.load();
    VistadivisasController controller = loader.getController();
   //Setteo todas las listas para poder trabajar la navegabilidad entre ventanas 
    controller.setMovimientos(movimientos);
    controller.Tablamovdivisas.setItems(divisas);// la tabla de la seccion transferencias.
    controller.setTransferencias(transferencias);
    controller.setPagomovil(pagomovil);
    controller.setCompras(compras);
     controller.setComisiones(comisiones);
   controller.setNomina(nomina);
    controller.setCreditos(creditos);
    controller.setDivisas(divisas);
  controller.setNombrecuenta(Nombrecuenta);
       controller.labelcuenta.setText(Nombrecuenta);

    if (controller != null) {
        System.out.println("Controlador Vistadivisas cargado correctamente.");
    } else {
        System.out.println("Error al cargar el controlador de Vistadivisass.");
    }

    Scene scene = new Scene(root);
    Stage stage = new Stage();
    stage.setScene(scene);
    stage.show();

    // Cierra la ventana actual
    Stage myStage = (Stage) btncomisiones.getScene().getWindow();
    myStage.close();

} catch (IOException ex) {
    Logger.getLogger(VistaoneController.class.getName()).log(Level.SEVERE, null, ex);
}


         }
        });
        
        btnlimpiardatos.setOnAction(event -> {
        movimientos.clear();
        transferencias.clear();
        pagomovil.clear();
        compras.clear();
        comisiones.clear();
        nomina.clear();
        creditos.clear();   
        divisas.clear();
           Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("Se han eliminado correctamente los movimientos");
        alerta.showAndWait();
        });
        
         txtbuscar.textProperty().addListener((observable, oldValue, newValue) -> {
        buscarMovimientos(newValue);
    });

btnexportar.setOnAction(event -> {
    exportarAExcel();
});

    }

   
    
    
    
    
    
    
    
    
    private void openFile() throws IOException {
        // Crear un file chooser para seleccionar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");

        // Mostrar solo archivos Excel
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de Excel", "*.xls", "*.xlsx")
        );

        Stage stage = (Stage) Openbutton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
            readExcelFile(selectedFile);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }
    }

    private void readExcelFile(File file) throws IOException {
        System.out.println("Leyendo archivo: " + file.getAbsolutePath());

        // Verificar la extensión del archivo
        Workbook workbook = null;
        if (file.getName().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(new FileInputStream(file));  // Para archivos .xlsx
        } else if (file.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook(new FileInputStream(file));  // Para archivos .xls
        } else {
            System.out.println("El archivo no es un archivo Excel válido.");
            return;
        }

        // Leer el archivo Excel
        processExcelFile(workbook);

        // Cerrar el archivo Excel después de leerlo
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void setMovimientoProperty(Movimiento movimiento, int columnIndex, String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Formato de fecha
        if (value == null || value.isEmpty()) {
            System.out.println("Valor vacío para columna: " + columnIndex);
            return;
        }

        System.out.println("Columna: " + columnIndex + ", Valor: " + value);
        switch (columnIndex) {
            case 1: // Columna B - Fecha
                try {
                    if (!value.equalsIgnoreCase("Totales")) {
                        movimiento.setFecha(LocalDate.parse(value, formatter)); // Usar el formateador
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Error al parsear la fecha: " + value);
                }
                break;
            case 2: // Columna C - Código
                try {
                    movimiento.setCodigo(Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    System.out.println("El valor en la columna de código no es un número: " + value);
                }
                break;
            case 3: // Columna D - Tipo
                movimiento.setTipot(value);
                break;
            case 6: // Columna E - Tipo de operación
                movimiento.setTipoO(value);
                break;
            case 7: // Columna F - Descripción
                movimiento.setDescripcion(value);
                break;
            case 12: // Columna G - Referencia
             try {
        // Convertir a número entero
               int referenciaValue = (int) Double.parseDouble(value);
                movimiento.setReferencia(referenciaValue);
              } catch (NumberFormatException e) {
                  System.out.println("El valor en la columna de referencia no es un número: " + value);
                 }
                break;
            case 13: // Columna H - Debe
                try {
                    movimiento.setDebe(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    System.out.println("El valor en la columna de debe no es un número: " + value);
                }
                break;
            case 15: // Columna I - Haber
                try {
                    movimiento.setHaber(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    System.out.println("El valor en la columna de haber no es un número: " + value);
                }
                break;
            case 16: // Columna J - Saldo
                try {
                    movimiento.setSaldo(Double.parseDouble(value));
                } catch (NumberFormatException e) {
                    System.out.println("El valor en la columna de saldo no es un número: " + value);
                }
                break;
        }
    }

    

    
    private void processExcelFile(Workbook workbook) {
    Sheet sheet = workbook.getSheetAt(0);  // Tomamos la primera hoja
    System.out.println("Número de filas en la hoja: " + sheet.getPhysicalNumberOfRows());

    // Obtener todas las celdas combinadas (rangos de celdas combinadas)
    List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();

    int filasDesconocidas = 0;  // Contador de filas con datos desconocidos

    // Recorremos cada fila del Excel, comenzando desde la fila 17 (índice 16)
    for (int rowIndex = 16; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) { // Comenzamos desde la fila 17
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            System.out.println("Procesando fila " + (rowIndex + 1) + " (índice " + rowIndex + ")");
            
            Movimiento movimiento = new Movimiento();
            boolean isRowValid = false;  // Indicador para verificar si la fila tiene datos válidos

            // Verificamos cada celda de la fila, solo las columnas de interés (de la columna B en adelante)
            for (int i = 1; i < 17; i++) {  // Empezamos desde la columna B (índice 1)
                Cell cell = row.getCell(i);
                
                // Verificar si la celda está dentro de un rango combinado
                String value = getCellValueForMergedCells(cell, mergedRegions);
                
                // Si el valor no es vacío ni "Dato desconocido", lo procesamos
                if (value != null && !value.equals("Dato desconocido") && !value.isEmpty()) {
                    setMovimientoProperty(movimiento, i, value); // Asignar el valor de la celda al movimiento
                    isRowValid = true; // Marcar la fila como válida si al menos una celda tiene datos válidos
                }
            }

            // Si la fila tiene al menos un dato válido, procesamos y agregamos el movimiento
            if (isRowValid && movimiento.getFecha() != null && movimiento.getCodigo() != null) {
                movimientos.add(movimiento); // Agregar el objeto Movimiento a la lista
                System.out.println("Movimiento agregado: " + movimiento);
            } else {
                System.out.println("Fila no válida en el índice: " + rowIndex);
                filasDesconocidas++;  // Incrementar el contador de filas desconocidas
            }
        } else {
            System.out.println("Fila vacía en el índice " + rowIndex);
            filasDesconocidas++;  // Incrementar el contador de filas desconocidas
        }
    }

    // Validar si hay más de 40 filas con datos desconocidos
    if (filasDesconocidas > 40) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText("Archivo desconocido");
        alerta.setContentText("El archivo contiene más de 40 filas con datos desconocidos y no se puede procesar.");
        alerta.showAndWait();
        return;  // Salir del método y no procesar el archivo
    }
   Cell celdaB14 = sheet.getRow(13).getCell(1); // Fila 14, Columna 2 (B)
        String Nombrecuenta = celdaB14.getStringCellValue(); // Suponiendo que es un string
        labelcuenta.setText(Nombrecuenta);

    Tablamov.refresh(); // Actualizar la TableView
}

private String getCellValueForMergedCells(Cell cell, List<CellRangeAddress> mergedRegions) {
    if (cell == null) {
        return "Dato desconocido";  // Si la celda es nula, devolvemos "Dato desconocido"
    }

    // Comprobar si la celda está dentro de una celda combinada
    for (CellRangeAddress mergedRegion : mergedRegions) {
        // Si la celda está dentro del rango de celdas combinadas
        if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
            // Devolver el valor de la primera celda del rango combinado
            Row row = cell.getSheet().getRow(mergedRegion.getFirstRow());
            Cell firstCell = row.getCell(mergedRegion.getFirstColumn());
            return getCellValue(firstCell); // Retornar el valor de la primera celda
        }
    }

    // Si no está dentro de una celda combinada, simplemente devolvemos el valor de la celda
    return getCellValue(cell);
}

private String getCellValue(Cell cell) {
    if (cell == null) {
        return "Dato desconocido";  // Si la celda es nula, devolvemos "Dato desconocido"
    }

    String cellValue = null;  // Inicializamos con null para que si no hay valor, lo manejemos adecuadamente.
    
    switch (cell.getCellType()) {
        case STRING:
            // Si es texto, lo limpiamos de espacios innecesarios antes y después
            cellValue = cell.getStringCellValue().trim();
            break;
        case NUMERIC:
            // Si es un número, lo verificamos para fechas o números generales
            if (DateUtil.isCellDateFormatted(cell)) {
                // Si es fecha, la convertimos a texto
                cellValue = cell.getDateCellValue().toString();
            } else {
                // Si es un número, lo convertimos a cadena
                cellValue = String.valueOf(cell.getNumericCellValue());
            }
            break;
        case BOOLEAN:
            // Si es un valor booleano, lo convertimos a cadena
            cellValue = String.valueOf(cell.getBooleanCellValue());
            break;
        case FORMULA:
            // Si es una fórmula, devolvemos la fórmula
            cellValue = cell.getCellFormula();
            break;
        default:
            // Si no tiene un tipo válido, devolvemos "Dato desconocido"
            cellValue = "Dato desconocido";
            break;
    }
    
    // Si el valor es null o "Dato desconocido", lo devolvemos tal cual
    if (cellValue == null || cellValue.isEmpty()) {
        return "Dato desconocido";
    }

    // Devolvemos el valor limpio
    return cellValue.trim();
}


private Double Totalingresos(){
Double Ingresos = 0.0;

for (Movimiento x : movimientos){  
   Ingresos += x.getHaber();
}
return Ingresos;
}
private Double Totalegresos(){
Double egresos = 0.0;

for (Movimiento x : movimientos){  
   egresos += x.getDebe();
}
return egresos;
}

private Double Saldo(){
Movimiento xd = movimientos.get(-1);

Double Saldo = xd.getSaldo();
   
    return Saldo;

}

public void Agregartransferencias(){

for (Movimiento x : movimientos){  
   if(x.getCodigo() == 262){
      transferencias.add(x);
   }
}

}

public void Agregarpagomovil() {
    for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 387) {
            pagomovil.add(x);
            System.out.println("Movimiento agregado a PagoMovil: " + x);
        }
    }
}

public void AgregarCompras(){
   for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 376) {
            compras.add(x);
            System.out.println("Movimiento agregado a compras: " + x);
        }
    }
}

public void AgregarComisiones(){
   for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 751) {
            comisiones.add(x);
            System.out.println("Movimiento agregado a comisiones: " + x);
        }
    }
}

public void AgregarNomina(){
   for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 730) {
            nomina.add(x);
            System.out.println("Movimiento agregado a nomina : " + x);
        }
    }
}

public void AgregarCredito(){
   for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 487 || x.getCodigo() == 488 || x.getCodigo() == 489 ) {
            creditos.add(x);
            System.out.println("Movimiento agregado a credito: " + x);
        }
    }
}

public void AgregarDivisas(){
   for (Movimiento x : movimientos) {
        System.out.println("Código: " + x.getCodigo());  // Imprime el código para verificar
        if (x.getCodigo() == 647 || x.getCodigo() == 286) {
            divisas.add(x);
            System.out.println("Movimiento agregado a divisas: " + x);
        }
    }
}


    public void setMovimientos(ObservableList<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public void setTransferencias(ObservableList<Movimiento> transferencias) {
        this.transferencias = transferencias;
    }

    public void setPagomovil(ObservableList<Movimiento> pagomovil) {
        this.pagomovil = pagomovil;
    }

    public void setCompras(ObservableList<Movimiento> compras) {
        this.compras = compras;
    }

    public void setComisiones(ObservableList<Movimiento> comisiones) {
        this.comisiones = comisiones;
    }

    public void setNomina(ObservableList<Movimiento> nomina) {
        this.nomina = nomina;
    }

    public void setCreditos(ObservableList<Movimiento> creditos) {
        this.creditos = creditos;
    }

    public void setDivisas(ObservableList<Movimiento> divisas) {
        this.divisas = divisas;
    }

  public void refrescar(){Tablamov.setItems(movimientos);Tablamov.refresh();}

    public void setNombrecuenta(String Nombrecuenta) {
        this.Nombrecuenta = Nombrecuenta;
    }
  
  // Método para buscar movimientos
private void buscarMovimientos(String searchText) {
    // Crear una lista filtrada a partir de la lista original
    FilteredList<Movimiento> filteredData = new FilteredList<>(movimientos, p -> true);

    // Filtrar la lista según el texto ingresado
    filteredData.setPredicate(movimiento -> {
        // Si el texto de búsqueda está vacío, se muestran todos los elementos
        if (searchText == null || searchText.isEmpty()) {
            return true;
        }

        // Convertir el texto de búsqueda a minúsculas para una comparación no sensible a mayúsculas
        String lowerCaseFilter = searchText.toLowerCase();

        // Comprobar si alguno de los atributos coincide con el texto de búsqueda
        return movimiento.getDescripcion().toLowerCase().contains(lowerCaseFilter) ||
               movimiento.getTipot().toLowerCase().contains(lowerCaseFilter) ||
               movimiento.getTipoO().toLowerCase().contains(lowerCaseFilter) ||
               String.valueOf(movimiento.getCodigo()).contains(lowerCaseFilter) ||
               String.valueOf(movimiento.getReferencia()).contains(lowerCaseFilter)||
               String.valueOf(movimiento.getDebe()).contains(lowerCaseFilter) ||
               String.valueOf(movimiento.getHaber()).contains(lowerCaseFilter) ||
               String.valueOf(movimiento.getSaldo()).contains(lowerCaseFilter);
    });

    // Actualizar la TableView con los resultados filtrados
    Tablamov.setItems(filteredData);
}
  // Método para calcular y retornar los totales de debe y haber
    private Double[] calcularDeberesyHaberes(ObservableList<Movimiento> x) {
        Double debe = 0.0;
        Double haber = 0.0;

        for (Movimiento y : x) {
            debe += y.getDebe();
            haber += y.getHaber();
        }

        return new Double[]{debe, haber}; // Retorna un arreglo con [debe, haber]
    }
    // Método para calcular y mostrar los totales de todas las listas
    public void calcularTotalesDeTodasLasListas() {
        System.out.println("Totales de Movimientos:");
        Double[] totalesMovimientos = calcularDeberesyHaberes(movimientos);
        System.out.println("Total de gastos : " + totalesMovimientos[0]);
        System.out.println("Total de ingresos : " + totalesMovimientos[1]);

        System.out.println("Totales de Transferencias:");
        Double[] totalesTransferencias = calcularDeberesyHaberes(transferencias);
        System.out.println("Total de gastos : " + totalesTransferencias[0]);
        System.out.println("Total de ingresos : " + totalesTransferencias[1]);

        System.out.println("Totales de Pago Móvil:");
        Double[] totalesPagomovil = calcularDeberesyHaberes(pagomovil);
        System.out.println("Total de gastos : " + totalesPagomovil[0]);
        System.out.println("Total de ingresos : " + totalesPagomovil[1]);

        System.out.println("Totales de Compras:");
        Double[] totalesCompras = calcularDeberesyHaberes(compras);
        System.out.println("Total de gastos : " + totalesCompras[0]);
        System.out.println("Total de ingresos : " + totalesCompras[1]);

        System.out.println("Totales de Comisiones:");
        Double[] totalesComisiones = calcularDeberesyHaberes(comisiones);
        System.out.println("Total de gastos : " + totalesComisiones[0]);
        System.out.println("Total de ingresos : " + totalesComisiones[1]);

        System.out.println("Totales de Nómina:");
        Double[] totalesNomina = calcularDeberesyHaberes(nomina);
        System.out.println("Total de gastos : " + totalesNomina[0]);
        System.out.println("Total de ingresos : " + totalesNomina[1]);

        System.out.println("Totales de Créditos:");
        Double[] totalesCreditos = calcularDeberesyHaberes(creditos);
        System.out.println("Total de gastos : " + totalesCreditos[0]);
        System.out.println("Total de ingresos : " + totalesCreditos[1]);

        System.out.println("Totales de Divisas:");
        Double[] totalesDivisas = calcularDeberesyHaberes(divisas);
        System.out.println("Total de gastos : " + totalesDivisas[0]);
        System.out.println("Total de ingresos : " + totalesDivisas[1]);
    }
  
public void exportarAExcel() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Guardar archivo Excel");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Excel", "*.xlsx"));

    Stage stage = (Stage) btnexportar.getScene().getWindow();
    File file = fileChooser.showSaveDialog(stage);

    if (file != null) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Datos");

            // Crea un estilo para los encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Crea un estilo para los datos numéricos con formato negativo
            CellStyle decimalStyleNegative = workbook.createCellStyle();
            decimalStyleNegative.setDataFormat(workbook.createDataFormat().getFormat("-#,##0.00;-#,##0.00;;"));
            decimalStyleNegative.setBorderBottom(BorderStyle.THIN);
            decimalStyleNegative.setBorderTop(BorderStyle.THIN);
            decimalStyleNegative.setBorderLeft(BorderStyle.THIN);
            decimalStyleNegative.setBorderRight(BorderStyle.THIN);

            // Crea un estilo para los datos numéricos con formato positivo
            CellStyle decimalStylePositive = workbook.createCellStyle();
            decimalStylePositive.setDataFormat(workbook.createDataFormat().getFormat("+#,##0.00;+#,##0.00;;"));
            decimalStylePositive.setBorderBottom(BorderStyle.THIN);
            decimalStylePositive.setBorderTop(BorderStyle.THIN);
            decimalStylePositive.setBorderLeft(BorderStyle.THIN);
            decimalStylePositive.setBorderRight(BorderStyle.THIN);

            // Crea un estilo para las celdas de totales en amarillo
          CellStyle totalStyle = workbook.createCellStyle();
totalStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
totalStyle.setBorderBottom(BorderStyle.THIN);
totalStyle.setBorderTop(BorderStyle.THIN);
totalStyle.setBorderLeft(BorderStyle.THIN);
totalStyle.setBorderRight(BorderStyle.THIN);
totalStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));


            // Crea un estilo para los datos de texto
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setBorderBottom(BorderStyle.THIN);
            textStyle.setBorderTop(BorderStyle.THIN);
            textStyle.setBorderLeft(BorderStyle.THIN);
            textStyle.setBorderRight(BorderStyle.THIN);

            // Inserta el nombre de la cuenta en la celda A1
            String NombreCuenta = Nombrecuenta; // Reemplaza con el valor correcto de NombreCuenta
            Row firstRow = sheet.createRow(0);
            Cell firstCell = firstRow.createCell(0);
            firstCell.setCellValue(NombreCuenta);
            firstCell.setCellStyle(textStyle);

            // Agrega los encabezados de las columnas desde la columna B
            Row headerRow = sheet.createRow(1);
            Cell headerCell = headerRow.createCell(1); // Columna B
            headerCell.setCellValue("Descripción");
            headerCell.setCellStyle(headerStyle);
            
            headerCell = headerRow.createCell(2); // Celda vacía para espacio
            headerCell.setCellValue("");
            headerCell.setCellStyle(headerStyle);

            headerCell = headerRow.createCell(3); // Columna D
            headerCell.setCellValue("Total de Gastos");
            headerCell.setCellStyle(headerStyle);

            headerCell = headerRow.createCell(4); // Columna E
            headerCell.setCellValue("Total de Ingresos");
            headerCell.setCellStyle(headerStyle);

            // Agrega los datos
            int rowNum = 2;
            String[] categorias = {"Transferencias en línea", "Pago Móvil", "Compras Maestro/Tarjeta de Débito", "Comisiones Bancarias", "Nómina", "Créditos Inmediatos", "Gastos en relación a divisas"};
            ObservableList<Movimiento>[] datos = new ObservableList[]{transferencias, pagomovil, compras, comisiones, nomina, creditos, divisas};

            for (int i = 0; i < categorias.length; i++) {
                Double[] totales = calcularDeberesyHaberes(datos[i]);
                Row row = sheet.createRow(rowNum++);
                Cell cell = row.createCell(1); // Columna B
                cell.setCellValue(categorias[i]);
                cell.setCellStyle(textStyle);
                
                cell = row.createCell(2); // Celda vacía para espacio
                cell.setCellValue("");
                cell.setCellStyle(textStyle);
                
                cell = row.createCell(3); // Columna D (Total de Gastos)
                cell.setCellValue(totales[0]);
                cell.setCellStyle(decimalStyleNegative);
                
                cell = row.createCell(4); // Columna E (Total de Ingresos)
                cell.setCellValue(totales[1]);
                cell.setCellStyle(decimalStylePositive);
            }

            // Agrega los totales generales al final
            Double[] totalesMovimientos = calcularDeberesyHaberes(movimientos);
            Row row = sheet.createRow(rowNum++);
            Cell cell = row.createCell(1); // Columna B
            cell.setCellValue("Totales");
            cell.setCellStyle(textStyle);
            
            cell = row.createCell(2); // Celda vacía para espacio
            cell.setCellValue("");
            cell.setCellStyle(textStyle);
            
            // Celda para el total de gastos
cell = row.createCell(3); // Columna D
cell.setCellValue(totalesMovimientos[0]);
cell.setCellStyle(totalesMovimientos[0] != 0 ? totalStyle : decimalStyleNegative);

// Celda para el total de ingresos
cell = row.createCell(4); // Columna E
cell.setCellValue(totalesMovimientos[1]);
cell.setCellStyle(totalesMovimientos[1] != 0 ? totalStyle : decimalStylePositive);


            // Ajusta el tamaño de las columnas para que sean ajustables al texto y hace la celda C más grande
            sheet.autoSizeColumn(1); // Ajusta la columna de Descripción
            sheet.setColumnWidth(2, 4000); // Ajusta la celda vacía
            sheet.autoSizeColumn(3); // Ajusta la columna de Total de Gastos
            sheet.autoSizeColumn(4); // Ajusta la columna de Total de Ingresos

            // Escribe el archivo
            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
             Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Informacion");
        alerta.setHeaderText("Informacion");
        alerta.setContentText("El archivo se ha creado correctamente ");
        alerta.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



}
