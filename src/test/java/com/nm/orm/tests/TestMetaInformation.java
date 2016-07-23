package com.nm.orm.tests;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.nm.orm.jdbc.meta.ColumnFilter;
import com.nm.orm.jdbc.meta.MetaInformation;
import com.nm.orm.jdbc.meta.MetaInformationAssociation;
import com.nm.orm.jdbc.meta.MetaInformationAssociation.TypeOfAssociation;
import com.nm.orm.jdbc.meta.MetaRepository;

/**
 * 
 * @author Nabil MANSOURI
 *
 */
public class TestMetaInformation {

	@Test
	public void testShouldBuildColumns() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		Assert.assertEquals(3, meta.getColumnsCollection().size());
		Assert.assertEquals(2, meta.getFields(ColumnFilter.onlyInUniqGroup()).length);
		Assert.assertEquals(2, meta.getFields(ColumnFilter.onlyNonIds()).length);
	}

	@Test
	public void testShouldBuildIds() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		Assert.assertTrue(meta.isGenerated());
		Assert.assertEquals(1, meta.getFields(ColumnFilter.onlyIds()).length);
		Assert.assertEquals(1, meta.getFields(ColumnFilter.onlyGenerated()).length);
		//
		meta = MetaRepository.getOrCreate(ExampleModelClassicNotGenerated.class);
		Assert.assertFalse(meta.isGenerated());
		Assert.assertEquals(1, meta.getFields(ColumnFilter.onlyIds()).length);
		Assert.assertEquals(0, meta.getFields(ColumnFilter.onlyGenerated()).length);
	}

	@Test
	public void testShouldBuildRelationsOneToOne() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		Assert.assertEquals(3, meta.getAssociationsCollection().size());
		MetaInformationAssociation oneToOne = meta.getAssociationsCollection().stream()
				.filter(u -> u.getType().equals(TypeOfAssociation.OneToOne)).findFirst().get();
		Assert.assertNotNull(oneToOne);
		Assert.assertEquals("model", oneToOne.getField().getName());
		Assert.assertEquals(ExampleModel.class, oneToOne.getTarget());
		Assert.assertEquals(1, oneToOne.getTargetFields().size());
		Assert.assertEquals("classic", oneToOne.getTargetFields().get("FIELD_1").getName());
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic("YEAH");
		MapSqlParameterSource map = oneToOne.getMap(classic);
		Assert.assertEquals(1, map.getValues().size());
		Assert.assertEquals("YEAH", map.getValue("FIELD_1"));
	}

	@Test
	public void testShouldBuildRelationsOneToMany() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		Assert.assertEquals(3, meta.getAssociationsCollection().size());
		MetaInformationAssociation oneToMany = meta.getAssociationsCollection().stream()
				.filter(u -> u.getType().equals(TypeOfAssociation.OneToMany)
						&& u.getField().getName().equalsIgnoreCase("models"))
				.findFirst().get();
		Assert.assertNotNull(oneToMany);
		Assert.assertEquals("models", oneToMany.getField().getName());
		Assert.assertEquals(ExampleModel.class, oneToMany.getTarget());
		Assert.assertEquals(1, oneToMany.getTargetFields().size());
		Assert.assertEquals("classic", oneToMany.getTargetFields().get("FIELD_1").getName());
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic("YEAH");
		MapSqlParameterSource map = oneToMany.getMap(classic);
		Assert.assertEquals(1, map.getValues().size());
		Assert.assertEquals("YEAH", map.getValue("FIELD_1"));
	}

	@Test
	public void testShouldBuildRelationsOneToManyMultiple() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		Assert.assertEquals(3, meta.getAssociationsCollection().size());
		MetaInformationAssociation oneToMany = meta.getAssociationsCollection().stream()
				.filter(u -> u.getType().equals(TypeOfAssociation.OneToMany)
						&& !u.getField().getName().equalsIgnoreCase("models"))
				.findFirst().get();
		Assert.assertNotNull(oneToMany);
		Assert.assertEquals("modelsChildren", oneToMany.getField().getName());
		Assert.assertEquals(ExampleModelChild.class, oneToMany.getTarget());
		Assert.assertEquals(2, oneToMany.getTargetFields().size());
		System.out.println(oneToMany.getTargetFields());
		Assert.assertEquals("classic", oneToMany.getTargetFields().get("FIELD_1").getName());
		Assert.assertEquals("classic2", oneToMany.getTargetFields().get("FIELD_2").getName());
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic("YEAH");
		classic.setClassic2("YEAH2");
		MapSqlParameterSource map = oneToMany.getMap(classic);
		Assert.assertEquals(2, map.getValues().size());
		Assert.assertEquals("YEAH", map.getValue("FIELD_1"));
		Assert.assertEquals("YEAH2", map.getValue("FIELD_2"));
	}

	@Test
	public void testShouldFetchNonIds() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic2("YEAH");
		classic.setClassic3(Boolean.TRUE);
		//
		MapSqlParameterSource map = meta.getMap(classic, ColumnFilter.onlyNonIds());
		Assert.assertEquals(2, map.getValues().size());
		Assert.assertEquals("YEAH", map.getValue("CLASSIC_2"));
		Assert.assertEquals(Boolean.TRUE, map.getValue("CLASSIC_3"));
	}

	@Test
	public void testShouldFetchNonNonNull() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic3(true);
		//
		MapSqlParameterSource map = meta.getMap(classic, ColumnFilter.onlyNonNull());
		Assert.assertEquals(1, map.getValues().size());
		Assert.assertEquals(Boolean.TRUE, map.getValue("CLASSIC_3"));
		//
		classic.setClassic2("YEAH");
		map = meta.getMap(classic, ColumnFilter.onlyNonNull());
		Assert.assertEquals(2, map.getValues().size());
		Assert.assertEquals("YEAH", map.getValue("CLASSIC_2"));
	}

	@Test
	public void testShouldFetchValuesNonNonNull() throws Exception {
		MetaInformation meta = MetaRepository.getOrCreate(ExampleModelClassic.class);
		//
		ExampleModelClassic classic = new ExampleModelClassic();
		classic.setClassic3(true);
		//
		List<Object> map = meta.getValues(classic, ColumnFilter.onlyNonNull());
		Assert.assertEquals(1, map.size());
		Assert.assertTrue(map.contains(Boolean.TRUE));
		//
		classic.setClassic2("YEAH");
		map = meta.getValues(classic, ColumnFilter.onlyNonNull());
		Assert.assertEquals(2, map.size());
		Assert.assertTrue(map.contains("YEAH"));
	}

}
