package com.hims.controller;

import com.hims.domain.NatReport;
import com.hims.domain.Patient;
import com.hims.repository.PatientRepository;
import com.hims.serviceImpl.PatientServiceImpl;
import com.hims.serviceImpl.ReportServiceImpl;
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
    public PatientController(PatientServiceImpl patientService, ReportServiceImpl reportService,
                             PatientRepository patientRepository) {
        this.patientService = patientService;
        this.reportService = reportService;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/addPatient")
    @Transactional
    public ResponseEntity<?> addPatient(@RequestParam("ENurseId") String ENurseId,
                                        @RequestParam("NATResult") String NATResult,
                                        @RequestParam("address") String address,
                                        @RequestParam("age") String age,
                                        @RequestParam("name") String name,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("rating") String rating,
                                        @RequestParam("testDate") String testDate,
                                        @RequestParam("testTime") String testTime) {
        Patient patient = new Patient(name, age, phone,
                address, rating, Integer.parseInt(ENurseId));
        int id = patientService.insertNewPatient(patient);
//        NatReport natReport = new NatReport(id, NATResult, testDate, testTime, rating);
        NatReport natReport = new NatReport(id, NATResult, testDate, testTime);
        patientService.insertNewNATReport(natReport);
        return new ResponseEntity<>(patientService.transferPatient(id, rating), HttpStatus.OK);
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
    public ResponseEntity<?> addDailyReport(@RequestParam("p_id") String p_id,
                                            @RequestParam("date") String date,
                                            @RequestParam("temperature") String temperature,
                                            @RequestParam("symptom") String symptom,
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
    public ResponseEntity<?> fillNATReport(@RequestParam("id") String id,
                                           @RequestParam("result") String result,
                                           @RequestParam("date") String date,
                                           @RequestParam("time") String time) {
        reportService.fillNATReport(id, result, date, time);
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }
}
