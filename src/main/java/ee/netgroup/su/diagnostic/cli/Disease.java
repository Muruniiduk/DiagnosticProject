package ee.netgroup.su.diagnostic.cli;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Disease implements Comparable<Disease> {

    private String diseaseName;
    private Set<String> symptoms;


    public Disease(String diseaseName, String[] symptoms) {
        this.diseaseName = diseaseName;
        this.symptoms = new HashSet<>(Arrays.asList(symptoms));
    }


    public String getDiseaseName() {
        return diseaseName;
    }

    public Set<String> getSymptoms() {
        return symptoms;
    }


    @Override
    public String toString() {
        return "Disease: " + diseaseName
                + ", symptoms: " +  symptoms;
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
