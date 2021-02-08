package fr.epita.mob.services;

import fr.epita.mob.model.Image;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ImageCsvDAO {
    public static void showMatrix(Image matrix){


        for (int i=0 ;i<28;i++){
            for (int j=0;j<28;j++) {
                if (matrix.getDataMatrix()[i][j]<=100){
                    System.out.print("..");
                } else {
                    System.out.print("xx");
                }
            }
            System.out.println("");
        }


    }

    public static void showMatrix_v2(Image matrix){


        for (int i=0 ;i<28;i++){
            for (int j=0;j<28;j++) {
                double pixel=matrix.getDataMatrix()[i][j];
                if (pixel<=25){
                    System.out.print("..");
                } else if (pixel<=50){
                    System.out.print("--");
                }else if (pixel<=100){
                    System.out.print("oo");
                }else if (pixel<=150){
                    System.out.print("JJ");
                }else if (pixel<=200){
                    System.out.print("MM");
                }
                else {
                    System.out.print("##");
                }
            }
            System.out.println("");
        }


    }

    public static List<Image> getAllImages(String pathname) throws IOException {
        List<Image> list_images=new ArrayList<>();
        List<String> lines = Files.readAllLines(new File(pathname).toPath(), StandardCharsets.ISO_8859_1);
        List<List> file_data=new ArrayList<>();

        String header = lines.remove(0);
        for (String line : lines) {
            List<Double> parts = Arrays.asList(line.split(",")).stream()
                    .map(s -> Double.parseDouble(s))
                    .collect(Collectors.toList());
            file_data.add(parts);
        }

        for (List<Double> current_digit:file_data){
            Image current_image=new Image();
            current_image.setLabel(current_digit.get(0));
            double[][] current_matrix=new double[28][28];
            for (int i=0 ;i<28;i++){
                for (int j=0;j<28;j++) {
                    current_matrix[i][j] = current_digit.get(i * 28 + j);
                }
            }
            current_image.setDataMatrix(current_matrix);
            list_images.add(current_image);
        }

        return list_images;
    }

}
