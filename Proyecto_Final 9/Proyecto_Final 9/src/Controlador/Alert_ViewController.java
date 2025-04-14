package Controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Alert_ViewController implements Initializable {

    @FXML
    private Button btnAceptar;

    @FXML
    private ImageView imgIcon;

    @FXML
    private Label lbMSG;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imagenInicial = new Image("/Vista/imagenes/user check icon.png");
        imgIcon.setImage(imagenInicial);

        btnAceptar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
        });
    }

    public void cambiarImg(Character img) {
        String ruta = null;
        if (img == 'u') {
            ruta = "/Vista/imagenes/user check icon.png";
        } else if (img == 'e') {
            ruta = "/Vista/imagenes/error icon.png";
        }else if (img == 'c') {
            ruta = "/Vista/imagenes/check icon2.png";
        }
        
        Image nuevaImage = new Image(ruta);
        imgIcon.setImage(nuevaImage);
    }
    
    public void setText(String msg){
        lbMSG.setText(msg);
    }

}
