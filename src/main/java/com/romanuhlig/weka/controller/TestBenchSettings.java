package com.romanuhlig.weka.controller;

import com.romanuhlig.weka.classification.ClassifierFactory;
import com.romanuhlig.weka.classification.ClassifierFactory.ClassifierType;
import com.romanuhlig.weka.io.SensorPermutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class TestBenchSettings {


    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    //////////////////                                //////////////////
    //////////////////            Settings            //////////////////
    //////////////////                                //////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////


    // do not generate new features, read old file instead
    private static boolean useExistingFeatureFile = true;
    private static String forceFolderName = "3-4 trackers - 1 second";

    // scale all features by fixed amount (required for some algorithms)
    private static double scaleAllFeaturesBy = 1;

    // whether to allow the test data for any person to be included in the training data
    // WARNING: only meant for debugging, using the exact same data for training and testing is not realistic,
    //          even for a subject dependant model
    private static boolean includeTestDataInTrainingData = false;

    // force usage of exactly these sensor combinations, not more or less
    // if left empty, more generalized options below will be used
    private static String[][] onlyAllowSensorPermutations = new String[][]{
//            {"rForeArm", "lLeg"}
//            ,
//            {"rForeArm", "rLeg"}
//            ,
//            {"rArm", "lLeg"}
//            ,
//            {"rArm", "rLeg"}
//            ,
//            {"lForeArm", "lLeg"}
//            ,
//            {"lForeArm", "rLeg"}


//            {"head"},                                                                       // HMD
//            {"head", "lHand", "rHand"},                                                     // HMD + Hands
//            {"head", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                      // HMD + inverse kinematics
//            {"head", "lForeArm", "rForeArm", "lHand", "rHand", "hip", "lLeg", "rLeg"},    // HMD + IK + Hands
//            {"lHand", "rHand"},                                                             // Hands
//            {"lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                              // inverse kinematics
//            {"lForeArm", "rForeArm", "lHand", "rHand", "hip", "lLeg", "rLeg"},             // IK + Hands
//            {"lForeArm", "rForeArm", "lLeg", "rLeg"},                              // inverse kinematics
//            {"rForeArm", "lLeg", "rLeg"},                              // inverse kinematics

            //////////////////////   no additional sensors needed for 5s  ///////////////////////
//            {"head", "lHand", "rHand", "hip", "lLeg", "rLeg"},                               // HMD + base_IK + Hands
//            {"head", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                         // HMD + IK_2
//            {"head", "lHand", "rHand", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},       // HMD + IK_2 + Hands
//            {"lHand", "rHand", "hip", "lLeg", "rLeg"},                                       // base_IK + Hands
//            {"lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                                 // IK_2
//            {"lHand", "rHand", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},               // IK_2 + Hands

    };

    private static String[][] minimumSensorPermuation = new String[][]{
            /////////////////////////////   standard sensor set   /////////////////////////////
            {},                                                                              // trackers only
            {"head"},                                                                        // HMD
//            {"head", "lHand", "rHand"},                                                      // HMD + Hands
////            {"head", "hip", "lLeg", "rLeg"},                                                 // HMD + base_IK
//            {"head", "lHand", "rHand", "hip", "lLeg", "rLeg"},                               // HMD + base_IK + Hands
//            {"head", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                         // HMD + IK_2
//            {"head", "lHand", "rHand", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},       // HMD + IK_2 + Hands
//            {"lHand", "rHand"},                                                              // Hands
////            {"hip", "lLeg", "rLeg"},                                                         // base_IK
//            {"lHand", "rHand", "hip", "lLeg", "rLeg"},                                       // base_IK + Hands
//            {"lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},                                 // IK_2
//            {"lHand", "rHand", "lForeArm", "rForeArm", "hip", "lLeg", "rLeg"},               // IK_2 + Hands
    };

    // types of features to disallow
    private static ArrayList<FeatureTag> forbiddenFeatureTags = new ArrayList<>(Arrays.asList(
//            FeatureTag.Angular
//            FeatureTag.SubjectOrientationRelevant
//            FeatureTag.DualSensorCombination
    ));

    // sensor permutations to use during evaluation
    private static SensorUsage sensorUsageHMD = SensorUsage.MayInclude;
    private static SensorUsage sensorUsageHandControllers = SensorUsage.MayInclude;
    private static boolean allowSingleHandController = true;
    private static int minimumNumberOfTrackers = 3;
    private static int maximumNumberOfTrackers = 4;
    private static int minimumNumberOfSensors = -111;
    private static int maximumNumberOfSensors = -111;

    // input frame data
    private static double windowSizeForFrameDataToFeatureConversion = 1;
    private static double windowSpacingForFrameDataToFeatureConversion = 1;


    // algorithms to test
    private static ArrayList<ClassifierFactory.ClassifierType> classifiersToUse = new ArrayList<>(Arrays.asList(
//            ClassifierType.J48
//            , // 20min  , NAN-.25
//            ClassifierType.RandomForest
//            , //  < 1h  , .60-ALL_1 (most > .90)
//            ClassifierType.NaiveBayes
//            , // 4min   , results NAN-0.58
            ClassifierType.SMO
//            , // 3 min  , results .93-ALL_2
//            ClassifierType.LibSVM
//            ,
//            ClassifierType.LibLinear
//            ,
//            ClassifierType.LMT
//            , // 6h / 6p, results .11-.93
//            ClassifierType.JRip
//            , // 2h / 6p, results NAN-00
//            ClassifierType.SimpleLogistic
//            , // 2h / 6p, results .11-.93
//            ClassifierType.VotedPerceptron        // (originally for binary class, adapted via multiclass classifier)
//            , // 5h / 6p, results terrible (all < .4 average, NAN min)
//            ClassifierType.SGD                    // (originally for binary class, adapted via multiclass classifier)
//            , // 3h / 6p, results .80-.95
//            ClassifierType.Logistic
//            , // 14h / 6p, results .73-.96
//            ClassifierType.REPTree
//            , // 10min/6p, results NAN-00
//            ClassifierType.RandomTree
//            , // 2min / 6p, results NAN-.03
//            ClassifierType.OneR                   // only useful for sanity checks
//            ,
//            ClassifierType.ZeroR                  // only useful for sanity checks
//            ,
//            ClassifierType.IBk                    // lazy -> slow + needs to keep data -> not viable for actual usage
//            ,
//            ClassifierType.KStar                  // lazy -> slow + needs to keep data -> not viable for actual usage
//            ,
//            ClassifierType.MultilayerPerceptron   // very, very slow in training (deep neural network)
//            , // 1h not enough to train a single case
//            ClassifierType.BayesNet               // - bin problems (too similar, scaling does not always help)
//            ,
//            ClassifierType.DecisionTable          // - bin problems (too similar, scaling does not always help)
//            ,
//            ClassifierType.LinearRegression       // - regression
//            ,
//            ClassifierType.SMOreg                 // - regression
//            ,
//            ClassifierType.GaussianProcess        // - regression
//            ,
//            ClassifierType.M5P                    // - regression
    ));


    // result output
    private static boolean writeAllModelsToFolder = false;
    private static boolean useIndividualFeatureFilesForEachSubject = false;


    // folders
    private static String inputBaseFolder = "./inputFrameData/currentInput";
    private static String existingFeaturesInputFolder = "./inputFrameData/existingFeatures";
    private static String outputBaseFolder = "./outputResults/";
    private static String outputFolderTag = "";


    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    /////////////////                                 //////////////////
    /////////////////         End of Settings         //////////////////
    /////////////////                                 //////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////


    public enum SensorUsage {
        MayInclude, MustInclude, CannotInclude
    }

    public enum FeatureTag {
        Angular,
        SubjectOrientationRelevant,
        DualSensorCombination
    }

    public static String summarySingleLine() {

        if (forceFolderName != null && forceFolderName != "") {
            return forceFolderName;
        }

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("C");
        for (ClassifierFactory.ClassifierType classifier : classifiersToUse) {
            stringBuilder.append("_");
            stringBuilder.append(classifier.toString());
        }

        stringBuilder.append("   itd_" + includeTestDataInTrainingData);
        stringBuilder.append("   s_" + scaleAllFeaturesBy);
        stringBuilder.append("   ws_" + windowSizeForFrameDataToFeatureConversion);
        stringBuilder.append("   wsp_" + windowSpacingForFrameDataToFeatureConversion);
        stringBuilder.append("   hmd_" + sensorUsageHMD.toString());
        stringBuilder.append("   hc_" + sensorUsageHandControllers.toString());
        stringBuilder.append("   shc_" + allowSingleHandController);
        stringBuilder.append("   t_" + minimumNumberOfTrackers + "-" + maximumNumberOfTrackers);
        stringBuilder.append("   s_" + minimumNumberOfSensors + "-" + maximumNumberOfSensors);

        return stringBuilder.toString();
    }


    public static String summaryBig() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Classifiers:   ");
        for (ClassifierFactory.ClassifierType classifier : classifiersToUse) {
            stringBuilder.append(classifier.toString());
            stringBuilder.append("   ");
        }
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("scale all features by:   " + scaleAllFeaturesBy);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("Window Size for frame data to feature conversion:   " + windowSizeForFrameDataToFeatureConversion);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("Window Spacing for frame data to feature conversion:   " + windowSpacingForFrameDataToFeatureConversion);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("sensor usage HMD:   " + sensorUsageHMD.toString());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("sensor usage HandController:   " + sensorUsageHandControllers.toString());
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("disallow single HandController:   " + allowSingleHandController);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("maximum number Of Trackers:  " + maximumNumberOfTrackers);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("minimum number Of Trackers:  " + minimumNumberOfTrackers);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("maximum number of Sensors:   " + maximumNumberOfSensors);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("minimum number of Sensors:   " + minimumNumberOfSensors);
        stringBuilder.append(System.lineSeparator());


        return stringBuilder.toString();
    }


    public static ArrayList<ClassifierFactory.ClassifierType> getClassifiersToUse() {
        return classifiersToUse;
    }

    public static String getInputBaseFolder() {
        return inputBaseFolder;
    }

    public static String outputBaseFolder() {
        return outputBaseFolder;
    }

    public static String getOutputFolderTag() {
        return outputFolderTag;
    }

    public static double getWindowSizeForFrameDataToFeatureConversion() {
        return windowSizeForFrameDataToFeatureConversion;
    }

    public static double getWindowSpacingForFrameDataToFeatureConversion() {
        return windowSpacingForFrameDataToFeatureConversion;
    }

    public static SensorUsage getSensorUsageHMD() {
        return sensorUsageHMD;
    }

    public static SensorUsage getSensorUsageHandControllers() {
        return sensorUsageHandControllers;
    }


    public static int getMaximumNumberOfTrackers() {
        return maximumNumberOfTrackers;
    }

    public static int getMaximumNumberOfSensors() {
        return maximumNumberOfSensors;
    }

    public static boolean allowSingleHandController() {
        return allowSingleHandController;
    }

    public static int getMinimumNumberOfTrackers() {
        return minimumNumberOfTrackers;
    }

    public static int getMinimumNumberOfSensors() {
        return minimumNumberOfSensors;
    }

    public static boolean writeAllModelsToFolder() {
        return writeAllModelsToFolder;
    }


    public static boolean featureTagsAllowed(FeatureTag... tags) {

        for (FeatureTag tag : tags) {
            if (forbiddenFeatureTags.contains(tag)) {
                return false;
            }
        }

        return true;
    }

    public static boolean useIndividualFeatureFilesForEachSubject() {
        return useIndividualFeatureFilesForEachSubject;
    }

    public static boolean isSensorCombinationBlocked(SensorPermutation sensorPermutation) {
        // do not block any combination if no specific combination was requested
        if (onlyAllowSensorPermutations.length == 0) {
            return false;
            // otherwise, search for a fitting combination
        } else {

            ArrayList<String> sensorsInPermutation = sensorPermutation.getIncludedSensors();

            for (int i = 0; i < onlyAllowSensorPermutations.length; i++) {

                String[] allowedSensorPermutation = onlyAllowSensorPermutations[i];

                if (allowedSensorPermutation.length == sensorsInPermutation.size()) {
                    HashSet<String> sensorsAllowed = new HashSet<>(Arrays.asList(allowedSensorPermutation));
                    HashSet<String> sensorsIncluded = new HashSet<>(sensorsInPermutation);
                    if (sensorsAllowed.equals(sensorsIncluded)) {
                        return false;
                    }
                }
            }
        }

        // if no fitting combination was found, this combination is blocked
        return true;

    }


    public static boolean doesNotFulfillMinimumSensorRequirements(SensorPermutation sensorPermutation) {

        if (minimumSensorPermuation.length == 0) {
            return false;
        } else {

            ArrayList<String> sensorsInPermutation = sensorPermutation.getIncludedSensors();

            for (int i = 0; i < minimumSensorPermuation.length; i++) {

                String[] allowedSensorPermutation = minimumSensorPermuation[i];

                if (allowedSensorPermutation.length + maximumNumberOfTrackers >= sensorsInPermutation.size()) {

                    if (minimumNumberOfTrackers >= 0) {
                        if (allowedSensorPermutation.length + minimumNumberOfTrackers > sensorsInPermutation.size()) {
                            continue;
                        }
                    }

                    HashSet<String> allowedCombination = new HashSet<>(Arrays.asList(allowedSensorPermutation));
                    HashSet<String> sensorsIncluded = new HashSet<>(sensorsInPermutation);

                    if (sensorsIncluded.containsAll(allowedCombination)) {
                        if (sensorsIncluded.size() == allowedCombination.size()) {
                            return false;
                        } else {
                            sensorsIncluded.removeAll(allowedCombination);

                            if (!sensorsIncluded.contains("lHand") && !sensorsIncluded.contains("rHand")
                                    && !sensorsIncluded.contains("head")) {
                                return false;
                            }
                        }
                    }
                }
            }


        }


        return true;
    }


    public static boolean isSensorBlocked(String sensor) {
        // do not block sensor if no specific combination was requested
        if (onlyAllowSensorPermutations.length == 0) {
            return false;
            // otherwise, search for a fitting combination
        } else {
            for (int i = 0; i < onlyAllowSensorPermutations.length; i++) {

                String[] allowedSensorPermutation = onlyAllowSensorPermutations[i];

                for (int k = 0; k < allowedSensorPermutation.length; k++) {
                    if (allowedSensorPermutation[k].equals(sensor)) {
                        return false;
                    }
                }
            }
        }

        // if no fitting combination was found, this combination is blocked
        return true;
    }

    public static boolean specificSensorCombinationRequested() {
        return onlyAllowSensorPermutations.length > 0;
    }

    public static boolean minimumSensorCombinationRequested() {
        return minimumSensorPermuation.length > 0;
    }


    public static double scaleAllFeaturesBy() {
        return scaleAllFeaturesBy;
    }


    public static boolean useExistingFeatureFile() {
        return useExistingFeatureFile;
    }

    public static String getExistingFeaturesInputFolder() {
        return existingFeaturesInputFolder;
    }

    public static boolean includeTestDataInTrainingData() {
        return includeTestDataInTrainingData;
    }
}
