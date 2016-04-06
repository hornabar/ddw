
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.System;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SentimentAnalyser {

    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;

    // whether the GATE is initialised
    private static boolean isInitilised = false;

    //sentiment dictionary
    private static HashMap<String, Integer> sentimentsDict = null;

    private static void initialise() {
        //GATE
        try {
            // set GATE home folder
            // Eg. /Applications/GATE_Developer_7.0
            File gateHomeFile = new File("C:\\Program Files\\GATE_Developer_8.1");
            Gate.setGateHome(gateHomeFile);

            // set GATE plugins folder
            // Eg. /Applications/GATE_Developer_7.0/plugins
            File pluginsHome = new File("C:\\Program Files\\GATE_Developer_8.1\\plugins");
            Gate.setPluginsHome(pluginsHome);

            // set user config file (optional)
            // Eg. /Applications/GATE_Developer_7.0/user.xml
            //Gate.setUserConfigFile(new File("/Applications/GATE_Developer_7.0", "user.xml"));

            // initialise the GATE library
            Gate.init();

            // load ANNIE plugin
            CreoleRegister register = Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURL();
            register.registerDirectories(annieHome);


            // create an instance of a Document Reset processing resource
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");
            // create an instance of a English Tokeniser processing resource
            ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
            // create an instance of a Sentence Splitter processing resource
            ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");

            // create corpus pipeline
            annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");

            // add the processing resources (modules) to the pipeline
            annotationPipeline.add(documentResetPR);
            annotationPipeline.add(tokenizerPR);
            annotationPipeline.add(sentenceSplitterPR);


        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (GateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //DICTIONARY
        try{
            File csvData = new File(".\\src\\AFINN\\AFINN-111.txt");
            sentimentsDict = new HashMap<>();
            CSVParser parser = new CSVParser( new FileReader(csvData), CSVFormat.TDF);
            for (CSVRecord csvRecord : parser.getRecords()) {
                sentimentsDict.put(csvRecord.get(0), Integer.parseInt(csvRecord.get(1)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        isInitilised = true;
    }

    public static int analyse(String text){
        int sentimentAll = 0;

        if(!isInitilised){
            // initialise GATE
            initialise();
        }
        try {
            // create a document
            Document document = Factory.newDocument(text);

            // create a corpus and add the document
            Corpus corpus = Factory.newCorpus("");
            corpus.add(document);

            // set the corpus to the pipeline
            annotationPipeline.setCorpus(corpus);

            //run the pipeline
            annotationPipeline.execute();

            AnnotationSet annSet = document.getAnnotations();

            FeatureMap futureMap = null;
            // get all Token annotations
            AnnotationSet annSetTokens = annSet.get("Token", futureMap);

            ArrayList tokenAnnotations = new ArrayList(annSetTokens);

            // looop through the Token annotations
            for(int j = 0; j < tokenAnnotations.size(); ++j) {

                // get a token annotation
                Annotation token = (Annotation)tokenAnnotations.get(j);
                FeatureMap tokenFeatures = token.getFeatures();

                //if(tokenFeatures.get("kind").equals("word")) {
                // get the underlying string for the Token
                Node isaStart = token.getStartNode();
                Node isaEnd = token.getEndNode();
                String underlyingString = document.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();

                //sentiment counting
                Integer sentiment = sentimentsDict.get(underlyingString.toLowerCase());
                if (sentiment != null) {
                    sentimentAll += sentiment;
                }
                //}
            }

    } catch (GateException ex) {
        Logger.getLogger(SentimentAnalyser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex) {
        Logger.getLogger(SentimentAnalyser.class.getName()).log(Level.SEVERE, null, ex);
    }

        return sentimentAll;
    }
}
