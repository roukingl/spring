import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class SetDemo {

    public static void test1(Jedis jedis) {
        System.out.println("sadd and smembers");
        jedis.flushDB();
        jedis.sadd("key", "111", "222", "333");
        Set<String> set = jedis.smembers("key");
        System.out.println(set);
    }

    public static void test2(Jedis jedis) {
        System.out.println("sismember");
        jedis.flushDB();
        jedis.sadd("key", "111", "222");
        System.out.println(jedis.sismember("key", "1"));

    }

    public static void test3(Jedis jedis) {
        System.out.println("scard");
        jedis.flushDB();
        jedis.sadd("key", "111", "222", "333");
//        long result = jedis.scard("key");
//        System.out.println(result);
//        System.out.println(jedis.spop("key", 2));
//        System.out.println(jedis.srem("key", "111"));
//        System.out.println(jedis.scard("key"));
//        System.out.println(jedis.srandmember("key", 2));

    }

    public static void test4(Jedis jedis) {
        System.out.println("sinter and sinterstore");
        jedis.flushDB();
        jedis.sadd("key1", "111", "222", "333");
        jedis.sadd("key2", "222", "333", "444");

//        Set<String> set = jedis.sinter("key1", "key2");
//        System.out.println(set);
//        System.out.println(jedis.sintercard("key1", "key2"));

        System.out.println(jedis.sinterstore("key3", "key1", "key2"));
        System.out.println(jedis.smembers("key3"));
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("tcp://127.0.0.1:8888");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("147896325.drh");
            // test1(jedis);
            // test2(jedis);
            // test3(jedis);
            test4(jedis);
        }
    }
}
