package com.zhiliao.component.lucene.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.*;
import org.apache.lucene.search.highlight.Highlighter;
import java.io.StringReader;

/**
 * Description:sss
 *
 * @author Jin
 * @create 2017-05-18
 **/
public class DocumentUtil {

    public static Document IndexObject2Document(IndexObject indexObject) {
        Document doc = new Document();
        doc.add(new TextField("id",indexObject.getId(), Field.Store.YES));
        doc.add(new TextField("title",indexObject.getTitle(), Field.Store.YES));
        doc.add(new TextField("keywords",indexObject.getKeywords(),Field.Store.YES));
        doc.add(new TextField("description",indexObject.getDescription(),Field.Store.YES));
        doc.add(new StoredField("postDate", indexObject.getPostDate()));
        doc.add(new StoredField("url", indexObject.getUrl()));
        return doc;  
    }  
  
    public static  IndexObject document2IndexObject(Analyzer analyzer, Highlighter highlighter, Document doc,float score) throws Exception {
        IndexObject indexObject = new IndexObject();
        indexObject.setId(doc.get("id"));
        indexObject.setTitle(stringFormatHighlighterOut(analyzer, highlighter,doc,"title")==null?doc.get("title"):stringFormatHighlighterOut(analyzer, highlighter,doc,"title"));
        indexObject.setKeywords(stringFormatHighlighterOut(analyzer, highlighter,doc,"keywords")==null?doc.get("keywords"):stringFormatHighlighterOut(analyzer, highlighter,doc,"keywords"));
        indexObject.setDescription(stringFormatHighlighterOut(analyzer, highlighter,doc,"description")==null?doc.get("description"):stringFormatHighlighterOut(analyzer, highlighter,doc,"description"));
        indexObject.setPostDate(doc.get("postDate"));
        indexObject.setUrl(doc.get("url"));
        indexObject.setScore(score);
        return indexObject;
    }


    /*关键字加亮*/
    private static String stringFormatHighlighterOut(Analyzer analyzer, Highlighter highlighter, Document document, String field) throws Exception{
        String fieldValue = document.get(field);
        if(fieldValue!=null){
            TokenStream tokenStream=analyzer.tokenStream(field, new StringReader(fieldValue));
            fieldValue = highlighter.getBestFragment(tokenStream, fieldValue);
        }
        return fieldValue;
    }
}