package il.co.topq.integframework.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseSystemModule  {

	private final JdbcTemplate template;
	private List<ResultSetPrinter> resultSetPrinterList;

	public DatabaseSystemModule(final DataSource dataSource) {
		super();
		this.template = new JdbcTemplate(dataSource);
		resultSetPrinterList = new ArrayList<ResultSetPrinter>();
		resultSetPrinterList.add(new ResultSetDefaultPrinter());
	}

	private List<Map<String, Object>> getResultList(String sql) {
		List<Map<String, Object>> resultList = template.queryForList(sql);
		for (ResultSetPrinter printer : resultSetPrinterList) {
			printer.print(resultList);
		}
		return resultList;
	}

	public int countRowsInTable(String table) {
		return getResultList(String.format("SELECT * FROM %s", table)).size();
	}

}
