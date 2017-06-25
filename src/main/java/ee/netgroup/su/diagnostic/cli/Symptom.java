package ee.netgroup.su.diagnostic.cli;

public class Symptom implements Comparable<Symptom> {

    private String symptomName;
    private Integer inHowManyDiseases;



    public Symptom(String symptomName) {
        this.symptomName = symptomName;
        this.inHowManyDiseases = 0;
    }

    public Symptom(String symptomName, Integer inHowManyDiseases) {
        this.symptomName = symptomName;
        this.inHowManyDiseases = inHowManyDiseases;
    }

    public Integer getInHowManyDiseases() {
        return inHowManyDiseases;
    }

    public void setInHowManyDiseases(Integer inHowManyDiseases) {
        this.inHowManyDiseases = inHowManyDiseases;
    }

    @Override
    public String toString() {
        return symptomName;
    }

    @Override
    public int compareTo(Symptom o) {
        if (this.inHowManyDiseases < o.inHowManyDiseases)
            return 1;
        if (this.inHowManyDiseases > o.inHowManyDiseases)
            return -1;
        return this.symptomName.compareTo(o.symptomName);

    }
}
