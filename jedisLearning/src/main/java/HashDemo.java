import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashDemo {

    public static void test1(Jedis jedis) {
        System.out.println("hset and hset");
        jedis.flushDB();

        jedis.hset("key1", "field1", "111");
        Map<String, String> map = new HashMap<>();
        map.put("field2", "222");
        map.put("field3", "333");
        jedis.hset("key1", map);

        System.out.println(jedis.hget("key1", "field1"));
        System.out.println(jedis.hget("key1", "field3"));
        System.out.println(jedis.hget("key1", "f1"));
    }

    public static void test2(Jedis jedis) {
        System.out.println("hexists and hdel");
        jedis.flushDB();

        jedis.hset("key", "f1", "11");
        jedis.hset("key", "f2", "11");
        jedis.hset("key", "f3", "11");

        System.out.println(jedis.hexists("key", "f1"));
        System.out.println(jedis.hexists("key", "f4"));

        System.out.println(jedis.hdel("key", "f1"));
        System.out.println(jedis.hexists("key", "f1"));
    }

    public static void test3(Jedis jedis) {
        System.out.println("hkeys and hvalues and hgetall");
        jedis.flushDB();

        jedis.hset("key", "f1", "11");
        jedis.hset("key", "f2", "11");
        jedis.hset("key", "f3", "11");

        System.out.println(jedis.hkeys("key"));
        System.out.println(jedis.hvals("key"));
        System.out.println(jedis.hgetAll("key"));
    }

    public static void test4(Jedis jedis) {
        System.out.println("hmset and hmget and hlen");
        jedis.flushDB();

        Map<String, String> map = new HashMap<>();
        map.put("f1", "111");
        map.put("f2", "222");
        map.put("f3", "333");
        jedis.hmset("key", map);
        System.out.println(jedis.hmget("key", "f1", "f2", "h3", "f4"));
        System.out.println(jedis.hlen("key"));
    }

    public static void test5(Jedis jedis) {
        System.out.println("hincrby and hincrfloat");
        jedis.flushDB();

        Map<String, String> map = new HashMap<>();
        map.put("f1", "111");
        map.put("f2", "222");
        map.put("f3", "333");
        map.put("f4", "100");
        jedis.hmset("key", map);

        System.out.println(jedis.hincrBy("key", "f1", 10));
        System.out.println(jedis.hincrBy("key", "f2", -100));
        System.out.println(jedis.hincrByFloat("key", "f3", 10.3));
        System.out.println(jedis.hincrByFloat("key", "f4", -10.2));
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("tcp://127.0.0.1:8888");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("147896325.drh");
            // test1(jedis);
            // test2(jedis);
            // test3(jedis);
            // test4(jedis);
            test5(jedis);
        }
    }
}
