import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentManagementSystem extends JFrame {

    JTextField idField, nameField, marksField, searchField;
    JButton addBtn, updateBtn, deleteBtn, clearBtn, searchBtn;
    JTable table;
    DefaultTableModel model;

    Color primary = new Color(33, 150, 243);
    Color bg = new Color(245, 245, 245);

    public StudentManagementSystem() {
        setTitle("Student Management System");
        setSize(900, 500);
        setLayout(new BorderLayout());

        // 🔹 HEADER
        JLabel title = new JLabel("Student Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(primary);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // 🔹 MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(bg);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // 🔹 FORM PANEL
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createTitledBorder("Student Details"));
        form.setBackground(bg);
        form.setMaximumSize(new Dimension(500, 180));

        idField = new JTextField();
        nameField = new JTextField();
        marksField = new JTextField();
        searchField = new JTextField();

        form.add(new JLabel("ID:")); form.add(idField);
        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Marks (Java):")); form.add(marksField);
        form.add(new JLabel("Search:")); form.add(searchField);

        // 🔹 BUTTON PANEL
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(bg);

        addBtn = createButton("Add");
        updateBtn = createButton("Update");
        deleteBtn = createButton("Delete");
        clearBtn = createButton("Clear");
        searchBtn = createButton("Search");

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(searchBtn);

        // 🔹 TABLE
        model = new DefaultTableModel(new String[]{"ID", "Name", "Marks (Java)"}, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);

        // 🔹 ADD COMPONENTS
        mainPanel.add(form);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(btnPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(scroll);

        add(mainPanel);

        // 🔥 ACTIONS
        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());
        searchBtn.addActionListener(e -> searchStudent());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                idField.setText(model.getValueAt(row, 0).toString());
                nameField.setText(model.getValueAt(row, 1).toString());
                marksField.setText(model.getValueAt(row, 2).toString());
            }
        });

        loadData();

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    // 🎨 BUTTON STYLE
    JButton createButton(String text) {
        JButton btn = new JButton(text);

        btn.setBackground(primary);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(110, 40));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(25, 118, 210));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(primary);
            }
        });

        return btn;
    }

    // ✅ FUNCTIONS
    void addStudent() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() || marksField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fill all fields!");
            return;
        }

        model.addRow(new Object[]{
                idField.getText(),
                nameField.getText(),
                marksField.getText()
        });

        saveData();
        clearFields();
    }

    void updateStudent() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            model.setValueAt(idField.getText(), row, 0);
            model.setValueAt(nameField.getText(), row, 1);
            model.setValueAt(marksField.getText(), row, 2);
            saveData();
        }
    }

    void deleteStudent() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            model.removeRow(row);
            saveData();
        }
    }

    void searchStudent() {
        String search = searchField.getText().toLowerCase();

        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 1).toString().toLowerCase();
            if (name.contains(search)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Not Found!");
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        marksField.setText("");
        searchField.setText("");
    }

    void saveData() {
        try {
            FileWriter fw = new FileWriter("students.txt");
            for (int i = 0; i < model.getRowCount(); i++) {
                fw.write(model.getValueAt(i, 0) + "," +
                         model.getValueAt(i, 1) + "," +
                         model.getValueAt(i, 2) + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadData() {
        try {
            File file = new File("students.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                model.addRow(line.split(","));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new StudentManagementSystem();
    }
}