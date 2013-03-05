package org.jsystemtest.integration

import org.testng.annotations.Test;
import org.jsystemtest.infra.report.Reporter;
class GroovyTest {
	
	static class Bdd {
		
		void given(String str){
			System.out.println(str);	
		}
		
		static Bdd exec(Closure script){
			def bdd = new Bdd()
			script.delegate = bdd
			script()
			bdd
		}
	}
	

	@Test
	void testGroovy(){
		def bbd = {
			given "All kind of things"
			
			}
		
	
	}
	
	def execute(Closure... closures){
		for (Closure closure: closures){
			closure.call()
		}
	}
}
