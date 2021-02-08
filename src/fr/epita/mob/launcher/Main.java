package fr.epita.mob.launcher;


import fr.epita.mob.model.Image;

import java.awt.font.ImageGraphicAttribute;
import java.io.IOException;
import java.text.CompactNumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

import static fr.epita.mob.services.ImageCsvDAO.*;
import static fr.epita.mob.services.CentroidClassifier.*;

public class Main {
    public static List<Image> centroids;
    public static void main(String[] args) throws IOException {

        // Load the Train and Test files and get them as Image Object
        String pathname="C:\\Users\\je2f_\\OneDrive - EPITA\\MSC AIS Course material\\Java and OOP\\mob-programming\\mnist_train.csv";
        String pathname_test="C:\\Users\\je2f_\\OneDrive - EPITA\\MSC AIS Course material\\Java and OOP\\mob-programming\\mnist_test.csv";
        List<Image> Train_images=getAllImages(pathname);
        List<Image> Test_images=getAllImages(pathname_test);

/*
Try to print a more "accurate" image
        for(int a=0;a<10;a++){
            showMatrix_v2(Train_images.get(a));
            System.out.println("");
        }
*/
        // Print the distribution of the labels in the Train set
        System.out.println(calculateDistribution(Train_images));
        //List<Image> subset= All_images.subList(0,30);
        List<Image> centroids=trainCentroids(Train_images);
        List<Image> STDcentroids=trainSTDCentroids(Train_images,centroids);
        showMatrix_v2(STDcentroids.get(6));
        System.out.println("");
        showMatrix(STDcentroids.get(6));
        System.out.println("");
        showMatrix_v2(centroids.get(6));
        System.out.println("");
        showMatrix(centroids.get(6));

        // Load the first 10 "Zeros" of the Test sample in a list
        int i=0;
        int j=0;
        List<Image> first_zeros=new ArrayList<>();
        while (i<10){
            if (Test_images.get(j).getLabel()==0.0){
                first_zeros.add(Test_images.get(j));
                i++;
            }
            j++;
        }

        // Make predictions on these 10 "Zeros" with MEAN
        List<Double> first_one=new ArrayList<>();
        for (Image img:first_zeros){
            first_one.add(predict(img,centroids));
        }
        System.out.println(first_one);

        // Make predictions on these 10 "Zeros" with STD
        List<Double> first_one_STD=new ArrayList<>();
        for (Image img:first_zeros){
            first_one_STD.add(predictSTD(img,STDcentroids));
        }
        System.out.println(first_one_STD);

        int[][] Mean_Confusion_M = Display_Confusion_Matrix(Test_images, centroids, "mean");
        int[][] STD_Confusion_M=Display_Confusion_Matrix(Test_images,STDcentroids,"STD");
        Display_Sensitivity_Specificity(5,Mean_Confusion_M);
        Display_Sensitivity_Specificity(5,STD_Confusion_M);


    }



}
