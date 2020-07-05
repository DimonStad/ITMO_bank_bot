public class RateData {

    private int nominal;
    private double val;

    public RateData(int nominal, double val) {
        this.nominal = nominal;
        this.val = val;
    }

    public int getNominal() {
        return nominal;
    }

    public double getVal() {
        return val;
    }
}
