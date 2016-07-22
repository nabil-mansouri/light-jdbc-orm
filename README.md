# light-jdbc-orm
A lightweight jdbc ORM using JPA annotation.

# Author

* Nabil MANSOURI <nabil.mansouri.3@gmail.com>
* My blog : nabil.e-devservice.com

# How to use it

## 1- Import dependency

```

<dependency>
	<groupId>com.nabil.mansouri</groupId>
	<artifactId>light-jdbc-orm</artifactId>
	<version>0.0.1</version>
</dependency>
	
```	

		
## 2- Annotate your domain object using JPA annotations


```

@Table(name = "USER_TABLE", schema = "PUBLIC", 
uniqueConstraints = @UniqueConstraint(columnNames = { "FIRST_NAME", "NAME" }) )
public class User {
	//ID CAN BE GENERATED OR NOT (DB AUTOINCREMENT)
	@Id()
	@GeneratedValue
	@Column(name = "USER_ID")
	private Number id;
	//
	@Column(name = "FIRST_NAME")
	private String firstName;
	@Column(name = "NAME")
	private Date name;
	@OneToOne()
	@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "USER_ID")
	private Address address;
	@OneToMany
	@JoinColumn(name = "USER_ID")
	private List<Attribute> attributes = Lists.newArrayList();

}

```

## 3- Make a DAO

```
@Repository
public class DaoOrmUser extends AbstractJdbcOrmDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate j) {
		this.jdbcTemplate = j;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	protected SimpleJdbcInsertAdapter insertAdapter() {
		return new SimpleJdbcInsertSybaseAdapter();
	}
}
```

## 4- Use it

```
User model = new User();
//
dao.insert(model);
//
dao.update(model);
//
dao.saveOrUpdate(model);
// INSERT OR UPDATE MODEL BASED ON UNIQ COLUMNS
dao.saveOrUpdateUniq(model);
//
model = dao.get(User.class, 1);
//
Collection<User>users = dao.getByIds(User.class, Arrays.asList(1));
//
Collection<User>users = dao.findAll(User.class);
//
model.getAttributes().clear();
// REFRESH DATABASE STATE BASED ON ATTRIBUTE LIST
dao.cleanUnattached(model, Attribute.class);
// REFRESH ATTRIBUTE LIST BASED ON DATABASE STATE
dao.refresh(model, ModelPncResident.class);
// REFRESH DATABASE STATE FOR ALL EMBED ENTITIES
dao.cleanUnattached(model);
// REFRESH EMBED ENTITIES BASED ON DATABASE STATE
dao.refresh(model);
//
dao.delete(model);
```

# TODOS

Manage @ManyToMany entities using @JoinTable

