/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Patient;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class FXMLAfegirPacientController implements Initializable {

    @FXML
    private TextField identificacio;
    @FXML
    private TextField nom;
    @FXML
    private TextField cognoms;
    @FXML
    private TextField telefon;
    @FXML
    private ImageView imatgePacient;
    @FXML
    private Text textError;
    
    private Patient pacientNou;
    private boolean guarda;
    private String rutaImatge;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rutaImatge = "";
        textError.setText("");
        
        guarda = false;
        
        nom.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
            if (!"ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz ".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
        
        cognoms.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
            if (!"ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz ".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
        
        telefon.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
            if (!"0123456789".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
        
        identificacio.addEventFilter(KeyEvent.KEY_TYPED, (KeyEvent keyEvent) -> {
            if (!"0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz".contains(keyEvent.getCharacter())) {
                keyEvent.consume();
            }
        });
    }    

    @FXML
    private void guardarNouPacient(ActionEvent event) throws FileNotFoundException {
        String id = identificacio.getText().trim().toUpperCase(), n = nom.getText(),
                c = cognoms.getText(), t = telefon.getText();
        while (n.startsWith(" ")) { n = n.substring(1); }
        while (c.startsWith(" ")) { c = c.substring(1); }
        
        if (id.trim().equals("") || n.equals("") || c.equals("")
                || t.equals("")) {
            textError.setText("Falten camps per omplir");
        } else {
            //Creació del nou pacient
            pacientNou.setIdentifier(id);
            pacientNou.setName(n);
            pacientNou.setSurname(c);
            pacientNou.setTelephon(t);
            
            //S'afig foto a l'objecte de tipus Pacient si ha sigut inserida o
            // una per defecte en cas contrari
            Image avatar;
            if (!rutaImatge.equals("")) {
                avatar = new Image(new FileInputStream(rutaImatge));
            } else {
                File imageFile = new File("src/imatges/iconoPacient.png"); 
                String fileLocation = imageFile.toURI().toString(); 
                avatar = new Image(fileLocation);
            }
            pacientNou.setPhoto(avatar);
            
            guarda = true;
            
            ((Stage) nom.getScene().getWindow()).close();
        }
    
    }
    
    public void setPacient(Patient p) {
        pacientNou = p;
    }
    
    public boolean haGuardat() {
        return guarda;
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage) nom.getScene().getWindow()).close();
    }

    @FXML
    private void afegirFoto(ActionEvent event) throws FileNotFoundException {
        FileChooser finestra = new FileChooser();
        finestra.setTitle("Abrir fichero");
        finestra.getExtensionFilters().add(
                new ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
        File selectedFile = finestra.showOpenDialog(
                ((Node) event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            String ruta = selectedFile.getAbsolutePath();
            rutaImatge = ruta;
            Image avatar = new Image(new FileInputStream(ruta)); 
            imatgePacient.imageProperty().setValue(avatar);
        }
    }
}    
    