/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import aplicacio.ClinicaMedica;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.Patient;

/**
 * FXML Controller class
 *
 * @author Alejandro
 */
public class FXMLClinicaMedicaController implements Initializable {

    private ClinicDBAccess clinica;
    private ObservableList<Patient> pacients;
    private ObservableList<Doctor> metges;
    private ObservableList<Appointment> cites;
    DatePicker calendari;
    private Patient patient;
    
    @FXML
    private TableView<Patient> taulaPacients;
    @FXML
    private Button eliminaPacientBoto;
    @FXML
    private TableColumn<Patient, String> nomPacient;
    @FXML
    private TableColumn<Patient, String> identificacioPacient;
    @FXML
    private Label nomActual;
    @FXML
    private Label cognomsActual;
    @FXML
    private Label idActual;
    @FXML
    private Label tlfActual;
    @FXML
    private ImageView imatgeActual;
    @FXML
    private TextField buscadorPacient;
    @FXML
    private TextField buscadorMetge;
    @FXML
    private Button eliminaMetgeBoto;
    @FXML
    private TableView<Doctor> taulaMetges;
    @FXML
    private TableColumn<Doctor, String> nomMetge;
    @FXML
    private TableColumn<Doctor, String> identificacioMetge;
    @FXML
    private ImageView imatgeActualMetge;
    @FXML
    private Label nomActualMetge;
    @FXML
    private Label cognomsActualMetge;
    @FXML
    private Label idActualMetge;
    @FXML
    private Label tlfActualMetge;
    @FXML
    private Label diesLaborals;
    @FXML
    private Label salaConsultes;
    @FXML
    private VBox vBoxCalendari;
    @FXML
    private Label horariConsultes;
    @FXML
    private Button eliminaCita;
    @FXML
    private ImageView fonsCites;
    @FXML
    private ImageView fonsPacients;
    @FXML
    private ImageView fonsMetges;
    @FXML
    private GridPane gridAuxCites;
    @FXML
    private GridPane gridAuxPacients;
    @FXML
    private GridPane gridAuxMetges;
    @FXML
    private ComboBox<Patient> buscadorCita;
    @FXML
    private TableColumn<Appointment, String> nomPacientCita;
    @FXML
    private TableColumn<Appointment, String> nomMetgeCita;
    @FXML
    private TableColumn<Appointment, String> horaCita;
    @FXML
    private TableView<Appointment> taulaCites;
    @FXML
    private Button borraSeleccio;
    @FXML
    private VBox spacingVCites;
    @FXML
    private HBox spacingHCites;
    @FXML
    private HBox spacingHPacients;
    @FXML
    private VBox spacingVPacients;
    @FXML
    private HBox spacingHMetges;
    @FXML
    private VBox spacingVMetges;
        

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        clinica = ClinicDBAccess.getSingletonClinicDBAccess();
        clinica.setClinicName("IPC Medical Services Clinic");
        ClinicaMedica.setClinica(clinica);
        
