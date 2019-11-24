# 表示通过索引一次就找到了,const 用于比较 primary key 或者 unique 索引。 因为只匹配一行数据， 所以很快
# 如将主键置于 where 列表中， MySQL 就能将该查询转换为一个常量。
explain select * from (select id from t2 where t2.id = 1) a;

# 唯一性索引扫描， 对于每个索引键， 表中只有一条记录与之匹配。 常见于主键或唯一索引扫描。
explain select * from t1 ,t2 where t1.id = t2.id;

# 非唯一性索引扫描， 返回匹配某个单独值的所有行.本质上也是一种索引访问， 它返回所有匹配某个单独值的行，
# 然而， 它可能会找到多个符合条件的行， 所以他应该属于查找和扫描的混合体。
# explain select * from t1 ,t2 where t1.content = t2.content;
create index idx_t2_content on t2 (content);
explain select * from t1 ,t2 where t1.content = t2.content;

# 只检索给定范围的行,使用一个索引来选择行。 key 列显示使用了哪个索引一般就是在你的 where 语句中出现
# 了 between、 <、 >、 in 等的查询这种范围扫描索引扫描比全表扫描要好， 因为它只需要开始于索引的某一点， 而
# 结束语另一点， 不用扫描全部索引。
explain select * from t1 where t1.id < 10;

# 出现index是sql使用了索引但是没用通过索引进行过滤， 一般是使用了覆盖索引或者是利用索引进行了排序分组。
explain select * from t1;
explain select id from t1;

# Full Table Scan， 将遍历全表以找到匹配的行。
explain select * from t1;

# 在查询过程中需要多个索引组合使用， 通常出现在有 or 的关键字的 sql 中。
explain select * from t2 where t2.content is null or t2.id=100;
# 描述不一致

# 对于某个字段既需要关联条件， 也需要 null 值得情况下。 查询优化器会选择用 ref_or_null 连接查询。
explain select * from t2 where t2.content is null or t2.content='a';

# 利用索引来关联子查询， 不再全表扫描。
explain select * from t2 where t2.content in (select t3.content from t3);
create index idx_t2_content on t3 (content);
explain select * from t2 where t2.content in (select t3.content from t3);

# 该联接类型类似于 index_subquery。 子查询中的唯一索引
explain select * from t2 where t2.id in (select t3.id from t3);

