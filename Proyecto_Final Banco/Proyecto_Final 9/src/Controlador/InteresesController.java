package Controlador;

import Modelo.Cliente;
import Modelo.Transaccion;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class InteresesController implements Initializable {

    @FXML
    private BarChart bctInteereces;

    @FXML
    private Button btnConsultar;

    @FXML
    private Button btnGenerar;

    @FXML
    private DatePicker dtpDesde;

    @FXML
    private DatePicker dtpHasta;
    
    @FXML
    private Label lbError;

    @FXML
    private Label lbInteres;

    @FXML
    private Label lbTotalIntereces;

    @FXML
    private Pane pnGeneral;

    @FXML
    private Pane pnPersonal;

    @FXML
    private ToggleButton tbtnGeneral;

    @FXML
    private ToggleButton tbtnPersonal;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    private ObservableList<Transaccion> trasaccionespersonales;
    private ObservableList<Transaccion> trasaccionesgenerales;
    private ObservableList<Cliente> listaclientes;
    List<XYChart.Data> datos;
    private Cliente c;
    private XYChart.Series series1;
    private XYChart.Series serieInicial;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        pnGeneral.setVisible(false);
        lbError.setVisible(false);

        ToggleGroup tbnsConsultas = new ToggleGroup();

        tbtnPersonal.setToggleGroup(tbnsConsultas);
        tbtnGeneral.setToggleGroup(tbnsConsultas);

        tbtnPersonal.setSelected(true);

        datos = new ArrayList<>();

//        serieInicial = new XYChart.Series();
//        serieInicial.getData().add(new XYChart.Data("1", 100));
//        serieInicial.getData().add(new XYChart.Data("2", 1));
//        serieInicial.getData().add(new XYChart.Data("3", 50));
//        serieInicial.getData().add(new XYChart.Data("4", 15));
//        bctInteereces.getData().addAll(serieInicial);
////        bctInteereces.getData().clear();
        dtpDesde.setDayCellFactory(dp -> new DateCell() {
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

        dtpHasta.setDayCellFactory(dp -> new DateCell() {
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

        tbnsConsultas.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });

        tbtnPersonal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnPersonal.isSelected()) {
                    pnGeneral.setVisible(false);
                    pnPersonal.setVisible(true);

                }
            }
        });

        tbtnGeneral.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (tbtnGeneral.isSelected()) {
                    pnPersonal.setVisible(false);
                    pnGeneral.setVisible(true);

                }

            }
        });

        btnConsultar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bctInteereces.getData().clear();
                series1 = new XYChart.Series();
                series1.setName("intereses");

                LocalDate inicio = dtpDesde.getValue();
                LocalDate fin = dtpHasta.getValue();
                Double totalIntereses = 0.0;

               Map<String, Double> interesesPorMes = new HashMap<>();

        for (Cliente cliente : listaclientes) {
            LocalDate ultimInteresFecha = cliente.getUltimInteresFecha();
            if (ultimInteresFecha.getYear() >= inicio.getYear() && ultimInteresFecha.getYear() <= fin.getYear()) {
                int mesInicio = inicio.getMonthValue();
                int mesFin = fin.getMonthValue();
                int mesInteres = ultimInteresFecha.getMonthValue();

                boolean mesInRange = false;

                if (ultimInteresFecha.getYear() == inicio.getYear()) {
                    mesInRange = mesInteres >= mesInicio;
                } else if (ultimInteresFecha.getYear() == fin.getYear()) {
                    mesInRange = mesInteres <= mesFin;
                } else if (ultimInteresFecha.getYear() > inicio.getYear() && ultimInteresFecha.getYear() < fin.getYear()) {
                    mesInRange = true;
                } else if (ultimInteresFecha.getYear() == inicio.getYear() && ultimInteresFecha.getYear() == fin.getYear()) {
                    mesInRange = mesInteres >= mesInicio && mesInteres <= mesFin;
                }

                if (mesInRange) {
                    addInteresToMap(interesesPorMes, ultimInteresFecha, cliente.getUltimInteres());
                    totalIntereses += cliente.getUltimInteres();
                }
            }
        }

        for (Map.Entry<String, Double> entry : interesesPorMes.entrySet()) {
            series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }


        bctInteereces.getData().addAll(series1);
        lbTotalIntereces.setText(String.format("%.2f", totalIntereses));
    }
        });

        btnGenerar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (c.getUltimInteresFecha().equals(c.getFCreacion()) && c.getUltimInteres() == 0.0) {
                    Double monto = c.getMontoC();
                    double interesPorcentaje = c.getPorcentajeInteres(c.getMontoC());
                    double montoInteres = c.getMontoC() * interesPorcentaje / 100;
                    c.setMontoC(monto += montoInteres);
                    c.setUltimInteresFecha(LocalDate.now());
                    c.setUltimInteres(montoInteres);
                    lbInteres.setText(" " + montoInteres + " ");
                    guardarDatos();
                }else if (c.canGenerateInterest()) {
                    c.generateInterest();
                    guardarDatos();
                }else{
                    errorMsg("ya se generaron intereses este mes");
                    
                }
            }
        });
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

    public Cliente getcliente() {
        return c;
    }

    public void setcliente(Cliente c) {
        this.c = c;
    }

    public ObservableList<Cliente> getListaclientes() {
        return listaclientes;
    }

    public void setListaclientes(ObservableList<Cliente> listaclientes) {
        this.listaclientes = listaclientes;
    }

    public void setUltimInteres(Double interes) {
        lbInteres.setText(String.format("%.2f", interes)+"$");
    }
    
    private void errorMsg(String msg) {
        lbError.setText(msg);
        lbError.setVisible(true);
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
    
    private void addInteresToMap(Map<String, Double> interesesPorMes, LocalDate ultimInteresFecha, Double monto) {
    String mesAño = ultimInteresFecha.getMonthValue() + "-" + ultimInteresFecha.getYear();
    interesesPorMes.put(mesAño, interesesPorMes.getOrDefault(mesAño, 0.0) + monto);
}

}
