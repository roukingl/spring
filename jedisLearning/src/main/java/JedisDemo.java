import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Set;

public class JedisDemo {

    public static void test1(Jedis jedis) {
        System.out.println("get 和 set 的使用");
        jedis.flushDB();
        jedis.set("key1", "111");
        jedis.set("key2", "222");
        SetParams params = new SetParams();
        params.ex(10);
        params.nx();
        jedis.set("key1", "333", params);
        System.out.println(jedis.get("key1"));
    }


    public static void test2(Jedis jedis) {
        System.out.println("exists and del");
        jedis.flushDB();
        jedis.set("key1", "111");
        jedis.set("key2", "222");
        System.out.println(jedis.exists("key1"));
        System.out.println(jedis.del("key1"));
//        System.out.println(jedis.exists("key1", "key2", "key3"));
        System.out.println(jedis.exists("key1"));
    }

    public static void test3(Jedis jedis) {
        System.out.println("keys");
        jedis.flushDB();
        jedis.mset("key1", "111", "key2", "222", "key3", "333", "kay1", "saf", "ksgsy1", "ding");
        Set<String> set = jedis.keys("k[a-f]y1");
        System.out.println(set);
    }

    public static void test4(Jedis jedis) throws InterruptedException {
        System.out.println("expire and ttl");
        jedis.flushDB();
        jedis.set("key1", "111");
        jedis.set("key2", "222");
        System.out.println(jedis.expire("key1", 100));
        Thread.sleep(2000);
        System.out.println(jedis.ttl("key1"));

    }

    public static void test5(Jedis jedis) {
        System.out.println("type");
        jedis.flushDB();
        jedis.set("key1", "111");
        jedis.lpush("key2", "1", "2", "3", "4");
        jedis.hset("key3", "field1", "111");
        jedis.sadd("key4", "11", "2", "4");
        jedis.zadd("key5", 97, "ding");
        String type = jedis.type("key1");
        System.out.println(type);
        System.out.println(jedis.type("key2"));
        System.out.println(jedis.type("key3"));
        System.out.println(jedis.type("key4"));
        System.out.println(jedis.type("key5"));
    }

    public static void main(String[] args) {

        JedisPool jedisPool = new JedisPool("tcp://127.0.0.1:8888");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("147896325.drh");
            // String pong = jedis.ping();
            // System.out.println(pong);
            // test1(jedis);
            // test2(jedis);
            // test3(jedis);
            // test4(jedis);
            // test5(jedis);
        }
    }

}
