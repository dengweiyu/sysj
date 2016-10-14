package com.li.videoapplication.data.danmuku;

/**
 * 模板：解析XML数据
 */
public interface AbsXmlParser<T extends BaseXMLEntity> {

    /**
     * 解析输入流
     *
     * @param xml xml字符攒
     * @return 返回实体
     */
    T parse(String xml) throws Exception;

    /**
     * 序列化实体
     *
     * @return XML形式的字符串
     */
    String serialize(T entity) throws Exception;
}
