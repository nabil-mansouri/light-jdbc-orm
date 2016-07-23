package com.nm.orm.tests;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.collect.Lists;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
@Table(name = "CLASSIC", schema = "PUBLIC", uniqueConstraints = @UniqueConstraint(columnNames = { "FIELD_2",
		"FIELD_3" }) )
public class ExampleModelClassicNotGenerated {
	@Id
	@Column(name = "FIELD_1")
	private String field;
	private String field2;
	private boolean field3;
	@OneToOne
	@JoinColumn(referencedColumnName = "field")
	private ExampleModel model;
	@OneToMany
	@JoinColumn(referencedColumnName = "field")
	private Collection<ExampleModel> models = Lists.newArrayList();
	@OneToMany
	@JoinColumns({ @JoinColumn(referencedColumnName = "field"), @JoinColumn(referencedColumnName = "field2") })
	private Collection<ExampleModelChild> modelsChildren = Lists.newArrayList();

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

	public ExampleModel getModel() {
		return model;
	}

	public void setModel(ExampleModel model) {
		this.model = model;
	}

	public Collection<ExampleModel> getModels() {
		return models;
	}

	public void setModels(Collection<ExampleModel> models) {
		this.models = models;
	}

	public Collection<ExampleModelChild> getModelsChildren() {
		return modelsChildren;
	}

	public void setModelsChildren(Collection<ExampleModelChild> modelsChildren) {
		this.modelsChildren = modelsChildren;
	}
}