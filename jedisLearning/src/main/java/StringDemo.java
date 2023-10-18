import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class StringDemo {

    public static void test1(Jedis jedis) {
        System.out.println("mget and mset");
        jedis.flushDB();

        jedis.mset("key1", "111", "key2", "222", "key3", "333");
        List<String> list = jedis.mget("key1", "key2", "key4", "key3");
        System.out.println(list.toString());
    }

    public static void test2(Jedis jedis) {
        System.out.println("getrange and setrange");
        jedis.flushDB();
        jedis.set("key", "fasdfadsf");
        System.out.println(jedis.getrange("key", 1, 4));
        jedis.setrange("key", 2, "ding");
        System.out.println(jedis.get("key"));
    }

    public static void test3(Jedis jedis) {
        System.out.println("append and incr and decr");
        jedis.flushDB();
        jedis.set("key", "hello");
        jedis.set("key1", "12");
//        jedis.append("key", " world");
//        System.out.println(jedis.get("key"));
        System.out.println(jedis.incr("key1"));
        System.out.println(jedis.decr("key1"));
        System.out.println(jedis.incrBy("key1", 10));
        System.out.println(jedis.decrBy("key1", 30));
        System.out.println(jedis.incrByFloat("key1", + 10.3));
        System.out.println(jedis.incrByFloat("key1", -0.3));
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("tcp://127.0.0.1:8888");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("147896325.drh");
            // test1(jedis);
            // test2(jedis);
            test3(jedis);
        }
    }
}
