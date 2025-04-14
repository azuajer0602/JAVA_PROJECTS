package Controlador;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
     
          try {
            // Cargar la vista principal desde el archivo FXML
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/First_User_View.fxml"));
          Parent root = loader.load();

            // Configurar la ventana principal
          Scene scene = new Scene(root);
          primaryStage.setScene(scene);
          primaryStage.setTitle("Inicio");
          primaryStage.getIcons().add(new Image("/Vista/imagenes/Logo Bank Blanco sin fondo.png"));
          primaryStage.setResizable(false);
          primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
