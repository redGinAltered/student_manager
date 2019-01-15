package org.macbeth.students.gui;

import org.macbeth.students.logic.Student;

import javax.swing.table.AbstractTableModel;
import java.text.DateFormat;
import java.util.Vector;

public class StudentTableModel extends AbstractTableModel {

    private Vector<Student> students;
    private String[] columns = {"Фамилия","Имя","Отчество","Дата рождения"};

    public StudentTableModel(Vector<Student> students){
        this.students = students;
    }

    @Override
    public int getRowCount() {
        if(students != null){
            return students.size();
        }
        return 0;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(students != null){
            Student student = students.get(rowIndex);
            switch (columnIndex){
                case 0:
                    return student.getSurName();
                case 1:
                    return student.getGivenName();
                case 2:
                    return student.getPatronymicName();
                case 3:
                    return DateFormat.getDateInstance(DateFormat.SHORT).format(student.getDateOfBirth());
            }
        }
        return null;
    }

    public String getColumnName(int columnIndex){
        return this.columns[columnIndex];
    }

    public Student getStudent(int rowIndex){
        if(students != null)
            if(rowIndex >= 0 && rowIndex < students.size()){
                return students.get(rowIndex);
            }

        return null;
    }

    public boolean isEmpty(){
        return students.isEmpty();
    }

}
