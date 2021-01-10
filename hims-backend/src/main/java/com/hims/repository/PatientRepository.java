package com.hims.repository;

import com.hims.domain.DailyReport;
import com.hims.domain.NatReport;
import com.hims.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class PatientRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DailyReportRepository dailyReportRepository;
    @Autowired
    private NATReportRepository natReportRepository;

//    public Patient find(int id) {
//        String sql = "select * from patient where id=?";
//        return jdbcTemplate.queryForObject(sql, Patient.class, id);
//    }

    public List<Patient> getToTransfer(String rating) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient left join bed on patient.bed_id=bed.id left join ward on bed.w_id=ward.id where patient.rating = ? and patient.is_to_be_transferred = 1 and patient.bed_id is not null and bed.w_id is not null";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), rating);
    }

    public void update(int id, int w_nurse_id, int bed_id, String state,
                       int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET w_nurse_id=?,bed_id=?,state=?,is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, w_nurse_id, bed_id, state, is_to_be_released, is_to_be_transferred, id);
    }

    public void updateTArea(int id, int is_to_be_released, int is_to_be_transferred) {
        String sql = "UPDATE patient SET is_to_be_released=?,is_to_be_transferred=? WHERE id=?";
        jdbcTemplate.update(sql, is_to_be_released, is_to_be_transferred, id);
    }

    @Transactional
    public void updateRelease(int id) {
        String sql = "update patient set is_to_be_released = 1 where id=?";
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void updateRating(int id, String rating) {
        String sql = "update patient set rating = ?,is_to_be_transferred = 1 where id=?";
        jdbcTemplate.update(sql, rating, id);

    }

    @Transactional
    public void afterTransfer(int w_nurse_id, int bed_id, String rating) {
        String sql = "select IFNULL(min(id),-1) from patient where rating = ? and is_to_be_transferred = 1 and bed_id = 0";
        int p_id = jdbcTemplate.queryForObject(sql, Integer.class, rating);
        System.out.println(p_id);
        if (p_id != -1) {
            sql = "insert into to_be_transfer(p_id,w_nurse_id,bed_id,data_time) values (?,?,?,NOW())";
            jdbcTemplate.update(sql, p_id, w_nurse_id, bed_id);
        }
    }

    @Transactional
    public void updateState(int id, String state) {
        String sql = "update patient set state = ? where id=?";
        jdbcTemplate.update(sql, state, id);
        if (state.equals("discharge") || state.equals("dead")) {
            Patient old_patient = find(id);
            sql = "update bed set state = 'free' where id = ?";
            jdbcTemplate.update(sql, old_patient.getBed_id());
            sql = "select IFNULL(min(id),-1) from patient where rating = ? and is_to_be_transferred = 1 and bed_id = 0";
            int p_id = jdbcTemplate.queryForObject(sql, Integer.class, old_patient.getRating());
            if (p_id != -1) {
                sql = "insert into to_be_transfer(p_id,w_nurse_id,bed_id,data_time) values (?,?,?,NOW())";
                jdbcTemplate.update(sql, p_id, old_patient.getW_nurse_id(), old_patient.getBed_id());
            }
//            else {
//                sql = "select IFNULL(min(id),-1) from patient where rating = ? and is_to_be_transferred = 1 and bed_id != 0";
//                p_id = jdbcTemplate.queryForObject(sql, Integer.class, old_patient.getRating());
//                if (p_id != -1) {
//                    sql = "insert into to_be_transfer(p_id,w_nurse_id,bed_id,data_time) values (?,?,?,NOW())";
//                    jdbcTemplate.update(sql, p_id, old_patient.getW_nurse_id(), old_patient.getBed_id());
//                }
//            }
        }
    }

    @Transactional
    public List<Patient> getReleasedPatient() {
        List<Patient> all = findAll();
        List<Patient> current = new ArrayList<>();
        List<Patient> result = new ArrayList<>();
        if (all != null && all.size() > 0) {
            System.out.println("all" + all.size());
            for (Patient patient : all) {
                if (patient.getT_area_id() == 1 && (patient.getState().equals("hospitalized"))) {
                    current.add(patient);
                }
            }
        }
        System.out.println("current" + current.size());
        for (Patient patient : current) {
            int p_id = patient.getId();
            System.out.println("p_id" + p_id);
            List<DailyReport> dailyReports = dailyReportRepository.findByPIdAndDate(p_id);
            System.out.println("daily" + dailyReports.size());
            if (dailyReports.size() >= 3 && Float.parseFloat(dailyReports.get(0).getTemperature()) < 37.3 &&
                    Float.parseFloat(dailyReports.get(1).getTemperature()) < 37.3 &&
                    Float.parseFloat(dailyReports.get(2).getTemperature()) < 37.3) {
                List<NatReport> natReports = natReportRepository.findByPIdAndDate(p_id);
                if (natReports != null && natReports.size() >= 2) {
                    System.out.println(natReports.size());
//                    Map<String,Long> map = new HashMap<>();
                    Map<NatReport, Long> map = new HashMap<>();
//                    List<Date> dates = new ArrayList<>();
                    for (NatReport natReport : natReports) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date;
                        try {
                            date = sdf.parse(natReport.getDate() + " " + natReport.getTime());
                            System.out.println(date.getTime());
//                            dates.add(date);
//                            map.put(String.valueOf(natReport.getId()),date.getTime());
                            map.put(natReport, date.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    List<Map.Entry<NatReport, Long>> list = new ArrayList<>(map.entrySet());
                    list.sort((o1, o2) -> {
                        //return o1.getValue().compareTo(o2.getValue());
                        return o2.getValue().compareTo(o1.getValue());
                    });
                    Map.Entry<NatReport, Long> first = list.get(0);
                    System.out.println(first.getValue());
                    if (first.getKey().getResult().equals("negative")) {
                        for (int i = 1; i < list.size(); i++) {
                            Map.Entry<NatReport, Long> nat = list.get(i);
                            System.out.println((first.getValue() - nat.getValue()) / (1000 * 24 * 60 * 60));
                            System.out.println(nat.getKey().getResult());
                            if (nat.getKey().getResult().equals("negative") &&
                                    ((first.getValue() - nat.getValue()) / (1000 * 24 * 60 * 60)) >= 1) {
                                String s = "update patient set is_to_be_released = 1 where id=?";
                                jdbcTemplate.update(s, p_id);
                                System.out.println("aaa");
                                patient.setIs_to_be_released(true);
                                result.add(patient);
                                break;
                            } else if (nat.getKey().getResult().equals("positive")) {
                                break;
                            }
                        }
                    }


//                    for (int i = 1; i < dates.size(); i++) {
//                        if (natReports.get(i).getResult().equals("negative") &&
//                                ((dates.get(i).getTime() - dates.get(0).getTime()) / (1000 * 24 * 60 * 60)) >= 1) {
//                            String s = "update patient set is_to_be_released = 1 where id=?";
//                            jdbcTemplate.update(s, p_id);
//                            result.add(patient);
//                            break;
//                        } else if (natReports.get(i).getResult().equals("positive")) {
//                            break;
//                        }
//                    }
                }
            }
        }
        return result;
    }

    public String getPatientState(int id) {
        String sql = "select state from patient where id=?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public String getPatientRating(int id) {
        String sql = "select rating from patient where id=?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }

    public int insert(Patient patient) {
        String sql = "insert into patient(name,age,phone,address,rating,e_nurse_id) values (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getAge());
            ps.setString(3, patient.getPhone());
            ps.setString(4, patient.getAddress());
            ps.setString(5, patient.getRating());
            ps.setInt(6, patient.getE_nurse_id());
            return ps;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public Patient find(int id) {
        List<Patient> patients = findAll();
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public List<Patient> findAll() {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient left join bed on patient.bed_id=bed.id left join ward on bed.w_id=ward.id";
        try {
//            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class));
            List<Patient> patients = jdbcTemplate.query(sql, rs -> {
                List<Patient> list = new ArrayList<>();
                while (rs.next()) {
                    Patient patient = new Patient();
                    patient.setId(rs.getInt("id"));
                    patient.setName(rs.getString("name"));
                    patient.setAge(rs.getString("age"));
                    patient.setPhone(rs.getString("phone"));
                    patient.setAddress(rs.getString("address"));
                    patient.setRating(rs.getString("rating"));
                    patient.setE_nurse_id(rs.getInt("e_nurse_id"));
                    patient.setW_nurse_id(rs.getInt("w_nurse_id"));
                    patient.setT_area_id(rs.getInt("t_area_id"));
                    patient.setW_id(rs.getInt("w_id"));
                    patient.setBed_id(rs.getInt("bed_id"));
                    patient.setState(rs.getString("state"));
                    patient.setIs_to_be_released(rs.getBoolean("is_to_be_released"));
                    patient.setIs_to_be_transferred(rs.getBoolean("is_to_be_transferred"));
                    list.add(patient);
                }
                return list;
            });
            return patients;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findByWardId(int w_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where bed.w_id=? and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findByWNurseId(int w_nurse_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.w_nurse_id=? and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Patient> findHospitalizedByWNurseId(int w_nurse_id) {
        String sql = "select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.w_nurse_id=? and patient.state='hospitalized'and patient.bed_id=bed.id and bed.w_id=ward.id";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), w_nurse_id);
        } catch (Exception e) {
            return null;
        }
    }
}
