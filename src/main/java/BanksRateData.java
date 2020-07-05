import java.util.HashSet;

public class BanksRateData {

    private HashSet<BankRateData> rates;
    private int nominal;
    private String header;

    public BanksRateData(HashSet<BankRateData> rates, int nominal, String header){
        this.rates = rates;
        this.nominal = nominal;
        this.header = header;
    }

    public int getNominal() {
        return nominal;
    }

    public HashSet<BankRateData> getRates() {
        return rates;
    }

    public String getHeader() {
        return header;
    }
}
