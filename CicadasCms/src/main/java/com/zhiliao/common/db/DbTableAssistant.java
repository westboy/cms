package com.zhiliao.common.db;

import com.zhiliao.common.db.impl.M;

/**
 * Description:数据库建表
 *
 * @author Jin
 * @create 2017-04-18
 **/
public interface DbTableAssistant<T> {

    T create();

    T edit();

    T delete();

    String BuilderSQL();

    T TableName(String tableName);

    T InitColumn(String columnName, M columnType, Integer length, boolean autoIncrement, String defaultValue, boolean isNotNull,boolean isPrimaryKey);

    T AddColumn(String columnName,M columnType,Integer length, boolean autoIncrement,String defaultValue,boolean isNotNull,boolean isPrimaryKey);

    T ChangeColumn(String columnName,String newColumnNane,M columnType,Integer length,boolean autoIncrement,String defaultValue,boolean isNotNull);

    T DropColumn(String columnName,boolean isPrimaryKey);

    T AddIndex(String cloumnName,Integer length);

    T Engine(String engineName);

    T CharSet(String charset);

}
