package ee.netgroup.su.diagnostic.cli;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Disease {

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
}
