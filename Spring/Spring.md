## Spring开发步骤
> 1. 导入坐标
> 2. 创建Bean
```java
public interface UserDao {  
    public void save();
}
public class UserDaoImpl implements UserDao {  
        @Override  
        public void save() {
        	System.out.println("UserDao save method running....");
	}
}
```
> 3. 创建applicationContext.xml
> 4. 在配置文件中进行配置
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl" scope="singleton"></bean>
```
> 5. 创建ApplicationContext对象getBean
```java
    ApplicationContext app = new ClassPathXmlApplicationContex("applicationContext.xml");
    UserDao userDao = (UserDao)app.getBean("userDao");
```
## Bean标签
### Bean标签范围配置
| 取值范围         | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| singleton        | 默认值，单例的                                               |
| prototype        | 多例的                                                       |
| request          | WEB   项目中，Spring   创建一个   Bean   的对象，将对象存入到   request   域中 |
| session          | WEB   项目中，Spring   创建一个   Bean   的对象，将对象存入到   session   域中 |
| global   session | WEB   项目中，应用在   Portlet   环境，如果没有   Portlet   环境那么globalSession   相当于   session |
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl" scope="singleton"></bean>
```
> 区别
>> 当scope取值singleton，Bean只实例化一个，当加载配置文件时创建Bean实例，生命周期：加载应用，创建容器时被创建，卸载应用时销毁；取值prototype时，Bean可实例化多个，调用getBean()方法时创建实例，生命周期：使用对象时创建对象实例，当对象长时间不用时，被 Java 的垃圾回收器回收。
### Bean生命周期配置

init-method：指定类中的初始化方法名称

destroy-method：指定类中销毁方法名称
```xml
 <bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl" init-method="init" destroy-method="destory"></bean>
 ```
 ### Bean实例化三种方式

1） 使用无参构造方法实例化

​      它会根据默认无参构造方法来创建类对象，如果bean中没有默认无参构造函数，将会创建失败

```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>
```

2） 工厂静态方法实例化

​      工厂的静态方法返回Bean实例

```java
public class StaticFactoryBean {
    public static UserDao createUserDao(){    
    return new UserDaoImpl();
    }
}
```

```xml
<bean id="userDao" class="com.itheima.factory.StaticFactoryBean" 
      factory-method="createUserDao" />
```

3） 工厂实例方法实例化

​      工厂的非静态方法返回Bean实例

```java
public class DynamicFactoryBean {  
	public UserDao createUserDao(){        
		return new UserDaoImpl(); 
	}
}
```
```xml
<bean id="factoryBean" class="com.itheima.factory.DynamicFactoryBean"/>
<bean id="userDao" factory-bean="factoryBean" factory-method="createUserDao"/>
```

### Bean依赖注入
注入数据通常有两种方式，构造方法或set。构造方法相当于初始化属性，set方法相当于之后修改属性
>> 1. 构造方法
```java
public class UserServiceImpl implements UserService {
@Override
public void save() {
ApplicationContext applicationContext = new 
                 ClassPathXmlApplicationContext("applicationContext.xml");       UserDao userDao = (UserDao) applicationContext.getBean("userDao");    
          userDao.save();
    }
 }
```
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.itheima.service.impl.UserServiceImpl">      		   	<constructor-arg name="userDao" ref="userDao"></constructor-arg>
</bean>
```
>> 2. set方法，在UserServiceImpl中添加setUserDao方法，配置Spring容器调用set方法进行注入
```java
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;  
        } 
    @Override    
    public void save() {      
   		 userDao.save();
	}
}
```   
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl"/>
<bean id="userService" class="com.itheima.service.impl.UserServiceImpl">
	<property name="userDao" ref="userDao"/>
</bean>
```
### Bean的依赖注入的数据类型

上面的操作，都是注入的引用Bean，处了对象的引用可以注入，普通数据类型，集合等都可以在容器中进行注入。

> 1. 普通数据类型
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl">
        <property name="username" value="静静"></property>
        <property name="age" value="23"></property>
