package org.macbeth.students.gui;

import org.macbeth.students.logic.Group;
import org.macbeth.students.logic.ManagementSystem;
import org.macbeth.students.logic.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class StudentDialog extends JDialog{

    private static final int D_HEIGHT = 250;
    private static final int D_WIDTH = 450;
    private static final int LABEL_X = 10;
    private static final int LABEL_WIDTH = 100;
    private static final int COMPONENT_WIDTH = 150;

    private StudentFrame owner_frame;
    private boolean result = false;

    private int studentId = 0;
    private JTextField surName = new JTextField();
    private JTextField givenName = new JTextField();
    private JTextField patronymicName = new JTextField();
    private JSpinner dateOfBirth = new JSpinner(new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.YEAR));
    private ButtonGroup sex = new ButtonGroup();
    private JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2006, 1900, 2100, 1));
    private JComboBox<Group> groupBox;

    public StudentDialog(List<Group> groups, boolean isNewStudent, StudentFrame owner){

        this.owner_frame = owner;
        getContentPane().setLayout(new FlowLayout()); ////??????????

        groupBox = new JComboBox<>(new Vector<>(groups));

        JRadioButton male = new JRadioButton("Муж");
        JRadioButton female = new JRadioButton("Жен");
        male.setActionCommand("М");
        female.setActionCommand("Ж");
        sex.add(male);
        sex.add(female);

        getContentPane().setLayout(null);

        JLabel label = new JLabel("Фамилия: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 10, LABEL_WIDTH, 20);
        getContentPane().add(label);
        surName.setBounds(LABEL_X + LABEL_WIDTH + 10, 10, COMPONENT_WIDTH, 20);
        getContentPane().add(surName);

        label = new JLabel("Имя: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 30, LABEL_WIDTH, 20);
        getContentPane().add(label);
        givenName.setBounds(LABEL_X + LABEL_WIDTH +10, 30, COMPONENT_WIDTH, 20);
        getContentPane().add(givenName);

        label = new JLabel("Отчество: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 50, LABEL_WIDTH, 20);
        getContentPane().add(label);
        patronymicName.setBounds(LABEL_X + LABEL_WIDTH +10, 50, COMPONENT_WIDTH, 20);
        getContentPane().add(patronymicName);

        label = new JLabel("Пол: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 70, LABEL_WIDTH, 20);
        getContentPane().add(label);
        male.setBounds(LABEL_X + LABEL_WIDTH +10, 70, COMPONENT_WIDTH /2, 20);
//        male.setSelected(true);//////////////////////////////////////////////////////////////////////
        getContentPane().add(male);

        female.setBounds(LABEL_X + LABEL_WIDTH +(COMPONENT_WIDTH /2) + 10, 70, COMPONENT_WIDTH /2, 20);
        getContentPane().add(female);

        label = new JLabel("Дата рождения", JLabel.RIGHT);
        label.setBounds(LABEL_X, 90, LABEL_WIDTH, 20);
        getContentPane().add(label);
        dateOfBirth.setBounds(LABEL_X + LABEL_WIDTH + 10, 90, COMPONENT_WIDTH, 20);
        getContentPane().add(dateOfBirth);

        label = new JLabel("Группа: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 115, LABEL_WIDTH, 20);
        getContentPane().add(label);
        groupBox.setBounds(LABEL_X + LABEL_WIDTH +10, 115, COMPONENT_WIDTH, 20);
        getContentPane().add(groupBox);

        label = new JLabel("Год обучения: ", JLabel.RIGHT);
        label.setBounds(LABEL_X, 145, LABEL_WIDTH, 20);
        getContentPane().add(label);
        yearSpinner.setBounds(LABEL_X+LABEL_WIDTH+10, 145, COMPONENT_WIDTH, 20);
        getContentPane().add(yearSpinner);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener((ActionEvent e) -> {
            result = false;
            setVisible(false);
        });
        btnCancel.setBounds(LABEL_X+LABEL_WIDTH+COMPONENT_WIDTH+10+50, 40, 100, 25);
        getContentPane().add(btnCancel);

        if(isNewStudent){
            setTitle("Добавление новых студентов");
            JButton btnNew = new JButton("New");
            groupBox.setSelectedIndex(owner_frame.getGroupList().getSelectedIndex());
            yearSpinner.getModel().setValue(owner_frame.getYear());
            btnNew.addActionListener((ActionEvent e) -> {
                result = true;
                try{
                    if(isCorrect()){
                        ManagementSystem.getInstance().addStudent(getStudent());
                        owner_frame.reloadStudents();
                        surName.setText("");
                        givenName.setText("");
                        patronymicName.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Одно или несколько полей не заполнены");
                    }
                } catch (Exception sql_e) {
                    JOptionPane.showMessageDialog(this, sql_e.getMessage());
                }
            });
            btnNew.setBounds(LABEL_X + LABEL_WIDTH + COMPONENT_WIDTH + 10 +50, 10, 100, 25);
            getContentPane().add(btnNew);
        } else {
            setTitle("Редактирование данных студента");
            JButton btnOk = new JButton("OK");
            int rowIndex = owner_frame.getStudentTable().getSelectedRow();
            setStudent(((StudentTableModel)owner_frame.getStudentTable().getModel()).getStudent(rowIndex));
            btnOk.addActionListener((ActionEvent e) -> {
                try {
                    ManagementSystem.getInstance().updateStudent(getStudent());
                    owner_frame.reloadStudents();
                    result = true;
                    setVisible(false);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this, e1.getMessage());
                }
            });
            btnOk.setBounds(LABEL_X + LABEL_WIDTH + COMPONENT_WIDTH + 10 +50, 10, 100, 25);
            getContentPane().add(btnOk);
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(D_WIDTH, D_HEIGHT);
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }

    private void setStudent(Student student){

        studentId = student.getStudentId();
        surName.setText(student.getSurName());
        givenName.setText(student.getGivenName());
        patronymicName.setText(student.getPatronymicName());
        dateOfBirth.getModel().setValue(student.getDateOfBirth());
        for(Enumeration e = sex.getElements(); e.hasMoreElements();){
            AbstractButton abstractButton = (AbstractButton) e.nextElement();
            abstractButton.setSelected(abstractButton.getActionCommand().equals("" + student.getSex()));
        }
        yearSpinner.getModel().setValue(student.getEducationYear());
        for(int i = 0; i< groupBox.getModel().getSize(); i++){
            Group group = groupBox.getModel().getElementAt(i);
            if(group.getGroupId() == student.getGroupId()){
                groupBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private Student getStudent(){

        Student student = new Student();

        student.setStudentId(studentId);
        student.setSurName(surName.getText());
        student.setGivenName(givenName.getText());
        student.setPatronymicName(patronymicName.getText());
        student.setDateOfBirth(((SpinnerDateModel) dateOfBirth.getModel()).getDate());
        for (Enumeration e = sex.getElements(); e.hasMoreElements();){
            AbstractButton abstractButton = (AbstractButton) e.nextElement();
            if(abstractButton.isSelected()){
                student.setSex(abstractButton.getActionCommand().charAt(0));
            }
        }

        student.setEducationYear(((SpinnerNumberModel)yearSpinner.getModel()).getNumber().intValue());
        student.setGroupId(((Group)groupBox.getSelectedItem()).getGroupId());

        return student;
    }

    public boolean getResult(){
        return result;
    }

    private boolean isCorrect(){

        boolean RadioSelection = false;

        for(Enumeration e = sex.getElements(); e.hasMoreElements();){
            AbstractButton abstractButton = (AbstractButton) e.nextElement();
            if(abstractButton.isSelected()){
                RadioSelection = true;
                break;
            }
        }

        return !surName.getText().equals("") &&
                !givenName.getText().equals("") &&
                !patronymicName.getText().equals("") &&
                RadioSelection;
    }

}






























