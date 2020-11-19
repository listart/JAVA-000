# Week05 （周四）

## 2* Spring Bean的装配

写代码***实现 Spring Bean 的装配***，方式越多越好（XML、Annotation 都可以）, 提交到 Github。

> 源自官方文档[The IoC Container](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-class)
>
> In Spring, the objects that form the backbone of your application and that are managed by the Spring IoC container are called beans. A bean is an object that is ***instantiated***, ***assembled***, and ***managed*** by a Spring IoC container. Otherwise, a bean is simply one of many objects in your application. Beans, and the dependencies among them, are reflected in the configuration metadata used by a container.
>
> The `org.springframework.context.ApplicationContext` interface represents the Spring IoC container and is responsible for ***instantiating***, ***configuring***, and ***assembling*** the beans. The container gets its instructions on what objects to instantiate, configure, and <u>***assemble by reading configuration metadata***. The configuration metadata is represented in **XML**, **Java annotations**, or **Java code**.</u> It lets you express the objects that compose your application and the rich interdependencies between those objects.

通过对Spring Framework官方文档的查找，理解到：

1. Bean是Spring IoC容器***初始化***、***装配***和***管理***的对象。
2. ApplicationContext接口表示容器，并负责***初始化***、**配置**和***装配***这些bean。
3. 装配是***assemble***，不是assign。
4. 容器主要通过<u>读取配置元数据装配</u>。配置元数据的方式有：
   1. XML
   2. Java注解
   3. Java代码
5. ***instantiating***主要通过`Bean Definition`定义，***assembling***主要通过`Dependency Injection`配置bean之间的组合关系。



装配方式列举如下：

1. 基于构造器注入

   1. Java代码
   2. XML-based `<constructor-arg/>`
   3. XML-based byType
   4. XML-based byIndex
   5. XML-based byName
   6. static factory method `factory-method="createInstance"``
   7. `XML Shortcut with the c-namespace`

2. 基于属性注入

   1. annotated components (that is, classes annotated with `@Component`, `@Controller`, and so forth)

   2. @Bean` methods in Java-based `@Configuration` classes` 

   3.  `XML-based `<property/>

   4. ``XML Shortcut with the p-namespace`

   5. java.util.Properties

      `bean | ref | idref | list | set | map | props | value | null`

3. `Autowiring Collaborators`

   1. no
   2. byName
   3. byType
   4. constructor

4. Method Injection

   1. ApplicationContextAware +  getBean
   2. lookup-method
   3. @Lookup
   4. MethodReplacer





# Week05（周六）

## 4* 自动配置和starter

给前面课程提供的 Student/Klass/School 实现自动配置和 Starter。

> ***NOTE*** 中文配置乱码问题： `application.properties`不支持UTF8中文，可以使用`application.yaml`。





## 6* JDBC接口和数据库连接池

研究一下 JDBC 接口和数据库连接池，掌握它们的设计和用法：

1. 使用 JDBC 原生接口，实现数据库的增删改查操作。
2. 使用事务，PrepareStatement 方式，批处理方式，改进上述操作。
3. 配置 Hikari 连接池，改进上述操作。提交代码到 Github。



> 源自[JDK官方文档](https://docs.oracle.com/javase/tutorial/jdbc/basics/processingsqlstatements.html)
>
> The following statement is a `try`-with-resources statement, which declares one resource, `stmt`, that will be automatically closed when the `try` block terminates:
>
> ```java
>     try (Statement stmt = con.createStatement()) {
>       // ...
>     }
> ```

Statement、ResultSet之类的本地变量，可以通过try with resources的方式，自动close。