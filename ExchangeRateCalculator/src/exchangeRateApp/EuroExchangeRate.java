package exchangeRateApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class EuroExchangeRate.
 */
class EuroExchangeRate {

	/** The time. */
	Date time;

	/** The currency. */
	String currencyCode;

	String currencyName;

	/** The rate. */
	String rate;

	public Boolean equals(EuroExchangeRate obj) {
		return this.currencyCode.equals(obj.currencyCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "EUR to " + currencyCode + ": " + rate + " (date: " + time + ")";
	}

	// public Date getTime() {
	// return time;
	// }
	//
	// public String getCurrency() {
	// return currency;
	// }
	//
	// public String getRate() {
	// return rate;
	// }
	
	/**
	 * Gets the list.
	 *
	 * @return List of EuroExchangerate Objects from XML source at
	 *         http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SAXException
	 *             the SAX exception
	 */
	public List<EuroExchangeRate> getList() {
		List<EuroExchangeRate> ratesList = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}

		InputStream input = null;
		try {
			input = new URL(
					"http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml")
					.openStream();
		} catch (IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(input);
		} catch (SAXException | IOException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}

		NodeList nodeList = document.getDocumentElement().getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			if (nodeList.item(i).getNodeName() == "Cube") {
				NodeList upperNodes = nodeList.item(i).getChildNodes();

				for (int k = 0; k < upperNodes.getLength(); k++) {
					Node upperNode = upperNodes.item(k);
					if (upperNode instanceof Element) {
						String updateTime = upperNode.getAttributes()
								.getNamedItem("time").getNodeValue();
						// convert String value to sql date format
						Date sqlTime = Date.valueOf(updateTime);
						NodeList childNodes = upperNode.getChildNodes();
						for (int j = 0; j < childNodes.getLength(); j++) {
							Node node = childNodes.item(j);
							if (node instanceof Element) {
								EuroExchangeRate exRate = new EuroExchangeRate();
								exRate.time = sqlTime;
								exRate.currencyCode = node.getAttributes()
										.getNamedItem("currency")
										.getNodeValue();
								// casting String to Decimal for exchange rate
								// value; apparently not supported by SQLite...
								// BigDecimal tmpRate = new
								// BigDecimal(node.getAttributes().getNamedItem("rate").getNodeValue());
								// exRate.rate = tmpRate;
								exRate.rate = node.getAttributes()
										.getNamedItem("rate").getNodeValue();
								ratesList.add(exRate);
							}
						}
					}
				}
			}

		}
		return ratesList;
	}

}