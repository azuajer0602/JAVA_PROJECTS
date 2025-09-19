package Controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CerrarSesion_AlertController implements Initializable {
    
    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnCancelar;

    @FXML
    private ImageView imgIcon;
    
    private Boolean seleccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
 Image imagenInicial = new Image("/Vista/imagenes/user check icon.png");
        imgIcon.setImage(imagenInicial);

        btnAceptar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
                seleccion = true;
            }
        });
        
        btnCancelar.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
                seleccion = false;
            }
        });
    }

//    public void cambiarImg(Character img) {
//        String ruta = null;
//        if (img == 'u') {
//            ruta = "/Vista/imagenes/user check icon.png";
//        } else if (img == 'e') {
//            ruta = "/Vista/imagenes/error icon.png";
//        }else if (img == 'c') {
//            ruta = "/Vista/imagenes/check icon2.png";
//        }
//        
//        Image nuevaImage = new Image(ruta);
//        imgIcon.setImage(nuevaImage);
//    }

    public Boolean getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Boolean seleccion) {
        this.seleccion = seleccion;
    }
    
    
    
}
