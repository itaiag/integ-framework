package il.co.topq.integframework.bbdgroovy

import il.co.topq.integframework.bbdgroovy.Bdd;

import org.junit.Test;
class TestBdd {
	
	
	@Test
	@Bdd
	public void "Test BDD"(){
		print "firstLine\n"
		print "secondLine\n"
	}
	
}
