package com.hims;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
class Initializer {
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driver;
    @Value(value = "${spring.datasource.initial_url}")
    private String initial_url;
    @Value(value = "${spring.datasource.username}")
    private String userName;
    @Value(value = "${spring.datasource.password}")
    private String password;
    @Value(value = "${spring.datasource.createDatabase}")
    private String createDatabase;
    @Value(value = "${spring.datasource.useDatabase}")
    private String useDatabase;
    @Value(value = "${spring.datasource.createUser}")
    private String createUser;
    @Value(value = "${spring.datasource.insertUser}")
    private String insertUser;
    @Value(value = "${spring.datasource.createTreatment_area}")
    private String createTreatment_area;
    @Value(value = "${spring.datasource.insertTreatment_area}")
    private String insertTreatment_area;
    @Value(value = "${spring.datasource.createWard}")
    private String createWard;
    @Value(value = "${spring.datasource.insertWard}")
    private String insertWard;
    @Value(value = "${spring.datasource.createWard_nurse_ward}")
    private String createWard_nurse_ward;
    @Value(value = "${spring.datasource.insertWard_nurse_ward}")
    private String insertWard_nurse_ward;
    @Value(value = "${spring.datasource.createBed}")
    private String createBed;
    @Value(value = "${spring.datasource.insertBed}")
    private String insertBed;
    @Value(value = "${spring.datasource.createPatient}")
    private String createPatient;
    //    @Value(value = "${spring.datasource.insertPatient}")
//    private String insertPatient;
    @Value(value = "${spring.datasource.createNAT_report}")
    private String createNAT_report;
    @Value(value = "${spring.datasource.createDaily_report}")
    private String createDaily_report;
    @Value(value = "${spring.datasource.dropTrigger_on_patient}")
    private String dropTrigger_on_patient;
    @Value(value = "${spring.datasource.drop_transfer_patient_after_add_new_wnurse}")
    private String drop_transfer_patient_after_add_new_wnurse;
    //    @Value(value = "${spring.datasource.createTrigger_on_patient}")
//    private String createTrigger_on_patient;
    private String transfer_patient = "CREATE TRIGGER transfer_patient AFTER UPDATE ON patient FOR EACH ROW" +
            " BEGIN" +
            " declare p_id int;" +
            " if (new.state = \"discharge\") or (new.state = \"dead\") then" +
            " update bed set state = \"free\" where id = new.bed_id;" +
            " set p_id = (select IFNULL(min(id),-1) from patient where rating = new.rating and is_to_be_transferred = 1 and bed_id is null);" +
            " if (p_id != -1) then" +
            " update patient set w_nurse_id = new.w_nurse_id,bed_id = new.bed_id,state = \"hospitalized\",is_to_be_transferred = 0 where id = p_id;" +
            " update bed set state = \"occupied\" where id = new.bed_id;" +
            " else" +
            " set p_id = (select IFNULL(min(id),-1) from patient where rating = new.rating and is_to_be_transferred = 1 and bed_id is not null);" +
            " if (p_id != -1) then" +
            " update patient set w_nurse_id = new.w_nurse_id,bed_id = new.bed_id,is_to_be_transferred = 0 where id = p_id;" +
            " update bed set state = \"occupied\" where id = new.bed_id;" +
            " END IF;" +
            " END IF;" +
            " END IF;" +
            " END";
    private String transfer_patient_after_add_new_wnurse = "CREATE TRIGGER transfer_patient_after_add_new_wnurse AFTER INSERT ON ward_nurse_ward FOR EACH ROW" +
            " BEGIN" +
            " declare rating varchar(20);" +
            " declare bed_id int;" +
            " declare p_id int;" +
            " set rating = (select treatment_area.type from treatment_area,ward where ward.id = new.w_id and ward.t_area_id = treatment_area.id);" +
            " set bed_id = (select IFNULL(min(id),-1) from bed where state = \"free\" and w_id = new.w_id);" +
            " if (bed_id != -1) then" +
            " set p_id = (select IFNULL(min(id),-1) from patient where patient.rating = rating and is_to_be_transferred = 1 and bed_id is null);" +
            " if (p_id != -1) then" +
            " update patient set patient.w_nurse_id = new.w_nurse_id,patient.bed_id = bed_id,patient.state = \"hospitalized\",patient.is_to_be_transferred = 0 where patient.id = p_id;" +
            " update bed set state = \"occupied\" where id = bed_id;" +
            " else" +
            " set p_id = (select IFNULL(min(id),-1) from patient where patient.rating = rating and patient.is_to_be_transferred = 1 and patient.bed_id is not null);" +
            " if (p_id != -1) then" +
            " update patient set patient.w_nurse_id = new.w_nurse_id,patient.bed_id = bed_id,patient.is_to_be_transferred = 0 where patient.id = p_id;" +
            " update bed set state = \"occupied\" where id = bed_id;" +
            " END IF;" +
            " END IF;" +
            " END IF;" +
            " END";

    @Bean
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection conn = DriverManager.getConnection(initial_url, userName, password);
        Statement stat = conn.createStatement();
        stat.executeUpdate(createDatabase);
        stat.execute(useDatabase);
        stat.executeUpdate(createUser);
        stat.executeUpdate(insertUser);
        stat.executeUpdate(createTreatment_area);
        stat.executeUpdate(insertTreatment_area);
        stat.executeUpdate(createWard);
        stat.executeUpdate(insertWard);
        stat.executeUpdate(createWard_nurse_ward);
        stat.executeUpdate(insertWard_nurse_ward);
        stat.executeUpdate(createBed);
        stat.executeUpdate(insertBed);
        stat.executeUpdate(createPatient);
//        stat.executeUpdate(insertPatient);
        stat.executeUpdate(createNAT_report);
        stat.executeUpdate(createDaily_report);
        stat.executeUpdate(dropTrigger_on_patient);
        stat.execute(transfer_patient);
        stat.execute(drop_transfer_patient_after_add_new_wnurse);
        stat.execute(transfer_patient_after_add_new_wnurse);
        stat.close();
        conn.close();
    }

}
