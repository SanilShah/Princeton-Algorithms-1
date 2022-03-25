// SeamCarverClient.java resizes a user-inputted image.
// The user can resize the image either by width or height.

// Compile SeamCarverClient.java after compiling SeamCarver.java
// Compile SeamCarverClient.java with $ javac -d . SeamCarverClient.java

// After SeamCarver.java and SeamCarverClient.java are compiled, Client
// can be run with $ java client

import edu.princeton.cs.algs4.Picture;

import java.util.Scanner;


public class SeamCarverClient {

    static Scanner scanner = new Scanner(System.in);

    // Make it true FOR TESTING, showing seam lines instead of resizing.
    private static boolean testingMode = false;

    public static String mode(String input)
    {
        input = input.toLowerCase();

        if (input.equals("(h)") || input.equals("h") || input.equals("(h)eight") ||
                input.equals("height"))
        {
            return "height";
        }

        if (input.equals("(w)") || input.equals("w") || input.equals("(w)idth") ||
                input.equals("width"))
        {
            return "width";
        }

        throw new IllegalArgumentException("Invalid argument, choose either " +
                                                   "height or width");
    }


    public static void resizeWidth(SeamCarver seamCarver, Picture inputImg)
    {
        System.out.println("Enter new width:");
        int input = Integer.parseInt(scanner.nextLine());

        if (input > inputImg.width())
            throw new IllegalArgumentException("Width greater than original width.");

        // Resize image to the user-inputted width.
        Picture pic;
        if (!testingMode)
            pic = seamCarver.resizeTo("width", input);
        else
            pic = seamCarver.showSeamLine("width", input);

        // Display the image.
        pic.show();
    }


    public static void resizeHeight(SeamCarver seamCarver, Picture inputImg)
    {
        System.out.println("Enter new height:");
        int input = Integer.parseInt(scanner.nextLine());

        if (input > inputImg.height())
            throw new IllegalArgumentException("Height greater than original height.");

        // Resize image to the user-inputted width.
        Picture pic;
        if (!testingMode)
            pic = seamCarver.resizeTo("height", input);
        else
            pic = seamCarver.showSeamLine("height", input);
        // Display the image.
        pic.show();
    }


    public static void main(String args[])
    {
        System.out.println("Are you decreasing the picture's (h)eight or (w)idth?");
        System.out.println("Choose one:");
        String input = scanner.nextLine();
        String mode = mode(input);

        System.out.println();

        System.out.println("Enter the the image to resize:");
        input = scanner.nextLine();
        Picture inputImg = new Picture(input);
        SeamCarver seamCarver = new SeamCarver(inputImg);

        System.out.println(input + " Width: " + inputImg.width() + " Height: " + inputImg.height());
        inputImg.show();
        if (mode.equals("width")) {
            resizeWidth(seamCarver, inputImg);
        }

        if (mode.equals("height")) {
            resizeHeight(seamCarver, inputImg);
        }
    }
}