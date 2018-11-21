package application.classifier;

import application.dataModels.Mail;
import application.handlers.MailDataHandler;
import application.handlers.UserData;
import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className SpamClassifier
 * @date Nov/14/2018
 * @comments None
 */
public class SpamClassifier {
    private static final SpamClassifier ourInstance = new SpamClassifier();
    public static SpamClassifier getInstance() {
        return ourInstance;
    }
    // Create a new bayes classifier with string categories and string features
    private Classifier<String, String> bayes;

    private SpamClassifier() {
        bayes = new BayesClassifier<>();
    }

    public void initializeData() {
        // Creating an array of stacks, position 0 will be ham, position 1 will be spam
        Stack<String>[] processedData = (Stack<String>[]) new Stack[2];
        processedData[0] = new Stack<>();
        processedData[1] = new Stack<>();

        Stack<String> hamMessages;
        Stack<String> spamMessages;

        Stack<String>[] processedTestData = (Stack<String>[]) new Stack[2];
        processedTestData[0] = new Stack<>();
        processedTestData[1] = new Stack<>();

        // Training the model
        try {
            processTestData(processedTestData, "./data/testSpamCorpusBig.txt");
            hamMessages = processedTestData[0];
            spamMessages = processedTestData[1];

        } catch (IOException e) {
            System.out.println("Couldn't load data");
            e.printStackTrace();
            return;
        }

        // This values are hardcoded because of memory limitations,
        // but I managed to get a decent a accuracy with these settings.
        // After a little bit of experiments with the data is better to have a low amount of
        // data of the spam to have a better accuracy
        int counter = 0;
        while (counter++ < 800) {
            String[] message = hamMessages.pop().split("\\s");
            bayes.learn("ham", Arrays.asList(message));
        }

        counter = 0;
        while (!spamMessages.isEmpty() && counter++ < 200) {
            String[] message = spamMessages.pop().split("\\s");
            bayes.learn("spam", Arrays.asList(message));
        }
        // Testing the model

        try {
            processData(processedData, "./data/testSMSData.csv");
            hamMessages = processedData[0];
            spamMessages = processedData[1];

        } catch (IOException e) {
            System.out.println("Couldn't load data");
            e.printStackTrace();
            return;
        }

        int originalSize = hamMessages.size() + spamMessages.size();
        int correct = 0;
        String test;

        while (!hamMessages.isEmpty()) {
            test = hamMessages.pop();
            if (bayes.classify(Arrays.asList(test.split("\\s"))).getCategory().equals("ham")) correct++;
        }

        while (!spamMessages.isEmpty()) {
            test = spamMessages.pop();
            if (bayes.classify(Arrays.asList(test.split("\\s"))).getCategory().equals("spam")) correct++;
        }

        System.out.println("Accuracy: " + ((double)correct / (double)originalSize));
    }

    public boolean isSpam(String message) {
        return bayes.classify(Arrays.asList(message.split("\\s"))).getCategory().equals("spam");
    }

    public void addSpamMail(String type, String message) {
        bayes.learn(type, Arrays.asList(message.trim().split("\\s")));
    }

    private void processData(Stack<String>[] arrayOfData, String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        String[] data;

        while ((line = br.readLine()) != null) {
            line = line.substring(0, line.length()-4);
            data = line.split(",");
            // If the data is ham, we will push the message to the position 0, otherwise to the position 2
            if (data[0].equals("ham"))
                arrayOfData[0].push(data[1]);
            else
                arrayOfData[1].push(data[1]);
        }

        br.close();
    }

    private void processTestData(Stack<String>[] arrayOfData, String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        String[] data;

        while ((line = br.readLine()) != null) {
            data = line.split(",");
            // If the data is ham, we will push the message to the position 0, otherwise to the position 2
            if (data[1].equals("ham"))
                arrayOfData[0].push(data[0]);
            else
                arrayOfData[1].push(data[0]);

        }

        br.close();
    }
}
