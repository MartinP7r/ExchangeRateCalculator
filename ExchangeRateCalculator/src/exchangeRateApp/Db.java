package exchangeRateApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// TODO: Auto-generated Javadoc
/**
 *  Database connection class & utilities *.
 */

abstract class Db {

	/** The s driver. */
	public String sDriver = "";
	
	/** The s url. */
	public String sUrl = null;
	
	/** The i timeout. */
	public int iTimeout = 30;
	
	/** The conn. */
	public Connection conn = null;
	
	/** The statement. */
	public Statement statement = null;

	/*
	 * Stub constructor for quick instantiation o/t fly for using some of the
	 * ancillary stuff
	 */
	/**
	 * Instantiates a new db.
	 */
	public Db() {
	}

	/*
	 * quick and dirty constructor to test the database passing the
	 * DriverManager name and the fully loaded url to handle
	 */
	/*
	 * NB this will typically be available if you make this class concrete and
	 * not abstract
	 */
	/**
	 * Instantiates a new db.
	 *
	 * @param sDriverToLoad the s driver to load
	 * @param sUrlToLoad the s url to load
	 * @throws Exception the exception
	 */
	public Db(String sDriverToLoad, String sUrlToLoad) throws Exception {
		init(sDriverToLoad, sUrlToLoad);
	}

	/**
	 * Inits the.
	 *
	 * @param sDriverVar the s driver var
	 * @param sUrlVar the s url var
	 * @throws Exception the exception
	 */
	public void init(String sDriverVar, String sUrlVar) throws Exception {
		setDriver(sDriverVar);
		setUrl(sUrlVar);
		setConnection();
		setStatement();
	}

	/**
	 * Sets the driver.
	 *
	 * @param sDriverVar the new driver
	 */
	private void setDriver(String sDriverVar) {
		sDriver = sDriverVar;
	}

	/**
	 * Sets the url.
	 *
	 * @param sUrlVar the new url
	 */
	private void setUrl(String sUrlVar) {
		sUrl = sUrlVar;
	}

	/**
	 * Sets the connection.
	 *
	 * @throws Exception the exception
	 */
	public void setConnection() throws Exception {
		Class.forName(sDriver);
		conn = DriverManager.getConnection(sUrl);
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return conn;
	}

	/**
	 * Sets the statement.
	 *
	 * @throws Exception the exception
	 */
	public void setStatement() throws Exception {
		if (conn == null) {
			setConnection();
		}
		statement = conn.createStatement();
		statement.setQueryTimeout(iTimeout); // set timeout to 30 sec.
	}

	/**
	 * Gets the statement.
	 *
	 * @return the statement
	 */
	public Statement getStatement() {
		return statement;
	}

	/**
	 * Execute stmt.
	 *
	 * @param instruction the instruction
	 * @throws SQLException the SQL exception
	 */
	public void executeStmt(String instruction) throws SQLException {
		statement.executeUpdate(instruction);
	}

	// processes an array of instructions e.g. a set of SQL command strings
	// passed from a file
	// NB you should ensure you either handle empty lines in files by either
	// removing them or parsing them out
	// since they will generate spurious SQLExceptions when they are encountered
	// during the iteration....
	/**
	 * Execute stmt.
	 *
	 * @param instructionSet the instruction set
	 * @throws SQLException the SQL exception
	 */
	public void executeStmt(String[] instructionSet) throws SQLException {
		for (int i = 0; i < instructionSet.length; i++) {
			executeStmt(instructionSet[i]);
		}
	}

	/**
	 * Execute qry.
	 *
	 * @param instruction the instruction
	 * @return the result set
	 * @throws SQLException the SQL exception
	 */
	public ResultSet executeQry(String instruction) throws SQLException {
		return statement.executeQuery(instruction);
	}

	/**
	 * Close connection.
	 */
	public void closeConnection() {
		try {
			conn.close();
		} catch (Exception ignore) {
		}
	}

}
