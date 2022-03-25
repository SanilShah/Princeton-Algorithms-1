/* *****************************************************************************
 *  Name:              Sanil Shah
 *  Last modified:     March, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.HashMap;

public class SeamCarver {

    private Picture pic;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        pic = picture;
        width = picture.width();
        height = picture.height();
    }

    // current picture
    public Picture picture() {
        return pic;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1) {
            return Math.pow(255, 2) * 3;
        }

        double deltaX = 0.0, deltaY = 0.0;
        Color x1, x2, y1, y2;

        x1 = pic.get(x - 1, y);
        x2 = pic.get(x + 1, y);
        y1 = pic.get(x, y - 1);
        y2 = pic.get(x, y + 1);

        deltaX = Math.pow(x1.getRed() - x2.getRed(), 2) + Math.pow(x1.getGreen() - x2.getGreen(), 2) + Math.pow(x1.getBlue() - x2.getBlue(), 2);
        deltaY = Math.pow(y1.getRed() - y2.getRed(), 2) + Math.pow(y1.getGreen() - y2.getGreen(), 2) + Math.pow(y1.getBlue() - y2.getBlue(), 2);

        return deltaX + deltaY;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        String mode = "h";
        HashMap<String, String> edgeTo = new HashMap<String, String>();
        HashMap<String, Double> energyTo = new HashMap<String, Double>();
        double cost = Double.MAX_VALUE;
        // curr represents current pixel.
        // next represents potentially next pixel which will be connected by curr.
        String curr, next, end = null;

        for (int col = 0; col < width() - 1; col++) {
            for (int row = 0; row < height(); row++) {
                curr = idToStr(col, row);

                if (col == 0) {
                    edgeTo.put(curr, null);
                    energyTo.put(curr, energy(col, row));
                }

                for (int i = row - 1; i <= row + 1; i++) {
                    if (i >= 0 && i < height()) {
                        next = idToStr(col + 1, i);
                        double newEnergy = energy(col + 1, i) + energyTo.get(curr);
                        // If we don't have a next edge yet, add one. Or, if this edge
                        // is better than the one we have, use it.
                        if (energyTo.get(next) == null || newEnergy < energyTo.get(next)) {
                            edgeTo.put(next, curr);
                            energyTo.put(next, newEnergy);
                            // End at the second to last column, because 'next' inolves
                            // the next column.
                            if (col + 1 == width() - 1 && newEnergy < cost) {
                                cost = newEnergy;
                                end = next;
                            }
                        }
                    }
                }

            }
        }

        return getSeam(mode, edgeTo, end);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        String mode = "v";
        HashMap<String, String> edgeTo = new HashMap<String, String>();
        HashMap<String, Double> energyTo = new HashMap<String, Double>();
        String curr, next, end  = null;
        double cost = Double.MAX_VALUE;

        for (int row = 0; row < height()-1; row++) {
            for (int col = 0; col < width(); col++) {
                curr = idToStr(col, row);

                if (row == 0) {
                    edgeTo.put(curr, null);
                    energyTo.put(curr, energy(col, row));
                }

                for (int k = col - 1; k <= col + 1; k++) {
                    if (k >= 0 && k < width()) {
                        next = idToStr(k, row + 1);
                        double newEnergy = energy(k, row + 1) + energyTo.get(curr);
                        if (energyTo.get(next) == null || newEnergy < energyTo.get(next)) {
                            edgeTo.put(next, curr);
                            energyTo.put(next, newEnergy);
                            if (row + 1 == height() - 1 && newEnergy < cost) {
                                end = next;
                                cost = newEnergy;
                            }
                        }
                    }
                }

            }
        }

        return getSeam(mode, edgeTo, end);
    }

    private int[] getSeam(String mode, HashMap<String, String> edgeTo, String end) {
        int size;
        if (mode.equals("h"))
            size = width();
        else
            size = height();

        int[] path = new int[size];
        String curr = end;

        while (size > 0) {
            path[--size] = str2Id(mode, curr);
            curr = (String) edgeTo.get(curr);
        }
        // path represents the seam as a 1D array of the coordinates in the seam.
        // y-coordinates are stored if the seam traverses horizontally.
        // x-coordinates are stored if the seam traverses vertically.
        return path;
    }

    private int str2Id(String mode, String str) {
        if (mode.equals("v")) {
            return Integer.parseInt(str.split(" ")[0]);
        }
        else
            return Integer.parseInt(str.split(" ")[1]);
    }

    private String idToStr(int row, int col) {
        return row + " " + col;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length > width() || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width(), height()-1);

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                if (row < seam[col]) {
                    newPic.set(col, row, pic.get(col, row));
                }
                else
                    newPic.set(col, row, pic.get(col, row + 1));
            }
        }
        height--;
        pic = new Picture(newPic);
    }

    // ONLY FOR TESTING
    public void showHorizontalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length > width() || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width(), height()-1);

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row++) {
                if (row < seam[col] || row > seam[col]) {
                    newPic.set(col, row, pic.get(col, row));
                }
                else if ( row == seam[col])
                    newPic.set(col, row, Color.RED);
            }
        }
        height--;
        pic = new Picture(newPic);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length > height() || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width() - 1, height());

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width() - 1; col++) {
                if (col < seam[row])
                    newPic.set(col, row, pic.get(col, row));
                else
                    newPic.set(col, row, pic.get(col + 1, row));
            }
        }
        width--;
        pic = new Picture(newPic);
    }

    public void showVerticalSeam(int[] seam) {
        if (width() <= 1 || height() <= 1 || seam.length < 0 || seam.length > height() || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }

        Picture newPic = new Picture(width() - 1, height());

        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width() - 1; col++) {
                if (col < seam[row] || col > seam[row])
                    newPic.set(col, row, pic.get(col, row));
                else
                    newPic.set(col, row, Color.RED);
            }
        }
        width--;
        pic = new Picture(newPic);
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                return false;
            }
        }
        return true;
    }

    public Picture resizeTo(String mode, int dimension) {
        // Resize / decrease the width, remove vertical seams.
        if (mode.equals("width")) {
            while (this.width() > dimension) {
                System.out.println("Resizing...currently at width " + this.width());
                int[] seam = this.findVerticalSeam();
                this.removeVerticalSeam(seam);
            }
        }
        // Resize / decrease the width, remove vertical seams.
        else if (mode.equals("height")) {
            while (this.height() > dimension) {
                System.out.println("Resizing...currently at height " + this.height());
                int[] seam = this.findHorizontalSeam();
                this.removeHorizontalSeam(seam);
            }
        }
        // Return resized image.
        return this.picture();
    }

    // ONLY FOR TESTING
    public Picture showSeamLine(String mode, int dimension) {
        // Show vertical seam line
        if (mode.equals("width")) {
            while (this.width() > dimension) {
                System.out.println("Plotting...currently at width " + this.width());
                int[] seam = this.findVerticalSeam();
                this.showVerticalSeam(seam);
            }
        }
        // Show horizontal seam line
        else if (mode.equals("height")) {
            while (this.height() > dimension) {
                System.out.println("Plotting...currently at height " + this.height());
                int[] seam = this.findHorizontalSeam();
                this.showHorizontalSeam(seam);
            }
        }
        // Return seam-lined image.
        return this.picture();
    }

    public static void main(String[] args) {

    }
}
