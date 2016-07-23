package com.nm.orm.tests;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
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
@Table(name = "CLASSIC", schema = "PUBLIC", uniqueConstraints = @UniqueConstraint(columnNames = { "CLASSIC_2",
		"CLASSIC_3" }) )
public class ExampleModelClassic {
	@Id
	@GeneratedValue
	@Column(name = "CLASSIC_1")
	private String classic;
	@Column(name = "CLASSIC_2")
	private String classic2;
	@Column(name = "CLASSIC_3")
	private boolean classic3;
	@OneToOne
	@JoinColumn(name = "FIELD_1")
	private ExampleModel model;
	@OneToMany
	@JoinColumn(name = "FIELD_1")
	private Collection<ExampleModel> models = Lists.newArrayList();
	@OneToMany
	@JoinColumns({ @JoinColumn(name = "FIELD_1", referencedColumnName = "CLASSIC_1"),
			@JoinColumn(name = "FIELD_2", referencedColumnName = "CLASSIC_2") })
	private Collection<ExampleModelChild> modelsChildren = Lists.newArrayList();

	public String getClassic() {
		return classic;
	}

	public void setClassic(String classic) {
		this.classic = classic;
	}

	public String getClassic2() {
		return classic2;
	}

	public void setClassic2(String classic2) {
		this.classic2 = classic2;
	}

	public boolean isClassic3() {
		return classic3;
	}

	public void setClassic3(boolean classic3) {
		this.classic3 = classic3;
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