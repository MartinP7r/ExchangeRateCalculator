package exchangeRateApp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.RowSetDynaClass;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class SQLiteTest.
 */
public class SQLiteTest {

	// private List<EuroExchangeRate> rates;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SAXException
	 *             the SAX exception
	 */
	public static void main(String args[]) throws MalformedURLException,
			ParserConfigurationException, IOException, SAXException {
		SQLiteTest sqlTest = new SQLiteTest();

		sqlTest.createTable();

		List<EuroExchangeRate> ratesList = new EuroExchangeRate().getList();
		/*
		 * String dateString = "2014-05-26"; SimpleDateFormat formatter = new
		 * SimpleDateFormat("yyyy-MM-dd"); java.util.Date currentDate;
		 * currentDate = null; try { currentDate = formatter.parse(test.time); }
		 * catch (ParseException e) { e.printStackTrace(); } java.sql.Date
		 * testDateSql = new java.sql.Date(currentDate.getTime());
		 */
		
		// if date in XML feed is younger/higher than date in DB then do upsert
		EuroExchangeRate currentEER = ratesList.get(0); // first set in EER list
														// (= USD)
		Date dbTime = sqlTest.checkTime(); // date in DB for USD
		if (currentEER.time.compareTo(dbTime) > 0) {
			sqlTest.upsertRates(ratesList);
		}

		/*
		 * System.out.println(rsTest.getDynaProperties()+"\n"); List<DynaBean>
		 * rsTestRows = rsTest.getRows(); for (DynaBean dc : rsTestRows) {
		 * System.out.println(dc.getName());
		 * System.out.println(dc.getDynaProperties());
		 * System.out.println(dc.toString()); System.out.println(dc.get(null));
		 * System.out.println(dc.getDynaClass()); }
		 * System.out.println(rsTestRows);
		 */
	}

	/**
	 * Creates the table.
	 */
	public void createTable() {
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC"); // loads the driver for JDBC
			c = DriverManager.getConnection("jdbc:sqlite:exchangeRates.db"); // connect

			stmt = c.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS rates "
					+ "(currency_code 		VARCHAR(3)		PRIMARY KEY UNIQUE,"
					+ " currency_name		VARCHAR(40)		, "
					+ " rate				VARCHAR(16)		NOT NULL, "
					+ " time        		DATE)";
			stmt.executeUpdate(sql);

			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Insert rates.
	 *
	 * @param ratesList
	 *            the rates list
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SAXException
	 *             the SAX exception
	 */
	public void upsertRates(List<EuroExchangeRate> ratesList)
			throws MalformedURLException, ParserConfigurationException,
			IOException, SAXException {
		Connection c = null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;

		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:exchangeRates.db");
			c.setAutoCommit(false);

			// get ArrayList with all exchangeRates

			stmt1 = c
					.prepareStatement("INSERT OR IGNORE INTO rates VALUES (?, ? , ?, ?);");
			stmt2 = c
					.prepareStatement("UPDATE rates SET rate = ?, time = ? WHERE currency_code = ?;");

			for (EuroExchangeRate rate : ratesList) {
				// put country later as a separate one IF a new one got inserted
				stmt1.setString(1, rate.currencyCode);
				// convert currency code to full name of currency
				try {
					Currency curr = Currency.getInstance(rate.currencyCode);
					stmt1.setString(2, curr.getDisplayName());
				} catch (NullPointerException e) {
					stmt1.setString(2, "N/A");
				}
				stmt1.setString(3, rate.rate);
				stmt1.setDate(4, rate.time);
				stmt1.addBatch();
				stmt2.setString(1, rate.rate);
				stmt2.setDate(2, rate.time);
				stmt2.setString(3, rate.currencyCode);
				stmt2.addBatch();
			}
			stmt1.executeBatch();
			stmt2.executeBatch();
			stmt1.close();
			// stmt2.close();
			c.commit();
			c.close();

		} catch (SQLException | ClassNotFoundException e) {
			// TODO: handle exception
			// System.err.println( e.getClass().getName() + ": " +
			// e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}

	}

	public void delsertRate() {
		
	}

	/**
	 * Check time of last USD rate update.
	 *
	 * @return the date
	 */
	public Date checkTime() {
		Date date = null;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC"); // loads the driver for JDBC
			c = DriverManager.getConnection("jdbc:sqlite:exchangeRates.db"); // connect

			stmt = c.createStatement();
			// DATEADD(day, DATEDIFF(day, 0, MyDateColumn), 0)
			// older date: 1401141600000
			ResultSet rs = stmt
					.executeQuery("SELECT time FROM rates WHERE currency_code = 'USD';");
			while (rs.next()) {
				date = rs.getDate("time");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return date;
	}

	/**
	 * Gets the rates.
	 *
	 * @return the rates
	 */
	public List<EuroExchangeRate> getRates() {
		List<EuroExchangeRate> rates = new ArrayList<>();
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC"); // loads the driver for JDBC
			c = DriverManager.getConnection("jdbc:sqlite:exchangeRates.db"); // connect

			stmt = c.createStatement();
			// DATEADD(day, DATEDIFF(day, 0, MyDateColumn), 0)
			// older date: 1401141600000
			ResultSet rs = stmt.executeQuery("SELECT * FROM rates;");
			// Array rsArr01 = rs.getArray(0);
			// System.out.println(rsArr01.toString());
			while (rs.next()) {
				EuroExchangeRate r = new EuroExchangeRate();
				r.currencyCode = rs.getString("currency_code");
				r.currencyName = rs.getString("currency_name");
				r.rate = rs.getString("rate");
				r.time = rs.getDate("time");
				rates.add(r); // nullpointerexception
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return rates;
	}

	public RowSetDynaClass getDyna() {
		RowSetDynaClass rsdc = null;
		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC"); // loads the driver for JDBC
			c = DriverManager.getConnection("jdbc:sqlite:exchangeRates.db"); // connect

			stmt = c.createStatement();
			// DATEADD(day, DATEDIFF(day, 0, MyDateColumn), 0)
			// older date: 1401141600000
			ResultSet rs = stmt.executeQuery("SELECT * FROM rates;");
			// Array rsArr01 = rs.getArray(0);
			// System.out.println(rsArr01.toString());
			rsdc = new RowSetDynaClass(rs);
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return rsdc;
	}

}