package com.zhiliao.component.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;

/**
 * Description:query util
 *
 * @author Jin
 * @create 2017-05-19
 **/
public class QueryUtil {

    public static Query query(String query, Analyzer analyzer, String... fields) throws ParseException {
        BooleanQuery.setMaxClauseCount(32768);
        query = QueryParser.escape(query);
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        return parser.parse(query);
    }

}
