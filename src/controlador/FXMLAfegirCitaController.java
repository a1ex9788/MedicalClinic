/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import DBAccess.ClinicDBAccess;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import model.Appointment;
import model.Days;
import model.Doctor;
import model.ExaminationRoom;
import model.Patient;

/**
 * FXML Controller class
 *
 * @author frand
 */
public class FXMLAfegirCitaController implements Initializable {

    @FXML
    private ComboBox<Patient> pacientCita;
    @FXML
    private ComboBox<LocalTime> horaConsultaCita;
    @FXML
    private ComboBox<Doctor> metgeCita;
    @FXML
    private DatePicker diaConsultaCita;
    @FXML
    private Text textError;
    
    private ClinicDBAccess clinica;
    private boolean guarda = false;
    private Appointment novaCita;
//    private boolean jaAgafada = false;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textError.setText("");

        pacientCita.valueProperty().addListener(a -> {
            metgeCita.setValue(null);
            diaConsultaCita.setValue(null);
            horaConsultaCita.setValue(null);
            
            diaConsultaCita.setDisable(true);
            horaConsultaCita.setDisable(true);
        });
        
        metgeCita.valueProperty().addListener(a -> {
            diaConsultaCita.setDisable(false);
            
            diaConsultaCita.setValue(null);
            horaConsultaCita.setValue(null);
            horaConsultaCita.setDisable(true);
        });

        diaConsultaCita.valueProperty().addListener(a -> {
            horaConsultaCita.setDisable(false);
            
            actualitzaHores();
        });
        
        diaConsultaCita.setShowWeekNumbers(false);
        
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {

                super.updateItem(item, empty);

                this.setDisable(false);
                this.setBackground(null);
                this.setTextFill(Color.BLACK);

                if (item.isBefore(LocalDate.now())) {
                    this.setDisable(true);
                }

                if (metgeCita.getValue() != null) {
                    Doctor d = metgeCita.getValue();
                    DayOfWeek dayweek = item.getDayOfWeek();
                    boolean[] laborals = new boolean[7];
                    Days[] dies = {Days.Monday, Days.Tuesday, Days.Wednesday,
                        Days.Thursday, Days.Friday, Days.Saturday, Days.Sunday};
                    for (int i = 0; i < dies.length; i++) {
                        laborals[i] = d.getVisitDays().contains(dies[i]);
                    }

                    if (!laborals[dayweek.getValue() - 1])  this.setDisable(true);
                } 
            }
            
        };
        
        diaConsultaCita.setDayCellFactory(dayCellFactory);
    }
    
    private void actualitzaHores() {
        if (diaConsultaCita.getValue() != null) {
            horaConsultaCita.getItems().clear();
            Doctor d = metgeCita.getValue();
            for (LocalTime i = d.getVisitStartTime(); i.isBefore(d.getVisitEndTime()); i = i.plus(15, ChronoUnit.MINUTES)) {
                LocalDateTime dataIHora = LocalDateTime.of(diaConsultaCita.getValue(), i);
                boolean lliure = true;
                for (int j = 0; j < clinica.getDoctorAppointments(d.getIdentifier()).size(); j++) {
                    boolean ocupada = false; //Sala ocupa per altre metge
                    for (int k = 0; !ocupada && k < clinica.getAppointments().size(); k++) {
                        Appointment aa = clinica.getAppointments().get(k);
                        if (aa.getAppointmentDateTime().equals(dataIHora)
                                && aa.getDoctor().getExaminationRoom().getIdentNumber()
                                == metgeCita.getValue().getExaminationRoom().getIdentNumber()) {
                            ocupada = true;
                        }
                    }
                    if (clinica.getDoctorAppointments(d.getIdentifier()).get(j).getAppointmentDateTime().equals(dataIHora)
                            || ocupada) { lliure = false; }
                }
                if (lliure
                        && !appointmentDateAlreadyTaken(pacientCita.getValue(), i, metgeCita.getValue().getExaminationRoom())) { 
                    if (!diaConsultaCita.getValue().equals(LocalDate.now())) {
                        horaConsultaCita.getItems().add(i); 
                    } else {
                        if (i.isAfter(LocalTime.now())) { horaConsultaCita.getItems().add(i); }
                    }
                }
            }
        }
    }

    @FXML
    private void guardarNovaCita(ActionEvent event) {
        if (pacientCita.getValue() == null || metgeCita.getValue() == null
                || diaConsultaCita.getValue() == null || horaConsultaCita.getValue() == null) {
            textError.setText("Falten camps per omplir");
        } else {
            LocalDate data = LocalDate.of(diaConsultaCita.getValue().getYear(),
                        diaConsultaCita.getValue().getMonth(), diaConsultaCita.getValue().getDayOfMonth());
            LocalTime hora = LocalTime.of(horaConsultaCita.getValue().getHour(), horaConsultaCita.getValue().getMinute());
            LocalDateTime dataIHora = LocalDateTime.of(data, hora);
            
            novaCita.setPatient(pacientCita.getValue());
            novaCita.setDoctor(metgeCita.getValue());
            novaCita.setAppointmentDateTime(dataIHora);
                
            guarda = true;
            ((Stage) diaConsultaCita.getScene().getWindow()).close();
        }
    }
    
    private boolean appointmentDateAlreadyTaken(Patient p, LocalTime lt, ExaminationRoom er){
        ArrayList<Appointment> aux = clinica.getPatientAppointments(p.getIdentifier());
        LocalTime aux2;
        for(int i = 0; i < aux.size(); i++){
            aux2 = LocalTime.of(aux.get(i).getAppointmentDateTime().getHour(), aux.get(i).getAppointmentDateTime().getMinute());
            if (aux2.equals(lt)){
                return true;}
        }
        return false;
    } 
    
    public void setClinica(ClinicDBAccess c) {
        clinica = c;

        pacientCita.getItems().addAll(clinica.getPatients());
        pacientCita.setConverter(new StringConverter<Patient>() {
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
        
        metgeCita.getItems().addAll(clinica.getDoctors());
        metgeCita.setConverter(new StringConverter<Doctor>() {
            @Override
            public String toString(Doctor d) {
                return d.getSurname() + ", " + d.getName() + " - " + d.getIdentifier();
            }

            @Override
            public Doctor fromString(String s) {
                for (int i = 0; i < clinica.getPatients().size(); i++) {
                    Doctor d = clinica.getDoctors().get(i);
                    String aux = s.substring(s.lastIndexOf(" "), s.length() - 1);
                    if (d.getIdentifier().equals(aux)) { return d; }
                }
                return null; //mai es va a aplegar a esta instrucció
            }
        });
    }
    
    public void setCita(Appointment c) {
        novaCita =  c;
    }
    
    public boolean haGuardat() {
        return guarda;
    }
    

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage) diaConsultaCita.getScene().getWindow()).close();
    }


}
