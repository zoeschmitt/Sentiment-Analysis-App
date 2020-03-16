package movieReviewClassification;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;

public class ReviewHandler extends AbstractReviewHandler {

    private static int ID;

    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     * @param filePath The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     * 2 = Unknown).
     *
     */
    @Override
    public void loadReviews(String filePath, int realClass, JTextArea text) {
        File fileOrFolder = new File(filePath);
        try {
            if (fileOrFolder.isFile()) {
                // File
                if (filePath.endsWith(".txt")) {
                    // Import review
                    MovieReview review = readReview(filePath, realClass);
                    // Add to database
                    database.put(review.getId(), review);
                    //Output result: single file
                    text.append("\nReview imported.");
                    text.append("\nID: " + review.getId());
                    text.append("\nText: " + review.getText());
                    text.append("\nReal Class: " + review.getRealPolarity());
                    text.append("\nClassification result: " + review.getPredictedPolarity());
                    if (realClass == 2) {
                        text.append("\nReal class unknown.");
                    } else if (realClass == review.getPredictedPolarity()) {
                        text.append("\nCorrectly classified.");
                    } else {
                        text.append("\nMisclassified.");
                    }
                    text.append("\n");

                } else {
                    // Cannot import non-txt files
                    text.append("Input file path is neither a txt file nor folder.");
                    return;
                }
            } else {
                // Folder
                String[] files = fileOrFolder.list();
                String fileSeparatorChar = System.getProperty("file.separator");
                int counter = 0;
                for (String fileName : files) {
                    if (fileName.endsWith(".txt")) {
                        // Only import txt files
                        // Import review
                        MovieReview review = readReview(filePath + fileSeparatorChar + fileName, realClass);
                        // Add to database
                        database.put(review.getId(), review);
                        // Count correct classified reviews, only real class is known
                        if (realClass != 2 && review.getRealPolarity() == review.getPredictedPolarity()) {
                            counter++;
                        }
                    } else {
                        continue;
                    }
                }
                // Output result: folder
                text.append("\nFolder imported.");
                text.append("\nNumber of entries: " + files.length);

                // Only output accuracy if real class is known
                if (realClass != 2) {
                    text.append("\nCorrectly classified: " + counter);
                    text.append("\nMisclassified: " + (files.length - counter));
                    text.append("\nAccuracy: " + ((double)counter / (double)files.length * 100) + "%");
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        }

    }

    /**
     * Reads a single review file and returns it as a MovieReview object.
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be openned.
     */
    @Override
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {
        // Read file for text
        Scanner inFile = new Scanner(new FileReader(reviewFilePath));
        String text = "";
        while (inFile.hasNextLine()) {
            text += inFile.nextLine();
        }
        // Remove the <br /> occurences in the text and replace them with a space
        text = text.replaceAll("<br />"," ");

        // Create review object, assigning ID and real class
        MovieReview review = new MovieReview(ID, text, realClass);
        // Update ID
        ID++;
        // Classify review
        classifyReview(review);

        return review;
    }

    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    @Override
    public void deleteReview(int id, JTextArea text) {

        if (!database.containsKey(id)) {
            // Review with given ID does not exist
            text.append("\nID " + id + " does not exist.");
        } else {
            database.remove(id);
        }
        return;
    }

    /**
     * Loads review database.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadSerialDB() {
        // serialize the database
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream(DATA_FILE_NAME);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            database = (Map<Integer, MovieReview>)input.readObject();

            // get the maximum ID
            for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
                if (entry.getKey() > ID) {
                    ID = entry.getKey();
                }
            }
            ID = ID + 1;

            input.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } finally {
            close(file);
        }
    }

    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    @Override
    public MovieReview searchById(int id) {
        if (database.containsKey(id)) {
            return database.get(id);
        }
        return null;
    }

    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    @Override
    public List<MovieReview> searchBySubstring(String substring) {
        List<MovieReview> tempList = new ArrayList<MovieReview>();

        for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
            if (entry.getValue().getText().contains(substring)) {
                tempList.add(entry.getValue());
            }
        }

        if (tempList.size()!=0) {
            return tempList;
        } else {
            // No review has given substring
            return null;
        }

    }

    public List<MovieReview> getAllReviews(String substring) {
        List<MovieReview> tempList = new ArrayList<MovieReview>();

        for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
            tempList.add(entry.getValue());
        }

        if (tempList.size()!=0) {
            return tempList;
        } else {
            // No review has given substring
            return null;
        }
    }
}
