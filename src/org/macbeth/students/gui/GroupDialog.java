package org.macbeth.students.gui;

import org.macbeth.students.logic.Group;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

public class GroupDialog extends JDialog{

    private static final int D_HEIGHT = 150;
    private static final int D_WIDTH = 350;
    private JSpinner yearSpinner;
    private JComboBox<Group> groupBox;
    private JButton btnOK = new JButton("OK");
    private JButton btnCancel = new JButton("Cancel");
    private boolean result = false;

    public GroupDialog(List<Group> groups, int year, int currentIndex){

        setTitle("Изменение группы для студентов");
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(5,5,5,5);

        JLabel label = new JLabel("Новая группа: ");
        gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagLayout.setConstraints(label, gridBagConstraints);
        getContentPane().add(label);

        groupBox = new JComboBox<>(new Vector<>(groups));
        groupBox.setSelectedIndex(currentIndex);
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagLayout.setConstraints(groupBox, gridBagConstraints);
        getContentPane().add(groupBox);

        label = new JLabel("Новый год обучения: ");
        gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagLayout.setConstraints(label, gridBagConstraints);
        getContentPane().add(label);

        yearSpinner = new JSpinner(new SpinnerNumberModel(++year, 1900, 2100, 1));
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagLayout.setConstraints(yearSpinner, gridBagConstraints);
        getContentPane().add(yearSpinner);

        gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        btnOK.setName("OK");
        btnOK.addActionListener((ActionEvent e) ->{
            result = true;
            GroupDialog.this.setVisible(false);
        });
        btnCancel.setName("Cancel");
        btnCancel.addActionListener((ActionEvent e) ->{
            result = false;
            GroupDialog.this.setVisible(false);
        });
        gridBagLayout.setConstraints(btnOK, gridBagConstraints);
        getContentPane().add(btnOK);
        gridBagLayout.setConstraints(btnCancel, gridBagConstraints);
        getContentPane().add(btnCancel);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(D_WIDTH, D_HEIGHT);
        setLocationRelativeTo(null);
        setModal(true);
        setVisible(true);
    }

    public int getYear(){
        return ( (SpinnerNumberModel)yearSpinner.getModel()).getNumber().intValue();
    }

    public Group getGroup(){
        if(groupBox.getModel().getSize() > 0){
            return (Group) groupBox.getSelectedItem();
        }
        return null;
    }

    public boolean getResult(){
        return result;
    }

}



