        //PACIENTS
        pacients = FXCollections.observableArrayList(clinica.getPatients());
        nomPacient.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getSurname() + ", " + a.getValue().getName()));
        identificacioPacient.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getIdentifier()));
        taulaPacients.setItems(pacients);
        
        taulaPacients.getSelectionModel().selectedIndexProperty().addListener(a -> {
            eliminaPacientBoto.setDisable(false);
        });
        
        fonsPacients.fitHeightProperty().bind(gridAuxPacients.heightProperty());
        fonsPacients.fitWidthProperty().bind(gridAuxPacients.widthProperty());
        
        buscadorPacient.textProperty().addListener(a -> {
            pacients.clear();
            String cadena = buscadorPacient.getText().toLowerCase();
            for (int i = 0; i < clinica.getPatients().size(); i++) {
                Patient p = clinica.getPatients().get(i);
                String nomComplet = p.getName() + " " + p.getSurname();
                if (nomComplet.toLowerCase().contains(cadena)) {
                    pacients.add(p);
                }
            }
        });
        
        taulaPacients.getSelectionModel().selectedItemProperty().addListener(a -> {
            Patient p = taulaPacients.getSelectionModel().getSelectedItem();
        
            if (p != null) {
                nomActual.setText(p.getName());
                cognomsActual.setText(p.getSurname());
                idActual.setText(p.getIdentifier());
                tlfActual.setText(p.getTelephon());
                imatgeActual.imageProperty().setValue(p.getPhoto());
            }
        });
        
        gridAuxPacients.widthProperty().addListener(a -> {
            spacingHPacients.setSpacing(gridAuxPacients.getWidth() /15);
        });
        
        gridAuxPacients.heightProperty().addListener(a -> {
            spacingVPacients.setSpacing(gridAuxPacients.getWidth() /30);
        });
        
        //METGES I METGESSES
        metges = FXCollections.observableArrayList(clinica.getDoctors());
        nomMetge.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getSurname() + ", " + a.getValue().getName()));
        identificacioMetge.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getIdentifier()));
        taulaMetges.setItems(metges); 
        
        taulaMetges.getSelectionModel().selectedIndexProperty().addListener(a -> {
            eliminaMetgeBoto.setDisable(false);
        });
        
        fonsMetges.fitHeightProperty().bind(gridAuxMetges.heightProperty());
        fonsMetges.fitWidthProperty().bind(gridAuxMetges.widthProperty());
        
        buscadorMetge.textProperty().addListener(a -> {
            metges.clear();
            String cadena = buscadorMetge.getText().toLowerCase();
            for (int i = 0; i < clinica.getDoctors().size(); i++) {
                Doctor d = clinica.getDoctors().get(i);
                String nomComplet = d.getName() + " " + d.getSurname();
                if (nomComplet.toLowerCase().contains(cadena)) {
                    metges.add(d);
                }
            }
        });
        
        taulaMetges.getSelectionModel().selectedItemProperty().addListener(a -> {
            Doctor d = taulaMetges.getSelectionModel().getSelectedItem();
        
            if (d != null) {
                nomActualMetge.setText(d.getName());
                cognomsActualMetge.setText(d.getSurname());
                idActualMetge.setText(d.getIdentifier());
                tlfActualMetge.setText(d.getTelephon());
                imatgeActualMetge.imageProperty().setValue(d.getPhoto());

                String aux = "";
                if (d.getVisitDays().contains(Days.Monday)) { aux += "DL" +  ", "; }
                if (d.getVisitDays().contains(Days.Tuesday)) { aux += "DT" +  ", "; }
                if (d.getVisitDays().contains(Days.Wednesday)) { aux += "DC" +  ", "; }
                if (d.getVisitDays().contains(Days.Thursday)) { aux += "DJ" +  ", "; }
                if (d.getVisitDays().contains(Days.Friday)) { aux += "DV" +  ", "; }
                if (d.getVisitDays().contains(Days.Saturday)) { aux += "DS" +  ", "; }
                if (d.getVisitDays().contains(Days.Sunday)) { aux += "DG" +  ", "; }
                if (!aux.equals("")) { aux = aux.substring(0, aux.length() - 2); }
                diesLaborals.setText(aux);

                horariConsultes.setText(d.getVisitStartTime() + " - " + d.getVisitEndTime());

                salaConsultes.setText(d.getExaminationRoom().getIdentNumber() + "");
            }
        });
        
        gridAuxMetges.widthProperty().addListener(a -> {
            spacingHMetges.setSpacing(gridAuxMetges.getWidth() /15);
        });
        
        gridAuxMetges.heightProperty().addListener(a -> {
            spacingVMetges.setSpacing(gridAuxMetges.getWidth() /30);
        });
        
        //CITES
        fonsCites.fitHeightProperty().bind(gridAuxCites.heightProperty());
        fonsCites.fitWidthProperty().bind(gridAuxCites.widthProperty());
        
        buscadorCita.getItems().addAll(clinica.getPatients());
        buscadorCita.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient p) {
                return p.getSurname() + ", " + p.getName() + " - " + p.getIdentifier();
            }

            @Override
            public Patient fromString(String s) {
                for (int i = 0; i < clinica.getPatients().size(); i++) {
                    Patient p = clinica.getPatients().get(i);
                    String aux = s.substring(s.lastIndexOf(" "), s.length() - 1);
                    if (p.getIdentifier().equals(aux)) { return p; }
                }
                return null; //mai es va a aplegar a esta instrucció
            }
        });
        
        cites = FXCollections.observableArrayList(clinica.getAppointments());
        nomPacientCita.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getPatient().getSurname()
                + ", " + a.getValue().getPatient().getName()));
        nomMetgeCita.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().getDoctor().getSurname()
                + ", " + a.getValue().getDoctor().getName() + " ("
                + a.getValue().getDoctor().getExaminationRoom().getIdentNumber() + ")"));
        horaCita.setCellValueFactory(a -> new SimpleStringProperty(LocalTime.of(a.getValue().getAppointmentDateTime().getHour(),a.getValue().getAppointmentDateTime().getMinute()).toString()));
        taulaCites.setItems(cites);
        
        taulaCites.getSelectionModel().selectedItemProperty().addListener(a -> {
            eliminaCita.setDisable(false);
        });
        
        calendari = new DatePicker(LocalDate.now());
        calendari.setShowWeekNumbers(false);
        DatePickerSkin skin = new DatePickerSkin(calendari);
        Node node = skin.getPopupContent();
        vBoxCalendari.getChildren().add(node);
        
        calendari.setOnAction(a -> {
            eliminaCita.setDisable(true);
            
            cites.clear();
            LocalDate d = calendari.getValue();
            
            ArrayList<Appointment> citesTotals = clinica.getAppointments();
            for(int i = 0; i < citesTotals.size(); i++){
                LocalDateTime data = citesTotals.get(i).getAppointmentDateTime();
                if((LocalDate.of(data.getYear(), data.getMonth(), data.getDayOfMonth())).equals(d)){
                    cites.add(citesTotals.get(i));
                }
            }
            
            eliminaCita.setDisable(true);
        });
        
        //Per a que al obrir l'aplicació mostre les cites de hui
        calendari.setValue(null);
        calendari.setValue(LocalDate.now());
        
        buscadorCita.valueProperty().addListener(a -> {
            if (buscadorCita.getValue() != null) {
                borraSeleccio.setDisable(false);
                patient = buscadorCita.getValue();
                calendari = new DatePicker(LocalDate.now());
                calendari.setShowWeekNumbers(false);
                putGreen(patient, calendari);
                DatePickerSkin skin2 = new DatePickerSkin(calendari);
                Node node2 = skin2.getPopupContent();
                vBoxCalendari.getChildren().remove(0);
                vBoxCalendari.getChildren().add(node2);

                calendari.setOnAction(b -> {
                    cites.clear();
                    LocalDate d = calendari.getValue();

                    ArrayList<Appointment> citesTotals = clinica.getAppointments();
                    for (int i = 0; i < citesTotals.size(); i++) {
                        LocalDateTime data = citesTotals.get(i).getAppointmentDateTime();
                        if ((LocalDate.of(data.getYear(), data.getMonth(), data.getDayOfMonth())).equals(d)) {
                            cites.add(citesTotals.get(i));
                        }
                    }
                });
                calendari.setValue(null);
            }
        });
        
        gridAuxCites.widthProperty().addListener(a -> {
            spacingHCites.setSpacing(gridAuxCites.getWidth() /15);
        });
        
        gridAuxCites.heightProperty().addListener(a -> {
            spacingVCites.setSpacing(gridAuxCites.getWidth() /30);
        });
    }    

    //PACIENTS
    @FXML
    private void nouPacient(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLAfegirPacient.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Afegir pacient");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        Patient p = new Patient();
        ((FXMLAfegirPacientController) loader.getController()).setPacient(p);

        stage.showAndWait();

        if (((FXMLAfegirPacientController) loader.getController()).haGuardat()) {
            boolean yaEsta = false;
            Patient aux;
            for (int i = 0; i < pacients.size(); i++) {
                aux = pacients.get(i);
                if (aux.getIdentifier().equals(p.getIdentifier())) {
                    yaEsta = true;
                }
            }

            if (yaEsta) {
                Alert fi = new Alert(Alert.AlertType.ERROR);
                fi.setTitle("Error");
                fi.setHeaderText(null);
                fi.setContentText("Ja existeix un pacient amb identificador '"
                        + p.getIdentifier() + "' a la llista");
                ButtonType acceptar = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar);
                fi.showAndWait();
            } else {
                //Inserim el Pacient en ordre alfabètic
                int i = 0;
                if (clinica.getPatients().size() > 0) {
                    while (i < clinica.getPatients().size()
                            && clinica.getPatients().get(i).getSurname().compareTo(p.getSurname()) < 0) { i++; }
                }
                clinica.getPatients().add(i, p);
                buscadorPacient.setText("");
                
                Alert fi = new Alert(Alert.AlertType.INFORMATION);
                fi.setTitle("Afegiment");
                fi.setHeaderText(null);
                fi.setContentText("El pacient '" + p.getName() + " "
                        + p.getSurname() + "' ha sigut afegit correctament");
                ButtonType acceptar2 = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar2);
                fi.showAndWait();
            }
        }
    }

    @FXML
    private void eliminaPacient(ActionEvent event) {
        Patient p = taulaPacients.getSelectionModel().getSelectedItem();
        if (!clinica.hasAppointments(p)) {

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Eliminar pacient");
            confirm.setHeaderText(null);
            confirm.setContentText("Segur que vol eliminar el pacient '"
                    + p.getName() + " " + p.getSurname() + "'?\n\n");

            ButtonType acceptar = new ButtonType("Acceptar");
            ButtonType cancellar = new ButtonType("Cancel·lar");
            confirm.getButtonTypes().setAll(acceptar, cancellar);

            Optional<ButtonType> resposta = confirm.showAndWait();

            if (resposta.isPresent() && resposta.get() == acceptar) {
                taulaPacients.getSelectionModel().select(null);

                File imageFile = new File("src/imatges/iconoPacient.png");
                String fileLocation = imageFile.toURI().toString();
                Image avatar = new Image(fileLocation);

                nomActual.setText("");
                cognomsActual.setText("");
                idActual.setText("");
                tlfActual.setText("");
                imatgeActual.setImage(avatar);

                clinica.getPatients().remove(p);
                pacients.remove(p);
                eliminaPacientBoto.setDisable(true);

                Alert fi = new Alert(Alert.AlertType.INFORMATION);
                fi.setTitle("Esborrament");
                fi.setHeaderText(null);
                fi.setContentText("El pacient '" + p.getName() + " "
                        + p.getSurname() + "' ha sigut esborrat correctament");
                ButtonType acceptar2 = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar2);
                fi.showAndWait();
            }
        } else {
            Alert confirm = new Alert(Alert.AlertType.ERROR);
            confirm.setTitle("Error");
            confirm.setHeaderText(null);
            confirm.setContentText("El pacient que vol eliminar té cites pendents o ja n'ha tingut");

            ButtonType acceptar = new ButtonType("Acceptar");
            confirm.getButtonTypes().setAll(acceptar);

            confirm.showAndWait();
        }
    }

    //METGESSES I METGES
    @FXML
    private void nouMetge(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLAfegirMetge.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Afegir metge/metgessa");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        Doctor d = new Doctor();
        ((FXMLAfegirMetgeController) loader.getController()).setMetge(d);
        ((FXMLAfegirMetgeController) loader.getController()).setSales(clinica.getExaminationRooms());

        stage.showAndWait();

        if (((FXMLAfegirMetgeController) loader.getController()).haGuardat()) {
            boolean yaEsta = false;
            Doctor aux;
            for (int i = 0; i < metges.size(); i++) {
                aux = metges.get(i);
                if (aux.getIdentifier().equals(d.getIdentifier())) {
                    yaEsta = true;
                }
            }

            if (yaEsta) {
                Alert fi = new Alert(Alert.AlertType.ERROR);
                fi.setTitle("Error");
                fi.setHeaderText(null);
                fi.setContentText("Ja existeix un metge/metgessa amb identificador '"
                        + d.getIdentifier() + "' a la llista");
                ButtonType acceptar = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar);
                fi.showAndWait();
            } else {
                //Inserim el Metge en ordre alfabètic
                int i = 0;
                if (clinica.getDoctors().size() > 0) {
                    while (i < clinica.getDoctors().size()
                            && clinica.getDoctors().get(i).getSurname().compareTo(d.getSurname()) < 0) { i++; }
                }
                clinica.getDoctors().add(i, d);
                buscadorMetge.setText("");
                
                Alert fi = new Alert(Alert.AlertType.INFORMATION);
                fi.setTitle("Afegiment");
                fi.setHeaderText(null);
                fi.setContentText("El metge/metgessa '" + d.getName() + " "
                        + d.getSurname() + "' ha sigut afegit correctament");
                ButtonType acceptar2 = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar2);
                fi.showAndWait();
            }
        }
    }

    @FXML
    private void eliminaMetge(ActionEvent event) {
        
        Doctor d = taulaMetges.getSelectionModel().getSelectedItem();
        if (!clinica.hasAppointments(d)) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Eliminar metge/metgessa");
            confirm.setHeaderText(null);
            confirm.setContentText("Segur que vol eliminar el metge/metgessa '"
                    + d.getName() + " " + d.getSurname() + "'?\n\n");
            
            ButtonType acceptar = new ButtonType("Acceptar");
            ButtonType cancellar = new ButtonType("Cancel·lar");
            confirm.getButtonTypes().setAll(acceptar, cancellar);
            
            Optional<ButtonType> resposta = confirm.showAndWait();
            
            if (resposta.isPresent() && resposta.get() == acceptar) {
                taulaMetges.getSelectionModel().select(null);
                
                File imageFile = new File("src/imatges/iconoMetge.png");                
                String fileLocation = imageFile.toURI().toString();                
                Image avatar = new Image(fileLocation);
                
                nomActualMetge.setText("");
                cognomsActualMetge.setText("");
                idActualMetge.setText("");
                tlfActualMetge.setText("");
                imatgeActualMetge.setImage(avatar);
                horariConsultes.setText("");
                salaConsultes.setText("");
                diesLaborals.setText("");
                
                clinica.getDoctors().remove(d);
                metges.remove(d);
                eliminaMetgeBoto.setDisable(true);
                
                Alert fi = new Alert(Alert.AlertType.INFORMATION);
                fi.setTitle("Esborrament");
                fi.setHeaderText(null);
                fi.setContentText("El metge/metgessa '" + d.getName() + " "
                        + d.getSurname() + "' ha sigut esborrat correctament");
                ButtonType acceptar2 = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar2);
                fi.showAndWait();
            }
        } else {
            Alert confirm = new Alert(Alert.AlertType.ERROR);
            confirm.setTitle("Error");
            confirm.setHeaderText(null);
            confirm.setContentText("El metge/metgessa que vol eliminar té cites pendents o ja n'ha tingut");
            
            ButtonType acceptar = new ButtonType("Acceptar");
            confirm.getButtonTypes().setAll(acceptar);
            
            confirm.showAndWait();
        }
    }
    
    //CITES
    @FXML
    private void eliminaCita(ActionEvent event) {
        Appointment cita = taulaCites.getSelectionModel().getSelectedItem();
        if (cita.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            Alert fi = new Alert(Alert.AlertType.ERROR);
            fi.setTitle("Error");
            fi.setHeaderText(null);
            fi.setContentText("La cita no es pot esborrar perquè ja ha passat");
            ButtonType acceptar2 = new ButtonType("Acceptar");
            fi.getButtonTypes().setAll(acceptar2);
            fi.showAndWait();
            
        } else {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Eliminar pacient");
            confirm.setHeaderText(null);
            confirm.setContentText("Segur que vol eliminar la cita seleccionada?\n\n");

            ButtonType acceptar = new ButtonType("Acceptar");
            ButtonType cancellar = new ButtonType("Cancel·lar");
            confirm.getButtonTypes().setAll(acceptar, cancellar);

            Optional<ButtonType> resposta = confirm.showAndWait();

            if (resposta.isPresent() && resposta.get() == acceptar) {
                cites.remove(cita);
                clinica.getAppointments().remove(cita);
                taulaCites.getSelectionModel().select(null);

                eliminaCita.setDisable(true);

                Alert fi = new Alert(Alert.AlertType.INFORMATION);
                fi.setTitle("Esborrament");
                fi.setHeaderText(null);
                fi.setContentText("La cita ha sigut esborrada correctament");
                ButtonType acceptar2 = new ButtonType("Acceptar");
                fi.getButtonTypes().setAll(acceptar2);
                fi.showAndWait();
            }
        }
    }

    @FXML
    private void novaCita(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/FXMLAfegirCita.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        Scene scene = new Scene(root);

        stage.setTitle("Afegir cita");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        Appointment citaNova = new Appointment();
        ((FXMLAfegirCitaController) loader.getController()).setCita(citaNova);
        ((FXMLAfegirCitaController) loader.getController()).setClinica(clinica);

        stage.showAndWait();
        
        if (((FXMLAfegirCitaController) loader.getController()).haGuardat()) {
            //Inserim la cita en ordre cronològic
            int i = 0;
            if (clinica.getAppointments().size() > 0) {
                while (i < clinica.getAppointments().size()
                        && clinica.getAppointments().get(i).getAppointmentDateTime().isBefore(citaNova.getAppointmentDateTime())) { i++; }
            }
            clinica.getAppointments().add(i, citaNova);
            //Per a que es mostren les cites del dia on ha sigut inserida la nova
            calendari.setValue(null);
            calendari.setValue(LocalDate.of(citaNova.getAppointmentDateTime().getYear(),
                    citaNova.getAppointmentDateTime().getMonth(), citaNova.getAppointmentDateTime().getDayOfMonth()));
            
            Alert fi = new Alert(Alert.AlertType.INFORMATION);
            fi.setTitle("Afegiment");
            fi.setHeaderText(null);
            fi.setContentText("La cita ha sigut creada correctament");
            ButtonType acceptar2 = new ButtonType("Acceptar");
            fi.getButtonTypes().setAll(acceptar2);
            fi.showAndWait();
        }
    }

    @FXML
    private void borraSeleccio(ActionEvent event) {
        buscadorCita.setValue(null);
        borraSeleccio.setDisable(true);
        calendari = new DatePicker(LocalDate.now());
        calendari.setShowWeekNumbers(false);
        DatePickerSkin skin2 = new DatePickerSkin(calendari);
        Node node2 = skin2.getPopupContent();
        vBoxCalendari.getChildren().remove(0);
        vBoxCalendari.getChildren().add(node2);

        calendari.setOnAction(a -> {
            cites.clear();
            LocalDate d = calendari.getValue();

            ArrayList<Appointment> citesTotals = clinica.getAppointments();
            for (int i = 0; i < citesTotals.size(); i++) {
                LocalDateTime data = citesTotals.get(i).getAppointmentDateTime();
                if ((LocalDate.of(data.getYear(), data.getMonth(), data.getDayOfMonth())).equals(d)) {
                    cites.add(citesTotals.get(i));
                }
            }
        });
        calendari.setValue(null);
        calendari.setValue(LocalDate.now());
    }
    public boolean isAppointment(Patient p, LocalDate ld){
        ArrayList<Appointment> aux = clinica.getPatientAppointments(p.getIdentifier());
        LocalDateTime aux2;
        for(int i = 0; i < aux.size(); i++){
            aux2 = aux.get(i).getAppointmentDateTime();
            if(LocalDate.of(aux2.getYear(), aux2.getMonth(), aux2.getDayOfMonth()).equals(ld)){
                return true;
            }
        }
        return false;
    }       
    private void putGreen(Patient p, DatePicker dp) {
        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (isAppointment(p, item)) {
                            this.setStyle("-fx-background-color: green");
                        }

                    }
                };
            }
        };

        calendari.setDayCellFactory(dayCellFactory);

    }

    @FXML
    private void buscadorCita(Event event) {
        buscadorCita.getItems().clear();
        buscadorCita.getItems().addAll(clinica.getPatients());
    }
    
}
