package com.springcloud.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisParameter redisParameter;


    private String getRedisKey() {
        return redisParameter.getPerKey() + ":";
    }

    /**
     * 加锁的lua脚本
     */
    private final static RedisScript<Long> LOCK_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call(\"setnx\", KEYS[1], KEYS[2]) == 1 then return redis.call(\"expire\", KEYS[1], KEYS[3]) else return 0 end"
            , Long.class
    );

    /**
     * 加锁失败结果
     */
    private final static Long LOCK_FAIL = 0L;

    /**
     * 解锁的lua脚本
     */
    private final static RedisScript<Long> UNLOCK_LUA_SCRIPT = new DefaultRedisScript<>(
            "if redis.call(\"get\",KEYS[1]) == KEYS[2] then return redis.call(\"del\",KEYS[1]) else return -1 end"
            , Long.class
    );

    /**
     * 解锁失败结果
     */
    private final static Long UNLOCK_FAIL = -1L;

    /**
     * 加锁方法
     * 对key加锁，value为key对应的值，expire是锁自动过期时间防止死锁
     *
     * @param key    key
     * @param value  value
     * @param expire 锁自动过期时间(秒）
     * @return 是否加锁成功
     */
    public boolean lock(String key, String value, Long expire) {
        if (key == null || value == null || expire == null) {
            return false;
        }
        List<String> keys = Arrays.asList(getRedisKey() + key, value, expire.toString());
        Long res = redisTemplate.execute(LOCK_LUA_SCRIPT, keys);
        return !LOCK_FAIL.equals(res);
    }

    /**
     * 加锁方法
     * 对key加锁，value为key对应的值，默认30秒过期时间，请确保业务能在30秒内完成，否则请传入过期时间
     *
     * @param key   key
     * @param value value
     */
    public boolean lock(String key, String value) {
        return lock(key, value, 30L);
    }

    /**
     * 解锁方法
     * 对key解锁，只有value值等于redis中key对应的值才能解锁，避免误解锁
     *
     * @param key   key
     * @param value value
     */
    public boolean unlock(String key, String value) {
        if (key == null || value == null) {
            return false;
        }
        List<String> keys = Arrays.asList(getRedisKey() + key, value);
        Long res = redisTemplate.execute(UNLOCK_LUA_SCRIPT, keys);
        return !UNLOCK_FAIL.equals(res);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(getRedisKey() + key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*** 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(getRedisKey() + key, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(getRedisKey() + key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(getRedisKey() + key));
            }
        }
    }

    /**
     * 模糊匹配删除
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delByFuzzy(String key) {
        Set<String> keys = null;
        if (key != null) {
            keys = redisTemplate.keys(getRedisKey() + key + (key.endsWith(":") ? "*" : ":*"));
        }
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
    // ============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(getRedisKey() + key);
    }

    /**
     * 模糊查询缓存key
     *
     * @param key 键
     * @return 值
     */
    public Set<String> getKeys(String key) {
        if (key == null) {
            return new HashSet<>();
        }
        Set<String> keys = redisTemplate.keys(getRedisKey() + key);
        Set<String> temp = new HashSet<>();
        if (keys != null) {
            keys.forEach(item -> temp.add(item.replace(getRedisKey(), "")));
        }
        return temp;
    }

    /**
     * 模糊查询缓存key
     *
     * @param key 键
     * @return 值
     */
    public Set<String> getLikeKeys(String key) {
        if (key == null) {
            return new HashSet<>();
        }
        Set<String> keys = redisTemplate.keys("*" + getRedisKey() + key + "*");
        Set<String> temp = new HashSet<>();
        if (keys != null) {
            keys.forEach(item -> {
                temp.add(item.replace(getRedisKey(), ""));
            });
        }
        return temp;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(getRedisKey() + key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(getRedisKey() + key, value, time, TimeUnit.SECONDS);
            } else {
                set(getRedisKey() + key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
