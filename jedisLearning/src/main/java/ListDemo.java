import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class ListDemo {


    public static void test1(Jedis jedis) {
        System.out.println("lpush and lpop and rpush and rpop and lrange and llen");
        jedis.flushDB();
        jedis.lpush("key", "1", "2", "3", "4");
        System.out.println(jedis.lrange("key", 0, -1));
        System.out.println(jedis.lpop("key"));
        jedis.rpush("key", "1", "2", "3", "4");
        System.out.println(jedis.lrange("key", 0, -1));
        System.out.println(jedis.rpop("key"));
        System.out.println(jedis.lrange("key", 0, -1));
        System.out.println(jedis.llen("key"));

    }

    public static void test2(Jedis jedis) {
        System.out.println("blpop and rpop");
        jedis.flushDB();
        List<String> list = jedis.brpop(100, "key");
        System.out.println("list[0]: " + list.get(0));
        System.out.println("list[1]: " + list.get(1));
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("tcp://127.0.0.1:8888");

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.auth("147896325.drh");
            // test1(jedis);
            test2(jedis);
        }
    }
}
