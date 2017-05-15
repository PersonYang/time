package com.apin.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/19.
 */

public class Redis {

    private static final Logger logger= LoggerFactory.getLogger(Redis.class);

    @Autowired
    private RedisExecuteTemplate redisExecuteTemplate;

    public void setRedisExecuteTemplate(RedisExecuteTemplate redisExecuteTemplate) {
        this.redisExecuteTemplate = redisExecuteTemplate;
    }

    /**
     *
     * @param redisType
     * @param key
     * @return
     * @throws Throwable
     */
    public String get(RedisType redisType,String key) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String)redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            public Object command(Jedis jedis) {
                return jedis.get(redisKey);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param field
     * @return
     * @throws Throwable
     */
    public String hget(RedisType redisType,String key, final String field) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String)redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hget(redisKey, field);
            }
        });
    }


    /**
     *
     * @param redisType
     * @param key
     * @param field
     * @param value
     * @return
     * @throws Throwable
     */
    public long hset(RedisType redisType,String key,final String field,final String value) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (long)redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hset(redisKey, field,value);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param fields
     * @return
     * @throws Throwable
     */
    public List<String> hmget(RedisType redisType,String key, final String... fields) throws  Throwable{
        final String redisKey=redisType.toString()+key;
        return (List<String>) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hmget(redisKey, fields);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @return
     * @throws Throwable
     */
    public boolean exists(RedisType redisType,String key) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (boolean) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.exists(redisKey);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param fieldValuePair
     * @return
     * @throws Throwable
     */
    public String hmset(RedisType redisType,String key,final Map<String,String> fieldValuePair) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.hmset(redisKey, fieldValuePair);
            }
        });
    }

    /**
     *
     * @param redisType
     * @param key
     * @param delay
     * @return
     * @throws Throwable
     */
    public long expire(RedisType redisType,String key, final int delay) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (long) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.expire(redisKey, delay);
            }
        });
    }

    /**
     * only when the key does not exist
     * @param redisType
     * @param key
     * @param value
     * @param delay
     * @return
     * @throws Throwable
     */
    public String setnxex(RedisType redisType,String key,final String value,final int delay) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (String) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.set(redisKey, value, "nx", "ex", delay);
            }
        });
    }


    /**
     *
     * @param redisType
     * @param key
     * @return
     * @throws Throwable
     */
    public long delete(RedisType redisType,String key) throws Throwable{
        final String redisKey=redisType.toString()+key;
        return (long) redisExecuteTemplate.excute(new RedisExecuteTemplate.ExecuteCallback() {
            @Override
            public Object command(Jedis jedis) {
                return jedis.del(redisKey);
            }
        });
    }


}
