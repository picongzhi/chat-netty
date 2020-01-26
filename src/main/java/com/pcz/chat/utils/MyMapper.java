package com.pcz.chat.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author picongzhi
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
