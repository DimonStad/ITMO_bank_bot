import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class XmlParser {

    private final String[] charCodes = {"USD", "EUR", "GBP", "CNY", "JPY"};
    private final String pattern = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    public HashMap<String, RateData> parseXml(String date) throws IOException {
        Document document = Jsoup.connect(pattern + date).get();
        Elements valutes = document.select("Valute");

        HashMap<String, RateData> rates = new HashMap<String, RateData>();
        for (int i = 0; i < valutes.size(); i++){
            Element node = valutes.get(i);
            String name = node.select("CharCode").text();
            if(!Arrays.asList(charCodes).contains(name)) continue;

            int nominal = Integer.parseInt(node.select("Nominal").text());
            double value = Double.valueOf(node.select("Value").text().replace(',','.'));
            rates.put(name, new RateData(nominal, value));
        }

        return rates;
    }
}
