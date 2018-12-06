package com.romanuhlig.weka.controller;

import com.romanuhlig.weka.classification.ClassificationResult;
import com.romanuhlig.weka.classification.ClassifierFactory;
import com.romanuhlig.weka.data.FrameDataReader;
import com.romanuhlig.weka.io.*;
import com.romanuhlig.weka.time.TimeHelper;
import org.apache.commons.lang3.time.StopWatch;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestBench {


    public void run() throws Exception {

        StopWatch stopwatchFullProcess = new StopWatch();
        stopwatchFullProcess.start();

        // FrameDataReader.readFrameDataSet("./inputFrameData/TestUser__Task1__SID1__1542122933.csv");

        String startTime = TimeHelper.getDateWithSeconds();

        String outputFolderPath = TestBenchSettings.outputBaseFolder() + startTime + TestBenchSettings.getOutputFolderTag() + "/";

        FeatureExtractionResults featureExtractionResults = FrameDataReader.createFeatureSets(TestBenchSettings.getInputBaseFolder(), outputFolderPath);

        ArrayList<SensorPermutation> sensorPermutations = SensorPermutation.generateAllPermutations(featureExtractionResults.getAllSensorPositions());
        GlobalData.setAllAvailableSensors(featureExtractionResults.getAllSensorPositions());

        // System.out.println("permutations:     " + sensorPermutations.size());
//        for (SensorPermutation permutation : sensorPermutations) {
        //  System.out.println();

        //  for (String sensor : permutation.getIncludedSensors()) {
        //     System.out.print(sensor + "   ");
        //  }
//        }


        ClassifierFactory classifierFactory = new ClassifierFactory();


        StopWatch stopWatchEvaluation = new StopWatch();
        stopWatchEvaluation.start();

        int numberOfEvaluationsCompleted = 0;

        String resultsBaseFolder = outputFolderPath + "results/";

        ArrayList<ClassificationResult> allResults = new ArrayList<>();

        HashMap<Integer, ArrayList<ClassificationResult>> sensorNumberResults = new HashMap<>();

        // Weka evaluation
        for (SensorPermutation sensorPermutation : sensorPermutations) {

            ArrayList<ClassificationResult> sensorPermutationResults = new ArrayList<>();

            String outputFolderSensorPermutation = resultsBaseFolder + sensorPermutation.getNumberOfSensors() + " sensors/" + sensorPermutation.getFolderStringRepresentation() + "/";

            ArrayList<Classifier> classifiers = classifierFactory.getClassifiers(TestBenchSettings.getClassifiersToUse());

            for (Classifier classifier : classifiers) {

                ArrayList<ClassificationResult> classifierResults = new ArrayList<>();


                String outputFolderClassifier = outputFolderSensorPermutation + classifier.getClass().getSimpleName() + "/";

                for (TrainingAndTestFilePackage filePackage : featureExtractionResults.getTrainingAndTestFilePackages()) {

                    String outputFolderSubject = outputFolderClassifier + filePackage.getSubject() + "/";

                    // setup data sources
                    // training data
                    Instances trainingDataUnfiltered = filePackage.getTrainingDataUnfiltered();
                    // test data
                    Instances testDataUnfiltered = filePackage.getTestDataUnfiltered();


                    // remove attributes from sensors that should not be included in this sensor permutation
                    Enumeration<Attribute> allAttributes = trainingDataUnfiltered.enumerateAttributes();
                    ArrayList<Attribute> allAttributesList = Collections.list(allAttributes);
                    ArrayList<Integer> attributesToRemove = new ArrayList<>();
                    for (int i = 0; i < allAttributesList.size(); i++) {
                        if (sensorPermutation.attributeForbidden(allAttributesList.get(i))) {
                            attributesToRemove.add(i);
                        }
                    }

                    int[] attributeIndicesToRemove = ConversionHelper.integerListToIntArray(attributesToRemove);


                    Instances trainingData = trainingDataUnfiltered;
                    Instances testData = testDataUnfiltered;

                    if (attributeIndicesToRemove.length > 0) {
                        Remove remove = new Remove();
                        remove.setAttributeIndicesArray(attributeIndicesToRemove);
                        remove.setInputFormat(trainingData);
                        trainingData = Filter.useFilter(trainingDataUnfiltered, remove);
                        testData = Filter.useFilter(testDataUnfiltered, remove);
                    }


                    // actual evaluation
                    classifier.buildClassifier(trainingData);
                    Evaluation eval = new Evaluation(trainingData);
                    eval.evaluateModel(classifier, testData);


                    // file output
                    ClassificationResult classificationResult = ClassificationResult.constructClassificationResult(eval, classifier, trainingData, filePackage.getSubject(), sensorPermutation);
                    FileWriter.writeClassificationResult(classificationResult, outputFolderSubject, "classificationResult");

                    // collect result for summaries
                    classifierResults.add(classificationResult);


                    // console output

                    System.out.println(numberOfEvaluationsCompleted++);

                    /*
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();
                    System.out.println();

                    System.out.println(filePackage.getSubject());

                    System.out.println(sensorPermutation.getFolderStringRepresentation());

                    ArrayList<Attribute> attributesTraining = Collections.list(trainingData.enumerateAttributes());
                    ArrayList<Attribute> attributesTest = Collections.list(testData.enumerateAttributes());
                    System.out.println("number of training attributes from weka   " + attributesTraining.size());
                    System.out.println("number of test attributes from weka   " + attributesTest.size());


                    System.out.println(eval.toSummaryString("\nResults\n======\n", false));

                    // confusion matrix
                    double[][] matrix = eval.confusionMatrix();
                    for (int line = 0; line < matrix.length; line++) {
                        for (int column = 0; column < matrix[line].length; column++) {
                            System.out.print(Double.toString(matrix[line][column]));
                            System.out.print("     ");
                        }
                        System.out.println();
                    }
                    */

                }

                ClassificationResult classifierResultSummary = ClassificationResult.summarizeClassifierResults(classifierResults);
                classifierResults.add(classifierResultSummary);
                FileWriter.writeClassificationResults(classifierResults, outputFolderClassifier, "classificationResult");

                // collect for overall summary
                allResults.add(classifierResultSummary);

                // collect for sensor permutation summary
                sensorPermutationResults.add(classifierResultSummary);
                // collect for sensor number summary
                if (sensorNumberResults.containsKey(sensorPermutation.getNumberOfSensors())) {
                    sensorNumberResults.get(sensorPermutation.getNumberOfSensors()).add(classifierResultSummary);
                } else {
                    ArrayList<ClassificationResult> sensorNumberResultList = new ArrayList<>();
                    sensorNumberResultList.add(classifierResultSummary);
                    sensorNumberResults.put(sensorPermutation.getNumberOfSensors(), sensorNumberResultList);
                }


            }

            FileWriter.writeClassificationResults(sensorPermutationResults, outputFolderSensorPermutation, "classificationResult");


        }

        // write all results
        allResults.sort(ClassificationResult.getF1Comparator());
        FileWriter.writeClassificationResults(allResults, resultsBaseFolder, "classificationResult");

        // write sensor number results
        Iterator<Integer> sensorNumberIterator = sensorNumberResults.keySet().iterator();
        while (sensorNumberIterator.hasNext()) {
            Integer sensorNumber = sensorNumberIterator.next();
            String outputFolderSensorNumber = resultsBaseFolder + sensorNumber + " sensors/";
            ArrayList<ClassificationResult> sensorNumberResultsSorted = sensorNumberResults.get(sensorNumber);
            sensorNumberResultsSorted.sort(ClassificationResult.getF1Comparator());
            FileWriter.writeClassificationResults(sensorNumberResultsSorted, outputFolderSensorNumber, "classificationResult");
        }

        // output runtime
        stopWatchEvaluation.stop();
        stopwatchFullProcess.stop();
        System.out.println("Evaluation took: " + stopWatchEvaluation.getTime(TimeUnit.SECONDS));
        System.out.println("everything took: " + stopwatchFullProcess.getTime(TimeUnit.SECONDS));
    }


}