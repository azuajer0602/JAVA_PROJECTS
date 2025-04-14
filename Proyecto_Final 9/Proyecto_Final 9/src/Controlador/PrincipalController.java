package Controlador;

import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PrincipalController implements Initializable {

    @FXML
    private ImageView imgPerfil;

    @FXML
    private Label lbCVVTarjeta;

    @FXML
    private Label lbFechaTarjeta;

    @FXML
    private Label lbNombre;

    @FXML
    private Label lbNombreTarjeta;

    @FXML
    private Label lbNumTarjeta;

    @FXML
    private Label lbSaldo;

    @FXML
    private Label lbUltimInteres;
    
    First_User_ViewController fuvc;
    private String imagen;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fuvc = new First_User_ViewController();
        
        lbNombre.setText(fuvc.getCliente().getNombre()+" "+fuvc.getCliente().getApellido());
        
        lbSaldo.setText(String.format("%.2f", fuvc.getCliente().getMontoC())+" $");
        
        imagen = fuvc.getCliente().getPerfil();
        Image perfil = new Image(imagen);
        imgPerfil.setImage(perfil);
        
        lbNombreTarjeta.setText(fuvc.getCliente().getNombre()+" "+fuvc.getCliente().getApellido());
        
        lbUltimInteres.setText(String.format("%.2f", fuvc.getCliente().getUltimInteres())+"$");
        
        lbNumTarjeta.setText(generarNumTarjeta());
        lbFechaTarjeta.setText(generarFechaExpiracion());
        lbCVVTarjeta.setText(generarCVC());
    }    
    
    public String generarNumTarjeta() {
        Random random = new Random();
        StringBuilder numeroTarjeta = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            numeroTarjeta.append(random.nextInt(10));
        }
        return numeroTarjeta.toString();
    }

    public String generarFechaExpiracion() {
        Random random = new Random();
        int mes = random.nextInt(12) + 1;
        int año = random.nextInt(10) + LocalDate.now().getYear() + 1;
        return String.format("%02d/%d", mes, año % 100);
    }

    public String generarCVC() {
        Random random = new Random();
        StringBuilder cvc = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvc.append(random.nextInt(10));
        }
        return cvc.toString();
    }
    
}
