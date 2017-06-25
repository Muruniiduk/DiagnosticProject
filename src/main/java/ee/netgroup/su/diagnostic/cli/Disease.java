package ee.netgroup.su.diagnostic.cli;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Disease implements Comparable<Disease> {

    private String diseaseName;
    private Set<Symptom> symptoms;


    public Disease(String diseaseName, String[] symptomsString) {
        this.diseaseName = diseaseName;
        this.symptoms = new HashSet<>();
        for (String symptom: symptomsString)
            this.symptoms.add(new Symptom(symptom));
    }

    public Disease(String diseaseName) {
        this.diseaseName = diseaseName;
        this.symptoms = new HashSet<>();
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public Set<Symptom> getSymptoms() {
        return symptoms;
    }

    public void addSymptom(Symptom symptom) {
        symptoms.add(symptom);
    }

    public String toStringWithSymptoms() {
        String symptomsAsString = "";
        for(Symptom symptom: symptoms)
            symptomsAsString = symptomsAsString.concat(symptom.toString() + ", ");
        symptomsAsString = symptomsAsString.substring(0,symptomsAsString.length()-2);
        return "Disease: " + diseaseName
                + "; symptoms: " +  symptomsAsString;
    }


    @Override
    public String toString() {
        return diseaseName;
    }

    @Override
    public int compareTo(Disease o){
        if(this.symptoms.size() == o.symptoms.size())
            return this.diseaseName.compareTo(o.diseaseName);
        if(this.symptoms.size() < o.symptoms.size())
            return 1;
        return -1;
    }

}
