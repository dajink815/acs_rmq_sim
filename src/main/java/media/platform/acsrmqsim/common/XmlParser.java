package media.platform.acsrmqsim.common;

import media.platform.acsrmqsim.AppInstance;
import media.platform.acsrmqsim.config.Config;
import media.platform.acsrmqsim.json.HeaderField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dajin kim
 */
public class XmlParser {
    private static final Logger log = LoggerFactory.getLogger(XmlParser.class);
    private final AppInstance instance = AppInstance.getInstance();
    private final Config config = instance.getConfig();
    private static final String HEADER = "/msg/header/name";
    private static final String BODY = "/msg/body/name";
    private static final String KEYWORD = "/msg/sessionId";

    public XmlParser() {
        // Do Nothing
    }

    public void xmlParsing() {
        readXmlFile(HEADER);
        readXmlFile(BODY);
        readXmlFile(KEYWORD);
    }

    private void readXmlFile(String xPath) {
        try {
            InputSource is = new InputSource(new FileReader(config.getXmlPath()));
            //InputSource is = new InputSource(new FileReader("/Users/kimdajin/Simulator/acsrmqsim/src/main/resources/xml/jsonField.xml"));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            XPath xpath = XPathFactory.newInstance().newXPath();

            NodeList nodeList = (NodeList) xpath.compile(xPath).evaluate(document, XPathConstants.NODESET);
            List<String> valueList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++)
                valueList.add(nodeList.item(i).getTextContent());

            if (HEADER.equalsIgnoreCase(xPath)) {
                List<HeaderField> headerFields = new ArrayList<>();
                valueList.forEach(headerKey -> headerFields.add(HeaderField.getTypeEnum(headerKey)));
                instance.setHeaderList(headerFields);
            } else if (KEYWORD.equalsIgnoreCase(xPath)) {
                instance.setKeyWord(valueList.get(0));
                //log.info("({}) - valueList ({})", KEYWORD, valueList);
                log.info("XmlParser - KeyWord is {}", instance.getKeyWord());
/*                for (String keyWord : valueList) {
                    instance.addKeyWord(keyWord);
                }*/
                instance.setKeyWordList(valueList);
                log.info("KeyWordList : {}", instance.getKeyWordList());
            } else {
                instance.setBodyList(valueList);
            }

        } catch (Exception e) {
            log.error("XmlParser.readXmlFile.Exception ", e);
        }
    }
}
