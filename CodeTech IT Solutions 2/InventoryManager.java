import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class StudentMarks {
    private ArrayList<Double> marks;
    private String studentName;

    public StudentMarks(String studentName) {
        this.marks = new ArrayList<>();
        this.studentName = studentName;
    }

    public void addMark(double mark) {
        marks.add(mark);
    }

    public double calculateAverage() {
        if (marks.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (double mark : marks) {
            sum += mark;
        }
        return sum / marks.size();
    }

    public char getOverallMark(double average) {
        if (average >= 90) {
            return 'A';
        } else if (average >= 80) {
            return 'B';
        } else if (average >= 70) {
            return 'C';
        } else if (average >= 60) {
            return 'D';
        } else {
            return 'F';
        }
    }

    public double getGPA(double mark) {
        if (mark >= 91) {
            return 10.0;
        } else if (mark >= 81) {
            return 9.0;
        } else if (mark >= 71) {
            return 8.0;
        } else if (mark >= 61) {
            return 7.0;
        } else if (mark >= 51) {
            return 6.0;
        } else if (mark >= 41) {
            return 5.0;
        } else {
            return 0.0;
        }
    }

    public String displayMarks() {
        StringBuilder result = new StringBuilder();
        double sumGPA = 0.0;
        int failCount = 0;
        int subjectCount = marks.size();

        for (double mark : marks) {
            double gpa = getGPA(mark);
            if (gpa == 0.0) {
                failCount++;
            } else {
                sumGPA += gpa;
            }
        }

        if (failCount > 0) {
            result.append("Student: ").append(studentName).append("\n");
            result.append("Number of subjects failed: ").append(failCount).append("\n");
        } else {
            double averageGPA = sumGPA / subjectCount;
            char grade = getOverallMark(averageGPA * 10);
            result.append("Student: ").append(studentName).append("\n");
            result.append("GPA: ").append(String.format("%.2f", averageGPA)).append("\n");
            result.append("Overall Grade: ").append(grade).append("\n");
        }

        return result.toString();
    }
}

class StudentMarksGUI extends JFrame {
    private JTextField studentNameField;
    private JTextField numberOfSubjectsField;
    private JTextArea marksArea;
    private JTextArea resultArea;

    public StudentMarksGUI() {
        setTitle("Student Marks");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.decode("#BE41A6"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel studentNameLabel = new JLabel("Enter student name:");
        studentNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        studentNameLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(studentNameLabel, gbc);

        studentNameField = new JTextField(20);
        studentNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        studentNameField.setForeground(Color.BLACK);
        studentNameField.setBackground(Color.decode("#41A6BE"));
        gbc.gridx = 1;
        inputPanel.add(studentNameField, gbc);

        JLabel numberOfSubjectsLabel = new JLabel("Enter number of subjects:");
        numberOfSubjectsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        numberOfSubjectsLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(numberOfSubjectsLabel, gbc);

        numberOfSubjectsField = new JTextField(20);
        numberOfSubjectsField.setFont(new Font("Arial", Font.PLAIN, 14));
        numberOfSubjectsField.setForeground(Color.BLACK);
        numberOfSubjectsField.setBackground(Color.decode("#41A6BE"));
        gbc.gridx = 1;
        inputPanel.add(numberOfSubjectsField, gbc);

        JLabel marksLabel = new JLabel("Enter marks for subjects (comma-separated):");
        marksLabel.setFont(new Font("Arial", Font.BOLD, 14));
        marksLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(marksLabel, gbc);

        marksArea = new JTextArea(5, 20);
        marksArea.setFont(new Font("Arial", Font.PLAIN, 14));
        marksArea.setForeground(Color.BLACK);
        marksArea.setBackground(Color.decode("#41A6BE"));
        marksArea.setLineWrap(true);
        marksArea.setWrapStyleWord(true);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(marksArea), gbc);

        JLabel resultLabel = new JLabel("Results:");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(resultLabel, gbc);

        resultArea = new JTextArea(5, 20);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setForeground(Color.BLACK);
        resultArea.setBackground(Color.decode("#41A6BE"));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setText("Results display here");
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(resultArea), gbc);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(Color.BLUE);
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(new CalculateListener());

        add(inputPanel, BorderLayout.CENTER);
        add(calculateButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private class CalculateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String studentName = studentNameField.getText();
            int numberOfSubjects;
            try {
                numberOfSubjects = Integer.parseInt(numberOfSubjectsField.getText());
            } catch (NumberFormatException ex) {
                resultArea.setText("Invalid number of subjects. Please enter a valid number.");
                return;
            }
            String[] marksInput = marksArea.getText().split(",");
            StudentMarks studentMarks = new StudentMarks(studentName);

            if (marksInput.length != numberOfSubjects) {
                resultArea.setText("Number of marks entered does not match the number of subjects.");
                return;
            }

            for (String markStr : marksInput) {
                try {
                    double mark = Double.parseDouble(markStr.trim());
                    if (mark >= 0 && mark <= 100) {
                        studentMarks.addMark(mark);
                    } else {
                        resultArea.setText("Invalid mark. Please enter values between 0 and 100.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    resultArea.setText("Invalid mark. Please enter numeric values.");
                    return;
                }
            }

            resultArea.setText(studentMarks.displayMarks());
        }
    }

    public static void main(String[] args) {
        new StudentMarksGUI();
    }
}