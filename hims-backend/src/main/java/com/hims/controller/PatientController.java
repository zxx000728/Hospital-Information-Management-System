package com.hims.controller;

import com.hims.controller.request.AddPatientRequest;
import com.hims.domain.Patient;
import com.hims.serviceImpl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientController {
    @Autowired
    private PatientServiceImpl patientService;

    @Autowired
    public PatientController(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/addPatient")
    public ResponseEntity<?> addPatient(@RequestBody AddPatientRequest request) {
        Patient patient = new Patient(request.getName(), request.getAge(), request.getPhone(),
                request.getAddress(), request.getRating(), Integer.parseInt(request.getE_nurse_id()),
                false, true);
        
        return new ResponseEntity<>("OK!", HttpStatus.OK);
    }
}
