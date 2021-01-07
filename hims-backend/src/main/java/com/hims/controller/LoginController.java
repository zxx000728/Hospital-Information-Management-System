package com.hims.controller;

import com.hims.controller.request.LoginRequest;
import com.hims.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {
    private UserServiceImpl userService;

    @Autowired
    public LoginController(UserServiceImpl userService) {
        this.userService = userService;
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
        return ResponseEntity.badRequest().body("Bad request");
    }
}
