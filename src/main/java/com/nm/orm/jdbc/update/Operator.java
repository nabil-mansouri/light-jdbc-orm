package com.nm.orm.jdbc.update;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public enum Operator {
	EQUALS("="), LESS_THAN("<"), GREATER_THAN(">");

	private String op;

	private Operator(final String op) {
		this.op = op;
	}

	@Override
	public String toString() {
		return op;
	}
}
