package com.hims.service;

import com.hims.domain.Patient;
import com.hims.domain.User;

import java.util.List;

public interface UserService {
    void save(User user);

    void delete(int id);

    User find(int id);

    List<User> findAll();

    User findHeadNurseByDoctorId(int id);

    int findTreatmentAreaIdByDoctorId(int id);

    int findTreatmentAreaIdByHeadNurseId(int id);

    List<Integer> findWardIdByDoctorId(int id);

    List<Integer> findWardIdByHeadNurseId(int id);

    List<User> findWardNurseByWardId(int id);

    List<Patient> findPatientByWardId(int id);

    void deleteWardNurseByWNurseId(int id);

//    List<User> findFreeWardNurse();
//
//    void insertWardNurse(int w_nurse_id, int w_id);
}
