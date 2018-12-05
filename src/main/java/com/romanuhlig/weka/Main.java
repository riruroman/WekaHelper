package com.romanuhlig.weka;

import com.romanuhlig.weka.data.FrameDataReader;
import com.romanuhlig.weka.io.FeatureExtractionResults;
import com.romanuhlig.weka.io.SensorPermutation;
import com.romanuhlig.weka.io.TrainingAndTestFilePackage;
import com.romanuhlig.weka.time.TimeHelper;

import java.util.*;

import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Main {


    static String inputFilePath = "./inputFrameData";
    static String outputFilePathBase = "./outputResults/";
    static String outputFolderTag = "";


    public static void main(String[] args) throws Exception {

        // FrameDataReader.readFrameDataSet("./inputFrameData/TestUser__Task1__SID1__1542122933.csv");

        String startTime = TimeHelper.getDateWithSeconds();

        String outputFilePath = outputFilePathBase + startTime + outputFolderTag;

        FeatureExtractionResults featureExtractionResults = FrameDataReader.createFeatureSets(inputFilePath, outputFilePath);

        ArrayList<SensorPermutation> sensorPermutations = SensorPermutation.generateAllPermutations(featureExtractionResults.getSensorPositions());

        System.out.println("permutations:     " + sensorPermutations.size());
        for (SensorPermutation permutation : sensorPermutations) {

            System.out.println();

            for (String sensor : permutation.getIncludedSensors()) {
                System.out.print(sensor + "   ");
            }

        }

        for (SensorPermutation sensorPermutation : sensorPermutations) {

            // test weka
            for (TrainingAndTestFilePackage filePackage : featureExtractionResults.getTrainingAndTestFilePackages()) {

                // setup data sources
                // training data
                DataSource trainingSource = new DataSource(filePackage.getTrainingFilePath());
                Instances trainingDataUnfiltered = trainingSource.getDataSet();
                trainingDataUnfiltered.setClassIndex(trainingDataUnfiltered.numAttributes() - 1);
                // test data
                DataSource testSource = new DataSource(filePackage.getTestFilePath());
                Instances testDataUnfiltered = testSource.getDataSet();
                testDataUnfiltered.setClassIndex(testDataUnfiltered.numAttributes() - 1);


                // remove attributes from sensors that should not be included in this permutation
                Enumeration<Attribute> allAttributes = trainingDataUnfiltered.enumerateAttributes();
                ArrayList<Attribute> allAttributesList = Collections.list(allAttributes);
                ArrayList<Integer> attributesToRemove = new ArrayList<>();
                System.out.println(sensorPermutation.getExcludedSensors().size());
                for (int i = 0; i < allAttributesList.size(); i++) {
                    if (sensorPermutation.attributeForbidden(allAttributesList.get(i))) {
                        System.out.println("forbidden " + allAttributesList.get(i).name());
                        attributesToRemove.add(i);
                    }
                }

                int[] attributeIndicesToRemove = attributesToRemove.stream().mapToInt(i -> i).toArray();


                Instances trainingData = trainingDataUnfiltered;
                Instances testData = testDataUnfiltered;

                if (attributeIndicesToRemove.length > 0) {
                    System.out.println("removing");
                    Remove remove = new Remove();
                    remove.setAttributeIndicesArray(attributeIndicesToRemove);
                    remove.setInputFormat(trainingData);
                    trainingData = Filter.useFilter(trainingDataUnfiltered, remove);
                    testData = Filter.useFilter(testDataUnfiltered, remove);
                }


                // actual evaluation
                Classifier classifier = new J48();
                classifier.buildClassifier(trainingData);
                Evaluation eval = new Evaluation(trainingData);
                eval.evaluateModel(classifier, testData);


                // output
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
            }


        }


    }

}
