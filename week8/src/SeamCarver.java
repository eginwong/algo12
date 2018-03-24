import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final double BORDER_ENERGY = 1000.0;
    private Picture localCopy;
    private double[][] energyParty;

    public SeamCarver(Picture picture) {
        checkNull(picture);
        localCopy = new Picture(picture);

        updateEnergy();
        //    populate energyParty here
    }               // create a seam carver object based on the given picture

    private void updateEnergy() {
        energyParty = new double[localCopy.width()][localCopy.height()];

        // by column
        for (int i = 0; i < localCopy.width(); i++) {
            // by row
            for (int j = 0; j < localCopy.height(); j++) {
                energyParty[i][j] = calculateEnergy(i, j);
            }
        }
    }

    public Picture picture() {
        // defensive return
        return new Picture(localCopy);
    }                     // current picture

    public int width() {
        return localCopy.width();
    }                           // width of current picture

    public int height() {
        return localCopy.height();
    }                          // height of current picture

    // x is column
    // y is row
    public double energy(int x, int y) {
        //    check if all vars are in X,Y
        checkAllowableDimension(x, width(), "not in allowable column");
        checkAllowableDimension(y, height(), "not in allowable row");
        return energyParty[x][y];
    }              // energy of pixel at column x and row y

    public int[] findHorizontalSeam() {
        transpose();
        int[] hseam = findVerticalSeam();
        transpose();
        // for output, but not necessary as the calculation is correct
        updateEnergy();
        return hseam;
    }              // sequence of indices for horizontal seam

    public int[] findVerticalSeam() {
        // in case of transposition
        updateEnergy();
        // make bigger than required in order to have virtual node
        double[][] distTo = new double[localCopy.width()][localCopy.height()];
        int[][] edgeTo = new int[localCopy.width()][localCopy.height()];

        // initialize edgeTo
        for (int i = 0; i < localCopy.width(); i++) {
            for (int j = 0; j < localCopy.height(); j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        // need a virtual source at the top and virtual source at the bottom
        for (int x = 0; x < localCopy.width(); x++) {
            edgeTo[x][0] = Integer.MIN_VALUE;
            distTo[x][0] = energyParty[x][0];
        }

        // start relaxing for each row
        for (int relaxY = 0; relaxY < localCopy.height(); relaxY++) {
            // for each column
            for (int relaxX = 0; relaxX < localCopy.width(); relaxX++) {
                //    start reaching vertices connected to me
                if (relaxY < localCopy.height() - 1) {

                    // straight down good
                    // if next distance is greater than current, replace with current
                    double newEnergy = distTo[relaxX][relaxY] + energyParty[relaxX][relaxY + 1];
                    if (distTo[relaxX][relaxY + 1] > newEnergy) {
                        distTo[relaxX][relaxY + 1] = newEnergy;
                        // from which col shall we go down into the deep?
                        edgeTo[relaxX][relaxY + 1] = relaxX;
                    }

                    // down left good
                    if (relaxX > 0) {
                        newEnergy = distTo[relaxX][relaxY] + energyParty[relaxX - 1][relaxY + 1];
                        if (distTo[relaxX - 1][relaxY + 1] > newEnergy) {
                            distTo[relaxX - 1][relaxY + 1] = newEnergy;
                            edgeTo[relaxX - 1][relaxY + 1] = relaxX;
                        }
                    }

                    // down right good
                    if (relaxX < localCopy.width() - 1) {
                        newEnergy = distTo[relaxX][relaxY] + energyParty[relaxX + 1][relaxY + 1];
                        if (distTo[relaxX + 1][relaxY + 1] > newEnergy) {
                            distTo[relaxX + 1][relaxY + 1] = newEnergy;
                            edgeTo[relaxX + 1][relaxY + 1] = relaxX;
                        }
                    }

                }
            }
        }

        // virtual source to determine lowest energy route
        int sink = Integer.MAX_VALUE;
        double lowestTotalEnergy = Double.POSITIVE_INFINITY;

        for (int i = 0; i < localCopy.width(); i++) {
            double challenger = distTo[i][localCopy.height() - 1];
            if (challenger < lowestTotalEnergy) {
                lowestTotalEnergy = challenger;
                sink = i;
            }
        }

        if (sink < localCopy.width()) {
            int[] seam = new int[height()];

            // go backwards
            for (int i = localCopy.height() - 1; i >= 0; i--) {

                seam[i] = sink;
                sink = edgeTo[sink][i];
            }

            return seam;
        }
        // no seam found
        return new int[]{};
    }                 // sequence of indices for vertical seam

    public void removeHorizontalSeam(int[] seam) {
        checkRemoveOnOne(height(), "not tall enough to cut");
        checkNull(seam);
        transpose();
        removeVerticalSeam(seam);
        transpose();
        updateEnergy();
    }   // remove horizontal seam from current picture

    public void removeVerticalSeam(int[] seam) {
        checkRemoveOnOne(width(), "not fat enough to cut");
        checkNull(seam);
        //    check if all vars are in X,Y
        if (seam.length != height()) throw new IllegalArgumentException("too many args");
        for (int i = 0; i < seam.length; i++) {
            checkAllowableDimension(seam[i], width(), "not in allowable row");
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) throw new IllegalArgumentException("jump is too big");
        }

        Picture placeHolder = new Picture(localCopy.width() - 1, localCopy.height());
        // need separate counter because we're slicing a varying value out of the photo
        int actualPixel = 0;
        for (int row = 0; row < localCopy.height(); row++) {
            for (int col = 0; col < localCopy.width(); col++) {
                if (col != seam[row]) {
                    placeHolder.set(actualPixel, row, localCopy.get(col, row));
                    actualPixel++;
                }
            }
            actualPixel = 0;
        }

        localCopy = placeHolder;
        // recalculate energy along the seam
        updateEnergy();
    }    // remove vertical seam from current picture

    private double calculateEnergy(int x, int y) {
        if (x == 0 || x == (width() - 1) || y == 0 || y == (height() - 1)) return BORDER_ENERGY;
        Color leftX = localCopy.get(x - 1, y);
        Color rightX = localCopy.get(x + 1, y);
        Color upY = localCopy.get(x, y - 1);
        Color downY = localCopy.get(x, y + 1);
        double deltaX = getDeltaSquared(leftX, rightX);
        double deltaY = getDeltaSquared(upY, downY);
        return Math.sqrt(deltaX + deltaY);
    }

    private double getDeltaSquared(Color neg, Color pos) {
        int negRGB = neg.getRGB();
        int posRGB = pos.getRGB();
        return Math.pow(extractFromRGB(posRGB, 16) - extractFromRGB(negRGB, 16), 2) +
                Math.pow(extractFromRGB(posRGB, 8) - extractFromRGB(negRGB, 8), 2) +
                Math.pow(extractFromRGB(posRGB, 0) - extractFromRGB(negRGB, 0), 2);
    }

    private int extractFromRGB(int negRGB, int i) {
        return (negRGB >> i) & 0xFF;
    }

    private void transpose() {
        // 'cause we be swapping
        Picture placeholder = new Picture(localCopy.height(), localCopy.width());

        for (int i = 0; i < localCopy.height(); i++) {
            for (int j = 0; j < localCopy.width(); j++) {
                placeholder.set(i, j, localCopy.get(j, i));
            }
        }
        localCopy = placeholder;
    }

    private void checkAllowableDimension(int param, int dim, String s) {
        if (param < 0 || param >= dim) throw new IllegalArgumentException(s);
    }

    private void checkRemoveOnOne(int dimension, String s) {
        if (dimension <= 1) throw new IllegalArgumentException(s);
    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("null input");
    }
}
