/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */

package Controlador;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
 

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar la vista principal desde el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Principal.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista principal
            PrincipalController controller = loader.getController();
            controller.cargartodo(); // Cargar datos al iniciar
            
            // Configurar la ventana principal
            Image icono = new Image("/Modelo/pixelcut2.png");
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu Principal");
            primaryStage.setResizable(false);
            primaryStage.getIcons().add(icono);
            primaryStage.show();
            
            // Guardar datos al cerrar la aplicación
            primaryStage.setOnCloseRequest(e -> {
                controller.guardartodo();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    
    public static void main(String[] args) {
        launch(args);
    }
}
