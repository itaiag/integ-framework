#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.automation_infra_tests;

import il.co.topq.integframework.reporting.Reporter;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import com.google.common.collect.Sets;
import ${package}.automation.infra.Lab;

@ContextConfiguration(locations = "classpath:META-INF/context.xml")
public class BaseTestCase extends AbstractTestNGSpringContextTests {

	protected final static Set<String> packagesToFilter = Sets.newHashSet("sun.reflect", "java.lang.reflect", "org.testng",
			"org.apache.maven", "org.codehaus.plexus.classworlds.launcher", "org.springframework.test.context.testng");
	@Autowired
	protected Lab lab;

	static {
		Reporter.filterPackagesOnThrowables(packagesToFilter);
	}

}
