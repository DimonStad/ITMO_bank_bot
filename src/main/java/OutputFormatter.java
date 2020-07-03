import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OutputFormatter {

    private XmlParser xmlParser;

    public OutputFormatter(){
        xmlParser = new XmlParser();
    }

    public String formatXmlOutput(String date) throws IOException {
        HashMap<String, RateData> rateData = xmlParser.parseXml(date);
        String output = "";
        for (Map.Entry<String, RateData> rates: rateData.entrySet()){
            output = output + rates.getValue().nominal + " " + rates.getKey() +
                    " = " + rates.getValue().val + " RUB\n";
        }
        return output;
    }
}
