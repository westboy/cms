package com.zhiliao.component.beetl.tag;

import com.google.common.collect.Maps;
import org.beetl.core.TagFactory;
import org.beetl.ext.spring.SpringBeanTagFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Description:aa
 *
 * @author Jin
 * @create 2017-06-03
 **/
@Component
public class TagRegisterFactory {


    /* 内容列表标签 */
    @Bean(name = "contentListTagFactory")
    public SpringBeanTagFactory ContentListTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("contentListTag");
        return  springBeanTagFactory;
    }

    /* 内容分页标签 */
    @Bean(name = "contentPageTagFactory")
    public SpringBeanTagFactory ContentPageTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("contentPageTag");
        return  springBeanTagFactory;
    }

    /* 分页标签 */
    @Bean(name = "paginationTagFactory")
    public SpringBeanTagFactory PaginationTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("paginationTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "contentTagFactory")
    public SpringBeanTagFactory contentTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("contentTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "categoryListTagFactory")
    public SpringBeanTagFactory categoryListTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("categoryListTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "categoryTagFactory")
    public SpringBeanTagFactory categoryTagFactory() {
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("categoryTag");
        return springBeanTagFactory;
    }



    @Bean(name = "printSitePositionTagFactory")
    public SpringBeanTagFactory  printSitePositionTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("printSitePositionTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "searchModelFiledValueTagFactory")
    public SpringBeanTagFactory searchModelFiledValueTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("searchModelFiledValueTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "lucenePageTagFactory")
    public SpringBeanTagFactory lucenePageTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("lucenePageTag");
        return  springBeanTagFactory;
    }


    /* 分页标签 */
    @Bean(name = "lucenePaginationTagFactory")
    public SpringBeanTagFactory lucenePaginationTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("lucenePaginationTag");
        return  springBeanTagFactory;
    }

    /* 分页标签 */
    @Bean(name = "indexSilderTagFactory")
    public SpringBeanTagFactory indexSilderTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("indexSilderTag");
        return  springBeanTagFactory;
    }


    @Bean(name = "topicListTagFactory")
    public SpringBeanTagFactory topicListTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("topicListTag");
        return  springBeanTagFactory;
    }

    @Bean(name = "topicContentTagFactory")
    public SpringBeanTagFactory topicContentTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("topicContentTag");
        return  springBeanTagFactory;
    }


    /* 友情链接标签 */
    @Bean(name = "friendLinkListTagFactory")
    public SpringBeanTagFactory friendLinkListTagFactory(){
        SpringBeanTagFactory springBeanTagFactory = new SpringBeanTagFactory();
        springBeanTagFactory.setName("friendLinkListTag");
        return  springBeanTagFactory;
    }

    /* TagFactory */
    @Bean(name = "tagFactory")
    public Map<String,TagFactory> Tag(@Qualifier("contentListTagFactory") SpringBeanTagFactory contentListTag,
                                      @Qualifier("contentPageTagFactory") SpringBeanTagFactory contentPageTag,
                                      @Qualifier("paginationTagFactory") SpringBeanTagFactory paginationTag,
                                      @Qualifier("contentTagFactory") SpringBeanTagFactory contentTag,
                                      @Qualifier("categoryListTagFactory") SpringBeanTagFactory categoryListTag,
                                      @Qualifier("categoryTagFactory") SpringBeanTagFactory categoryTag,
                                      @Qualifier("searchModelFiledValueTagFactory") SpringBeanTagFactory SearchModelFiledValueTag,
                                      @Qualifier("printSitePositionTagFactory") SpringBeanTagFactory printSitePositionTagFactory,
                                      @Qualifier("lucenePageTagFactory") SpringBeanTagFactory lucenePageTagFactory,
                                      @Qualifier("lucenePaginationTagFactory") SpringBeanTagFactory lucenePaginationTagFactory,
                                      @Qualifier("indexSilderTagFactory") SpringBeanTagFactory indexSilderTagFactory,
                                      @Qualifier("topicListTagFactory") SpringBeanTagFactory topicListTagFactory,
                                      @Qualifier("topicContentTagFactory") SpringBeanTagFactory topicContentTagFactory,
                                      @Qualifier("friendLinkListTagFactory") SpringBeanTagFactory friendLinkListTagFactory


    ){
        Map<String,TagFactory> tag = Maps.newHashMap();
        tag.put("cms_content_list",contentListTag);
        tag.put("cms_content_page",contentPageTag);
        tag.put("cms_pagination",paginationTag);
        tag.put("cms_content",contentTag);
        tag.put("cms_category_list",categoryListTag);
        tag.put("cms_category",categoryTag);
        tag.put("cms_modelfiled_find",SearchModelFiledValueTag);
        tag.put("cms_site_pos",printSitePositionTagFactory);
        tag.put("cms_lucene_page",lucenePageTagFactory);
        tag.put("cms_lucene_pagination",lucenePaginationTagFactory);
        tag.put("cms_index_silder",indexSilderTagFactory);
        tag.put("cms_topic_list",topicListTagFactory);
        tag.put("cms_topic_content",topicContentTagFactory);
        tag.put("cms_friendlink",friendLinkListTagFactory);
        return  tag;
    }

}
