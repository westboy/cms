package com.zhiliao.common.db.impl;


/**
 * Description:token
 *
 * @author Jin
 * @create 2017-04-05
 **/
public enum M{


	INT_TYPE("int"),
	TNY_INT_TYPE("tinyint"),
	BIG_INT_TYPE("bigint"),
	SMALL_INT_TYPE("smallint"),
	MEDIUM_INT_TYPE("mediumint"),

	/* 小数 */
	DECIMAL_TYPE("decimal"),

	/*日期*/
	DATE_TYPE("date"),
	TIMESTAMP_TYPE("timestamp"),

	/* 文本 */
	CHAR_TYPE("char"),
	VARCHAR_TYPE("varchar"),
	TEXT_TYPE("text"),
	MEDIUM_TEXT_TYPE("mediumText"),
	LONG_TEXT_TYPE("longText");

	private String value;

	M(String _value){
		this.value=_value;
	}


	@Override
	public String toString() {
		return value;
	}

	public  static void  main(String [] args){
		System.out.println(M.valueOf("CHAR_TYPE"));
	}
}