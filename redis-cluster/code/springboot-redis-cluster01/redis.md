## Redis简介
https://blog.csdn.net/miss1181248983/article/details/90056960?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-1-90056960-blog-124599828.pc_relevant_vip_default&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-1-90056960-blog-124599828.pc_relevant_vip_default&utm_relevant_index=1
https://www.cnblogs.com/gossip/p/5993922.html

https://www.cnblogs.com/chihuobao/p/16166234.html
Redis：目前使用最广泛的缓存中间件，非关系数据库。

## 存储类型

数据以Key-Value的形式存储，支持以下数据类型：
- String（字符串）
- Hash（哈希）
- List（列表）
- Set（集合）
- Zset（Sorted Set：有序集合）

## 优势

- 性能极高：读的速度可达到110000次/s,写的速度是81000次/s 。
- 丰富的数据类型：Redis支持二进制案例的 Strings, Lists, Hashes, Sets 及 Ordered Sets 数据类型操作。
- 原子性：支持高并发。
- 丰富的特性：Redis还支持 publish/subscribe, 通知, key 过期等等特性。

同时，还支持数据的定时持久化等。
