package fr.epita.mob.services;

import fr.epita.mob.model.Image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CentroidClassifier {

    public static List<Image> trainCentroids(List<Image> image_list){
        List<Image> centroid_list=new ArrayList<>();
        HashMap<Double, Integer> distrib=calculateDistribution(image_list);

        // Create 10 images for the 10 Mean centroids
        for (int i=0;i<10;i++) {
            Image current_centroid = new Image();
            current_centroid.setLabel(i);
            current_centroid.setDataMatrix(new double[28][28]);
            centroid_list.add(current_centroid);
        }
            // Add all the pixel value for each label for all the images in the list label by label
        for (Image current_img:image_list){
            for(int j=0;j<28;j++){
                for(int k=0;k<28;k++){
                    centroid_list.get((int) current_img.getLabel()).getDataMatrix()[j][k]
                            +=current_img.getDataMatrix()[j][k];
                }
            }
        }
        // Divide the value of each digit by number of occurrences
        for (int a=0;a<10;a++){
            for(int m=0;m<28;m++){
                for(int n=0;n<28;n++){
                    centroid_list.get(a).getDataMatrix()[m][n]
                            =centroid_list.get(a).getDataMatrix()[m][n]/distrib.get((double) a) ;
                }
            }
        }
        return centroid_list;
    }

    public static List<Image> trainSTDCentroids(List<Image> image_list, List<Image> mean_centroids){
        List<Image> centroid_list=new ArrayList<>();
        HashMap<Double, Integer> distrib=calculateDistribution(image_list);

        // Create 10 images for the STD centroids
        for (int i=0;i<10;i++) {
            Image current_centroid = new Image();
            current_centroid.setLabel(i);
            current_centroid.setDataMatrix(new double[28][28]);
            centroid_list.add(current_centroid);
        }
        // Compute and sum the square of the difference between each pixel for each pictures and and the Mean centroids
        for (Image current_img:image_list){
            int img_label=(int) current_img.getLabel();
            for(int j=0;j<28;j++){
                for(int k=0;k<28;k++){
                    centroid_list.get(img_label).getDataMatrix()[j][k]
                        +=Math.pow(current_img.getDataMatrix()[j][k]-mean_centroids.get(img_label).getDataMatrix()[j][k],2);
                }
            }
        }
        // Divide the value of each digit by the number of occurrences and then take its square root
        for (int a=0;a<10;a++){
            for(int m=0;m<28;m++){
                for(int n=0;n<28;n++){
                    centroid_list.get(a).getDataMatrix()[m][n]
                            =Math.sqrt(centroid_list.get(a).getDataMatrix()[m][n]/distrib.get((double) a)) ;
                }
            }
        }
        return centroid_list;
    }

    public static HashMap<Double,Integer> calculateDistribution(List<Image> image_list){
        HashMap<Double,Integer> distrib= new HashMap<>();
        Double current_label;
        for(Image current_im:image_list){
            current_label=current_im.getLabel();
            Integer actualCount = distrib.get(current_label);
            distrib.put(current_label , actualCount == null ? 1 : actualCount + 1);

        }
        return distrib;
    }

    public static Double predict(Image image_to_test, List<Image> centroids){
        double distance_matrix=0.0;
        double prediction;
        List<Double> list_dist=new ArrayList<>();
        for (Image curr_centroid:centroids){
            for(int m=0;m<28;m++){
                for(int n=0;n<28;n++){
                    distance_matrix=distance_matrix
                            +Math.abs(image_to_test.getDataMatrix()[m][n]-curr_centroid.getDataMatrix()[m][n]);
                }

            }
            list_dist.add(Math.sqrt(distance_matrix));
            distance_matrix=0.0;


        }
        prediction=list_dist.indexOf(Collections.min(list_dist));
        return prediction;
    }

    public static Double predictSTD(Image image_to_test, List<Image> centroids){
        double distance_matrix=0.0;
        double prediction;
        List<Double> list_dist=new ArrayList<>();
        for (Image curr_centroid:centroids){
            for(int m=0;m<28;m++){
                for(int n=0;n<28;n++){
                    distance_matrix=distance_matrix
                            +Math.pow(image_to_test.getDataMatrix()[m][n]-curr_centroid.getDataMatrix()[m][n],2);
                }

            }
            list_dist.add(Math.sqrt(distance_matrix));
            distance_matrix=0.0;


        }
        prediction=list_dist.indexOf(Collections.min(list_dist));
        return prediction;
    }

    public static int[][] Confusion_matrix(List<Image> list_img, List<Image> centroids, String method){
        double prediction=0.0;
        int[][] matrix=new int[10][10];
        for (Image img :list_img){
            if (method=="mean") {
                prediction = predict(img, centroids);
            } else if (method=="STD"){
                prediction=predictSTD(img,centroids);
            }
            matrix[(int) prediction][(int) img.getLabel()]+=1;
        }
        return matrix;
    }

    public static void print_confusion_M (int[][] conf){
        for (int i=0;i<10;i++){
            for(int j=0;j<10;j++){
                if (conf[i][j]<10){
                    System.out.print("    "+conf[i][j]);
                }
                else if (conf[i][j]<100){
                    System.out.print("   "+conf[i][j]);
                }
                else if (conf[i][j]<1000){
                    System.out.print("  "+conf[i][j] );
                }
                else {
                    System.out.print(" "+conf[i][j] );
                }
            }
            System.out.println("");

        }
    }

    public static double[] Sensitivity_Specificity (int digit, int[][] conf){
        int true_pos;
        int false_neg=0;
        int true_neg=0;
        int false_pos=0;
        double sensitivity;
        double specificity;
        true_pos=conf[digit][digit];
        for (int i=0;i<10;i++){
            if (i!=digit) {
                false_neg += conf[i][digit];
            }
        }
        for (int j=0;j<10;j++){
            for (int k=0;k<10;k++){
                if (j!=digit && k!=digit){
                    true_neg+=conf[j][k];
                }
            }
        }
        for (int l=0;l<10;l++){
            if (l!=digit) {
                false_pos += conf[digit][l];
            }
        }
        sensitivity=((double) true_pos)/(true_pos+false_neg);
        specificity=((double) true_neg)/(true_neg+false_pos);
        double[] res={sensitivity,specificity,true_pos,true_neg,false_pos,false_neg};
        return res;
    }

    public static int[][] Display_Confusion_Matrix(List<Image> Test_images, List<Image> centroids, String method) {
        // Make predictions for all the Test dataset and print confusion matrix

        int[][] confusion_matrix = Confusion_matrix(Test_images, centroids, method);
        System.out.printf("%nConfusion Matrix with the method %s %n",method);
        print_confusion_M(confusion_matrix);
        return confusion_matrix;


    }

    public static void Display_Sensitivity_Specificity(int digit, int[][] confusion_matrix) {
        double[] result_for_zeros=Sensitivity_Specificity(digit, confusion_matrix);
        System.out.printf("%nConfusion Matrix for the test data:%n");
        System.out.println("For the digit " + digit +":");
        System.out.printf("The sensitivity is: %.2f%% and the specificity is: %.2f%% %n",result_for_zeros[0]*100
                ,result_for_zeros[1]*100);
        System.out.println("True positive: "+(int) result_for_zeros[2]);
        System.out.println("True negative: "+(int) result_for_zeros[3]);
        System.out.println("False positive: "+ (int)result_for_zeros[4]);
        System.out.println("False negative: "+ (int) result_for_zeros[5]);
    }
}
