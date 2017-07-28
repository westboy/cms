package com.zhiliao.common.db.impl;

import com.zhiliao.common.db.DbTableAssistant;
import com.zhiliao.common.db.kit.MysqlFiledUtil;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Description:mysql
 *
 * @author Jin
 * @create 2017-04-18
 **/
@Component
@Scope("prototype")
public class MysqlDbTableAssistant implements DbTableAssistant<MysqlDbTableAssistant> {

    private final static Logger log = LoggerFactory.getLogger(MysqlDbTableAssistant.class);

    /* 创建表 开始*/
    private final static String CREATE_TABLE_BEGIN = "CREATE TABLE `{table}` (";

    /* 创建表 结束*/
    private final static String CREATE_TABLE_END = ") ";

    /*删除表*/
    private final static String DROP_TABLE = "DROP TABLE `{table}` ";

    /*编辑表*/
    private final static String ALTER_TABLE_BEGIN = "ALTER TABLE `{table}`";

    private final static String CHANGE_COLUMN = " CHANGE COLUMN ";

    private final static String ADD_COLUMN = " ADD COLUMN ";

    private final static String DROP_COLUMN = " DROP COLUMN ";

    private String sql_head = "";

    private String sql_body="";

    private String sql_foot="";

    private void init(){

        this.sql_head = "";

        this.sql_body = "";

        this.sql_foot = "";
    }

    @Override
    public MysqlDbTableAssistant create() {
        this.init();
        this.sql_head=CREATE_TABLE_BEGIN;
        this.sql_foot=CREATE_TABLE_END;
        return this;
    }

    @Override
    public MysqlDbTableAssistant edit() {
        this.init();
        this.sql_head = ALTER_TABLE_BEGIN;
        return this;
    }

    @Override
    public MysqlDbTableAssistant delete() {
        this.init();
        this.sql_head = DROP_TABLE;

        return this;
    }

    @Override
    public String BuilderSQL() {
        String SQL = this.sql_head+this.sql_body+this.sql_foot;
        log.info(SQL);
        return SQL;
    }

    @Override
    public MysqlDbTableAssistant TableName(String tableName) {
        this.sql_head=this.sql_head.replace("{table}",tableName);
        return this;
    }

    @Override
    public MysqlDbTableAssistant InitColumn(String columnName, M columnType, Integer length,boolean autoIncrement, String defaultValue, boolean isNotNull,boolean isPrimaryKey) {
        if(MysqlFiledUtil.isCharTextFiled(columnType))/*判断字段是否允许设置长度*/
            this.sql_body = " `"+columnName+"`" + columnType+"("+length+")";
        else
            this.sql_body = " `"+columnName+"`" + columnType;
        if(isNotNull)/*判断字段是否不为空*/
            this.sql_body+=" NOT null";
        if (!autoIncrement) {
            if(!isNotNull)/*判断字段是否允许为空*/
                this.sql_body+=" NULL ";
            this.sql_body += " DEFAULT " +  (StrUtil.isBlank(defaultValue)?null:"'"+defaultValue+"'");
        } else { /* 判断字段类型是否支持自动增长 */
            if(MysqlFiledUtil.isAutoIncrementFiled(columnType)) {
                if (!isNotNull)
                    this.sql_body += " NOT null";
                this.sql_body += " AUTO_INCREMENT";
            }
        }
        if(isPrimaryKey)/*是否为主键*/
            this.sql_body +=" , PRIMARY KEY (`"+columnName+"`) ";
        return this;
    }

    @Override
    public MysqlDbTableAssistant AddColumn(String columnName,M columnType,Integer length, boolean autoIncrement,String defaultValue,boolean isNotNull,boolean isPrimaryKey) {
        this.sql_body = ADD_COLUMN;
        if(MysqlFiledUtil.isCharTextFiled(columnType))/*判断字段是否允许设置长度*/
            this.sql_body += " `"+columnName+"` " + columnType+"("+length+")";
        else
            this.sql_body += " `"+columnName+"` " + columnType;
        if(isNotNull)/*判断字段是否不为空*/
            this.sql_body+=" NOT NULL";
        if(autoIncrement) { /* 判断字段类型是否支持自动增长 */
            if(MysqlFiledUtil.isAutoIncrementFiled(columnType)) {
                if (!isNotNull)
                    this.sql_body += " NOT null";
                this.sql_body += " AUTO_INCREMENT";
            }
        }else {
            if(!isNotNull)/*判断字段是否允许为空*/
                this.sql_body+=" NULL ";
            if(!MysqlFiledUtil.isNotDefaultValue(columnType)) {
                /*判断字段类型是否为数值型*/
                if(MysqlFiledUtil.isAutoIncrementFiled(columnType)&& !StringUtils.isNumeric(defaultValue))
                    /*todo 有待做日期类型验证*/
                    throw new SystemException("数值型字段不能设置默认值为字符");
                this.sql_body += " DEFAULT " + (StrUtil.isBlank(defaultValue) ? null : "'" + defaultValue + "'");
            }
        }
        if(isPrimaryKey)/*是否为主键*/
            this.sql_body +="  , ADD PRIMARY KEY (`"+columnName+"`) ";
        return this;
    }

    @Override
    public MysqlDbTableAssistant ChangeColumn(String columnName,String newColumnName,M columnType,Integer length,boolean autoIncrement,String defaultValue,boolean isNotNull) {
        this.sql_body = this.CHANGE_COLUMN;
        if(MysqlFiledUtil.isCharTextFiled(columnType))/*判断字段是否允许设置长度*/
            this.sql_body += " `"+columnName+"` " +" `"+newColumnName+"` "+ columnType+"("+length+")";
        else
            this.sql_body += " `"+columnName+"` " +" `"+newColumnName+"` " + columnType;
        if(isNotNull)
            this.sql_body+=" NOT null";
        if(autoIncrement) { /* 判断字段类型是否支持自动增长 */
            if(MysqlFiledUtil.isAutoIncrementFiled(columnType)) {
                if (!isNotNull)
                    this.sql_body += " NOT null";
                this.sql_body += " AUTO_INCREMENT ;";
            }
        }else {
            if(!isNotNull)/*判断字段是否允许为空*/
                this.sql_body+=" NULL ";
            if(MysqlFiledUtil.isNotDefaultValue(columnType))
               this.sql_body += " DEFAULT " +  (StrUtil.isBlank(defaultValue)?null:"'"+defaultValue+"'")+";";
        }

        return this;
    }

    @Override
    public MysqlDbTableAssistant DropColumn(String columnName,boolean isPrimaryKey) {
        this.sql_body=DROP_COLUMN +"`"+columnName+"`";
        return this;
    }


    @Override
    public MysqlDbTableAssistant AddIndex(String cloumnName, Integer length) {
        return null;
    }

    @Override
    public MysqlDbTableAssistant Engine(String engineName) {
        this.sql_foot+=" ENGINE="+engineName+" ";
        return this;
    }

    @Override
    public MysqlDbTableAssistant CharSet(String charset) {
        this.sql_foot+=" DEFAULT CHARSET="+charset+" ";
        return this;
    }


}
