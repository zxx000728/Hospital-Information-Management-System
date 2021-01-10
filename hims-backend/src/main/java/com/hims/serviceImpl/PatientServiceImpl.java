package com.hims.serviceImpl;

import com.hims.domain.Bed;
import com.hims.domain.NatReport;
import com.hims.domain.Patient;
import com.hims.repository.*;
import com.hims.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private DailyReportRepository dailyReportRepository;
    @Autowired
    private NATReportRepository natReportRepository;
    @Autowired
    private TreatmentAreaRepository treatmentAreaRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private BedRepository bedRepository;
    @Autowired
    private WardNurseAndWardRepository wardNurseAndWardRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, UserServiceImpl userService,
                              DailyReportRepository dailyReportRepository, NATReportRepository natReportRepository,
                              TreatmentAreaRepository treatmentAreaRepository, WardRepository wardRepository,
                              BedRepository bedRepository, WardNurseAndWardRepository wardNurseAndWardRepository) {
        this.patientRepository = patientRepository;
        this.userService = userService;
        this.dailyReportRepository = dailyReportRepository;
        this.natReportRepository = natReportRepository;
        this.treatmentAreaRepository = treatmentAreaRepository;
        this.wardRepository = wardRepository;
        this.bedRepository = bedRepository;
        this.wardNurseAndWardRepository = wardNurseAndWardRepository;
    }

    public Map<String, Object> getPatientDataPanelByHNurseId(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> wards = userService.findWardIdByHeadNurseId(Integer.parseInt(id));
        List<Patient> patients = new ArrayList<>();
        for (Integer integer : wards) {
            patients.addAll(patientRepository.findByWardId(integer));
        }
        map.put("patient", patients);
        return map;
    }

    public Map<String, Object> getPatientDataPanelByDoctorId(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> wards = userService.findWardIdByDoctorId(Integer.parseInt(id));
        List<Patient> patients = new ArrayList<>();
        for (Integer integer : wards) {
            patients.addAll(patientRepository.findByWardId(integer));
        }
        map.put("patient", patients);
        return map;
    }

    public Map<String, Object> getPatientDataPanelByWNurseId(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("patient", patientRepository.findByWNurseId(Integer.parseInt(id)));
        return map;
    }

    public Map<String, Object> getPatientDataPanelByENurseId() {
        Map<String, Object> map = new HashMap<>();
        map.put("patient", patientRepository.findAll());
        return map;
    }

    public int insertNewPatient(Patient patient) {
        return patientRepository.insert(patient);
    }

    public void insertNewNATReport(NatReport natReport) {
        natReportRepository.insert(natReport);
    }

    @Transactional
    public String transferPatientToOther(int id, String rating, String old_rating) {
        Patient patient = patientRepository.find(id);
        int t_area_id = treatmentAreaRepository.findByType(rating);
        List<Integer> w_id = wardRepository.findByTreatmentAreaId(t_area_id);
        List<Bed> freeBed = new ArrayList<>();
        for (Integer integer : w_id) {
            List<Bed> temp = bedRepository.findFreeByWardId(integer);
            if (temp != null && temp.size() >= 1) {
                freeBed.addAll(temp);
            }
        }
        if (freeBed.size() == 0) {
            patientRepository.updateTArea(id, 0, 1);
            return "No free bed, wait.";
        }
        for (Bed bed : freeBed) {
            int ward_id = bed.getW_id();
            System.out.println("ward_id" + ward_id);
            int w_nurse_id = wardNurseAndWardRepository.findFreeWardNurse(ward_id, t_area_id);
            if (w_nurse_id != -1) {
                patientRepository.update(id, w_nurse_id, bed.getId(), "hospitalized", 0, 0);
                bedRepository.updateState(patient.getBed_id(), "free");
                bedRepository.updateState(bed.getId(), "occupied");
                System.out.println("auto transfer");
                System.out.println("old" + old_rating);
                System.out.println("old_n_id" + patient.getW_nurse_id());
                System.out.println("old_b_id" + patient.getBed_id());
                patientRepository.afterTransfer(patient.getW_nurse_id(), patient.getBed_id(), old_rating);
                return "OK!";
            }
        }
        patientRepository.updateTArea(id, 0, 1);
        return "No free ward nurse, wait.";
    }

    public String transferPatient(int id, String rating) {
        int t_area_id = treatmentAreaRepository.findByType(rating);
        List<Integer> w_id = wardRepository.findByTreatmentAreaId(t_area_id);
        List<Bed> freeBed = new ArrayList<>();
        for (Integer integer : w_id) {
            List<Bed> temp = bedRepository.findFreeByWardId(integer);
            if (temp != null && temp.size() >= 1) {
                freeBed.addAll(temp);
            }
        }
        if (freeBed.size() == 0) {
            patientRepository.updateTArea(id, 0, 1);
            return "No free bed, transfer to quarantine.";
        }
        for (Bed bed : freeBed) {
            int ward_id = bed.getW_id();
            System.out.println("ward_id" + ward_id);
            int w_nurse_id = wardNurseAndWardRepository.findFreeWardNurse(ward_id, t_area_id);
            if (w_nurse_id != -1) {
                patientRepository.update(id, w_nurse_id, bed.getId(), "hospitalized", 0, 0);
                bedRepository.updateState(bed.getId(), "occupied");
                return "OK!";
            }
        }
        patientRepository.updateTArea(id, 0, 1);
        return "No free ward nurse, transfer to quarantine.";
    }
}
