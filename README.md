# Hospital Information Management System

This is a course project for Database Design, Fall 2020 by Zhu Xiaoxuan & Shen Yunfei.

### Database: hims

#### E-R diagram: 

![](ER1.1%20(1).png)

#### Tables:

1. ##### user

   **index**: id, u_type

| Field         | Type        | Example                              |
| ------------- | ----------- | ------------------------------------ |
| <u>**id**</u> | int         | 1                                    |
| name          | varchar(20) | Alice                                |
| password      | varchar(50) | 123456                               |
| age           | varchar(20) | 20                                   |
| email         | varchar(50) | 123456@fudan.edu.cn                  |
| phone         | varchar(20) | 12345678901                          |
| u_type        | varchar(20) | e_nurse / w_nurse / h_nurse / doctor |

2. ##### patient

   **index**: id, e_nurse_id, w_nurse_id, bed_id, state

| Field                | Type        | Example                         |
| -------------------- | ----------- | ------------------------------- |
| <u>**id**</u>        | int         | 1                               |
| name                 | varchar(20) | Peter                           |
| age                  | varchar(20) | 20                              |
| phone                | varchar(20) | 12345678901                     |
| address              | varchar(50) | fudan                           |
| rating               | varchar(20) | mild / severe / critical        |
| e_nurse_id           | int         | 1                               |
| w_nurse_id           | int         | 1                               |
| bed_id               | int         | 1                               |
| state                | varchar(20) | discharge / hospitalized / dead |
| is_to_be_released    | boolean     | 0 / 1                           |
| is_to_be_transferred | boolean     | 0 / 1                           |

3. ##### daily_report

   **index**: p_id, w_nurse_id, date

| Field         | Type        | Example                         |
| ------------- | ----------- | ------------------------------- |
| <u>**id**</u> | int         | 1                               |
| p_id          | int         | 1                               |
| date          | DATE        | 2021-01-10                      |
| temperature   | varchar(20) | 37                              |
| symptom       | varchar(50) | fever                           |
| state         | varchar(20) | discharge / hospitalized / dead |
| w_nurse_id    | int         | 1                               |

4. ##### nat_report

   **index**: p_id, date, time

| Field         | Type        | Example             |
| ------------- | ----------- | ------------------- |
| <u>**id**</u> | int         | 1                   |
| p_id          | int         | 1                   |
| result        | varchar(20) | negative / positive |
| date          | DATE        | 2021-01-01          |
| time          | TIME        | 14:30:10            |

5. ##### treatment_area

| Field         | Type        | Example                  |
| ------------- | ----------- | ------------------------ |
| <u>**id**</u> | int         | 1                        |
| type          | varchar(20) | mild / severe / critical |
| doctor_id     | int         | 1                        |
| h_nurse_id    | int         | 1                        |

6. ##### ward

   **index**: t_area_id

| Field         | Type | Example |
| ------------- | ---- | ------- |
| <u>**id**</u> | int  | 1       |
| t_area_id     | int  | 1       |

7. ##### ward_nurse_ward

   **index**: w_nurse_id, w_id

| Field                 | Type | Example |
| --------------------- | ---- | ------- |
| <u>**w_nurse_id**</u> | int  | 1       |
| w_id                  | int  | 1       |

8. ##### bed

   **index**: w_id, state

| Field         | Type        | Example         |
| ------------- | ----------- | --------------- |
| <u>**id**</u> | int         | 1               |
| w_id          | int         | 1               |
| state         | varchar(20) | occupied / free |

9. ##### to_be_transfer（用作触发器的中间表）

| Field      | Type      | Example |
| ---------- | --------- | ------- |
| p_id       | int       | 1       |
| w_nurse_id | int       | 1       |
| bed_id     | int       | 1       |
| time       | TIMESTAMP | NOW()   |

#### 核心功能的 Sql 语句

1. ##### 增加新的病房护士并返回 id：

   ```mysql
   insert into user(name,password,age,email,phone,u_type) values(?,?,?,?,?,?);
   ```

   ```java
   KeyHolder keyHolder = new GeneratedKeyHolder();
   PreparedStatementCreator preparedStatementCreator = con -> {
       PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
       ps.setString(1, name);
       ps.setString(2, "123456");
       ps.setString(3, age);
       ps.setString(4, email);
       ps.setString(5, phone);
       ps.setString(6, "w_nurse");
       return ps;
   };
   jdbcTemplate.update(preparedStatementCreator, keyHolder);
   return Objects.requireNonNull(keyHolder.getKey()).intValue();
   ```

   ```mysql
   insert into ward_nurse_ward(w_nurse_id,w_id) values (?,?);
   ```

