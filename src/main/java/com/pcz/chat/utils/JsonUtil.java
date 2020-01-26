package com.pcz.chat.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @author picongzhi
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串
     *
     * @param data 对象
     * @return json字符串
     */
    public static String objectToJson(Object data) {
        try {
            return OBJECT_MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json数据转换成对象
     *
     * @param jsonData json数据
     * @param clazz    类
     * @param <T>      泛型类
     * @return 对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonData, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json数据转换成pojo list
     *
     * @param jsonData json数据
     * @param clazz    类
     * @param <T>      泛型类
     * @return List<T>
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> clazz) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return OBJECT_MAPPER.readValue(jsonData, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
