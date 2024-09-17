package org.example;

import javax.swing.*;

public class MainForm extends JFrame {
    private JCheckBox kkkkCheckBox;
    private JButton button1;
    private JPanel mainPanel;

    public static void main(String[] args){
        MainForm mainForm = new MainForm();
        mainForm.setContentPane(mainForm.mainPanel);
        mainForm.setTitle("Hello");
        mainForm.setSize(300,400);
        mainForm.setVisible(true);
        mainForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