2. ##### 删除病房护士：

   ```mysql
   delete from ward_nurse_ward where w_nurse_id=?;
   ```

3. ##### 修改个人信息：

   ```mysql
   update user set name=?,password=?,age=?,email=?,phone=? where id=?;
   ```

4. ##### 查找病房中的护士：

   ```mysql
   select user.* from user,ward_nurse_ward where ward_nurse_ward.w_id=? and ward_nurse_ward.w_nurse_id=user.id;
   ```

5. ##### 查找护士负责的病房：

   ```mysql
   select w_id from ward_nurse_ward where w_nurse_id=?;
   ```

6. ##### 查找病房护士当前负责的病人数：

   ```mysql
   select count(id) from patient where w_nurse_id=? and state='hospitalized';
   ```

7. ##### 查找病区信息：

   ```mysql
   select user.* from user,treatment_area where treatment_area.doctor_id=? and treatment_area.h_nurse_id=user.id; // head nurse
   select id from treatment_area where doctor_id=?; // find by doctor id
   select type from treatment_area where h_nurse_id=?; // find type by head nurse id
   select id from treatment_area where h_nurse_id=?; // find by head nurse id
   ```

8. ##### 查找等待转区的病人：

   ```mysql
   select patient.*,bed.w_id,ward.t_area_id from patient left join bed on patient.bed_id=bed.id left join ward on bed.w_id=ward.id where patient.rating = ? and patient.is_to_be_transferred = 1 and patient.bed_id is not null and bed.w_id is not null; // 已经住院但病情评级改变后等待转区
   select IFNULL(min(id),-1) from patient where rating = ? and is_to_be_transferred = 1 and bed_id = 0; // 在隔离区中选一个病人
   ```

9. ##### 病人转区更新：

   ```mysql
   UPDATE patient SET w_nurse_id=?,bed_id=?,state=?,is_to_be_released=?,is_to_be_transferred=? WHERE id=?;
   ```

10. ##### 查找所有病人：

    ```mysql
    select patient.*,bed.w_id,ward.t_area_id from patient left join bed on patient.bed_id=bed.id left join ward on bed.w_id=ward.id;
    ```

11. ##### 查找当前病房的病人：

    ```mysql
    select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where bed.w_id=? and patient.bed_id=bed.id and bed.w_id=ward.id；
    ```

12. ##### 查找病房护士负责的病人：

    ```mysql
    select patient.*,bed.w_id,ward.t_area_id from patient,bed,ward where patient.w_nurse_id=? and patient.bed_id=bed.id and bed.w_id=ward.id；
    ```

13. ##### 新增核酸检测报告：

    ```mysql
    insert into nat_report(p_id,result,date,time) values (?,?,?,?);
    ```

14. ##### 查找病人的核酸检测报告并按时间排序：

    ```mysql
    select * from nat_report where p_id = ? order by date DESC;
    ```

15. ##### 查找病人的每日报告并按时间排序：

    ```mysql
    select * from daily_report where p_id = ? ORDER BY date DESC;
    ```

16. ##### 增加新的每日报告：

    ```mysql
    insert into daily_report(p_id,date,temperature,symptom,state,w_nurse_id) values (?,?,?,?,?,?);
    ```

17. ##### 更新床位状态：

    ```mysql
    UPDATE bed SET state=? WHERE id=?;
    ```

18. ##### 查找当前病房的空床：

    ```mysql
    select bed.* from bed where bed.w_id=? and bed.state='free';
    ```

#### 触发器说明

1. ##### to_be_transfer

   因为 mysql 触发器不能递归调用，所以用 to_be_transfer 作为中间表。当有病人的状态改为 discharge / dead 时，将该病人的病床状态设为 free，并从隔离区中选择一个病情评级与该病人相同的待转入的病人，向 to_be_transfer 插入一条新数据，记录待转入病人的 id，待转入病房的病房护士 id，待转入的病床 id，以及转入时间。当 to_be_transfer 表插入新数据时，本触发器会被触发，更新转入病人的数据并将该病床的状态改为 occupied。

