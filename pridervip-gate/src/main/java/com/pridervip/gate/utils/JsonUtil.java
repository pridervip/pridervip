/*
package com.bighuobi.api.gate.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.*;

*/
/**
 * JSON转换工具,此类能够完成Object到String以及String到Object的相互转换。
 * <p>
 * 
 * 
 * @author zhengrunjin
 * @since 0.1
 *//*

public class JsonUtil {


    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    */
/**
     * JSON转换器，所有JSON操作都依赖于此类。
     *//*

    private static ObjectMapper mapper = new ObjectMapper();

    */
/**
     * 静态块，当JsonUtil第一次被调用时执行，切只执行一次。
     *//*

    static {

        // 定义日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        
       
        mapper.setDateFormat(dateFormat);        
        // 反序列化时忽略多余的属性
        mapper.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        // TODO:搞明白这几个属性
        // 忽略Null的值,节省空间.
        // mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        // 忽略Default值木有变化的属性,更节省空间,用于接收方有相同的Class
        // 如int属性初始值为0,那么这个属性将不会被序列化
        // mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_DEFAULT);
    }


    */
/**
     * 获得JSON字符串字段的字符串形式。
     * <p>
     * 注意
     * <ol>
     * <li>若字段的值是一个字符串，则返回时会多双引号。例{"name":"richard"},方法返回时如下:""richard""，而非 "richard"</li>
     * </ol>
     * 
     * @param jsonStr JSON字符串。
     * @param fieldName 字段名称。
     * @return <p>
     *         当找不到字段时，返回{@link StringUtil#EMPTY}。
     *         <p>
     *         当找到字段时，返回表示其字符串形式。
     * 
     * @throws Exception 解析发生错误。
     *//*

    public static JsonNode getField(String jsonStr, String fieldName) throws Exception 
    {

        log.debug("getFiledString() executing jsonStr={},fieldName={}", jsonStr, fieldName);
        if (jsonStr == null) {
            return null;
        }
        if (fieldName == null) {
            return null;
        }
        JsonNode rootNode = readValue(jsonStr, JsonNode.class);
        if (rootNode == null) 
        {        
            return null;
        }
        for(String thisFN : fieldName.split("\\."))
        {
            rootNode = rootNode.get(thisFN);            
        }
        return rootNode;
    }
    public static String getFieldString(String jsonStr, String fieldName) throws Exception {
        log.debug("getFiledString() executing jsonStr={},fieldName={}", jsonStr, fieldName);
        if (jsonStr == null) {
            return null;
        }
        JsonNode rootNode = readValue(jsonStr, JsonNode.class);
        if (rootNode == null) {
            log.error("readValue() return is null.jsonStr={}", jsonStr);
            return null;
        }
        JsonNode path = rootNode.path(fieldName);
        if (!path.isMissingNode()) {
            return path.toString();
        } else {
            log.error("path is missing node,fieldName={}", fieldName);
            return null;
        }
    }
    */
/**
     * 将对象转换为JSON字符串。
     * 
     * <b>已知问题</b>
     * <ol>
     * <li>设User u; Agent a;Object o; 当u依赖a，a又依赖u，且o同时依赖u和a则不能正常转化。</li>
     * </ol>
     * 
     * 
     * @param obj 需要转换的对象
     * 
     * @return 当转换成功，则返回JSON字符串。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static String toJson(Object obj) throws Exception {
        if (obj == null) {
            log.error("Prameter 'obj' is null.");
            return StringUtils.EMPTY;
        }
        return mapper.writeValueAsString(obj);
    }
    
    public static String toString(Object obj) 
    {
       try {
        return toJson(obj);
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    }
    
    */
/**
     * 将一个json字符串转化为一个java对象
     * 
     * @param <T>
     * @param jsonStr json格式的字符串
     * @param clazz 目标类型
     * @return 返回类型为T的对象
     * @throws Exception
     *//*

    public static <T> T toObject(String jsonStr, Class<? extends T> clazz) {
        if (jsonStr == null) {
            log.error("Prameter 'jsonStr' is null.");
            return null;
        }
        return readValue(jsonStr, clazz);
    }

    */
/**
     * 一个jsonStr包含多个java对象，取其中一个转化为java对象的方法
     * 
     * @param content json格式的字符串
     * @param key 要转换的子json串的key
     * @param clazz 目标类型
     * @return 返回类型为T的对象
     * @throws Exception
     *//*

    public static <T> T toObject(String content, String key, Class<? extends T> clazz) throws Exception {
        JsonNode rootNode = mapper.readValue(content, JsonNode.class);
        JsonNode path = rootNode.path(key);
        if (!path.isMissingNode()) {
            return toObject(path.toString(), clazz);
        } else {
            return null;
        }
    }

    */
