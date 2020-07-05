import org.junit.Test;
import org.junit.Assert;
import sun.tools.jar.Main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Tests {

    @Test
    public void XmlParserTest() throws IOException {
        OutputFormatter outputFormatter = new OutputFormatter();
        String expected = "100 JPY = 60.6066 RUB\n" +
                "1 EUR = 75.5841 RUB\n" +
                "1 GBP = 85.9118 RUB\n" +
                "1 USD = 66.3309 RUB\n" +
                "10 CNY = 97.9734 RUB\n";

        String actual = outputFormatter.formatXmlOutput("21/01/2019");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void SiteParserTest() throws IOException {
        SiteParser siteParser = new SiteParser();
        HashMap<String, BanksRateData> parsed = siteParser.parseSite("kemerovo");
        String actual = "";
        for (Map.Entry<String, BanksRateData> rates: parsed.entrySet()){
            actual = actual + rates.getValue().getHeader() + "\n";
        }

        String expected = "Курс йены в банках Кемерово\n" +
                "Курс евро в банках Кемерово на сегодня\n" +
                "Курс фунта стерлингов в банках Кемерово\n" +
                "Курс доллара в Кемерово\n" +
                "Курс юаня в банках Кемерово\n";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void SiteParserValidTest() throws IOException {
        SiteParser siteParser = new SiteParser();
        HashMap<String, BanksRateData> parsed = siteParser.parseSite("kemerovo");
        for (Map.Entry<String, BanksRateData> rates: parsed.entrySet()){
            Iterator<BankRateData> iterator = rates.getValue().getRates().iterator();
            while (iterator.hasNext()){
                BankRateData rateData = iterator.next();
                Assert.assertTrue(rateData.getBuy() != rateData.getSell());
            }
        }
    }
}
