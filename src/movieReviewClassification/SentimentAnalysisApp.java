package movieReviewClassification;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SentimentAnalysisApp extends JFrame implements ActionListener{

    private JPanel sidePanel;
    private static JTextArea outputTextArea;

    private JButton showDataBtn;
    private JButton saveDataBtn;
    private JButton loadDataBtn;
    private JButton deleteRevBtn;
    private JButton searchIDBtn;
    private JButton searchSubstringBtn;
    private JButton loadConfirmBtn;
    private JButton delConfirmBtn;
    private JButton IDConfirmBtn;
    private JButton substringConfirmBtn;

    private JTextField deleteIDTextField;
    private JTextField searchIDTextField;
    private JTextField searchSubTextField;
    private JTextField pathTextField;
    private JTextField classTextField;

    private JLabel deleteIDLabel;
    private JLabel searchIDLabel;
    private JLabel searchSubLabel;
    private JLabel filePathLabel;
    private JLabel classLabel;

    private String idString;
    private String substring;
    private String path;
    private String realClass;

    ReviewHandler rh = new ReviewHandler();
    private List<MovieReview> movieList;

    public SentimentAnalysisApp(){
        super("SentimentAnaysisApp");

        setLayout(new BorderLayout());
        sidePanel = new JPanel();

        //add panel
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        add(sidePanel, BorderLayout.WEST);
        
        //load data button
        loadDataBtn = new JButton("Load new movie review collection");
        loadDataBtn.setPreferredSize(new Dimension(235, 45));
        loadDataBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(loadDataBtn);

        //delete review button
        deleteRevBtn = new JButton("Delete review from database by ID");
        deleteRevBtn.setPreferredSize(new Dimension(235, 45));
        deleteRevBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(deleteRevBtn);

        //search by ID button
        searchIDBtn = new JButton("Search reviews in database by ID");
        searchIDBtn.setPreferredSize(new Dimension(235, 45));
        searchIDBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(searchIDBtn);

        //search by substring button
        searchSubstringBtn = new JButton("Search reviews in database by substring");
        searchSubstringBtn.setPreferredSize(new Dimension(235, 45));
        searchSubstringBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(searchSubstringBtn);

        //show data buttons
        showDataBtn = new JButton("Display Database");
        showDataBtn.setPreferredSize(new Dimension(235, 45));
        showDataBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(showDataBtn);

        //save data button
        saveDataBtn = new JButton("Save Database");
        saveDataBtn.setPreferredSize(new Dimension(235, 45));
        saveDataBtn.setFont(new Font("Helvetica", Font.PLAIN, 10));
        sidePanel.add(saveDataBtn);

        //file path label
        filePathLabel = new JLabel("Enter the path of file/folder: ");
        filePathLabel.setVisible(false);
        sidePanel.add(filePathLabel);

        //path text field
        pathTextField = new JTextField();
        pathTextField.setPreferredSize(new Dimension(35, 18));
        pathTextField.setVisible(false);
        sidePanel.add(pathTextField);

        //real class label
        classLabel = new JLabel("Please input real class (0 = neg, 1 = pos, 2 = ?): ");
        classLabel.setVisible(false);
        sidePanel.add(classLabel);

        //real class text field
        classTextField = new JTextField();
        classTextField.setPreferredSize(new Dimension(35, 18));
        classTextField.setVisible(false);
        sidePanel.add(classTextField);

        //delete by id label
        deleteIDLabel = new JLabel("ID of the review to delete: ");
        deleteIDLabel.setVisible(false);
        sidePanel.add(deleteIDLabel);

        //delete by id text field
        deleteIDTextField = new JTextField();
        deleteIDTextField.setPreferredSize(new Dimension(100, 22));
        deleteIDTextField.setVisible(false);
        sidePanel.add(deleteIDTextField);

        //search by id label
        searchIDLabel = new JLabel("ID of the review to search for: ");
        searchIDLabel.setVisible(false);
        sidePanel.add(searchIDLabel);

        //search by id textfield
        searchIDTextField = new JTextField();
        searchIDTextField.setPreferredSize(new Dimension(100, 22));
        searchIDTextField.setVisible(false);
        sidePanel.add(searchIDTextField);

        //search by substring label
        searchSubLabel = new JLabel("Substring of review you are searching for: ");
        searchSubLabel.setVisible(false);
        sidePanel.add(searchSubLabel);

        //search by substring textfield
        searchSubTextField = new JTextField();
        searchSubTextField.setPreferredSize(new Dimension(100, 22));
        searchSubTextField.setVisible(false);
        sidePanel.add(searchSubTextField);

        //Text Area
        outputTextArea = new JTextArea();
        outputTextArea.setFont(new Font("Courier", Font.PLAIN, 18));
        outputTextArea.setEditable(false);
        add(new JScrollPane(outputTextArea), BorderLayout.CENTER); //makes the output area scrollable
        open();

        //ActionListeners

        showDataBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {

                        movieList = rh.getAllReviews(substring);
                        printJTable(movieList);
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        saveDataBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {

                        outputTextArea.append("\nSaving Database...");
                        rh.saveSerialDB();
                        outputTextArea.append("\nDatabase saved.");
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        loadDataBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {
                        loadDataBtn.setVisible(false);
                        deleteRevBtn.setVisible(false);
                        searchIDBtn.setVisible(false);
                        searchSubstringBtn.setVisible(false);
                        pathTextField.setVisible(true);
                        filePathLabel.setVisible(true);
                        classTextField.setVisible(true);
                        classLabel.setVisible(true);

                        outputTextArea.setText("");
                        outputTextArea.append("\nPress confirm to enter.");

                        loadConfirmBtn = new JButton("Confirm");
                        loadConfirmBtn.setPreferredSize(new Dimension(100, 45));
                        loadConfirmBtn.setVisible(true);

                        loadConfirmBtn.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent event) {
                                Runnable myRunnable = new Runnable(){
                                    public void run() {

                                        path = pathTextField.getText();
                                        realClass = classTextField.getText();

                                        if (realClass.equals("0")) {
                                            rh.loadReviews(path, 0, outputTextArea);
                                        } else if (realClass.equals("1")) {
                                            rh.loadReviews(path, 1, outputTextArea);
                                        } else if (realClass.equals("2")) {
                                            rh.loadReviews(path, 2, outputTextArea);
                                        } else {
                                            outputTextArea.append("\nIllegal input.");
                                        }

                                        pathTextField.setText("");
                                        classTextField.setText("");
                                        outputTextArea.setText("\nDatabase size: " + rh.database.size());
                                        outputTextArea.append("\nChoose one of the following functions above:\n\n");

                                        loadDataBtn.setVisible(true);
                                        deleteRevBtn.setVisible(true);
                                        searchIDBtn.setVisible(true);
                                        searchSubstringBtn.setVisible(true);

                                        pathTextField.setVisible(false);
                                        filePathLabel.setVisible(false);
                                        classTextField.setVisible(false);
                                        classLabel.setVisible(false);

                                        sidePanel.remove(loadConfirmBtn);
                                        sidePanel.revalidate();
                                        sidePanel.repaint();
                                    }
                                };
                                Thread thread = new Thread(myRunnable);
                                thread.start();
                            }
                        });
                        sidePanel.add(loadConfirmBtn);
                        sidePanel.revalidate();
                        sidePanel.repaint();
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        deleteRevBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {

                        loadDataBtn.setVisible(false);
                        deleteRevBtn.setVisible(false);
                        searchIDBtn.setVisible(false);
                        searchSubstringBtn.setVisible(false);

                        deleteIDTextField.setVisible(true);
                        deleteIDLabel.setVisible(true);

                        outputTextArea.setText("");
                        outputTextArea.append("\nPress confirm to enter.");

                        delConfirmBtn = new JButton("Confirm");
                        delConfirmBtn.setPreferredSize(new Dimension(100, 45));
                        delConfirmBtn.setVisible(true);

                        delConfirmBtn.addActionListener(new ActionListener () {
                            @Override
                            public void actionPerformed(ActionEvent event){
                                Runnable myRunnable = new Runnable(){
                                    public void run() {

                                        idString = deleteIDTextField.getText();
                                        int id = Integer.parseInt(idString);
                                        if (!idString.matches("-?(0|[1-9]\\d*)")) {
                                            // Input is not an integer
                                            outputTextArea.append("\nIllegal input.");
                                        } else {
                                            rh.deleteReview(id, outputTextArea);
                                        }

                                        deleteIDTextField.setText("");
                                        outputTextArea.setText("\nDatabase size: " + rh.database.size());
                                        outputTextArea.append("\nReview with ID " + id + " deleted.");
                                        outputTextArea.append("\nChoose one of the following functions above:\n\n");

                                        loadDataBtn.setVisible(true);
                                        deleteRevBtn.setVisible(true);
                                        searchIDBtn.setVisible(true);
                                        searchSubstringBtn.setVisible(true);

                                        deleteIDTextField.setVisible(false);
                                        deleteIDLabel.setVisible(false);
                                        
                                        sidePanel.remove(delConfirmBtn);
                                        sidePanel.revalidate();
                                        sidePanel.repaint();
                                    }
                                };
                                Thread thread = new Thread(myRunnable);
                                thread.start();
                            }
                        });
                        sidePanel.add(delConfirmBtn);
                        sidePanel.revalidate();
                        sidePanel.repaint();
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        searchIDBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {

                        loadDataBtn.setVisible(false);
                        deleteRevBtn.setVisible(false);
                        searchIDBtn.setVisible(false);
                        searchSubstringBtn.setVisible(false);

                        searchIDTextField.setVisible(true);
                        searchIDLabel.setVisible(true);
                        
                        outputTextArea.setText("");
                        outputTextArea.append("\nPress confirm to enter.");

                        IDConfirmBtn = new JButton("Confirm");
                        IDConfirmBtn.setPreferredSize(new Dimension(100, 45));
                        IDConfirmBtn.setVisible(true);

                        IDConfirmBtn.addActionListener(new ActionListener (){
                            @Override
                            public void actionPerformed(ActionEvent event){
                                Runnable myRunnable = new Runnable(){
                                    public void run() {

                                        idString = searchIDTextField.getText();

                                        if (!idString.matches("-?(0|[1-9]\\d*)")) {
                                            // Input is not an integer
                                            outputTextArea.append("\nIllegal input.");
                                        } else {
                                            int id = Integer.parseInt(idString);
                                            MovieReview mr = rh.searchById(id);
                                            if (mr != null) {
                                                String[] columnNames = {"ID","Predicted","Real","Text"};
                                                String[][] values = new String[1][4];
                                                String predicted = "";
                                                if(mr.getPredictedPolarity() == 0) {
                                                    predicted = "Negative";
                                                } else if(mr.getPredictedPolarity() == 1) {
                                                    predicted = "Positive";
                                                } else if(mr.getPredictedPolarity() == 2) {
                                                    predicted = "Unknown";
                                                }

                                                String real = "";
                                                if(mr.getRealPolarity() == 0) {
                                                    real = "Negative";
                                                } else if(mr.getRealPolarity() == 1) {
                                                    real = "Positive";
                                                } else if(mr.getRealPolarity() == 2) {
                                                    real = "Unknown";
                                                }
                                                values[0][0] = idString;
                                                values[0][1] = predicted;
                                                values[0][2] = real;
                                                values[0][3] = mr.getText();

                                                JTable table = new JTable(values, columnNames){
                                                    public boolean isCellEditable(int row, int column) {
                                                        return false;
                                                    }
                                                };

                                                table.setFillsViewportHeight(true);
                                                table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

                                                JScrollPane scrollPane = new JScrollPane(table);
                                                scrollPane.createVerticalScrollBar();
                                                scrollPane.createHorizontalScrollBar();

                                                JFrame.setDefaultLookAndFeelDecorated(true);
                                                JFrame resultFrame = new JFrame("Search Results: Movie Reviews");
                                                resultFrame.setBounds(300, 200, 900, 600);
                                                resultFrame.add(scrollPane);
                                                resultFrame.setVisible(true);
                                            } else {
                                                outputTextArea.append("\nReview not found.");
                                            }
                                        }

                                        searchIDTextField.setText("");
                                        outputTextArea.setText("\nDatabase size: " + rh.database.size());
                                        outputTextArea.append("\nChoose one of the following functions above:\n\n");

                                        loadDataBtn.setVisible(true);
                                        deleteRevBtn.setVisible(true);
                                        searchIDBtn.setVisible(true);
                                        searchSubstringBtn.setVisible(true);

                                        searchIDTextField.setVisible(false);
                                        searchIDLabel.setVisible(false);
                                        
                                        sidePanel.remove(IDConfirmBtn);
                                        sidePanel.revalidate();
                                        sidePanel.repaint();
                                    }
                                };
                                Thread thread = new Thread(myRunnable);
                                thread.start();
                            }
                        });
                        sidePanel.add(IDConfirmBtn);
                        sidePanel.revalidate();
                        sidePanel.repaint();
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        searchSubstringBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                Runnable myRunnable = new Runnable(){
                    public void run() {

                        loadDataBtn.setVisible(false);
                        deleteRevBtn.setVisible(false);
                        searchIDBtn.setVisible(false);
                        searchSubstringBtn.setVisible(false);

                        searchSubTextField.setVisible(true);
                        searchSubLabel.setVisible(true);
                        
                        outputTextArea.setText("");
                        outputTextArea.append("\nPress confirm to enter.");

                        substringConfirmBtn = new JButton("Confirm");
                        substringConfirmBtn.setPreferredSize(new Dimension(100, 45));
                        substringConfirmBtn.setVisible(true);

                        substringConfirmBtn.addActionListener(new ActionListener(){
                            @Override
                            public void actionPerformed(ActionEvent event){
                                Runnable myRunnable = new Runnable(){
                                    public void run() {

                                        substring = searchSubTextField.getText();
                                        movieList = rh.searchBySubstring(substring);
                                        if(movieList != null){
                                            printJTable(movieList);
                                        } else {
                                            outputTextArea.append("\nNo review found with that substring.");
                                        }
                                        
                                        searchSubTextField.setText("");
                                        outputTextArea.setText("\nDatabase size: " + rh.database.size());
                                        outputTextArea.append("\nChoose one of the following functions above:\n\n");

                                        loadDataBtn.setVisible(true);
                                        deleteRevBtn.setVisible(true);
                                        searchIDBtn.setVisible(true);
                                        searchSubstringBtn.setVisible(true);

                                        searchSubTextField.setVisible(false);
                                        searchSubLabel.setVisible(false);

                                        sidePanel.remove(substringConfirmBtn);
                                        sidePanel.revalidate();
                                        sidePanel.repaint();
                                    }
                                };
                                Thread thread = new Thread(myRunnable);
                                thread.start();
                            }
                        });
                        sidePanel.add(substringConfirmBtn);
                        sidePanel.revalidate();
                        sidePanel.repaint();
                    }
                };
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        });

        pack();
        setBounds(250,150,900, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void open(){
        // Load database if it exists.
        File databaseFile = new File(rh.DATA_FILE_NAME);
        if (databaseFile.exists()) {
            rh.loadSerialDB();
            outputTextArea.append("Reading database... " + rh.database.size() + " entry(s) loaded.");
            outputTextArea.append("\nDone.");
        }
        outputTextArea.append("\nDatabase size: " + rh.database.size());
        outputTextArea.append("\nChoose one of the following functions above:\n\n");
    }

    public void actionPerformed(ActionEvent event) {}

    public static void main(String [] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new SentimentAnalysisApp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void printJTable(List<MovieReview> movieList){

        String[] columnNames = {"ID","Predicted","Real","Text"};
        String[][] values = new String[movieList.size()][4];

        for(int i = 0; i < movieList.size(); i++) {

            String predicted = "";
            if(movieList.get(i).getPredictedPolarity() == 0) {
                predicted = "Negative";
            } else if(movieList.get(i).getPredictedPolarity() == 1) {
                predicted = "Positive";
            } else if(movieList.get(i).getPredictedPolarity() == 2) {
                predicted = "Unknown";
            }

            String real = "";
            if(movieList.get(i).getRealPolarity() == 0) {
                real = "Negative";
            } else if(movieList.get(i).getRealPolarity() == 1) {
                real = "Positive";
            } else if(movieList.get(i).getRealPolarity() == 2) {
                real = "Unknown";
            }

            values[i][0] = String.valueOf(movieList.get(i).getId());
            values[i][1] = predicted;
            values[i][2] = real;
            values[i][3] = movieList.get(i).getText();
        }

        JTable table = new JTable(values, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.createHorizontalScrollBar();
        scrollPane.createVerticalScrollBar();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame resultFrame = new JFrame("Search Results: Movie Reviews");
        resultFrame.setBounds(300, 200, 900, 600);
        resultFrame.setContentPane(scrollPane);
        resultFrame.setVisible(true);
    }
}
