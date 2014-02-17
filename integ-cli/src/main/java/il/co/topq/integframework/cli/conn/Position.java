/*
 * Created on Jul 29, 2005
 *
 * Copyright 2005-2010 Ignis Software Tools Ltd. All rights reserved.
 */
package il.co.topq.integframework.cli.conn;

/**
 * Present a CLI position.
 * @author guy.arieli
 */
public class Position {

	String name;
	String[] enters;
	String[] exits;

	public String[] getEnters() {
		return enters;
	}
	public void setEnters(String[] enters) {
		this.enters = enters;
	}
	public String[] getExits() {
		return exits;
	}
	public void setExits(String[] exits) {
		this.exits = exits;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
