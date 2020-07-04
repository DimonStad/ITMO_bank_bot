public class BankRateData {

    private double buy;
    private double sell;
    private String bankName;

    public BankRateData(double buy, double sell, String bankName){
        this.buy = buy;
        this.sell = sell;
        this.bankName = bankName;
    }

    public double getBuy() {
        return buy;
    }

    public double getSell() {
        return sell;
    }

    public String getBankName() {
        return bankName;
    }
}