</bean>
```
> 2.引用数据类型

> 3.集合数据类型
```xml
<bean id="userDao" class="com.itheima.dao.impl.UserDaoImpl">
    <property name="strList">
        <list>
            <value>刘义隆</value>
            <value>拓跋焘</value>
        </list>
    </property>
    <property name="userMap">
        <map>
            <entry key="user1" value-ref="user1"></entry>
            <entry key="user2" value-ref="user2"></entry>
        </map>
    </property>
    <property name="properties">
        <props>
            <prop key="p1">徐羡之</prop>
            <prop key="p2">谢晦</prop>
            <prop key="p3">檀道济</prop>
        </props>
    </property>
</bean>
```

### 引入其他配置文件（分模块开发）

实际开发中，Spring的配置内容非常多，这就导致Spring配置很繁杂且体积很大，所以，可以将部分配置拆解到其他配置文件中，而在Spring主配置文件通过import标签进行加载

```xml
<import resource="applicationContext-xxx.xml"/>
```

### getBean()方法使用

```java
public Object getBean(String name) throws BeansException {  
	assertBeanFactoryActive();   
	return getBeanFactory().getBean(name);
}
public <T> T getBean(Class<T> requiredType) throws BeansException {   			    	assertBeanFactoryActive();
	return getBeanFactory().getBean(requiredType);
}
```

其中，当参数的数据类型是字符串时，表示根据Bean的id从容器中获得Bean实例，返回是Object，需要强转。

当参数的数据类型是Class类型时，表示根据类型从容器中匹配Bean实例，当容器中相同类型的Bean有多个时，则此方法会报错

**getBean()方法使用**

```java
ApplicationContext applicationContext = new 
            ClassPathXmlApplicationContext("applicationContext.xml");
  UserService userService1 = (UserService) applicationContext.getBean("userService");
  UserService userService2 = applicationContext.getBean(UserService.class);
```

### 数据源（连接池）的作用 

数据源(连接池)是提高程序性能如出现的

事先实例化数据源，初始化部分连接资源

使用连接资源时从数据源中获取

使用完毕后将连接资源归还给数据源

常见的数据源(连接池)：DBCP、C3P0、BoneCP、Druid等

**开发步骤**

①导入数据源的坐标和数据库驱动坐标
>> 导入数据库驱动时注意mysql的版本，有5.1和8.0的版本，否则会连接失败
```xml
<!-- mysql驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.21</version>
</dependency>
```

②创建数据源对象
>> *创建C3P0连接池*
```java
@Test
public void testC3P0() throws Exception {
	//创建数据源
	ComboPooledDataSource dataSource = new ComboPooledDataSource();
	//设置数据库连接参数
    dataSource.setDriverClass("com.mysql.jdbc.Driver");    	               	               dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
    dataSource.setUser("root");
    dataSource.setPassword("root");
	//获得连接对象
	Connection connection = dataSource.getConnection();
	System.out.println(connection);
}

```
>> *创建Druid连接池*

```java
@Test
public void testDruid() throws Exception {
    //创建数据源
    DruidDataSource dataSource = new DruidDataSource();
    //设置数据库连接参数
    dataSource.setDriverClassName("com.mysql.jdbc.Driver"); 
    dataSource.setUrl("jdbc:mysql://localhost:3306/test");   
    dataSource.setUsername("root");
    dataSource.setPassword("root");
    //获得连接对象
    Connection connection = dataSource.getConnection();    
    System.out.println(connection);
}
```
③设置数据源的基本连接数据
```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test
jdbc.username=root
jdbc.password=root
```
④使用数据源获取连接资源和归还连接资源
```java
@Test
public void testC3P0ByProperties() throws Exception {
    //加载类路径下的jdbc.properties
    ResourceBundle rb = ResourceBundle.getBundle("jdbc");
    ComboPooledDataSource dataSource = new ComboPooledDataSource(); 
    dataSource.setDriverClass(rb.getString("jdbc.driver"));   
    dataSource.setJdbcUrl(rb.getString("jdbc.url")); 
    dataSource.setUser(rb.getString("jdbc.username")); 
    dataSource.setPassword(rb.getString("jdbc.password"));
    Connection connection = dataSource.getConnection();   
    System.out.println(connection);
}
```

### Spring配置数据源
可以将DataSource的创建权交由Spring容器去完成,DataSource有无参构造方法，而Spring默认就是通过无参构造方法实例化对象的,DataSource要想使用需要通过set方法设置数据库连接信息，而Spring可以通过set方法进行字符串注入
```xml
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="com.mysql.jdbc.Driver"/>
    <property name="jdbcUrl" value="jdbc:mysql://localhost:3306/test"/>
    <property name="user" value="root"/>
    <property name="password" value="root"/>
