# 作业

1、(选做)用今天课上学习的知识，分析自己系统的SQL和表结构

>  [ec_structure.sql](../Week_06/ec_structure.sql) 
>
> 针对schema文件做了以下优化：
>
> 1. 去除所有外键设置
> 2. SN字段CHAR(20) => CHAR(32)
> 3. JSON字段类型替换为VARCHAR(2048)



2、(必做)按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。

> 来自课件P51
>
> ***总结1:写入优化***
>
> * 大批量写入的优化 
> * PreparedStatement 减少SQL解析 
> * Multiple Values/Add Batch 
> * 减少交互 Load Data
> * 直接导入
> * 索引和约束问题

执行结果如下：

```bash
generating samples...
generated samples.size() = 1000000
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
BigImporter.insertOneByOne inserted 1000000 rows in 3672201 ms.
BigImporter.preparedInsertOneByOne inserted 1000000 rows in 3627730 ms.
BigImporter.insertMultipleValues inserted 1000000 rows in 86013 ms.
BigImporter.preparedInsertBatch inserted 1000000 rows in 3653889 ms.
```

逐个insert，甚至batch方式prepareInsert，插入速度都相当慢。
multiple values方式每次拼凑1000条记录的插入语句提交，效率是其他三种方式的45倍。



3、(选做)按自己设计的表结构，插入1000万订单模拟数据，测试不同方式的插入效率。

4、(选做)使用不同的索引或组合，测试不同方式查询效率。

5、(选做)调整测试数据，使得数据尽量均匀，模拟1年时间内的交易，计算一年的销售报 表:销售总额，订单数，客单价，每月销售量，前十的商品等等(可以自己设计更多指标)。

6、(选做)尝试自己做一个ID生成器(可以模拟Seq或Snowflake)。

7、(选做)尝试实现或改造一个非精确分页的程序。

> ***参考***  [JDBC basics tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)



# 思考

## `NOW()`与`SYSDATE()`

```sql
mysql> Select NOW(), SLEEP(5), NOW();
+---------------------+----------+---------------------+
| NOW()               | SLEEP(5) | NOW()               |
+---------------------+----------+---------------------+
| 2017-10-31 09:57:36 | 0        | 2017-10-31 09:57:36 |
+---------------------+----------+---------------------+
1 row in set (5.11 sec)

mysql> Select SYSDATE(), SLEEP(5), SYSDATE();

+---------------------+----------+---------------------+
| SYSDATE()           | SLEEP(5) | SYSDATE()           |
+---------------------+----------+---------------------+
| 2017-10-31 09:58:13 | 0        | 2017-10-31 09:58:18 |
+---------------------+----------+---------------------+
1 row in set (5.00 sec)
```

> ***设计考量*** 
>
> 在同一个SQL中，`NOW()`返回值具有幂等性，适合对外体现一致性，比如同时设置创建语句中的`createtime`和`updatetime`;
>
> `SYSDATE()`具有实时性，适合体现内部差异。







# 预习记录

1. 去IOE = 去IBM小型机+Oracle数据库+EMC存储
