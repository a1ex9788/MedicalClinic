package aplicacio;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DBAccess.ClinicDBAccess;
import java.io.File;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Alejandro
 */
public class ClinicaMedica extends Application {
    
    private static ClinicDBAccess clinica;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXMLClinicaMedica.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Serveis Mèdics Avançats");
        File imageFile = new File("src/imatges/simbol.png"); 
        String fileLocation = imageFile.toURI().toString(); 
        Image icono = new Image(fileLocation);
        stage.getIcons().add(icono);
        stage.setOnCloseRequest(a -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle(clinica.getClinicName());
            confirm.setHeaderText("Salvar els canvis");
            confirm.setContentText("Vol que els canvis es guarden en la base de dades??\n\n");

            ButtonType acceptar = new ButtonType("SI");
            ButtonType cancellar = new ButtonType("NO");
            confirm.getButtonTypes().setAll(acceptar, cancellar);

            Optional<ButtonType> resposta = confirm.showAndWait();

            if (resposta.isPresent() && resposta.get() == acceptar) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Serveis Mèdics Avançats");
                alerta.setHeaderText(null);
                alerta.setContentText("L'aplicació està guardant els canvis. Aquest procés pot tardar uns minuts.\n\n");
                alerta.getButtonTypes().clear();
                alerta.show();
                clinica.saveDB();
                
                Platform.exit();
            }
        });
        stage.setScene(scene);
        stage.show();
    }
    
    public static void setClinica(ClinicDBAccess c) {
        clinica = c;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
