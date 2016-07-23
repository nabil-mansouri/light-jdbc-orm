package com.nm.orm.tests;

import javax.persistence.Column;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
public class ExampleModelChild extends ExampleModel {
	@Column(name = "FIELD_4")
	private String fiel4;
	private String field5;
	private boolean field6;
	@Column(name = "FIELD_7")
	private String fiel7;

	public String getFiel4() {
		return fiel4;
	}

	public void setFiel4(String fiel4) {
		this.fiel4 = fiel4;
	}

	public String getField5() {
		return field5;
	}

	@Column(name = "FIELD_5")
	public void setField5(String field5) {
		this.field5 = field5;
	}

	public boolean isField6() {
		return field6;
	}

	@Column(name = "FIELD_6")
	public void setField6(boolean field6) {
		this.field6 = field6;
	}

}