/**
     * 将JSON字符串转换为List，采用ArrayList返回。
     * 
     * @param <T> 元素类型
     * @param jsonStr 需要转换的JSON字符串
     * @param clazz 元素类型，与T类型相同
     * @return 当解析成功，返回一组clazz类型的列表。
     * 
     * @throws Exception 解析发生错误
     *//*

    @SuppressWarnings("deprecation")
    public static <T> List<T> toList(String jsonStr, Class<? extends T> clazz) throws Exception {
        if (jsonStr == null) {
            log.error("Prameter 'jsonStr' is null.");
            return null;
        }
        return readValue(jsonStr, TypeFactory.collectionType(ArrayList.class, clazz));
    }

    */
/**
     * 将JSON字符串的某一字段转换为List，采用ArrayList返回。
     * 
     * @param <T> 元素类型
     * @param jsonStr 需要转换的JSON字符串
     * @param fieldName 字段名称
     * @param clazz 元素类型，与T类型相同
     * @return 当解析成功，返回一组clazz类型的列表。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static <T> List<T> toList(String jsonStr, String fieldName, Class<? extends T> clazz) throws Exception {
        return toList(getFieldString(jsonStr, fieldName), clazz);
    }

    */
/**
     * 将JSON字符串转换为数组。
     * 
     * @param <T> 元素类型。
     * @param jsonStr JSON字符串
     * @param clazz 元素类型，与T类型相同。
     * 
     * @return 当解析成功，返回一个数组。
     * 
     * @throws Exception 解析发生错误
     *//*

    @SuppressWarnings("deprecation")
    public static <T> T[] toArray(String jsonStr, Class<? extends T> clazz) throws Exception {
        if (jsonStr == null) {
            log.error("Prameter 'jsonStr' is null.");
            return null;
        }
        return readValue(jsonStr, TypeFactory.arrayType(clazz));
    }

    */
/**
     * 将JSON字符串的某一字段转换为数组。
     * 
     * @param <T> 元素类型。
     * @param jsonStr JSON字符串
     * @param fieldName 字段名称
     * @param clazz 元素类型，与T类型相同。
     * 
     * @return 当解析成功，返回一个数组。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static <T> T[] toArray(String jsonStr, String fieldName, Class<? extends T> clazz) throws Exception {
        return toArray(getFieldString(jsonStr, fieldName), clazz);
    }

    */
/**
     * 将JSON字符串的某一字段转换为日期。
     * 
     * @param jsonStr JSON字符串
     * @param fieldName 字段名称
     * @return 当转换成功，则返回日期。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static Date toDate(String jsonStr, String fieldName) throws Exception {
        return toObject(getFieldString(jsonStr, fieldName), Date.class);
    }

    */
/**
     * 将JSON字符串的某一字段转换为整形。
     * 
     * @param jsonStr JSON字符串
     * @param fieldName 字段名称
     * @return 当转换成功，则返回整形。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static Integer toInteger(String jsonStr, String fieldName) throws Exception {
        return toObject(getFieldString(jsonStr, fieldName), Integer.class);
    }

    */
/**
     * 将JSON字符串转换为整形数组。
     * 
     * @param jsonStr JSON字符串
     * @return 当转换成功，则返回整形数组。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static Integer[] toIntegerArray(String content) throws Exception {
        return toArray(content, Integer.class);
    }

    */
/**
     * 将JSON字符串的某一字段转换为整形数组。
     * 
     * @param jsonStr JSON字符串
     * @param fieldName 字段名称
     * @return 当转换成功，则返回整形数组。
     * 
     * @throws Exception 解析发生错误
     *//*

    public static Integer[] toIntegerArray(String jsonStr, String fieldName) throws Exception {
        return toIntegerArray(getFieldString(jsonStr, fieldName));
    }

    */
/**
     * 将对象转换为JSON字符串，并写入OutputStream。
     * 
     * @param output 将被写入的输出流。
     * @param o 需要转换的对象。
     * @throws Exception 解析或写入发生错误
     * 
     *//*

    public static void writeJson(OutputStream output, Object o) throws Exception {
        mapper.writeValue(output, o);
    }

    */
/**
     * 将对象转换为JSON字符串，并写入Writer。
     * 
     * @param output 将被写入的输出流。
     * @param o 需要转换的对象。
     * @throws Exception 解析或写入发生错误
     *//*

    public static void writeJson(Writer output, Object o) throws Exception {
        mapper.writeValue(output, o);
    }

    */
/**
     * 读取一个字符串到JSON对象。
     * 
     * @param jsonStr JSON字符串
     * @param clazz JSON对象类型。
     * @return JSON对象。
     *//*

    private static <T> T readValue(String jsonStr, Class<? extends T> clazz) {
        T rootNode = null;
        try {
            rootNode = mapper.readValue(jsonStr, clazz);
        } catch (JsonParseException e) {
            log.error("read json pase error {}", jsonStr, e);
        } catch (JsonMappingException e) {
            log.error("read json maping error {}", jsonStr, e);
        } catch (IOException e) {
            log.error("read json io error {}", jsonStr, e);
        }
        return rootNode;
    }

    */
