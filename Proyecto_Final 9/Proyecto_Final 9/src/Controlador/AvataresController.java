package Controlador;

import Modelo.Avatar;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AvataresController implements Initializable {

    @FXML
    private Button btnAceptar;
    
    @FXML
    private Label lbError;


    @FXML
    private ToggleButton tbtnPerfil1;

    @FXML
    private ToggleButton tbtnPerfil10;

    @FXML
    private ToggleButton tbtnPerfil11;

    @FXML
    private ToggleButton tbtnPerfil12;

    @FXML
    private ToggleButton tbtnPerfil2;

    @FXML
    private ToggleButton tbtnPerfil3;

    @FXML
    private ToggleButton tbtnPerfil4;

    @FXML
    private ToggleButton tbtnPerfil5;

    @FXML
    private ToggleButton tbtnPerfil6;

    @FXML
    private ToggleButton tbtnPerfil7;

    @FXML
    private ToggleButton tbtnPerfil8;

    @FXML
    private ToggleButton tbtnPerfil9;
    
    private String ImagenSleccionada;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
         lbError.setVisible(false);
         errorView();
         
         
        ToggleGroup avatares = new ToggleGroup();
        tbtnPerfil1.setToggleGroup(avatares);
        tbtnPerfil2.setToggleGroup(avatares);
        tbtnPerfil3.setToggleGroup(avatares);
        tbtnPerfil4.setToggleGroup(avatares);
        tbtnPerfil5.setToggleGroup(avatares);
        tbtnPerfil6.setToggleGroup(avatares);
        tbtnPerfil7.setToggleGroup(avatares);
        tbtnPerfil8.setToggleGroup(avatares);
        tbtnPerfil9.setToggleGroup(avatares);
        tbtnPerfil10.setToggleGroup(avatares);
        tbtnPerfil11.setToggleGroup(avatares);
        tbtnPerfil12.setToggleGroup(avatares);
        
        btnAceptar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                if (seleccion()) {
                    errorMsg("Selccione uno");
                    return;
                }
                ImagenSleccionada = setImagenSeleccionada();

                
                Stage myStage = (Stage) btnAceptar.getScene().getWindow();
                myStage.close();
            }
        });
        
        
        
    }
    
    private boolean seleccion(){
        boolean p = false;
        
        if (!tbtnPerfil1.isSelected() && !tbtnPerfil2.isSelected() && !tbtnPerfil3.isSelected() && !tbtnPerfil4.isSelected() && !tbtnPerfil5.isSelected() && !tbtnPerfil6.isSelected() && 
                !tbtnPerfil7.isSelected() && !tbtnPerfil8.isSelected() && !tbtnPerfil9.isSelected() && !tbtnPerfil10.isSelected() && !tbtnPerfil11.isSelected() && !tbtnPerfil12.isSelected()) {
            p = true;
        }
        
        return p;
    }
    
    
    private void errorMsg(String msg) {
        lbError.setText(msg);
        lbError.setVisible(true);
    }
    
    private void errorView() {
        tbtnPerfil1.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil2.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil3.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil4.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil5.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil6.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil7.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil8.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil9.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil10.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil11.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
        
        tbtnPerfil12.setOnMouseClicked(event -> {
            lbError.setVisible(false);
        });
    }
    
    private String setImagenSeleccionada(){
        if (tbtnPerfil1.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_1.png";
        } else if (tbtnPerfil2.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_2.png";
        } else if (tbtnPerfil3.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_3.png";
        }else if (tbtnPerfil4.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_4.png";
        }else if (tbtnPerfil5.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_5.png";
        }else if (tbtnPerfil6.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_6.png";
        }else if (tbtnPerfil7.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_7.png";
        }else if (tbtnPerfil8.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_8.png";
        }else if (tbtnPerfil9.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_9.png";
        }else if (tbtnPerfil10.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_10.png";
        }else if (tbtnPerfil11.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_11.png";
        }else if (tbtnPerfil12.isSelected()) {
            return "/Vista/Paginas/ImgAvatares/perfil_12.png";
        }
        
        return null;
    }
    
    public String getImagenSeleccionada(){
        return ImagenSleccionada;
    }


}
