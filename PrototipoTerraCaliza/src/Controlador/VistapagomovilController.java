/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controlador;

import Modelo.Movimiento;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author Programación Reimil
 */
public class VistapagomovilController implements Initializable {

    private ObservableList<Movimiento> movimientos;
    private ObservableList<Movimiento> transferencias;
    private ObservableList<Movimiento> pagomovil;
    private ObservableList<Movimiento> compras;
    private ObservableList<Movimiento> comisiones;
    private ObservableList<Movimiento> nomina;
    private ObservableList<Movimiento> creditos;
    private ObservableList<Movimiento> divisas;
    public String Nombrecuenta = null;
    @FXML
    private Button btnregresar;
    @FXML
    private Button btnExportar;
    public TableView<Movimiento> Tablamovpm;
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
    @FXML
    private Label labelgasto;
    @FXML
    private Label labelingreso;
    @FXML
    private TextField txtbuscar;
    @FXML
    public Label labelcuenta;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        movimientos = FXCollections.observableArrayList(); // Inicializar la lista
        transferencias = FXCollections.observableArrayList(); // Inicializar la lista
        pagomovil = FXCollections.observableArrayList(); // Inicializar la lista
        compras = FXCollections.observableArrayList(); // Inicializar la lista
        comisiones = FXCollections.observableArrayList(); // Inicializar la lista
        nomina = FXCollections.observableArrayList(); // Inicializar la lista
        creditos = FXCollections.observableArrayList(); // Inicializar la lista
        divisas = FXCollections.observableArrayList(); // Inicializar la lista

        Tablamovpm.setItems(pagomovil); // Vincular la lista a la TableView

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
        
        actualizarTotales();

        txtbuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            buscarMovimientos(newValue);
        });

        btnregresar.setOnAction(event -> {

            try {
                // Cargar la ventana principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Vistaone.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                VistaoneController controlador = loader.getController();

                controlador.setMovimientos(movimientos);
                controlador.setTransferencias(transferencias);
                controlador.setPagomovil(pagomovil);
                controlador.setCompras(compras);
                controlador.setComisiones(comisiones);
                controlador.setNomina(nomina);
                controlador.setCreditos(creditos);
                controlador.setDivisas(divisas);
                controlador.setNombrecuenta(Nombrecuenta);
                controlador.labelcuenta.setText(Nombrecuenta);
                controlador.agregarTotales();
                for (Movimiento x : transferencias) {
                    System.out.println(x.toString());
                }
                for (Movimiento c : pagomovil) {
                    System.out.println("Listo el pago :" + c.toString());
                }

                controlador.refrescar();
                // Obtener el stage de la ventana principal
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Ventana Principal");
                stage.show();

                // Cerrar la ventana actual
                Stage myStage = (Stage) btnregresar.getScene().getWindow();
                myStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        
        btnExportar.setOnAction(event-> {
            
            exportarAMovimientos();
        });

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

    public void refrescar() {
        Tablamovpm.setItems(transferencias);
        Tablamovpm.refresh();
    }
    
    public void actualizarTotales() {
    labelgasto.setText("Total Gastos: " + calcularTotalGastos());
    labelingreso.setText("Total Ingresos: " + calcularTotalIngresos());
}


private String calcularTotalGastos() {
    Double totalGastos = 0.0;
    for (Movimiento movimiento : pagomovil) {
        totalGastos += movimiento.getDebe(); 
    }
    DecimalFormat df = new DecimalFormat("#,##0.00");
    return df.format(totalGastos); 
}


private String calcularTotalIngresos() {
    Double totalIngresos = 0.0;
    for (Movimiento movimiento : pagomovil) {
        totalIngresos += movimiento.getHaber(); 
    }
    DecimalFormat df = new DecimalFormat("#,##0.00");
    return df.format(totalIngresos); 
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
            return movimiento.getDescripcion().toLowerCase().contains(lowerCaseFilter)
                    || movimiento.getTipot().toLowerCase().contains(lowerCaseFilter)
                    || movimiento.getTipoO().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getCodigo()).contains(lowerCaseFilter)
                    ||   String.valueOf(movimiento.getReferencia()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getDebe()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getHaber()).contains(lowerCaseFilter)
                    || String.valueOf(movimiento.getSaldo()).contains(lowerCaseFilter);
        });
        
        

        // Actualizar la TableView con los resultados filtrados
        Tablamovpm.setItems(filteredData);
    }

    public void setNombrecuenta(String Nombrecuenta) {
        this.Nombrecuenta = Nombrecuenta;
    }
    
    public void ajustarAnchoColumnas() {
        // Establecer un ancho fijo para las columnas "Código" y "Tipo de Transacción"
        for (TableColumn<Movimiento, ?> column : Tablamovpm.getColumns()) {
            String columnName = column.getText(); // Obtener el texto del encabezado de la columna

            // Establecer el ancho fijo para columnas específicas
            if (columnName.equals("Código")) {
                column.setPrefWidth(100); // Ancho fijo para la columna "Código"
            } else if (columnName.equals("Tipo de Transacción")) {
                column.setPrefWidth(150); // Ancho fijo para la columna "Tipo de Transacción"
            } else {
                // Para otras columnas, ajustar el ancho según el contenido
                double maxWidth = 0;

                // Iterar sobre cada fila en la tabla
                for (int i = 0; i < Tablamovpm.getItems().size(); i++) {
                    Object cellValue = column.getCellData(i);
                    String cellText = (cellValue != null) ? cellValue.toString() : "";

                    // Calcular el ancho del texto del contenido
                    double contentWidth = new Text(cellText).getLayoutBounds().getWidth();
                    maxWidth = Math.max(maxWidth, contentWidth);
                }

                // También considerar el ancho del encabezado para las columnas ajustables
                double headerWidth = new Text(columnName).getLayoutBounds().getWidth();
                maxWidth = Math.max(maxWidth, headerWidth); // Asegurarse de que el encabezado se ajuste

                // Ajustar el ancho de la columna
                column.setPrefWidth(maxWidth + 20); // Añadir un margen extra
            }
        }
    }
    
    private void exportarAMovimientos() {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Movimientos");

    // Crear un estilo para el encabezado
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // Color de fondo gris
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Establecer el patrón de relleno

    // Crear la fila de encabezado
    Row headerRow = sheet.createRow(0);
    String[] columnHeaders = {"Fecha", "Código", "Tipo de Transacción", "Tipo de Operación", "Descripción", "Referencia", "Debe", "Haber", "Saldo"};
    for (int i = 0; i < columnHeaders.length; i++) {
        Cell cell = headerRow.createCell(i);
        cell.setCellValue(columnHeaders[i]);
        cell.setCellStyle(headerStyle); // Aplicar el estilo de encabezado
    }

    // Crear un objeto DecimalFormatSymbols para la convención venezolana
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
    symbols.setGroupingSeparator('.'); // Establecer el punto como separador de miles
    symbols.setDecimalSeparator(',');   // Establecer la coma como separador decimal

    // Crear un objeto DecimalFormat con los símbolos personalizados
    DecimalFormat df = new DecimalFormat("#,##0.00", symbols);

    // Agregar los movimientos
    int rowNum = 1;
    for (Movimiento movimiento : pagomovil) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(movimiento.getFecha().toString());
        row.createCell(1).setCellValue(movimiento.getCodigo());
        row.createCell(2).setCellValue(movimiento.getTipot());
        row.createCell(3).setCellValue(movimiento.getTipoO());
        row.createCell(4).setCellValue(movimiento.getDescripcion());
        row.createCell(5).setCellValue(movimiento.getReferencia());

        // Formatear el "Debe" y "Haber"
        double debe = movimiento.getDebe();
        double haber = movimiento.getHaber();

        // Colocar el signo negativo en "Debe" y positivo en "Haber"
        if (debe != 0) {
            row.createCell(6).setCellValue(debe < 0 ? df.format(debe) : df.format(-debe)); // Asegurarse de que sea negativo
        } else {
            row.createCell(6).setCellValue(0); // No mostrar nada si es 0
        }

        if (haber != 0) {
            row.createCell(7).setCellValue(df.format(haber)); // Mostrar el valor positivo
        } else {
            row.createCell(7).setCellValue(0); // No mostrar nada si es 0
        }

        row.createCell(8).setCellValue(df.format(movimiento.getSaldo())); // Formatear el saldo
    }

    // Calcular totales
    double totalDebe = pagomovil.stream().mapToDouble(Movimiento::getDebe).sum();
    double totalHaber = pagomovil.stream().mapToDouble(Movimiento::getHaber).sum();
    double totalSaldo = pagomovil.stream().mapToDouble(Movimiento::getSaldo).sum();

    // Agregar filas vacías para separación
    for (int i = 0; i < 3; i++) {
        sheet.createRow(rowNum++); // Crear 3 filas vacías
    }

    // Crear un estilo para la fila de totales
    CellStyle totalStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true); // Hacer el texto en negrita
    totalStyle.setFont(font);

    // Establecer el color de fondo amarillo
    totalStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
    totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // Establecer el patrón de relleno

    // Agregar una fila para los totales
    Row totalRow = sheet.createRow(rowNum);
    totalRow.createCell(5).setCellValue("Totales");
    totalRow.createCell(6).setCellValue(df.format(totalDebe < 0 ? totalDebe : -totalDebe)); // Asegurarse de que el total de "Debe" sea negativo
    totalRow.createCell(7).setCellValue(df.format(totalHaber)); // Mostrar el total de "Haber"
    totalRow.createCell(8).setCellValue(df.format(totalSaldo)); // Formatear el total de saldo

    // Aplicar el estilo de totales a las celdas correspondientes
    for (int i = 5; i <= 8; i++) {
        totalRow.getCell(i).setCellStyle(totalStyle);
    }

    // Ajustar la altura de la fila de totales
    totalRow.setHeightInPoints(20); // Ajustar la altura de la fila (puedes cambiar el valor según sea necesario)

    // Ajustar el ancho de las columnas automáticamente
    for (int i = 0; i < columnHeaders.length; i++) {
        sheet.autoSizeColumn(i);
    }

    // Guardar el archivo
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Guardar archivo Excel");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
    File file = fileChooser.showSaveDialog(btnExportar.getScene().getWindow());
    if (file != null) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    try {
        workbook.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
