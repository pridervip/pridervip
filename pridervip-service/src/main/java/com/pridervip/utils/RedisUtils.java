package com.pridervip.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {
	private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);

	public void set(Integer index, final byte[] key, final byte[] value,
					final long liveTime) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	public void set(Integer index, String key, String value, long liveTime) {
		this.set(index, key.getBytes(), value.getBytes(), liveTime);
	}

	public Object get(Integer index, final String key) {
		final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.get(key.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redisTemplate.getStringSerializer().deserialize(
							bvalue);
				}
			}
		});
	}

	public Object hGet(Integer index, final String key, final String field) {
		final RedisTemplate<String, ?> redisTemplate = getRedisTemplate(index);
		return redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection conn) {
				byte[] bvalue = conn.hGet(key.getBytes(), field.getBytes());
				if (null == bvalue) {
					return null;
				} else {
					return redisTemplate.getStringSerializer().deserialize(
							bvalue);
				}
			}
		});
	}

	public void hSet(Integer index, final String key, final String field,
					 final String value) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				conn.hSet(key.getBytes(), field.getBytes(), value.getBytes());
				return null;
			}
		});
	}

	public void rPush(Integer index, final String key, final String value) {
		getRedisTemplate(index).execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection conn)
					throws DataAccessException {
				conn.rPush(key.getBytes(), value.getBytes());
				return null;
			}
		});
	}

	public void expired(Integer index, String key, long timeout) {
		getRedisTemplate(index).expire(key, timeout, TimeUnit.HOURS);
	}

	private RedisTemplate<String, ?> getRedisTemplate(Integer index) {
		return ApplicationContextUtils.getBean("redisTemplate" + index);
	}

}