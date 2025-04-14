package Modelo;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

public class After_entrar_cargador {

    private Pane pantalla;

    /**
     * Obtiene una pantalla FXML a partir del nombre del archivo.
     *
     * @param archivo El nombre del archivo FXML (sin extensión).
     * @return La pantalla cargada como un objeto Pane.
     */
    public Pane obtenerPantalla(String archivo) {
        try {
            // Construye la URL del archivo FXML
            URL direccion = getClass().getResource("/Vista/Paginas/" + archivo + ".fxml");

            if (direccion == null) {
                throw new IOException("No se encontró el archivo FXML: " + archivo);
            }

            // Carga el archivo FXML
            FXMLLoader cargador = new FXMLLoader(direccion);
            pantalla = cargador.load();
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo FXML: " + archivo);
            e.printStackTrace();
        }

        return pantalla;
    }
}
