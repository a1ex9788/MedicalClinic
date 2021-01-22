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
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;

/**
 * FXML Controller class
 *
 * @author FRACOPED
 */
public class FXMLAfegirMetgeController implements Initializable {

    private Doctor metgeNou;
    private boolean guarda;
    private String rutaImatge;
    private ArrayList<ExaminationRoom> sales;
    private ExaminationRoom habitacio;

    @FXML
    private ImageView imatgeMetge;
    @FXML
    private Text textError;
    @FXML
    private TextField nom;
    @FXML
    private TextField cognoms;
    @FXML
    private TextField identificacio;
    @FXML
    private TextField telefon;
    @FXML
    private ComboBox<String> salaConsulta;
    @FXML
    private ComboBox<LocalTime> iniciConsulta;
    @FXML
    private ComboBox<LocalTime> fiConsulta;
    @FXML
    private CheckBox dillunsBox;
    @FXML
    private CheckBox dimartsBox;
    @FXML
    private CheckBox dimecresBox;
    @FXML
    private CheckBox dijousBox;
    @FXML
    private CheckBox divendresBox;
    @FXML
    private CheckBox disabteBox;
    @FXML
    private CheckBox diumengeBox;
    

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
        
        //Suposem que l'horari es de 8:00 a 22:00
        afegirDataEnRang(LocalTime.of(8, 00), LocalTime.of(21, 0), iniciConsulta.getItems());
        
        iniciConsulta.valueProperty().addListener(a -> {
            fiConsulta.setDisable(false);
            fiConsulta.setValue(null);
            fiConsulta.getItems().clear();
            
            afegirDataEnRang(iniciConsulta.getValue().plus(1, ChronoUnit.HOURS), LocalTime.of(22, 0), fiConsulta.getItems());
        });
    }
    
    private void afegirDataEnRang(LocalTime a, LocalTime b, ObservableList<LocalTime> llista) {
        LocalTime i;
        for (i = a; i.isBefore(b); i = i.plus(30, ChronoUnit.MINUTES)) {
            llista.add(i);
        }
        llista.add(i);
    }

    @FXML
    private void guardarNouMetge(ActionEvent event) throws FileNotFoundException {
        String id = identificacio.getText().trim().toUpperCase(), n = nom.getText(),
                c = cognoms.getText(), t = telefon.getText();
        while (n.startsWith(" ")) { n = n.substring(1); }
        while (c.startsWith(" ")) { c = c.substring(1); }
        
        ArrayList<Days> vd = new ArrayList();
        if (dillunsBox.isSelected()) { vd.add(Days.Monday); }
        if (dimartsBox.isSelected()) { vd.add(Days.Tuesday); }
        if (dimecresBox.isSelected()) { vd.add(Days.Wednesday); }
        if (dijousBox.isSelected()) { vd.add(Days.Thursday); }
        if (divendresBox.isSelected()) { vd.add(Days.Friday); }
        if (disabteBox.isSelected()) { vd.add(Days.Saturday); }
        if (diumengeBox.isSelected()) { vd.add(Days.Sunday); }

        LocalTime ic = iniciConsulta.getValue(), fc = fiConsulta.getValue();

        if (id.equals("") || n.trim().equals("") || c.trim().equals("")
                || t.trim().equals("") ||  vd.isEmpty() || ic == null
                || fc == null || salaConsulta.getValue() == null) {
                textError.setText("Falten camps per omplir");
        } else {
            //Creació del nou metge
            metgeNou.setIdentifier(id);
            metgeNou.setName(n);
            metgeNou.setSurname(c);
            metgeNou.setTelephon(t);
            for (int i = 0; i < sales.size(); i++) {
                ExaminationRoom r = sales.get(i);
                Integer i1 = r.getIdentNumber(); //Amb ints normals dona excepció
                Integer i2 = Integer.parseInt(salaConsulta.getValue().substring(0, salaConsulta.getValue().indexOf(" ")));
                if (Objects.equals(i1, i2)) {
                    habitacio = r;
                    break;
                }
            }
            metgeNou.setExaminationRoom(habitacio);
            metgeNou.setVisitDays(vd);
            metgeNou.setVisitStartTime(ic);
            metgeNou.setVisitEndTime(fc);

            //S'afig foto a l'objecte de tipus Doctor si ha sigut inserida o
            // una per defecte en cas contrari
            Image avatar;
            if (!rutaImatge.equals("")) {
                avatar = new Image(new FileInputStream(rutaImatge));
            } else {
                File imageFile = new File("src/imatges/iconoMetge.png");
                String fileLocation = imageFile.toURI().toString();
                avatar = new Image(fileLocation);
            }
            metgeNou.setPhoto(avatar);

            guarda = true;

            ((Stage) nom.getScene().getWindow()).close();
        }

    }

    public void setMetge(Doctor d) {
        metgeNou = d;
    }
    
    public void setSales(ArrayList<ExaminationRoom> s) {
        sales = s;
        
        salaConsulta.getItems().clear();
        for (int i = 0; i < sales.size(); i++) {
            salaConsulta.getItems().add(sales.get(i).getIdentNumber() + " -> "
                    + sales.get(i).getEquipmentDescription());
        }
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
            imatgeMetge.imageProperty().setValue(avatar);
        }

    }
}