```java
// old patient's state - discharge / dead
// bed - free
// insert into to_be_transfer(p_id,w_nurse_id,bed_id,data_time) values (6,13,2,NOW());
private String transfer = " CREATE TRIGGER transfer AFTER INSERT ON to_be_transfer" +
    					  " FOR EACH ROW" +
            			  " BEGIN" +
            			  " update patient set w_nurse_id = new.w_nurse_id,bed_id = new.bed_id,state = \"hospitalized\",is_to_be_transferred = 0 where id = new.p_id;" +
            			  " update bed set state = \"occupied\" where id = new.bed_id;" +
            			  " END";
private String drop_transfer = "DROP TRIGGER IF EXISTS transfer;";
```

2. ##### transfer_patient_after_add_new_wnurse

   由于病房护士不足，可能会出现有空床但病人无法转入的情况。当护士长添加新的护士后，本触发器会被触发，自动转入在隔离区等待的对应病人。首先会根据该病区的类型找到对应 rating 的在隔离区等待的病人，如果有病人在等待，就从中选择一个 id 最小的。再判断该区域是否有空床，如果有的话选择一张床，更新该病人的数据将病人转入。

```java
private String transfer_patient_after_add_new_wnurse = 
    " CREATE TRIGGER transfer_patient_after_add_new_wnurse" +
    " AFTER INSERT ON ward_nurse_ward FOR EACH ROW" +
    " BEGIN" +
    " declare r varchar(20);" +
    " declare b int;" +
    " declare p_id int;" +
    " set r = (select treatment_area.`type` from treatment_area,ward where ward.id = new.w_id and ward.t_area_id = treatment_area.id);" +
    " set b = (select IFNULL(min(id),-1) from bed where state = \"free\" and w_id = new.w_id);" +
    " if (b != -1) then" +
    " set p_id = (select IFNULL(min(id),-1) from patient where rating = r and is_to_be_transferred = 1 and bed_id = 0);" +
    " if (p_id != -1) then" +
    " update patient set w_nurse_id = new.w_nurse_id,bed_id = b,state = \"hospitalized\",is_to_be_transferred = 0 where id = p_id;" +
    " update bed set state = \"occupied\" where id = b;" +
    " END IF;" +
    " END IF;" +
    " END";
```

## 前端

- optional 注册 & 登录

- 医护：修改个人信息（登陆密码等）

- 检测单 Component

- 病人信息 Component

- 每日登记 Component

- 所有病人和医护人员信息的增删改查（医护信息、病人信息、病床信息 Table）

  |          | 医护信息                                                                                       | 病人信息                                                                                                                                                                                                                  | 病床信息                                                                                 |
  | -------- | ---------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------- |
  | 主治医生 | 查看当前治疗区域的护士长及病房护士信息，以及病房护士负责的病人                                 | 查看当前治疗区域的病人信息并支持不同条件的筛选（例如根据是否满足出院条件、是否待转入其他治疗区域、病人生命状态等）// 修改病人的病情评级、生命状态；为病人**添加新的检测单**；轻症治疗区域的主治医生决定病人是否可康复出院 |                                                                                          |
  | 护士长   | 查看本治疗区域的病房护士信息，以及病房护士负责的病人信息*(跳转 ?)* // 增删该区域的病房护士信息 | 查看当前治疗区域的病人信息并支持不同条件的筛选（例如根据是否满足出院条件、是否待转入其他治疗区域、病人生命状态等）                                                                                                        | 查看本治疗区域的病床信息，以及病床的病人信息*(跳转 ?)*（若未安排病人，则病床状态为空置） |
  | 急诊护士 |                                                                                                | 查看各区域病人信息并支持不同条件的筛选（例如根据治疗区域、是否在隔离区等待、病人病情评级、病人生命状态等）// **登记病人基本信息以及病情等级**                                                                             |                                                                                          |
  | 病房护士 |                                                                                                | 查看自己负责的病人的信息并支持不同条件的筛选（例如根据是否可以出院、生命状态等）// **每天信息登记**（包括体温、存在的症状、生命状态、核酸检测结果等）                                                                     |                                                                                          |

- 通知

  - 空位提示：如果该治疗区域的护士或者床位没有空余，条件允许时通知病人前来住院治疗
  - 转入提示：若需要转入的治疗区域当前条件不允许转入，后继系统可以查询到此类病人，当待转入治疗区域条件满足时帮助其转入，并向该区域的护士长发出提示。
  - 出院提示：病人满足出院条件时，系统会自动向其主治医生发出提示，主治医生也可以查询到可以出院的病人及其信息，并允许病人出院。