/**
     * 读取一个字符串到JSON对象。
     * 
     * @param jsonStr JSON字符串
     * @param clazz JSON对象类型。
     * @return JSON对象。
     *//*

    private static <T> T readValue(String jsonStr, JavaType valueType) {
        T rootNode = null;
        try {
            rootNode = mapper.readValue(jsonStr, valueType);
        } catch (JsonParseException e) {
            log.error("read json pase error {}", jsonStr, e);
        } catch (JsonMappingException e) {
            log.error("read json maping error {}", jsonStr, e);
        } catch (IOException e) {
            log.error("read json io error {}", jsonStr, e);
        }
        return rootNode;
    }    
    
    ///////////////////////////////////////////////////////
    // JSON 数据操作
    ///////////////////////////////////////////////////////
    
    */
/**
     * 将String转换为JSON对象。
     *//*

    public static JSONObject toJsonObj(String strJson) {
    	JSONObject tempNode = null;
    	
        try {
			if (strJson == null) {
			    return tempNode;
			}
			
			tempNode = new JSONObject(strJson);
		} catch (Exception e) {
			tempNode = null;
			e.printStackTrace();
		}
        
        return tempNode;
    }

    public static JSONArray toJsonArray(String strJson) {
    	JSONArray tempNode = null;
    	
        try {
			if (strJson == null) {
			    return tempNode;
			}
			
			tempNode = new JSONArray(strJson);
		} catch (Exception e) {
			tempNode = null;
			e.printStackTrace();
		}
        
        return tempNode;
    }
    
	// json数据操作
	public static JSONObject getNodeJSonItem(JSONObject jsonObject, String key) {
		JSONObject temp = null;
		try {
			if (jsonObject == null) {
				return null;
			}

			temp = (JSONObject) jsonObject.get(key);
		} catch (Exception e) {
			temp = null;
		}

		return temp;
	}

	public static JSONObject getNodeJSonItem(JSONArray jsonObject, int index) {
		JSONObject temp = null;
		try {
			if (jsonObject == null) {
				return null;
			}

			temp = (JSONObject) jsonObject.getJSONObject(index);
		} catch (Exception e) {
			temp = null;
		}

		return temp;
	}

	public static JSONArray getNodeJSonArray(JSONObject jsonObject, String key) {
		JSONArray temp = null;
		try {
			if (jsonObject == null) {
				return null;
			}

			temp = jsonObject.getJSONArray(key);
		} catch (Exception e) {
			temp = null;
		}

		return temp;
	}

	public static String getNodeJSonValue(JSONObject jsonObject, String key) {
		String temp = "";
		try {
			if (jsonObject == null || key == null || key.equals("")) {
				return temp;
			}

			temp = jsonObject.getString(key);
		} catch (Exception e) {
		}

		if (temp == null) {
			temp = "";
		}

		return temp;
	}

	public static Integer getNodeJSonValueIntNumber(JSONObject jsonObject, String key) {
		Integer temp = null;
		try {
			if (jsonObject == null || key == null || key.equals("")) {
				return temp;
			}

			temp = jsonObject.getInt(key);
		} catch (Exception e) {
			temp = null;
		}

		return temp;
	}

	public static Integer getNodeJSonValueInt(JSONObject jsonObject, String key) {
		Integer temp = null;
		try {
			String tempStr = getNodeJSonValue(jsonObject, key);
			if (tempStr == null || tempStr.equals("")) {
				return temp;
			}

			temp = toObject(tempStr, Integer.class);
		} catch (Exception e) {
		}

		if (temp == null) {
			temp = null;
		}

		return temp;
	}

	public static Float getNodeJSonValueFloat(JSONObject jsonObject, String key) {
		Float temp = null;
		try {
			String tempStr = getNodeJSonValue(jsonObject, key);
			if (tempStr == null || tempStr.equals("")) {
				return temp;
			}

			temp = toObject(tempStr, Float.class);
		} catch (Exception e) {
		}

		if (temp == null) {
			temp = null;
		}

		return temp;
	}

	public static BigDecimal getNodeJSonValueBigDecimal(JSONObject jsonObject, String key) {
		BigDecimal temp = null;
		try {
			String tempStr = getNodeJSonValue(jsonObject, key);
			if (tempStr == null || tempStr.equals("")) {
				return temp;
			}

			temp = new BigDecimal(tempStr);
		} catch (Exception e) {
		}

		if (temp == null) {
			temp = null;
		}

		return temp;
	}

	public static Long getNodeJSonValueLong(JSONObject jsonObject, String key) {
		Long temp = null;
		try {
			String tempStr = getNodeJSonValue(jsonObject, key);
			if (tempStr == null || tempStr.equals("")) {
				return temp;
			}

			temp = toObject(tempStr, Long.class);
		} catch (Exception e) {
		}

		if (temp == null) {
			temp = null;
		}

		return temp;
	}

    ///////////////////////////////////////////////////////
    // JSON 数据操作
    ///////////////////////////////////////////////////////
    
    public static void main(String[] adfadf) throws Exception
    {
      int a = 123;
      String b =toString(a);
        System.out.println(b);
    }
    
}
*/
