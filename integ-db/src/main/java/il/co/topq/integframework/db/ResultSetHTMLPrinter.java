package il.co.topq.integframework.db;

import il.co.topq.integframework.reporting.Reporter;

import java.util.List;
import java.util.Map;



public class ResultSetHTMLPrinter implements ResultSetPrinter {

	@Override
	public void print(List<Map<String, Object>> resultList) {
		if (resultList == null || resultList.size() == 0) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		boolean headerRow = true;
		sb.append("<table border='1'>");
		for (Map<String, Object> row : resultList) {

			if (headerRow) {
				sb.append("<tr>");
				headerRow = false;
				for (String header : row.keySet()) {
					sb.append("<td>").append(header).append("</td>");
				}
				sb.append("</tr>");
			}
			sb.append("<tr>");
			for (Object column : row.values()) {
				sb.append("<td>").append(column).append("</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		Reporter.log("SQL Table", sb.toString());

	}

}
