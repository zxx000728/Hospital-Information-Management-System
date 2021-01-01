# Hospital Information Management System
This is a course project for Database Design, Fall 2020 by Zhu Xiaoxuan & Shen Yunfei.

### Database: hims

#### Tables:

1. ##### user

| Field     | Type   | Example                              |
| --------- | ------ | ------------------------------------ |
| <u>id</u> | string | 1                                    |
| name      | string | Alice                                |
| password  | string | 123456                               |
| age       | string | 20                                   |
| email     | string | 123456@fudan.edu.cn                  |
| phone     | string | 12345678901                          |
| u_type    | string | e_nurse / w_nurse / h_nurse / doctor |

2. ##### patient

| Field      | Type   | Example                  |
| ---------- | ------ | ------------------------ |
| <u>id</u>  | string | 1                        |
| name       | string | Peter                    |
| age        | string | 20                       |
| phone      | string | 12345678901              |
| address    | string | fudan                    |
| rating     | string | mild / severe / critical |
| e_nurse_id | string | 1                        |
| w_nurse_id | string | 1                        |
| bed_id     | string | 1                        |

3. ##### daily_report

| Field       | Type   | Example                         |
| ----------- | ------ | ------------------------------- |
| <u>id</u>   | string | 1                               |
| p_id        | string | 1                               |
| temperature | string | 37                              |
| symptom     | string | fever                           |
| state       | string | discharge / hospitalized / dead |
| w_nurse_id  | string | 1                               |
| n_report_id | string | 1                               |

4. ##### nat_report

| Field     | Type   | Example                  |
| --------- | ------ | ------------------------ |
| <u>id</u> | string | 1                        |
| p_id      | string | 1                        |
| result    | string | negative / positive      |
| date      | string | 2021/1/1                 |
| rating    | string | mild / severe / critical |

5. ##### treatment_area

| Field      | Type   | Example                  |
| ---------- | ------ | ------------------------ |
| <u>id</u>  | string | 1                        |
| type       | string | mild / severe / critical |
| doctor_id  | string | 1                        |
| h_nurse_id | string | 1                        |

6. ##### ward

| Field     | Type   | Example |
| --------- | ------ | ------- |
| <u>id</u> | string | 1       |
| t_area_id | string | 1       |

7. ##### ward_nurse_ward

| Field             | Type   | Example |
| ----------------- | ------ | ------- |
| <u>w_nurse_id</u> | string | 1       |
| w_id              | string | 1       |

8. ##### bed

| Field     | Type   | Example         |
| --------- | ------ | --------------- |
| <u>id</u> | string | 1               |
| w_id      | string | 1               |
| state     | string | occupied / free |
