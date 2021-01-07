package com.hims.serviceImpl;

import com.hims.domain.Patient;
import com.hims.domain.User;
import com.hims.exception.BadCredentialsException;
import com.hims.exception.UserNotFoundException;
import com.hims.repository.*;
import com.hims.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TreatmentAreaRepository treatmentAreaRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private WardNurseAndWardRepository wardNurseAndWardRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TreatmentAreaRepository treatmentAreaRepository,
                           WardRepository wardRepository, PatientRepository patientRepository,
                           WardNurseAndWardRepository wardNurseAndWardRepository) {
        this.userRepository = userRepository;
        this.treatmentAreaRepository = treatmentAreaRepository;
        this.wardRepository = wardRepository;
        this.patientRepository = patientRepository;
        this.wardNurseAndWardRepository = wardNurseAndWardRepository;
    }

    public Map<String, Object> login(String id, String password) {
        User user = find(Integer.parseInt(id));
        if (user == null) {
            throw new UserNotFoundException(id);
        } else if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("user", user);
            return map;
        }
    }

    public Map<String, Object> getDoctorDataPanel(String id) {
        Map<String, Object> map = new HashMap<>();
        User headNurse = findHeadNurseByDoctorId(Integer.parseInt(id));
        map.put("headNurse", headNurse); // headNurse
        List<Integer> wards = findWardIdByDoctorId(Integer.parseInt(id));
        List<User> wardNurse = new ArrayList<>();
        for (Integer integer : wards) {
            wardNurse.addAll(findWardNurseByWardId(integer));
        }
        map.put("wardNurse", wardNurse); // wardNurse
        return map;
    }

    public Map<String, Object> getHeadNurseDataPanel(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> wards = findWardIdByHeadNurseId(Integer.parseInt(id));
        List<User> wardNurse = new ArrayList<>();
        for (Integer integer : wards) {
            wardNurse.addAll(findWardNurseByWardId(integer));
        }
        map.put("wardNurse", wardNurse); // wardNurse
        return map;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public User find(int id) {
        return userRepository.find(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findHeadNurseByDoctorId(int id) {
        return treatmentAreaRepository.findHeadNurseByDoctorId(id);
    }

    @Override
    public int findTreatmentAreaIdByDoctorId(int id) {
        return treatmentAreaRepository.findByDoctorId(id);
    }

    @Override
    public int findTreatmentAreaIdByHeadNurseId(int id) {
        return treatmentAreaRepository.findByHeadNurseId(id);
    }

    @Override
    public List<Integer> findWardIdByDoctorId(int id) {
        int t_area_id = findTreatmentAreaIdByDoctorId(id);
        return wardRepository.findByTreatmentAreaId(t_area_id);
    }

    @Override
    public List<Integer> findWardIdByHeadNurseId(int id) {
        int t_area_id = findTreatmentAreaIdByHeadNurseId(id);
        return wardRepository.findByTreatmentAreaId(t_area_id);
    }

    @Override
    public List<User> findWardNurseByWardId(int id) {
        return wardNurseAndWardRepository.findWardNurseByWardId(id);
    }

    @Override
    public List<Patient> findPatientByWardId(int id) {
        return patientRepository.findByWardId(id);
    }
}
