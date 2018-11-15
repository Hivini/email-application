package application.classifier;

import de.daslaboratorium.machinelearning.classifier.Classifier;
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Stack;

/**
 * @author Jorge Vinicio Quintero Santos
 * @className SpamClassifier
 * @date Nov/14/2018
 * @comments None
 */
public class SpamClassifier {

    // Create a new bayes classifier with string categories and string features
    private Classifier<String, String> bayes;

    public SpamClassifier() {
        bayes = new BayesClassifier<>();
        initializeData();
    }

    private void initializeData() {
        // Creating an array of stacks, position 0 will be ham, position 1 will be spam
        Stack<String>[] processedData = (Stack<String>[]) new Stack[2];
        processedData[0] = new Stack<>();
        processedData[1] = new Stack<>();
        Stack<String> hamMessages;
        Stack<String> spamMessages;
        try {
            processData(processedData);
            hamMessages = processedData[0];
            spamMessages = processedData[1];

        } catch (IOException e) {
            System.out.println("Couldn't load data");
            e.printStackTrace();
            return;
        }
        // TODO: 11/15/18 Remove this, testing
        System.out.println(hamMessages.size());
        System.out.println(spamMessages.size());

        int counter = 0;
        while (counter++ < 200) {
            String[] message = hamMessages.pop().split("\\s");
            bayes.learn("ham", Arrays.asList(message));
        }

        counter = 0;
        while (counter++ < 200) {
            String[] message = spamMessages.pop().split("\\s");
            bayes.learn("spam", Arrays.asList(message));
        }

        String test = "BUY AND WIN A NEW CAR";

        System.out.println(bayes.classify(Arrays.asList(test.split("\\s"))).getCategory());



    }

    private void processData(Stack<String>[] arrayOfData) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./data/trainingData.csv"));
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
}
