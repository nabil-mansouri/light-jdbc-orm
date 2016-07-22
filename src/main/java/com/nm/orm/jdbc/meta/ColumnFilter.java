package com.nm.orm.jdbc.meta;

import java.util.function.Predicate;

/**
 * 
 * @author MANSOURI Nabil <nabil.mansouri.3@gmail.com>
 *
 */
public class ColumnFilter {
	private boolean onlyNonNull;
	private boolean onlyId;
	private boolean onlyNonId;
	private boolean onlyInsertable;
	private boolean onlyUpdatable;
	private boolean onlyUniquable;
	private boolean onlyInUniqGroup;
	private boolean onlyGenerated;

	public static ColumnFilter noFilter() {
		return new ColumnFilter();
	}

	public static ColumnFilter onlyNonNull() {
		return new ColumnFilter().setOnlyNonNull(true);
	}

	public static ColumnFilter onlyNonIds() {
		return new ColumnFilter().setOnlyNonId(true);
	}

	public static ColumnFilter onlyIds() {
		return new ColumnFilter().setOnlyId(true);
	}

	public static ColumnFilter onlyUniquable() {
		return new ColumnFilter().setOnlyUniquable(true);
	}

	public static ColumnFilter onlyInUniqGroup() {
		return new ColumnFilter().setOnlyInUniqGroup(true);
	}

	public static ColumnFilter onlyGenerated() {
		return new ColumnFilter().setOnlyId(true).setOnlyGenerated(true);
	}

	public boolean isOnlyInUniqGroup() {
		return onlyInUniqGroup;
	}

	public ColumnFilter setOnlyInUniqGroup(boolean onlyInUniqGroup) {
		this.onlyInUniqGroup = onlyInUniqGroup;
		return this;
	}

	public ColumnFilter setOnlyUniquable(boolean onlyUniquable) {
		this.onlyUniquable = onlyUniquable;
		return this;
	}

	public boolean isOnlyGenerated() {
		return onlyGenerated;
	}

	public ColumnFilter setOnlyGenerated(boolean onlyGenerated) {
		this.onlyGenerated = onlyGenerated;
		return this;
	}

	public boolean isOnlyUniquable() {
		return onlyUniquable;
	}

	public ColumnFilter setOnlyNonNull(boolean onlyNonNull) {
		this.onlyNonNull = onlyNonNull;
		return this;
	}

	public boolean isOnlyNonNull() {
		return onlyNonNull;
	}

	public boolean isOnlyId() {
		return onlyId;
	}

	public ColumnFilter setOnlyId(boolean onlyId) {
		this.onlyId = onlyId;
		return this;
	}

	public boolean isOnlyNonId() {
		return onlyNonId;
	}

	public ColumnFilter setOnlyNonId(boolean onlyNonId) {
		this.onlyNonId = onlyNonId;
		return this;
	}

	public boolean isOnlyInsertable() {
		return onlyInsertable;
	}

	public void setOnlyInsertable(boolean onlyInsertable) {
		this.onlyInsertable = onlyInsertable;
	}

	public boolean isOnlyUpdatable() {
		return onlyUpdatable;
	}

	public void setOnlyUpdatable(boolean onlyUpdatable) {
		this.onlyUpdatable = onlyUpdatable;
	}

	public Predicate<MetaInformationColumn> toPredicate() {
		Predicate<MetaInformationColumn> cunj = u -> true;
		if (this.onlyId) {
			cunj = cunj.and(u -> u.isId());
		}
		if (this.onlyGenerated) {
			cunj = cunj.and(u -> u.isGenerated());
		}
		if (this.onlyInsertable) {
			cunj = cunj.and(u -> u.getColumn().insertable());
		}
		if (this.onlyNonId) {
			cunj = cunj.and(u -> !u.isId());
		}
		if (this.onlyUpdatable) {
			cunj = cunj.and(u -> u.getColumn().updatable());
		}
		if (this.onlyUniquable) {
			cunj = cunj.and(u -> u.getColumn().unique());
		}
		if (this.onlyInUniqGroup) {
			cunj = cunj.and(u -> u.isInUniqGroup());
		}
		return cunj;
	}

	public Predicate<MetaInformationColumn> toPredicate(Object object) {
		Predicate<MetaInformationColumn> cunj = toPredicate();
		if (this.onlyNonNull) {
			cunj = cunj.and(u -> (u.fieldValueSafe(object) != null));
		}
		return cunj;
	}
}
