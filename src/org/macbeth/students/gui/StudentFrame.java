package org.macbeth.students.gui;

import org.macbeth.students.logic.Group;
import org.macbeth.students.logic.ManagementSystem;
import org.macbeth.students.logic.Student;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

public class StudentFrame extends JFrame{

    private ManagementSystem managementSystem;
    private JList<Group> groupList;
    private JTable studentTable;
    private JSpinner yearSpinner;

    public StudentFrame() throws Exception {

        getContentPane().setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Отчёты");

        JMenuItem menuItem = new JMenuItem("Все студенты");
        menuItem.addActionListener((ActionEvent e) -> showAllStudents());
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(new Label("Год обучения"));

        SpinnerModel spinnerModel = new SpinnerNumberModel(2006, 1900, 2100, 1);
        yearSpinner = new JSpinner(spinnerModel);
        yearSpinner.addChangeListener((ChangeEvent e) -> reloadStudents());
        topPanel.add(yearSpinner);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel(new BorderLayout()){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, 0);
            }
        };
        leftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        managementSystem = ManagementSystem.getInstance();

        Vector<Group> groupVector = new Vector<>(managementSystem.getGroups());
        leftPanel.add(new Label("Группы: "), BorderLayout.NORTH);
        groupList = new JList<>(groupVector);
        groupList.addListSelectionListener((ListSelectionEvent e) -> {
            if(!e.getValueIsAdjusting())
                reloadStudents();
        });
        leftPanel.add(new JScrollPane(groupList), BorderLayout.CENTER);

        JButton btnMoveGroup = new JButton("Переместить");
        JButton btnClearGroup = new JButton("Очистить");

        btnMoveGroup.addActionListener((ActionEvent e) -> moveGroup());
        btnClearGroup.addActionListener((ActionEvent e) -> clearGroup());

        JPanel panelBtnGroup = new JPanel(new GridLayout(1,2));
        panelBtnGroup.add(btnMoveGroup);
        panelBtnGroup.add(btnClearGroup);
        leftPanel.add(panelBtnGroup, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

        rightPanel.add(new Label("Студенты: "), BorderLayout.NORTH);
        studentTable = new JTable(1,4);
        rightPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JButton btnAddStudent = new JButton("Добавить");
        JButton btnUpdateStudent = new JButton("Исправить");
        JButton btnDeleteStudent = new JButton("Удалить");

        btnAddStudent.addActionListener((ActionEvent e) -> insertStudent());
        btnUpdateStudent.addActionListener((ActionEvent e) -> updateStudent());
        btnDeleteStudent.addActionListener((ActionEvent e) -> deleteStudent());

        JPanel panelBtnStudent = new JPanel(new GridLayout(1,3));
        panelBtnStudent.add(btnAddStudent);
        panelBtnStudent.add(btnUpdateStudent);
        panelBtnStudent.add(btnDeleteStudent);

        rightPanel.add(panelBtnStudent,BorderLayout.SOUTH);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.CENTER);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.CENTER);

        setTitle("Студенческий отдел");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(750, 500);
        setMinimumSize(new Dimension(750, 500));
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);
        groupList.setSelectedIndex(0);
    }

    public JList<Group> getGroupList() {
        return groupList;
    }

    public JTable getStudentTable() {
        return studentTable;
    }

    public int getYear(){
        return ((SpinnerNumberModel)yearSpinner.getModel()).getNumber().intValue();
    }

    public void reloadStudents(){
        //Start new thread so that the MainFrame thread doesn't stop
        new Thread(() -> {
            if(studentTable != null){
                Group group = groupList.getSelectedValue();
                int year = ((SpinnerNumberModel)yearSpinner.getModel()).getNumber().intValue();

                try{
                    Collection<Student> studVector =  managementSystem.getStudentsFromGroup(group, year);
                    studentTable.setModel(new StudentTableModel(new Vector<>(studVector)));

                } catch (SQLException e){
                    JOptionPane.showMessageDialog(StudentFrame.this, e.getMessage());
                }
            }
        }).start();
    }

    private void moveGroup(){
        new Thread(() -> {
            if(groupList.getSelectedValue() == null){
                return;
            }
            if(((StudentTableModel)studentTable.getModel()).isEmpty()){
                JOptionPane.showMessageDialog(this, "Группа пуста");
                return;
            }
            try {
                int year = ((SpinnerNumberModel)yearSpinner.getModel()).getNumber().intValue();
                GroupDialog groupDialog = new GroupDialog(managementSystem.getGroups(), year, groupList.getSelectedIndex());
                if(groupDialog.getResult()){
                    Group group = groupList.getSelectedValue();
                    managementSystem.moveStudentsToGroup(group, year, groupDialog.getGroup(), groupDialog.getYear());
                    reloadStudents();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(StudentFrame.this, e.getMessage());
            }
        }).start();
    }

    private void clearGroup(){
        new Thread(() ->{
            if(groupList.getSelectedValue() != null){
                if(((StudentTableModel)studentTable.getModel()).isEmpty()){
                    JOptionPane.showMessageDialog(this, "Группа пуста");
                    return;
                }
                int year = ((SpinnerNumberModel) yearSpinner.getModel()).getNumber().intValue();
                if(JOptionPane.showConfirmDialog(StudentFrame.this,
                        "Хотите удалить студентов из группы \""+ groupList.getSelectedValue().getGroupName()+"-" +
                                year+" \"?",
                        "Очистка группы", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    Group group = groupList.getSelectedValue();
                    try{
                        managementSystem.removeStudentsFromGroup(group, year);
                        reloadStudents();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentFrame.this, e.getMessage());
                    }
                }
            }
        }).start();
    }

    private void insertStudent(){
        new Thread(()->{
            try {
                new StudentDialog(managementSystem.getGroups(), true, this);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }).start();
    }

    private void updateStudent(){
        new Thread(() -> {
            if(studentTable != null){
                if(studentTable.getSelectedRow() < 0){
                    JOptionPane.showMessageDialog(this, "Студент не выбран");
                    return;
                }
                try {
                    new StudentDialog(managementSystem.getGroups(), false, this);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
            }
        }).start();
    }

    private void deleteStudent(){
        new Thread(()->{
            if(studentTable != null){
                StudentTableModel studentTableModel = (StudentTableModel) studentTable.getModel();

                if(studentTable.getSelectedRow() < 0){
                    JOptionPane.showMessageDialog(this, "Студент не выбран");
                    return;
                }
                Student student = studentTableModel.getStudent(studentTable.getSelectedRow());
                if(JOptionPane.showConfirmDialog(StudentFrame.this,
                        "Хотите удалить студента: "+ student.getShortInfo() +"?",
                        "Удаление студента", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                    try {
                        managementSystem.remoteStudent(student);
                        reloadStudents();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(StudentFrame.this, e.getMessage());
                    }
                }

            }
        }).start();
    }

    private void showAllStudents(){

    }
    ////////////////////////////////////////////

    public static void main(String[] args) {

        startFrame();
    }

    public static void startFrame(){

        SwingUtilities.invokeLater(() -> {
            try {
                new StudentFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
