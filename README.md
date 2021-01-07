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

| Field         | Type        | Example                         |
| ------------- | ----------- | ------------------------------- |
| <u>**id**</u> | int         | 1                               |
| name          | varchar(20) | Peter                           |
| age           | varchar(20) | 20                              |
| phone         | varchar(20) | 12345678901                     |
| address       | varchar(50) | fudan                           |
| rating        | varchar(20) | mild / severe / critical        |
| e_nurse_id    | int         | 1                               |
| w_nurse_id    | int         | 1                               |
| bed_id        | int         | 1                               |
| state         | varchar(20) | discharge / hospitalized / dead |

3. ##### daily_report

   **index**: p_id, w_nurse_id, n_report_id

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

   **index**: p_id

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

