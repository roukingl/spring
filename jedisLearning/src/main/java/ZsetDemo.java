import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.io.FilterOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZsetDemo {

    public static void test1(Jedis jedis) {
        System.out.println("zadd and zrange and zrangewithscores");
        jedis.flushDB();

        jedis.zadd("key",  95.2, "zhangsan");

        Map<String, Double> map = new HashMap<>();
        map.put("lisi", 30.2);
        map.put("wangwu", 14.3);
        jedis.zadd("key", map);

//        System.out.println(jedis.objectEncoding("key"));

        System.out.println(jedis.zrange("key", 0, -1));

        List<Tuple> tuple = jedis.zrangeWithScores("key", 0, -1);
        System.out.println(tuple);
        String member = tuple.get(0).getElement();
        double score = tuple.get(0).getScore();
        System.out.println("member: " + member + " score: " + score);

        List<String> list = jedis.zrangeByScore("key", 10, 40);
        System.out.println(list);

        List<Tuple> tupleList = jedis.zrangeByScoreWithScores("key", 0, 40);
        System.out.println(tupleList);
    }

    public static void test2(Jedis jedis) {
        System.out.println("zcard and zscore");
        jedis.flushDB();

        Map<String, Double> map = new HashMap<>();
        map.put("zhangsan", 48.2);
        map.put("lisi", 30.2);
        map.put("wangwu", 14.3);
        jedis.zadd("key", map);

        System.out.println(jedis.zcard("key"));
        System.out.println(jedis.zscore("key", "lisi"));
        System.out.println(jedis.zscore("key", "ding"));
    }

    public static void test3(Jedis jedis) {
        System.out.println("zrem and reverse查看 and zrank and zcount");
        jedis.flushDB();

        Map<String, Double> map = new HashMap<>();
        map.put("zhangsan", 48.2);
        map.put("lisi", 30.2);
        map.put("wangwu", 14.3);
        jedis.zadd("key", map);

//        System.out.println(jedis.zrange("key", 0, -1));
//        System.out.println(jedis.zrangeWithScores("key", 0, -1));
//        System.out.println(jedis.zrem("key", "lisi", "ding", "wangwu"));
//        System.out.println(jedis.zrangeWithScores("key", 0, -1));

//        System.out.println(jedis.zrevrange("key", 0, -1));
//        System.out.println(jedis.zrevrangeWithScores("key", 0, -1));

//        System.out.println(jedis.zrank("key", "wangwu"));
//        System.out.println(jedis.zcount("key", "10", "38"));
//        System.out.println(jedis.zremrangeByScore("key", 14, 43));
//        System.out.println(jedis.zrangeWithScores("key", 0, -1));
        
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
