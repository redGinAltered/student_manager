package org.macbeth.students.logic;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ManagementSystem {

    private static Connection connection;
    private static ManagementSystem instance;

    private ManagementSystem() throws Exception{

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/hr_department",
                    "postgres", "postgres");
        } catch (ClassNotFoundException e) {
            throw new Exception(e);
        } catch (SQLException e){
            throw new Exception(e);
        }

    }

    public static synchronized ManagementSystem getInstance() throws Exception{

        if(instance == null){
            instance = new ManagementSystem();
        }
        return instance;
    }

    public List<Group> getGroups() throws SQLException{

        List<Group> groups = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM groups");
            while (resultSet.next()){
                groups.add(new Group(resultSet));
            }
        } finally {
            if(resultSet != null)
                resultSet.close();
            if (statement != null)
                statement.close();
        }

        return groups;
    }

    public void getAllStudents() throws SQLException{

    }

    public Collection<Student> getStudentsFromGroup(Group group, int year) throws SQLException{
        Collection<Student> students = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{

            preparedStatement = connection.prepareStatement("SELECT * FROM students " +
                    "WHERE group_id=? AND education_year=? ORDER BY sur_name, given_name, patronymic_name");
            preparedStatement.setInt(1, group.getGroupId());
            preparedStatement.setInt(2, year);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                students.add(new Student(resultSet));
            }
        } finally {
            if (resultSet != null){
                resultSet.close();
            }
            if (preparedStatement != null){
                preparedStatement.close();
            }
        }

        return students;
    }

    public void moveStudentsToGroup(Group oldGroup, int oldYear, Group newGroup, int newYear) throws SQLException{

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("UPDATE students SET group_id=?, education_year=? " +
                    "WHERE group_id=? AND education_year=?");
            preparedStatement.setInt(1, newGroup.getGroupId());
            preparedStatement.setInt(2, newYear);
            preparedStatement.setInt(3, oldGroup.getGroupId());
            preparedStatement.setInt(4, oldYear);
            preparedStatement.execute();

        } finally {
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    public void removeStudentsFromGroup(Group group, int year) throws SQLException{

        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("DELETE FROM students " +
                    "WHERE group_id=? AND education_year=?");
            preparedStatement.setInt(1, group.getGroupId());
            preparedStatement.setInt(2, year);
            preparedStatement.execute();
        } finally {
            if(preparedStatement != null)
                preparedStatement.close();
        }
    }

    public void addStudent(Student student) throws SQLException{
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO students " +
                    "(sur_name, given_name, patronymic_name, date_of_birth, sex, group_id, education_year) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, student.getSurName());
            preparedStatement.setString(2, student.getGivenName());
            preparedStatement.setString(3, student.getPatronymicName());
            preparedStatement.setDate(4, new Date(student.getDateOfBirth().getTime()));
            preparedStatement.setString(5, new String(new char[] {student.getSex()}));
            preparedStatement.setInt(6, student.getGroupId());
            preparedStatement.setInt(7, student.getEducationYear());
            preparedStatement.execute();

        } finally {
            if(preparedStatement != null)
                preparedStatement.close();
        }
    }

    public void updateStudent(Student upStudent) throws SQLException {
        PreparedStatement preparedStatement = null;
       try{
           preparedStatement = connection.prepareStatement("UPDATE students SET " +
                   "sur_name=?, given_name=?, patronymic_name=?, " +
                   "date_of_birth=?, sex=?, group_id=?, education_year=?  " +
                   "WHERE student_id=?");
           preparedStatement.setString(1, upStudent.getSurName());
           preparedStatement.setString(2, upStudent.getGivenName());
           preparedStatement.setString(3, upStudent.getPatronymicName());
           preparedStatement.setDate(4, new Date(upStudent.getDateOfBirth().getTime()));
           preparedStatement.setString(5, new String(new char[] {upStudent.getSex()}));
           preparedStatement.setInt(6, upStudent.getGroupId());
           preparedStatement.setInt(7, upStudent.getEducationYear());
           preparedStatement.setInt(8, upStudent.getStudentId());
           preparedStatement.execute();

       } finally {
           if(preparedStatement != null)
               preparedStatement.close();
       }
    }

    public void remoteStudent(Student remStudent) throws SQLException {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement("DELETE FROM students WHERE student_id=?");
            preparedStatement.setInt(1, remStudent.getStudentId());
            preparedStatement.execute();

        } finally{
            if(preparedStatement != null){
                preparedStatement.close();
            }
        }
    }

    public static void printString(Object o){

        try {
            System.out.println(new String(o.toString().getBytes("windows-1251"), "windows-1251"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printString(){
        System.out.println();
    }


}





























