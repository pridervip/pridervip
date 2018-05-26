/*
 * Huobi.com Inc.
 *Copyright (c) 2014 火币天下网络技术有限公司. All Rights Reserved
 */
package com.pridervip.gate.utils;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.stereotype.Component;

@Component("redisUtils")
public class RedisUtils {

    private RedisTemplate<String, ?> redisTemplate0;

    private RedisTemplate<String, ?> redisTemplate6;
    
    public void set(int index, final String key, final String value, final Long liveTime) {
        getRedisTemplate(index).execute(new RedisCallback<Object>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(key.getBytes(), value.getBytes());
                if (null != liveTime && liveTime>0) {
                    connection.expire(key.getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }
    public void hSet(int index,final String key, final String field, final String value, final Long liveTime) {
        getRedisTemplate(index).execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                conn.hSet(key.getBytes(), field.getBytes(), value.getBytes());
                if (null != liveTime && liveTime>0) {
                    conn.expire(key.getBytes(), liveTime);
                }
                return 1L;
            }
        });
    }
    
    public Object get(int index, final String key) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) {
                byte[] bvalue = conn.get(key.getBytes());
                if (null == bvalue) {
                    return null;
                } else {
                    return redisTemplate.getStringSerializer().deserialize(bvalue);
                }
            }
        });
    }

    public Object hGet(int index, final String key, final String field) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) {
                byte[] bvalue = conn.hGet(key.getBytes(), field.getBytes());
                if (null == bvalue) {
                    return null;
                } else {
                    return redisTemplate.getStringSerializer().deserialize(bvalue);
                }
            }
        });
    }    

    public long del(int index, final String... keys) {
        return (long) getRedisTemplate(index).execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                long result = 0;
                for (int i = 0; i < keys.length; i++) {
                    result = result + connection.del(keys[i].getBytes());
                }
                return result;
            }
        });
    }
    public void hDel(int index,final String key, final String field) {
        getRedisTemplate(index).execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection conn) throws DataAccessException {
                return conn.hDel(key.getBytes(), field.getBytes());
            }
        });
    }
    
    @SuppressWarnings("unchecked")
	public List<Object> mget(int index, final String[] key) {
        final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
        return (List<Object>) redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection conn) {
                List<byte[]> bvalue = conn.mGet(serializeMulti(redisTemplate, key));
                if (null == bvalue) {
                    return null;
                } else {
                    return SerializationUtils.deserialize(bvalue, redisTemplate.getStringSerializer());
                }
            }
        });
    }
    
    
    
    /**
     * @param index
     * @return
     */
    private RedisTemplate<String, ?> getRedisTemplate(int index) {
        return redisTemplate0;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private byte[][] serializeMulti(RedisTemplate redisTemplate, String... keys) {
		byte[][] ret = new byte[keys.length][];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = redisTemplate.getStringSerializer().serialize(keys[i]);
		}
		return ret;
	}
}
