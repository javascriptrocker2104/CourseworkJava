package com.kurs;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;


public class App
{
    String[] columnNames = {"ID","Заказчик","Материал","Объем(м^3)","Кол-во",
            "Дата","Стоимость","Метка"};
    InfoTableModel itm;
    JTable table;
    private JButton delButton;
    private JButton addButton;
    private JFrame frame;
    JSpinner spin1;
    JSpinner spin2;
    JSpinner spin3;
    JTextField nameField;
    JTextField matField;
    JTextField dateField;
    JCheckBox mincheck;
    boolean minflag = false;

    private DBWorker dbw;

    App(File file){
        dbw = new DBWorker(file);
        if (file.exists())
            try {
                dbw.read();
            }
            catch (Exception x){
                JOptionPane.showMessageDialog(frame, x.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        else {
            try {
                file.createNewFile();
            }
            catch (IOException x){
                JOptionPane.showMessageDialog(frame, x.getMessage(), "Создание файла невозможно", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createAndShowGUI(){

        frame = new JFrame("Курсовая Беспалова БИБ2104");
        itm = new InfoTableModel();
        table = new JTable(itm);
        JPanel btmPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        ALClass al = new ALClass();

        delButton = new JButton("Удалить");
        addButton = new JButton("Добавить");
        mincheck = new JCheckBox("Мин.");

        JPanel s1 = new JPanel();
        JPanel s2 = new JPanel();
        JPanel s3 = new JPanel();
        JLabel label1 = new JLabel("Кол-во");
        JLabel label2 = new JLabel("Цена");
        JLabel label3 = new JLabel("Объем");
        frame.setLayout(new BorderLayout());
        s1.add(label1,BorderLayout.NORTH);
        s2.add(label2,BorderLayout.NORTH);
        s3.add(label3,BorderLayout.NORTH);

        spin1 = new JSpinner(new SpinnerNumberModel(1,1,999,1));
        spin2 = new JSpinner(new SpinnerNumberModel(100,1,99999,1));
        spin3 = new JSpinner(new SpinnerNumberModel(0.5,0.01,10,0.1));
        s1.add(spin1,BorderLayout.SOUTH);
        s2.add(spin2,BorderLayout.SOUTH);
        s3.add(spin3,BorderLayout.SOUTH);

        delButton.setActionCommand("del");
        delButton.addActionListener(al);
        addButton.setActionCommand("add");
        addButton.addActionListener(al);
        mincheck.addActionListener(al);
        mincheck.setActionCommand("min");
        JLabel namelabel = new JLabel("Заказчик");
        JLabel matlabel = new JLabel("Материал");
        JLabel datelabel = new JLabel("Дата");
        nameField = new JTextField(8);
        matField = new JTextField(8);
        dateField = new JTextField(8);
        btmPanel.add(namelabel,BorderLayout.NORTH);
        btmPanel.add(nameField,BorderLayout.NORTH);
        btmPanel.add(matlabel,BorderLayout.NORTH);
        btmPanel.add(matField, BorderLayout.NORTH);
        btmPanel.add(datelabel,BorderLayout.NORTH);
        btmPanel.add(dateField, BorderLayout.NORTH);
        btmPanel.add(s1,BorderLayout.CENTER);
        btmPanel.add(s2,BorderLayout.CENTER);
        btmPanel.add(s3,BorderLayout.CENTER);
        btmPanel.add(addButton,BorderLayout.SOUTH);
        btmPanel.add(delButton,BorderLayout.WEST);
        btmPanel.add(mincheck,BorderLayout.EAST);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(scrollPane, BorderLayout.NORTH);
        frame.add(btmPanel, BorderLayout.SOUTH);



        frame.pack(); frame.setVisible(true);
        frame.setResizable(false);
        table.setAutoCreateRowSorter(true);
    }

    private class ALClass implements java.awt.event.ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("del")) {
                dbw.removeMarkedEntries(true);
                try{
                    dbw.write();
                }
                catch (IOException x){
                    JOptionPane.showMessageDialog(frame, x.getMessage(), "Cохранение невозможно", JOptionPane.ERROR_MESSAGE);
                }
                itm.fireTableDataChanged();
                return;
            }
            if (e.getActionCommand().equals("add")) {
                String sdate = dateField.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (nameField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Введите заказчика");
                    return;
                }else
                    if (matField.getText().isEmpty()){
                        JOptionPane.showMessageDialog(frame, "Введите материал");
                        return;
                    } else
                    if (dateField.getText().isEmpty()){
                    JOptionPane.showMessageDialog(frame, "Введите дату поставки");
                    return;
                }
                try{
                    Date date = sdf.parse(sdate);
                    dbw.addEntry(nameField.getText(),matField.getText(),(Double)spin3.getValue(),
                            date, (int)spin1.getValue(),(int)spin2.getValue());
                    dbw.write();
                    itm.fireTableDataChanged();
                    return;
                }
                catch (Exception x){
                    JOptionPane.showMessageDialog(frame, x.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (e.getActionCommand().equals("min")){
                minflag = mincheck.isSelected();
                itm.fireTableDataChanged();
            }
        }

    };

    public class InfoTableModel extends DefaultTableModel{

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public int getRowCount() {
            if (minflag)
                return dbw.getMinMaterials().size();
            return dbw.getList().size();
        }

        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }


        @Override
        public Object getValueAt(int row, int col) {
            Info entry;
            if (minflag){
                entry = dbw.getMinMaterials().get(row);
            }
            else{
                entry = dbw.getList().get(row);
            }
            switch (col){
                case 0: return entry.getID();
                case 1: return entry.getName();
                case 2: return entry.getMaterial();
                case 3: return entry.getVolume();
                case 4: return entry.getQuantity();
                case 5: return entry.getDate();
                case 6: return entry.getPrice();
                case 7: return entry.getMetka();
            }
            return null;
        }

        public boolean isCellEditable(int row, int col) {
            if (col==7) {
                return true;
            } else {
                return false;
            }
        }

        public void setValueAt(Object value, int row, int col) {
            if (minflag){
                dbw.getMinMaterials().get(row).setMetka((boolean)value);
            }
            else{
                dbw.getList().get(row).setMetka((boolean)value);
            }
            fireTableCellUpdated(row, col);
        }

    }

    public static void main( String[] args )
    {
        File file = new File("data.bin");
        App app = new App(file);
        app.createAndShowGUI();
    }
}
