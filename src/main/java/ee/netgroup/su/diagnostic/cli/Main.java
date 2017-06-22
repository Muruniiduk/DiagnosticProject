package ee.netgroup.su.diagnostic.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

            List<Disease> diseases = new ArrayList<>(); //TODO: check if list suits
            while (true) {
                final String textLine = bufferedReader.readLine();
                if (textLine == null)
                    break;

                linesCount++;
                // TODO: line parsing logic...
                int indexOfFfirstComma = textLine.indexOf(',');
                String diseaseName = textLine.substring(0,indexOfFfirstComma);
                String[] symptoms = textLine.substring(indexOfFfirstComma+1).trim().split(",");
                diseases.add(new Disease(diseaseName, symptoms));

            }

            Collections.sort(diseases);
            for(Disease disease: diseases){
                System.out.println(disease.toString());
            }
            System.out.println("\n---------");
            System.out.println("Input file contains " + linesCount + " lines.");
        }
    }

}
