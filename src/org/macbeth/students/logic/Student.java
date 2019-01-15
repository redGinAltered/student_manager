package org.macbeth.students.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class Student implements Comparable {

    private int studentId;
    private String surName;
    private String givenName;
    private String patronymicName;
    private Date dateOfBirth;
    private char sex;
    private int groupId;
    private int educationYear;

    public Student(){

    }

    public Student(ResultSet resultSet) throws SQLException{

        setStudentId(resultSet.getInt("student_id"));
        setSurName(resultSet.getString("sur_name"));
        setGivenName(resultSet.getString("given_name"));
        setPatronymicName(resultSet.getString("patronymic_name"));
        setDateOfBirth(resultSet.getDate("date_of_birth"));
        setSex(resultSet.getString("sex").charAt(0));
        setGroupId(resultSet.getInt("group_id"));
        setEducationYear(resultSet.getInt("education_year"));
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPatronymicName() {
        return patronymicName;
    }

    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }


    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getEducationYear() {
        return educationYear;
    }

    public void setEducationYear(int educationYear) {
        this.educationYear = educationYear;
    }

    @Override
    public String toString() {
        return surName + " " + givenName + " " + patronymicName + ", " +
                DateFormat.getDateInstance(DateFormat.SHORT).format(dateOfBirth) +
                ", Пол: " + sex + ", Группа No." + groupId + " Год: " + educationYear;
    }

    public String getShortInfo(){
        return surName + " " + givenName + " " + patronymicName;
    }

    @Override
    public int compareTo(Object o) {
        Collator collator = Collator.getInstance(new Locale("ru"));
        collator.setStrength(Collator.PRIMARY);
        return collator.compare(this.toString(), o.toString());
    }
}