</bean>
```
测试从容器当中获取数据源

```java
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
Connection connection = dataSource.getConnection();
System.out.println(connection);
```
### 抽取jdbc配置文件
applicationContext.xml加载jdbc.properties配置文件获得连接信息。

首先，需要引入context命名空间和约束路径：

命名空间：xmlns:context="http://www.springframework.org/schema/context"

约束路径：http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context.xsd

```xml
<context:property-placeholder location="classpath:jdbc.properties"/>
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
    <property name="driverClass" value="${jdbc.driver}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="user" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```
### Spring容器加载properties文件
```xml
<context:property-placeholder location="xx.properties"/>
<property name="" value="${key}"/>
```

### Spring原始注解

Spring是轻代码而重配置的框架，配置比较繁重，影响开发效率，所以注解开发是一种趋势，注解代替xml配置文件可以简化配置，提高开发效率。 
Spring原始注解主要是替代<Bean>的配置

| 注解           | 说明                                           |
| -------------- | ---------------------------------------------- |
| @Component     | 使用在类上用于实例化Bean                       |
| @Controller    | 使用在web层类上用于实例化Bean                  |
| @Service       | 使用在service层类上用于实例化Bean              |
| @Repository    | 使用在dao层类上用于实例化Bean                  |
| @Autowired     | 使用在字段上用于根据类型依赖注入               |
| @Qualifier     | 结合@Autowired一起使用用于根据名称进行依赖注入 |
| @Resource      | 相当于@Autowired+@Qualifier，按照名称进行注入  |
| @Value         | 注入普通属性                                   |
| @Scope         | 标注Bean的作用范围                             |
| @PostConstruct | 使用在方法上标注该方法是Bean的初始化方法       |
| @PreDestroy    | 使用在方法上标注该方法是Bean的销毁方法         |
使用注解进行开发时，需要在applicationContext.xml中配置组件扫描，作用是指定哪个包及其子包下的Bean需要进行扫描以便识别使用注解配置的类、字段和方法。
```java
<!--注解的组件扫描-->
<context:component-scan base-package="com.itheima"></context:component-scan>
```
### Spring新注解

| 注解            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| @Configuration  | 用于指定当前类是一个 Spring   配置类，当创建容器时会从该类上加载注解 |
| @ComponentScan  | 用于指定 Spring   在初始化容器时要扫描的包。   作用和在 Spring   的 xml 配置文件中的   <context:component-scan   base-package="com.itheima"/>一样 |
| @Bean           | 使用在方法上，标注将该方法的返回值存储到   Spring   容器中   |
| @PropertySource | 用于加载.properties   文件中的配置                           |
| @Import         | 用于导入其他配置类                                           |
```java
@Configuration
@ComponentScan("com.itheima")
@PropertySource("classpath:jdbc.properties")
public class SpringConfig {

}
```
```java
public static void main(String[] args) {
    ApplicationContext app = new AnnotationConfigApplicationContext(SpringConfig.class);
    UserService userService = (UserService)app.getBean("userService");
    userService.save();
}
```
### Spring整合Junit
①导入spring集成Junit的坐标
```xml
<!--此处需要注意的是，spring5 及以上版本要求 junit 的版本必须是 4.12 及以上-->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```
②使用@Runwith注解替换原来的运行期
```java
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringJunitTest {
}
```
③使用@ContextConfiguration指定配置文件或配置类
```java
@RunWith(SpringJUnit4ClassRunner.class)
//加载spring核心配置文件
//@ContextConfiguration(value = {"classpath:applicationContext.xml"})
//加载spring核心配置类
@ContextConfiguration(classes = {SpringConfiguration.class})
public class SpringJunitTest {
}
```
④使用@Autowired注入需要测试的对象
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class SpringJunitTest {
    @Autowired
    private UserService userService;
}
```
⑤创建测试方法进行测试
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})public class SpringJunitTest {
    @Autowired
    private UserService userService;
    @Test
    public void testUserService(){
   	 userService.save();
    }
}
```
## AOP 
AOP 为 Aspect Oriented Programming 的缩写，意思为面向切面编程，是通过预编译方式和运行期动态代理实现程序功能的统一维护的一种技术。


