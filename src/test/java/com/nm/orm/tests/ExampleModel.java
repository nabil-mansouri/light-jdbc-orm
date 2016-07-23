package com.nm.orm.tests;

import javax.persistence.Column;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
public class ExampleModel {
	@Column(name = "FIELD_1")
	private String field;
	private String field2;
	private boolean field3;

	public String getField() {
		return field;
	}

	@Column(name = "FIELD_2")
	public String getField2() {
		return field2;
	}

	@Column(name = "FIELD_3")
	public boolean isField3() {
		return field3;
	}

	public void setField(String field) {
		this.field = field;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public void setField3(boolean field3) {
		this.field3 = field3;
	}
}