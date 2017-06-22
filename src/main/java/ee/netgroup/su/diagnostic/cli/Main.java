package ee.netgroup.su.diagnostic.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static File getInputFile(final String[] arguments) {
        if (arguments.length < 1)
            throw new IllegalArgumentException("No input file given at commandline.");

        final File inputFile = new File(arguments[0]);

        if (!inputFile.exists())
            throw new IllegalArgumentException("Specified input file does not exist: " + inputFile);

        if (!inputFile.canRead())
            throw new IllegalArgumentException("Specified input file is not readable: " + inputFile);

        return inputFile;
    }

    /**
     * Starting point for our application.
     */
    public static void main(final String[] arguments) throws IOException {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getInputFile(arguments)))){
            int linesCount = 0;
            while (true) {
                final String textLine = bufferedReader.readLine();
                if (textLine == null)
                    break;

                linesCount++;
                // TODO: line parsing logic...
                System.out.println(textLine);
            }

            System.out.println("Input file contains " + linesCount + " lines.");
        }
    }

}
