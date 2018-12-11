package com.romanuhlig.weka.lifeClassification;

import com.romanuhlig.weka.controller.TestBenchSettings;
import com.romanuhlig.weka.data.FrameData;
import com.romanuhlig.weka.data.FrameDataSet;

public class CppDataClassifier {

    private int numberOfDataPointsAdded = 0;

    double timeOfLastFrameData = 0;
    final double timeBetweenClassifications = 1;
    double timeOfLastClassification = 0;

    private FrameDataSet frameDataSet;


    public CppDataClassifier() {
        frameDataSet = new FrameDataSet("", "");
    }

    public void addFrameData(String sensorPosition, String subject, String activity,
                             double calPosX, double calPosY, double calPosZ,
                             double calRotX, double calRotY, double calRotZ, double calRotW,
                             double angVelX, double angVelY, double angVelZ,
                             double linVelX, double linVelY, double linVelZ,
                             double scale, double time) {

        // only consider a new classification once a new frame has started
        // that is, not while different sensors for the current frame might still be incoming
        if ((timeOfLastFrameData != time) && (time - timeOfLastClassification > timeBetweenClassifications)) {

            // only consider a new classification if enough data was collected
            if (frameDataSet.enoughDataForWindowSize(TestBenchSettings.getWindowSizeForFrameDataToFeatureConversion())) {

                timeOfLastClassification = time;

                outputClassifierResultToCpp("classifier Result = " + numberOfDataPointsAdded);
                outputClassifierResultToCpp(sensorPosition);
            }

        }


        // create and remember new frame data
        FrameData newFrameData = new FrameData(
                sensorPosition, subject, activity,
                calPosX, calPosY, calPosZ,
                calRotX, calRotY, calRotZ, calRotW,
                angVelX, angVelY, angVelZ,
                linVelX, linVelY, linVelZ,
                scale, time);
        frameDataSet.addFrameData(newFrameData);
        numberOfDataPointsAdded++;


// ---------------- Instance creation -------------------------
//        // Create empty instance with three attribute values
//        Instance inst = new DenseInstance(3);
//        Attribute length = new Attribute("length");
//        Attribute height = new Attribute("height");
//        Attribute width = new Attribute("width");
//
//        Classifier classifier;
//
//// Set instance's values for the attributes "length", "weight", and "position"
//        inst.setValue(length, 5.3);
//        inst.setValue(height, 300);
//        inst.setValue(width, "first");
//
//// Set instance's dataset to be the dataset "race"
//        inst.setDataset(inst);
//
//// Print the instance
//        System.out.println("The instance: " + inst);
//
//        classifier.classifyInstance(inst);


        timeOfLastFrameData = newFrameData.getTime();
    }

    // to be supplied in C++ through JNI
    public native void outputClassifierResultToCpp(String result);


}
