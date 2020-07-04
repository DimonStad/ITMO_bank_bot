import java.util.HashSet;

public class BanksRateData {

    private HashSet<BankRateData> rates;
    private int nominal;

    public BanksRateData(HashSet<BankRateData> rates, int nominal){
        this.rates = rates;
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

    public HashSet<BankRateData> getRates() {
        return rates;
    }
}
