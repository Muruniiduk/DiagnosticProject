package ee.netgroup.su.diagnostic.cli;

import com.sun.tools.javac.code.Symtab;
import sun.jvm.hotspot.debugger.cdbg.Sym;

import java.io.*;
import java.util.*;

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

            ArrayList<Disease> diseaseList = new ArrayList<>();
            ArrayList<Symptom> symptomList = new ArrayList<>();

            while (true) {
                final String textLine = bufferedReader.readLine();
                if (textLine == null)
                    break;

                addDiseaseAndSymptomsToLists(diseaseList, symptomList, textLine);
            }

            Collections.sort(diseaseList);
            Collections.sort(symptomList);
            System.out.println("Three diseases with the most symptoms:");
            for(int i = 0; i < 3; i++)
                System.out.println(diseaseList.get(i).toString());
            System.out.println("\n------------------------------------");
            System.out.println("There are " + symptomList.size() + " unique symptoms in the database. ");
            System.out.println("\n------------------------------------");
            System.out.println("Three most frequent symptoms:");
            for(int i = 0; i < 3; i++)
                System.out.println(symptomList.get(i).toString());
            System.out.println("\n------------------------------------");

            System.out.println("Enter symptoms to get list of possible diseases (s)");
            System.out.println("Let the program find the disease through yes/no questions (d)");
            BufferedReader cmdLineReader = new BufferedReader(new InputStreamReader(System.in));

            final String choice = cmdLineReader.readLine();
            if (choice.equals("s")) {
                System.out.println("Continue to write symptoms. Press enter to finish.");
                printFilteredDiseases(cmdLineReader, diseaseList, symptomList);
            }
            else if (choice.equals("d"))
                System.out.println("Answer if patient has above mentioned symptoms with a 'yes' (y) or 'no' (n)");
                diagnose(cmdLineReader, diseaseList, symptomList);

        }

    }

    /* The average quickest way to the correct answer through yes/no questions is by dividing the
        set into two subsets that are as equal in size as possible. This strategy is looked
        more closely in the following example: http://www.math.cornell.edu/~mec/Summer2009/Leung/puzzles_p1.htm
    */

    private static void diagnose(BufferedReader cmdLineReader, ArrayList<Disease> diseaseList, ArrayList<Symptom> symptomList) throws IOException {

        ArrayList<Disease> clonedDiseaseList = (ArrayList<Disease>) diseaseList.clone();
        Map<Symptom, Integer> symptomFreq = new HashMap<>();

        for (Symptom symptom: symptomList) {
            symptomFreq.put(symptom, symptom.getInHowManyDiseases());
        }

        while (true) {
            if (clonedDiseaseList.size() == 1) {
                System.out.println("Patient has been diagnosed with the following disease: " + clonedDiseaseList.get(0).toString());
                break;
            }
            if (clonedDiseaseList.size() == 0)
                throw new RuntimeException("Something wrong with the diagnosing algorithm or answers.");
            Symptom symptomThatHalvesPossibleDiseases = findSymptomThatHalvesPossibleDiseases(clonedDiseaseList, symptomFreq);
            System.out.println("Does the patient have the following symptom? " + symptomThatHalvesPossibleDiseases.toString());
            final String cmdLine = cmdLineReader.readLine();
            if (!(cmdLine.equals("y") || cmdLine.equals("n")))
                System.out.println("Answer only with a 'yes' (y) or 'no' (n)");
            else if (cmdLine.equals("")) { //probably unnecessary
                cmdLineReader.close();
                break;
            }
            else
                symptomFreq = filterOutDiseasesThatMatch(symptomFreq, clonedDiseaseList, symptomThatHalvesPossibleDiseases, cmdLine.equals("y"));
        }
    }

    private static Map<Symptom, Integer> filterOutDiseasesThatMatch(Map<Symptom, Integer> symptomFreq, ArrayList<Disease> clonedDiseaseList,
                                                   Symptom symptomThatHalvesPossibleDiseases,  boolean answeredYes) { //TODO: cant be changed to local because the variable needs to change?
        List<Disease> diseasesToRemove = new ArrayList<>();
        for (Disease disease: clonedDiseaseList){
            if (!disease.getSymptoms().contains(symptomThatHalvesPossibleDiseases) && answeredYes)
                //clonedDiseaseList.remove(disease);
                diseasesToRemove.add(disease);
            if (disease.getSymptoms().contains(symptomThatHalvesPossibleDiseases) && !answeredYes)
                //clonedDiseaseList.remove(disease);
                diseasesToRemove.add(disease);
        }

        for(Disease disease: diseasesToRemove) clonedDiseaseList.remove(disease);

        symptomFreq = new HashMap<>();
        ArrayList<String> newSymptomList = new ArrayList<>();
        for(Disease disease: clonedDiseaseList) {
            for (Symptom symptom : disease.getSymptoms()) {
                if (!symptomFreq.keySet().contains(symptom))
                    symptomFreq.put(symptom, 1);
                else symptomFreq.put(symptom, symptomFreq.get(symptom) + 1);
            }
        }

        return symptomFreq;
    }

    private static Symptom findSymptomThatHalvesPossibleDiseases(ArrayList<Disease> clonedDiseaseList, Map<Symptom, Integer> symptomFreq) {

        int diseaseListLength = clonedDiseaseList.size();
        int timesTried = 0;
        while (true) {

            for (Symptom symptom : symptomFreq.keySet()) {
                if (symptomFreq.get(symptom) + timesTried == Math.floor(((double) diseaseListLength) / 2))
                    return symptom;
                else if (symptomFreq.get(symptom) - timesTried == Math.floor(((double) diseaseListLength) / 2))
                    return symptom;
            }
            timesTried++;
            if (timesTried > 10) break;
        }
        return null;
    }


    private static void printFilteredDiseases(BufferedReader cmdLineReader, ArrayList<Disease> diseaseList, ArrayList<Symptom> symptomList) throws IOException {
        ArrayList<Disease> clonedDiseaseList = (ArrayList<Disease>) diseaseList.clone();
        while(true) {
            final String cmdLine = cmdLineReader.readLine();
            if (cmdLine.equals("")){
                cmdLineReader.close();
                break;
            }
            for (Symptom symptom: symptomList) {
                if (symptom.toString().equals(cmdLine)) {
                    for(Disease disease: diseaseList)
                        if(!disease.getSymptoms().contains(symptom))
                            clonedDiseaseList.remove(disease);
                    break;
                }
            }
        }
        System.out.println("Possible diseases:");
        for(Disease disease: clonedDiseaseList)
            System.out.println(disease.toString());
    }

    private static void addDiseaseAndSymptomsToLists(ArrayList<Disease> diseaseList, ArrayList<Symptom> symptomList, String textLine) {
        int indexOfFirstComma = textLine.indexOf(',');
        Disease disease = new Disease(textLine.substring(0,indexOfFirstComma));
        String[] symptomsAsArray = textLine.substring(indexOfFirstComma+1).trim().split(", ");
        List<String> symptoms = Arrays.asList(symptomsAsArray);
        countSymptomFreq(symptomList, disease, symptoms);
        diseaseList.add(disease);

    }

    private static void countSymptomFreq(ArrayList<Symptom> symptomList, Disease disease, List<String> symptoms) {
        for(String symptomString: symptoms) {
            boolean symptomExistsInList = false;
            for (Symptom symptomObj : symptomList) {
                if (symptomObj.toString().equals(symptomString)) {
                    symptomObj.setInHowManyDiseases(symptomObj.getInHowManyDiseases() + 1);
                    disease.addSymptom(symptomObj);
                    symptomExistsInList = true;
                }
            }
            if(!symptomExistsInList){
                Symptom symptomObjNew = new Symptom(symptomString, 1);
                symptomList.add(symptomObjNew);
                disease.addSymptom(symptomObjNew);
            }

        }
    }

}
