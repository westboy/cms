package com.zhiliao;

import com.google.common.collect.Maps;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.mybatis.mapper.TCmsContentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * Description:content test
 *
 * @author Jin
 * @create 2017-05-23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ContentTest {

    @Autowired
    private ContentService contentService;

    @Autowired
    private TCmsContentMapper contentMapper;


    public void findContentByContentIdAndTbleName(){
      Map map =  contentService.findContentByContentIdAndTableName(1l,"ceshi");
        map.forEach((key, value) -> System.out.println(key+" --> "+value));
    }
    @Test
    public void findContentListByCategoryIdAndSiteId(){
        contentService.findContentListBySiteIdAndCategoryId(0,26l,10,1,10,0,0,1,1);
    }


    public void findContentPageByTableNameAndParam(){
        Map param = Maps.newHashMap();
        param.put("nianling",25);
        param.put("danxuan",2);
        contentMapper.selectByTableNameAndMap("ceshi",32l,param);
    }


}
