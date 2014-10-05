package il.co.topq.integframework.db;

import il.co.topq.integframework.AbstractModuleImpl;
import il.co.topq.integframework.assertion.Assert;
import il.co.topq.integframework.assertion.CompareMethod;
import il.co.topq.integframework.assertion.NumberCompareAssertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseSystemModule extends AbstractModuleImpl {

	protected final JdbcTemplate template;
	private List<ResultSetPrinter> resultSetPrinterList;
	private int queryTimeout = 0;

	public DatabaseSystemModule(final DataSource dataSource) {
		super();
		this.template = new JdbcTemplate(dataSource);
		if (queryTimeout > 0)
			this.template.setQueryTimeout(queryTimeout);
	}

	@Override
	public void init() throws Exception {
		if (resultSetPrinterList == null) {
			initResultSetPrinters();
		}
	}

	public void initResultSetPrinters() {
		resultSetPrinterList = new ArrayList<ResultSetPrinter>();
		resultSetPrinterList.addAll(tablePrinters());
	}

	/**
	 * Override this method if you want to add more table printers. For example
	 * if you want to make use of the ResultSetHTMLPrinter
	 * 
	 * @return The table printers to add for each query
	 */
	protected List<ResultSetPrinter> tablePrinters() {
		List<ResultSetPrinter> printers = new ArrayList<ResultSetPrinter>();
		printers.add(new ResultSetHTMLPrinter());
		return printers;
	}

	/**
	 * 
	 * @param sql
	 * @return A list that representing the result set of the given query
	 */
	public List<Map<String, Object>> getResultList(final String sql) {
		if (null == sql || sql.isEmpty()) {
			throw new IllegalArgumentException("SQL query can't be empty");
		}

		List<Map<String, Object>> resultList = template.queryForList(sql);
		for (ResultSetPrinter printer : resultSetPrinterList) {
			printer.print(resultList);
		}
		setActual(resultList);
		return resultList;
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE or
	 * DELETE statement Compare between the number of rows returned as a result
	 * of 'executeUpdate' and the expected number of affected rows
	 * 
	 * @param sql
	 *            expectedNumOfAffectedRows
	 * @throws Exception
	 */
	public void executeUpdateStatement(final String sql) throws Exception {
		executeUpdateStatement(sql, -1);
	}

	/**
	 * Executes the given SQL statement, which may be an INSERT, UPDATE or
	 * DELETE statement Compare between the number of rows returned as a result
	 * of 'executeUpdate' and the expected number of affected rows
	 * 
	 * @param sql
	 *            expectedNumOfAffectedRows
	 * @param expectedNumOfAffectedRows
	 *            The number of rows that we expected to be affected as a result
	 *            of the query. specify -1 if you don't want to perform
	 *            assertion.
	 * @throws Exception
	 */
	public void executeUpdateStatement(final String sql, int expectedNumOfAffectedRows) throws Exception {
		if (null == sql || sql.isEmpty()) {
			throw new IllegalArgumentException("SQL query can't be empty");
		}
		int rows = template.update(sql);
		if (expectedNumOfAffectedRows >= 0) {
			Assert.assertLogic(rows, new NumberCompareAssertion(expectedNumOfAffectedRows, CompareMethod.EQUALS));
		}
	}

	/**
	 * Executes the given SQL statement, which may be an DROP, ALTER etc,.
	 * 
	 * @param sql
	 *            the statement to execute
	 * @throws Exception
	 */
	public void executeStatement(final String sql) {
		if (null == sql || sql.isEmpty()) {
			throw new IllegalArgumentException("SQL query can't be empty");
		}
		template.execute(sql);
	}

	/**
	 * Compare between the number of rows returned as a result of executing the
	 * specified query and the given number.
	 * 
	 * @param sql
	 *            SQL query
	 * @param expectedNumOfRows
	 *            the number of rows that are expected to return from the query
	 * @param compareMethod
	 *            The relations between the actual number of rows and the
	 *            expected
	 * @throws Exception
	 */
	public void assertNumOfRows(final String sql, int expectedNumOfRows, CompareMethod compareMethod) throws Exception {
		int actual = getResultList(sql).size();
		Assert.assertLogic(actual, new NumberCompareAssertion(expectedNumOfRows, compareMethod));
	}

	public void assertNumOfRows(final String sql, int expectedNumOfRows) throws Exception {
		assertNumOfRows(sql, expectedNumOfRows, CompareMethod.EQUALS);
	}

	/**
	 * @param table
	 * @return the number of rows in the specified table
	 */
	public int countRowsInTable(final String table) {
		if (null == table || table.isEmpty()) {
			throw new IllegalArgumentException("Table name can't be empty");
		}
		return getResultList(String.format("SELECT * FROM %s", table)).size();
	}

	public List<ResultSetPrinter> getResultSetPrinterList() {
		return resultSetPrinterList;
	}

	public void setResultSetPrinterList(List<ResultSetPrinter> resultSetPrinterList) {
		if (resultSetPrinterList == null) {
			this.resultSetPrinterList = new ArrayList<ResultSetPrinter>();
		} else {
			this.resultSetPrinterList = resultSetPrinterList;
		}
	}

	public int getQueryTimeout() {
		return queryTimeout;
	}

	public void setQueryTimeout(int queryTimeout) {
		this.queryTimeout = queryTimeout;
	}

}
