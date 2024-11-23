import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherFeedbackForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField teacherNameField, studentNameField;
    private JComboBox<Integer> interactionBox, teachingBox, knowledgeBox, clarityBox, punctualityBox;
    private JTextArea commentsArea;

    public TeacherFeedbackForm() {
        setTitle("Teacher Feedback Form");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // College Name Label
        JLabel collegeLabel = new JLabel("KONGU ENGINEERING COLLEGE", JLabel.CENTER);
        collegeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        collegeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        teacherNameField = new JTextField();
        studentNameField = new JTextField();
        interactionBox = createRatingComboBox();
        teachingBox = createRatingComboBox();
        knowledgeBox = createRatingComboBox();
        clarityBox = createRatingComboBox();
        punctualityBox = createRatingComboBox();
        commentsArea = new JTextArea(3, 20);

        // Submit Button
        JButton submitButton = new JButton("Submit");

        formPanel.add(new JLabel("Student Name:"));
        formPanel.add(studentNameField);

        formPanel.add(new JLabel("Teacher Name:"));
        formPanel.add(teacherNameField);

        formPanel.add(new JLabel("Interaction:"));
        formPanel.add(interactionBox);

        formPanel.add(new JLabel("Teaching:"));
        formPanel.add(teachingBox);

        formPanel.add(new JLabel("Subject Knowledge:"));
        formPanel.add(knowledgeBox);

        formPanel.add(new JLabel("Clarity:"));
        formPanel.add(clarityBox);

        formPanel.add(new JLabel("Punctuality:"));
        formPanel.add(punctualityBox);

        formPanel.add(new JLabel("Comments:"));
        formPanel.add(new JScrollPane(commentsArea));

        // Adding Components
        add(collegeLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

        // Action Listener for Submit Button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });
    }

    private JComboBox<Integer> createRatingComboBox() {
        Integer[] ratings = {1, 2, 3, 4, 5};
        return new JComboBox<>(ratings);
    }

    private void submitFeedback() {
        String studentName = studentNameField.getText();
        String teacherName = teacherNameField.getText();
        int interaction = (int) interactionBox.getSelectedItem();
        int teaching = (int) teachingBox.getSelectedItem();
        int knowledge = (int) knowledgeBox.getSelectedItem();
        int clarity = (int) clarityBox.getSelectedItem();
        int punctuality = (int) punctualityBox.getSelectedItem();
        String comments = commentsArea.getText();

        String query = "INSERT INTO teacher_feedback (student_name, teacher_name, interaction, teaching, knowledge, clarity, punctuality, comments) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, studentName);
            preparedStatement.setString(2, teacherName);
            preparedStatement.setInt(3, interaction);
            preparedStatement.setInt(4, teaching);
            preparedStatement.setInt(5, knowledge);
            preparedStatement.setInt(6, clarity);
            preparedStatement.setInt(7, punctuality);
            preparedStatement.setString(8, comments);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Feedback submitted successfully!");
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit feedback.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        studentNameField.setText("");
        teacherNameField.setText("");
        interactionBox.setSelectedIndex(0);
        teachingBox.setSelectedIndex(0);
        knowledgeBox.setSelectedIndex(0);
        clarityBox.setSelectedIndex(0);
        punctualityBox.setSelectedIndex(0);
        commentsArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TeacherFeedbackForm form = new TeacherFeedbackForm();
            form.setVisible(true);
        });
    }
}
