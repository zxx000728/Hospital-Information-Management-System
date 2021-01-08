package com.hims.controller;

import com.hims.controller.request.LoginRequest;
import com.hims.domain.User;
import com.hims.exception.WardNurseDeleteFailureException;
import com.hims.serviceImpl.PatientServiceImpl;
import com.hims.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private PatientServiceImpl patientService;

    @Autowired
    public LoginController(UserServiceImpl userService, PatientServiceImpl patientService) {
        this.userService = userService;
        this.patientService = patientService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request.getId(), request.getPassword()));
    }

    @GetMapping("/workerDataPanel")
    public ResponseEntity<?> getWorkerDataPanel(@RequestParam("id") String id,
                                                @RequestParam("type") String type) {
        switch (type) {
            case "doctor":
                return ResponseEntity.ok(userService.getDoctorDataPanel(id));
            case "h_nurse":
                return ResponseEntity.ok(userService.getHeadNurseDataPanel(id));
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/deleteWardNurse")
    public ResponseEntity<?> deleteWardNurse(@RequestParam("u_id") String u_id,
                                             @RequestParam("w_nurse_id") String w_nurse_id) {
        User user = userService.find(Integer.parseInt(u_id));
        if (user.getU_type().equals("h_nurse")) {
            try {
                userService.deleteWardNurseByWNurseId(Integer.parseInt(w_nurse_id));
                return ResponseEntity.ok("Deleted successfully!");
            } catch (WardNurseDeleteFailureException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("No authority!", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/patientDataPanel")
    public ResponseEntity<?> getPatientDataPanel(@RequestParam("id") String id,
                                                 @RequestParam("type") String type) {
        switch (type) {
            case "doctor":
                return ResponseEntity.ok(patientService.getPatientDataPanelByDoctorId(id));
            case "h_nurse":
                return ResponseEntity.ok(patientService.getPatientDataPanelByHNurseId(id));
            case "w_nurse":
                return ResponseEntity.ok(patientService.getPatientDataPanelByWNurseId(id));
            case "e_nurse":
                return ResponseEntity.ok(patientService.getPatientDataPanelByENurseId());
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/getFreeNurseData")
//    public ResponseEntity<Map<String, Object>> getFreeNurseData(@RequestParam("h_nurse_id") String h_nurse_id) {
//        return ResponseEntity.ok(userService.getFreeNurseData(h_nurse_id));
//    }
}
