import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.plugin2.message.JavaScriptBaseMessage;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class SiteParser {

    private static final String[] charCodes = {"usd", "eur", "gbp", "cny", "jpy"};
    private static final String site = ".bankiros.ru/currency/";

    private static final String nominalSelector = "#w0 > table > thead > tr:nth-child(1) > th.td-center.hid";
    private static final String ratesSelector = "#w0 > table > tbody > tr";
    private static final String bankNameSelector = "td:nth-child(1) > a";
    private static final String bankBuySelector = "td:nth-child(2) > span";
    private static final String bankSellSelector = "td:nth-child(3) > span";

    public static HashMap<String, BanksRateData> parseSite(String city) throws IOException {
        HashMap<String, BanksRateData> rates = new HashMap<String, BanksRateData>();
        for (int i = 0; i < charCodes.length; i++){
            rates.put(charCodes[i], getRate(charCodes[i], city));
        }
        return rates;
    }

    private static BanksRateData getRate(String charCode, String city) throws IOException {
        Document htmlSite = Jsoup.connect("https://" + city + site + charCode).get();
        int nominal = Integer.parseInt(htmlSite.select(nominalSelector).text().split(" ")[0]);

        HashSet<BankRateData> bankRateData = new HashSet<BankRateData>();
        Elements rates = htmlSite.select(ratesSelector);
        int count = 0;
        for (int i = 0; i < rates.size(); i++){
            if(count == 5)break;
            Element row = rates.get(i);
            if(row.attr("data-target").isEmpty()) continue;

            String name = row.select(bankNameSelector).text();
            double buy = Double.parseDouble(row.select(bankBuySelector).text());
            double sell = Double.parseDouble(row.select(bankSellSelector).text());

            bankRateData.add(new BankRateData(buy, sell, name));
            count++;
        }
        return new BanksRateData(bankRateData, nominal);
    }
}
