package fr.epita.mob.model;

import java.util.Arrays;
import java.util.List;

public class Image {
    private double label;
    private double[][] dataMatrix;

    @Override
    public String toString() {
        return "Image{" +
                "label=" + label +
                ", dataMatrix=" + Arrays.toString(dataMatrix) +
                '}';
    }

    public double getLabel() {
        return label;
    }

    public void setLabel(double label) {
        this.label = label;
    }

    public double[][] getDataMatrix() {
        return dataMatrix;
    }

    public void setDataMatrix(double[][] dataMatrix) {
        this.dataMatrix = dataMatrix;
    }
}
