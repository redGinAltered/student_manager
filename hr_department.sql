DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS groups;

CREATE TABLE groups(

    group_id SERIAL,
    group_name varchar(100) not null,
    curator varchar(200) not null,
    speciality varchar(200) not null,
    PRIMARY KEY(group_id)
);

CREATE TABLE students(

    student_id SERIAL,
    sur_name varchar(100) not null,
    given_name varchar(100) not null,
    patronymic_name varchar(100) not null,
    date_of_birth date not null,
    sex char(1),
    group_id integer not null,
    education_year integer not null,
    PRIMARY KEY(student_id),
    FOREIGN KEY(group_id) REFERENCES groups(group_id) ON DELETE RESTRICT
);

INSERT INTO groups (group_name, curator, speciality)
VALUES ('001', 'Влад Цепеш', 'Гематология');

INSERT INTO groups (group_name, curator, speciality)
VALUES ('002', 'Клод Рейнс', 'Отпика');

insert into groups (group_name, curator, speciality)
values ('003','Имма Хотеп','Египтология');

insert into groups (group_name, curator, speciality)
values ('004','Some One','Something');

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Стокер','Брэм', '-',  'М', '1996-03-20', 1, 2007);

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Харпер', 'Вильгельмина', '-', 'М', '1995-06-10', 1, 2007);

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Сторм', 'Сьюзан', '-', 'Ж', '1997-01-10', 2, 2007);

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Хи', 'СЩник', '-', 'М', '1995-03-12', 2, 2007);

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Лорд', 'Карнарвон', '-', 'М', '1996-07-19', 3, 2007);

insert into students (sur_name, given_name, patronymic_name, sex, date_of_birth, group_id, education_year)
values ('Картер', 'Говард', '-', 'М', '1995-04-29', 3, 2007);