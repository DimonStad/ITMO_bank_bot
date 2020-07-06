import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OutputFormatter {

    private XmlParser xmlParser;
    private SiteParser siteParser;

    public OutputFormatter(){
        xmlParser = new XmlParser();
        siteParser = new SiteParser();
    }

    public String formatXmlOutput(String date) throws IOException {
        HashMap<String, RateData> rateData = xmlParser.parseXml(date);
        String output = "";
        for (Map.Entry<String, RateData> rates: rateData.entrySet()){
            output = output + rates.getValue().getNominal() + " " + rates.getKey() +
                    " = " + rates.getValue().getVal() + " RUB\n";
        }
        return output;
    }

    public String formatSiteOutput(String city) throws IOException {
        HashMap<String, BanksRateData> siteOutput = siteParser.parseSite(city);
        String output = "";
        for (Map.Entry<String, BanksRateData> rates: siteOutput.entrySet()){
            String header = rates.getValue().getHeader() + "\n";
            output = output + header.toUpperCase() + "\n";

            Iterator<BankRateData> iterator = rates.getValue().getRates().iterator();

            int nominal = rates.getValue().getNominal();
            String charCode = rates.getKey();

            while (iterator.hasNext()){
                BankRateData rateData = iterator.next();

                String bankName = rateData.getBankName();
                double buy = rateData.getBuy();
                double sell = rateData.getSell();


                String bankInfo = bankName + "\n";
                String buyInfo = "Покупка " + nominal + " " + charCode + " : " + buy + "\n";
                String sellInfo = "Продажа " + nominal + " " + charCode + " : " + sell + "\n";
                output = output + bankInfo + buyInfo + sellInfo + "\n";
            }
        }
        return output;
    }

    public String formatSiteCurrencyOutput(String city, String charCode) throws IOException {
        BanksRateData rates = siteParser.getRate(charCode, city);
        String output = "";

        String header = rates.getHeader() + "\n";
        output = output + header.toUpperCase() + "\n";

        Iterator<BankRateData> iterator = rates.getRates().iterator();

        int nominal = rates.getNominal();

        while (iterator.hasNext()){
            BankRateData rateData = iterator.next();

            String bankName = rateData.getBankName();
            double buy = rateData.getBuy();
            double sell = rateData.getSell();


            String bankInfo = bankName + "\n";
            String buyInfo = "Покупка " + nominal + " " + charCode + " : " + buy + "\n";
            String sellInfo = "Продажа " + nominal + " " + charCode + " : " + sell + "\n";
            output = output + bankInfo + buyInfo + sellInfo + "\n";
        }
        return output;
    }
}
