package com.hims.controller;

import com.hims.domain.NatReport;
import com.hims.domain.Patient;
import com.hims.domain.User;
import com.hims.repository.PatientRepository;
import com.hims.repository.TreatmentAreaRepository;
import com.hims.repository.UserRepository;
import com.hims.serviceImpl.PatientServiceImpl;
import com.hims.serviceImpl.ReportServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class PatientController {
    @Autowired
    private PatientServiceImpl patientService;
    @Autowired
    private ReportServiceImpl reportService;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TreatmentAreaRepository treatmentAreaRepository;

    @Autowired
    public PatientController(PatientServiceImpl patientService, ReportServiceImpl reportService,
                             PatientRepository patientRepository, UserRepository userRepository,
                             TreatmentAreaRepository treatmentAreaRepository) {
        this.patientService = patientService;
        this.reportService = reportService;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.treatmentAreaRepository = treatmentAreaRepository;
    }

    @GetMapping("/addPatient")
    @Transactional
    public ResponseEntity<?> addPatient(@RequestParam("ENurseId") String ENurseId,
                                        @RequestParam("NATResult") String NATResult, @RequestParam("address") String address,
                                        @RequestParam("age") String age, @RequestParam("name") String name, @RequestParam("phone") String phone,
                                        @RequestParam("rating") String rating, @RequestParam("testDate") String testDate,
                                        @RequestParam("testTime") String testTime) {
        Patient patient = new Patient(name, age, phone, address, rating, Integer.parseInt(ENurseId));
        int id = patientService.insertNewPatient(patient);
        // NatReport natReport = new NatReport(id, NATResult, testDate, testTime,
        // rating);
        NatReport natReport = new NatReport(id, NATResult, testDate, testTime);
        patientService.insertNewNATReport(natReport);
        return new ResponseEntity<>(patientService.transferPatient(id, rating), HttpStatus.OK);
    }

    @GetMapping("/getPatientInfo")
    public ResponseEntity<?> getPatientInfo(@RequestParam("id") String id) {
        return new ResponseEntity<>(patientRepository.find(Integer.parseInt(id)), HttpStatus.OK);
    }

    @GetMapping("/permitRelease")
    public ResponseEntity<?> permitRelease(@RequestParam("id") String id) {
        patientRepository.updateState(Integer.parseInt(id), "discharge");
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }

    @GetMapping("/modifyPatientRating")
    public ResponseEntity<?> modifyPatientRating(@RequestParam("id") String id, @RequestParam("rating") String rating) {
        String old_rating = patientRepository.getPatientRating(Integer.parseInt(id));
        patientRepository.updateRating(Integer.parseInt(id), rating);
        return new ResponseEntity<>(patientService.transferPatientToOther(Integer.parseInt(id), rating, old_rating), HttpStatus.OK);
    }

    @GetMapping("/getMessage")
    public ResponseEntity<?> getMessage(@RequestParam("id") String id) {
        User user = userRepository.find(Integer.parseInt(id));
        switch (user.getU_type()) {
            case "doctor":
                return new ResponseEntity<>(patientRepository.getReleasedPatient(), HttpStatus.OK);
            case "h_nurse":
                String rating = treatmentAreaRepository.findTypeByHNurseId(user.getId());
                return new ResponseEntity<>(patientRepository.getToTransfer(rating), HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam("id") String id, @RequestParam("t_area_id") String t_area_id) {
        String old_rating = patientRepository.getPatientRating(Integer.parseInt(id));
        String rating;
        switch (t_area_id) {
            case "1":
                rating = "mild";
                break;
            case "2":
                rating = "severe";
                break;
            case "3":
                rating = "critical";
                break;
            default:
                rating = "";
                break;
        }
        return new ResponseEntity<>(patientService.transferPatientToOther(Integer.parseInt(id), rating, old_rating), HttpStatus.OK);
    }

    @GetMapping("/modifyPatientState")
    public ResponseEntity<?> modifyPatientState(@RequestParam("id") String id, @RequestParam("state") String state) {
        patientRepository.updateState(Integer.parseInt(id), state);
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }

    @GetMapping("/addNATReport")
    public ResponseEntity<?> addNATReport(@RequestParam("p_id") String p_id) {
        String message = reportService.addNATReport(Integer.parseInt(p_id));
        if (message.equals("OK!")) {
            return new ResponseEntity<>("OK!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/addDailyReport")
    public ResponseEntity<?> addDailyReport(@RequestParam("p_id") String p_id, @RequestParam("date") String date,
                                            @RequestParam("temperature") String temperature, @RequestParam("symptom") String symptom,
                                            @RequestParam("w_nurse_id") String w_nurse_id) {
        String state = patientRepository.getPatientState(Integer.parseInt(p_id));
        String message = reportService.addDailyReport(Integer.parseInt(p_id), date, Float.parseFloat(temperature),
                symptom, state, Integer.parseInt(w_nurse_id));
        if (message.equals("OK!")) {
            return new ResponseEntity<>("OK!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getDailyReport")
    public ResponseEntity<?> getDailyReport(@RequestParam("p_id") String p_id) {
        return new ResponseEntity<>(reportService.getDailyReport(Integer.parseInt(p_id)), HttpStatus.OK);
    }

    @GetMapping("/getNATReport")
    public ResponseEntity<?> getNATReport(@RequestParam("p_id") String p_id) {
        return new ResponseEntity<>(reportService.getNATReport(Integer.parseInt(p_id)), HttpStatus.OK);
    }

    @GetMapping("/fillNATReport")
    public ResponseEntity<?> fillNATReport(@RequestParam("id") String id, @RequestParam("result") String result,
                                           @RequestParam("date") String date, @RequestParam("time") String time) {
        reportService.fillNATReport(id, result, date, time);
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }
}
