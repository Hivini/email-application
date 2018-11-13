package application.handlers;

import application.dataModels.Mail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * The class that manipulates the data, saves the file and load the file.
 */
public class MailDataHandler {

    private static final String MAIL = "mail";
    private static final String SUBJECT = "subject";
    private static final String BODY = "body";
    private static final String SEND_FROM = "sendBy";
    private static final String SEND_TO = "sendTo";

    private String fileName;
    private ObservableList<Mail> mails;

    public MailDataHandler(String fileName) {
        this.fileName = fileName;
        this.mails = FXCollections.observableArrayList();
    }

    public void addMail(Mail item) {
        mails.add(item);
    }

    public void deleteMail(Mail item) {
        mails.remove(item);
    }

    /**
     * Reads the xml file and creates the mail objects, every time the mail object data is loaded it adds it to the
     * Observable Lis mails.
     */
    public void loadMails() {
        try {
            // First we create an XMLInputFactory for process
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader from the factory
            InputStream in = new FileInputStream(this.fileName);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // Read the XML document
            Mail mail = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an email item, we create a new mail
                    if (startElement.getName().getLocalPart().equals(MAIL)) {
                        mail = new Mail();
                        continue;
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals(SUBJECT)) {
                            event = eventReader.nextEvent();
                            mail.setSubject(event.asCharacters().getData());
                            continue;
                        }
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(BODY)) {
                        event = eventReader.nextEvent();
                        mail.setBody(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(SEND_FROM)) {
                        event = eventReader.nextEvent();
                        mail.setSendBy(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart().equals(SEND_TO)) {
                        event = eventReader.nextEvent();
                        mail.setSendTo(event.asCharacters().getData());
                        continue;
                    }
                }

                // If we reach the end of a mail element, we add it to the observable list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(MAIL)) {
                        mails.add(mail);
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform the ObservableList mails into a XML file.
     */
    public void saveMails() {
        try {
            // Create the factory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
            // Create the writer
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(fileName));
            // Create the event factory to process every line
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();
            // The end of every XML part
            XMLEvent end = eventFactory.createDTD("\n");
            // Create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);
            eventWriter.add(end);

            StartElement mailsStartElement = eventFactory.createStartElement("", "", "mails");
            eventWriter.add(mailsStartElement);
            eventWriter.add(end);

            for (Mail mail : this.mails) {
                saveMail(eventWriter, eventFactory, mail);
            }

            eventWriter.add(eventFactory.createEndElement("", "", "mails"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());
            eventWriter.close();

        } catch (FileNotFoundException e) {
            System.out.println("Problem with the emails file: " + e.getMessage());
            e.printStackTrace();
        } catch (XMLStreamException e) {
            System.out.println("Problem writing mail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves the email into the file writen
     *
     * @param eventWriter The writer of every tag
     * @param eventFactory The factory that helps the creation of the tags
     * @param mail The Mail going to be added
     * @throws XMLStreamException
     */
    private void saveMail(XMLEventWriter eventWriter, XMLEventFactory eventFactory, Mail mail)
            throws XMLStreamException {

        XMLEvent end = eventFactory.createDTD("\n");

        // Create mail tag
        StartElement configStartElement = eventFactory.createStartElement("", "", MAIL);
        eventWriter.add(configStartElement);
        eventWriter.add(end);
        // Write the different nodes
        createNode(eventWriter, SUBJECT, mail.getSubject());
        createNode(eventWriter, BODY, mail.getBody());
        createNode(eventWriter, SEND_FROM, mail.getSendBy());
        createNode(eventWriter, SEND_TO, mail.getSendTo());

        eventWriter.add(eventFactory.createEndElement("", "", MAIL));
        eventWriter.add(end);
    }

    /**
     * Creates the necessary node to append to the XML file
     * @param eventWriter The writer of every tag
     * @param name The name of the tag
     * @param value The data stored in the tag
     * @throws XMLStreamException
     */
    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // Create Start node
        StartElement startElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(startElement);
        // Create content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // Create End node
        EndElement endElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(endElement);
        eventWriter.add(end);
    }

    public ObservableList<Mail> getMails() {
        return mails;
    }
}
