# Hospital Information Management System
This is a course project for Database Design, Fall 2020 by Zhu Xiaoxuan & Shen Yunfei.

### Database: hims

#### Tables:

1. ##### user

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

| Field         | Type        | Example                  |
| ------------- | ----------- | ------------------------ |
| <u>**id**</u> | int         | 1                        |
| name          | varchar(20) | Peter                    |
| age           | varchar(20) | 20                       |
| phone         | varchar(20) | 12345678901              |
| address       | varchar(50) | fudan                    |
| rating        | varchar(20) | mild / severe / critical |
| e_nurse_id    | int         | 1                        |
| w_nurse_id    | int         | 1                        |
| bed_id        | int         | 1                        |

3. ##### daily_report

| Field         | Type        | Example                         |
| ------------- | ----------- | ------------------------------- |
| <u>**id**</u> | int         | 1                               |
| p_id          | int         | 1                               |
| temperature   | varchar(20) | 37                              |
| symptom       | varchar(50) | fever                           |
| state         | varchar(20) | discharge / hospitalized / dead |
| w_nurse_id    | int         | 1                               |
| n_report_id   | int         | 1                               |

4. ##### nat_report

| Field         | Type        | Example                  |
| ------------- | ----------- | ------------------------ |
| <u>**id**</u> | int         | 1                        |
| p_id          | int         | 1                        |
| result        | varchar(20) | negative / positive      |
| date          | varchar(20) | 2021/1/1                 |
| rating        | varchar(20) | mild / severe / critical |

5. ##### treatment_area

| Field         | Type        | Example                  |
| ------------- | ----------- | ------------------------ |
| <u>**id**</u> | int         | 1                        |
| type          | varchar(20) | mild / severe / critical |
| doctor_id     | int         | 1                        |
| h_nurse_id    | int         | 1                        |

6. ##### ward

| Field         | Type | Example |
| ------------- | ---- | ------- |
| <u>**id**</u> | int  | 1       |
| t_area_id     | int  | 1       |

7. ##### ward_nurse_ward

| Field                 | Type | Example |
| --------------------- | ---- | ------- |
| <u>**w_nurse_id**</u> | int  | 1       |
| w_id                  | int  | 1       |

8. ##### bed

| Field         | Type        | Example         |
| ------------- | ----------- | --------------- |
| <u>**id**</u> | int         | 1               |
| w_id          | int         | 1               |
| state         | varchar(20) | occupied / free |
