package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.*;
import com.zhiliao.component.lucene.util.IndexObject;
import com.zhiliao.module.web.cms.service.*;
import com.zhiliao.module.web.cms.vo.TCmsContentVo;
import com.zhiliao.mybatis.mapper.TCmsContentMapper;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsContent;
import com.zhiliao.mybatis.model.TCmsModel;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

;

/**
 * Description:内容服务
 *
 * @author Jin
 * @create 2017-04-18
 **/
@Service
@CacheConfig(cacheNames = "cms-content-cache")
public class ContentServiceImpl implements ContentService{

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Autowired
    private TCmsContentMapper contentMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LuceneService luceneService;

    @Autowired
    private ModelService modelService;

    @Override
    public String save(TCmsContent pojo) {
        return null;
    }

    @Override
    public String update(TCmsContent pojo) {
        return null;
    }

    @Override
    public String delete(Long[] ids) {
        boolean flag_=false;
        if(ids!=null&&ids.length>0)
            for (Long id : ids){
                TCmsContent content = contentMapper.selectByPrimaryKey(id);
                if(CmsUtil.isNullOrEmpty(content)) continue;
                TCmsCategory category = categoryService.findById(content.getCategoryId());
                if(!CmsUtil.isNullOrEmpty(category))
                    if(!StrUtil.isBlank(category.getPermissionKey())&&!SecurityUtils.getSubject().isPermitted(category.getPermissionKey())) throw new SystemException("对不起，您没有["+category.getCategoryName()+"]栏目的管理权限！");
                flag_ =  contentMapper.deleteByPrimaryKey(id)>0;
            }
        if(flag_)
            return JsonUtil.toSUCCESS("操作成功","content-tab",false);
         return JsonUtil.toERROR("操作失败");
    }

    @Override
    public PageInfo<TCmsContent> page(Integer pageNumber, Integer pageSize, TCmsContent pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(contentMapper.selectByCondition(pojo));
    }

    @Override
    public PageInfo<TCmsContent> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(contentMapper.selectAll());
    }

    @Override
    public TCmsContent findById(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsContent> findList(TCmsContent pojo) {
        return contentMapper.select(pojo);
    }

    @Override
    public List<TCmsContent> findAll() {
        return contentMapper.selectAll();
    }


    @Override
    public PageInfo<TCmsContent> page(Integer pageNumber, Integer pageSize, TCmsContentVo pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(contentMapper.selectByCondition(pojo));
    }

    @Cacheable(key = "'find-contentId-'+#p0+'-tableName-'+#p1")
    @Override
    public Map findContentByContentIdAndTableName(Long contentId, String tableName) {
        Map result =contentMapper.selectByContentIdAndTableName(contentId,tableName);
        if(CmsUtil.isNullOrEmpty(result))
            throw new CmsException("内容不存在或已删除！");
        return result;
    }

    @Override
    public String recovery(Long[] ids) {
        /*多此一举,哈哈*/
        boolean flag_=false;
        if(ids!=null&&ids.length>0)
            for (Long id : ids){
                TCmsContent cmsContent = contentMapper.selectByPrimaryKey(id);
                if(CmsUtil.isNullOrEmpty(cmsContent)) continue;
                TCmsCategory category = categoryService.findById(cmsContent.getCategoryId());
                if(!CmsUtil.isNullOrEmpty(category))
                    if(!StrUtil.isBlank(category.getPermissionKey())&&!SecurityUtils.getSubject().isPermitted(category.getPermissionKey())) throw new SystemException("对不起，您没有["+category.getCategoryName()+"]栏目的管理权限！");
                if(cmsContent.getStatus()<0)
                    cmsContent.setStatus(1);
                else
                    cmsContent.setStatus(-1);
                flag_ =  contentMapper.updateByPrimaryKeySelective(cmsContent)>0;
            }
        if(flag_)
            return JsonUtil.toSUCCESS("操作成功","content-tab",false);
        return JsonUtil.toERROR("操作失败");
    }

    @CacheEvict(cacheNames ="cms-content-cache",allEntries = true)
    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String save(TCmsContent content, String tableName, Map<String, Object> formParam, String[] tags) throws SQLException {
        /*初始化文章的推荐和顶置为false*/
        content.setRecommend(CmsUtil.isNullOrEmpty(content.getRecommend())?false:true);
        content.setTop(CmsUtil.isNullOrEmpty(content.getTop())?false:true);
        content.setUpdatedate(new Date());
        content.setInputdate(new Date());
        if(contentMapper.insert(content)>0) {
            /*创建lucene索引*/
            if(categoryService.findById(content.getCategoryId()).getAllowSearch()) {
                IndexObject indexObject = new IndexObject();
                indexObject.setId(content.getContentId().toString());
                indexObject.setTitle(content.getTitle());
                indexObject.setKeywords(content.getKeywords());
                indexObject.setDescription(content.getDescription());
                indexObject.setPostDate(DateUtil.formatDateTime(content.getInputdate()));
                indexObject.setUrl(this.httpProtocol + "://" + ControllerUtil.getDomain() + "/front/" + content.getSiteId() + "/" + content.getCategoryId() + "/" + content.getContentId());
                luceneService.save(indexObject);
            }
            /*保存和文章管理的Tag*/
            if (tags != null)
                for (String tag : tags) {
                    tagService.save(content.getContentId(), tag);
                }
            this.SaveModelFiledParam(formParam,content,tableName,null);
            return JsonUtil.toSUCCESS("操作成功", "", true);
        }
        return JsonUtil.toERROR("操作失败");
    }

    @CacheEvict(cacheNames ="cms-content-cache",allEntries = true)
    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String update(TCmsContent content, String tableName, List<TCmsModelFiled> cmsModelFileds, Map<String, Object> formParam, String[] tags) throws SQLException {
        /*初始化文章的推荐和顶置为false*/
        content.setRecommend(CmsUtil.isNullOrEmpty(content.getRecommend())?false:true);
        content.setTop(CmsUtil.isNullOrEmpty(content.getTop())?false:true);
        content.setUpdatedate(new Date());
        if(contentMapper.updateByPrimaryKeySelective(content)>0) {
             /*创建lucene索引*/
            if(categoryService.findById(content.getCategoryId()).getAllowSearch()) {
                IndexObject indexObject = new IndexObject();
                indexObject.setId(content.getContentId().toString());
                indexObject.setTitle(content.getTitle());
                indexObject.setKeywords(content.getKeywords());
                indexObject.setDescription(content.getDescription());
                indexObject.setPostDate(DateUtil.formatDateTime(content.getInputdate()));
                indexObject.setUrl(this.httpProtocol + "://" + ControllerUtil.getDomain() + "/front/" + content.getSiteId() + "/" + content.getCategoryId() + "/" + content.getContentId());
                luceneService.update(indexObject);
            }
            /*保存和文章管理的Tag*/
            if (tags != null)
                for (String tag : tags) {
                    tagService.save(content.getContentId(), tag);
                }
            this.SaveModelFiledParam(formParam,content,tableName,cmsModelFileds);
            return JsonUtil.toSUCCESS("操作成功", "", true);
        }
        return JsonUtil.toERROR("操作失败");
    }

    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    public int SaveModelFiledParam(Map<String, Object> formParam,TCmsContent content,String tableName,List<TCmsModelFiled> cmsModelFileds) throws SQLException {
        if(!CmsUtil.isNullOrEmpty(formParam)) {
            String exeSql;
            Connection  connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String selectSql = "select * from t_cms_content_"+tableName+" where content_id="+content.getContentId();
            ResultSet resultSet =statement.executeQuery(selectSql);
            /*判断内容扩展表是否存在数据*/
            if(!resultSet.next()) {
                exeSql = "insert into t_cms_content_" + tableName.trim() + " set ";
                exeSql += "`content_id`=" + content.getContentId() + ", ";
                for (Map.Entry<String, Object> entry : formParam.entrySet()) {
                    exeSql += "`" + entry.getKey() + "`";
                    if(CmsUtil.isNullOrEmpty(entry.getValue()))
                        return 0;
                    if (entry.getValue() instanceof Integer) {
                        exeSql += "=" + entry.getValue() + ", ";
                    } else if (entry.getValue().getClass().isArray()) {
                        /*遍历字符数组，数组来源为checkbox和多图上传*/
                        String[] result = (String[]) entry.getValue();
                        if (result != null) {
                            String tmp = "";
                            for (String value : result) {
                                if(StrUtil.isBlank(value))
                                    continue;
                                tmp += value + "#";
                            }
                            exeSql += "='" + tmp.substring(0, tmp.length() - 1) + "', ";
                        }
                    } else {
                        exeSql += "='" + entry.getValue() + "', ";
                    }
                }
                /*执勤Sql前截取最后面的空格和英文逗号，并加上‘;’*/
                exeSql = exeSql.substring(0, exeSql.length() - 2) + ";";
                int status= statement.executeUpdate(exeSql);
                statement.close();
                connection.close();
                return status;
            }else {
                exeSql = "UPDATE t_cms_content_" + tableName.trim() + " set ";
                      /*遍历Map保存扩展数据表,代码有些复杂*/
                for (TCmsModelFiled filed : cmsModelFileds) {
                    if(CmsUtil.isNullOrEmpty(formParam.get(filed.getFiledName()))) continue;
                    exeSql += "`" + filed.getFiledName() + "`";
                    if (formParam.get(filed.getFiledName()) instanceof Integer) {
                        exeSql += "=" + formParam.get(filed.getFiledName()) + ", ";
                    } else if (!CmsUtil.isNullOrEmpty(formParam.get(filed.getFiledName())) && formParam.get(filed.getFiledName()).getClass().isArray()) {
                    /*遍历字符数组，数组来源为checkbox和多图上传*/
                        String[] result = (String[]) formParam.get(filed.getFiledName());
                        if (result != null) {
                            String tmp = "";
                            for (String value : result) {
                                tmp += value + "#";
                            }
                            exeSql += "='" + tmp.substring(0, tmp.length() - 1) + "', ";
                        }
                    } else {
                        exeSql += "='" + formParam.get(filed.getFiledName()) + "', ";
                    }
                }
                /*截取最后面的空格和英文逗号，并加上‘;’*/
                exeSql = exeSql.substring(0, exeSql.length() - 2) + "where `content_id`=" + content.getContentId() + ";";
                int status =statement.executeUpdate(exeSql);
                statement.close();
                connection.close();
                return status;
            }
        }
        return 0;
    }

    @Override
    public PageInfo<TCmsContent> findContentListByModelFiledValue(int pageNumber,Long categoryId, String tableName, Map<String, Object> param) {
        PageHelper.startPage(pageNumber,this.categoryService.findPageSize(categoryId));
        return new PageInfo<>(contentMapper.selectByTableNameAndMap(tableName,categoryId,param));
    }

    @Cacheable(key = "'find-siteid-'+#p0+'-categoryid-'+#p1+'-orderby-'+#p2+'-pageNumber-'+#p3+'-pageSize-'+#p4+'-hasChild-'+#p5+'-isHot-'+#p6+'-isPic-'+#p7+'-isRecommend-'+#p8")
    @Override
    public PageInfo<Map> findContentListBySiteIdAndCategoryId(Integer siteId,
                                                                      Long categoryId,
                                                                      Integer orderBy,
                                                                      Integer pageNumber,
                                                                      Integer pageSize,
                                                                      Integer hasChild,
                                                                      Integer isHot,
                                                                      Integer isPic,
                                                                      Integer isRecommend) {
        Long[] categoryIds;
        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category)) throw new CmsException("栏目["+categoryId+"]不存在！");
        TCmsModel model = modelService.findById(category.getModelId());
        /*如果包含子类栏目*/
        if(hasChild==1) {
            List<TCmsCategory> cats =findChildCategory(categoryId);
            /*如果子栏目没有内容就查询当前自身*/
            if(CmsUtil.isNullOrEmpty(cats)){
                categoryIds =new Long[]{categoryId};
            }else{
                categoryIds =new Long[cats.size()+1];
                categoryIds[0]=categoryId;
                int i= 1;
                for(TCmsCategory cat :cats){
                    /*判断是否和父栏目内容模型一致*/
                    if(category.getModelId().intValue()!=cat.getModelId().intValue())
                        continue;
                    categoryIds[i]=cat.getCategoryId();
                    i++;
                }
            }
        }else{
            categoryIds =new Long[]{categoryId};
        }
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(contentMapper.selectByContentListBySiteIdAndCategoryIds(siteId,categoryIds,orderBy,isHot,isPic,isRecommend,model.getTableName()));
    }

    private List<TCmsCategory> findChildCategory(Long categoryId){
        List<TCmsCategory> result =  Lists.newArrayList();
        TCmsCategory category = new TCmsCategory();
        category.setParentId(categoryId);
        List<TCmsCategory> list =categoryService.findList(category);
        if(CmsUtil.isNullOrEmpty(list))return null;
        for (TCmsCategory subCat :list){
            result.add(subCat);
            List<TCmsCategory> subList =  findChildCategory(subCat.getCategoryId());
            if(CmsUtil.isNullOrEmpty(subList)) continue;
            result.addAll(subList);
        }
        return result;
    }

    @Cacheable(key = "'find-siteid-'+#p0+'-categoryids-'+#p1+'-orderby-'+#p2+'-size-'+#p3+'-isHot-'+#p4+'-isPic-'+#p5+'-isRecommend-'+#p6")
    @Override
    public PageInfo<Map> findTopicContentListBySiteIdAndCategoryIds(Integer siteId,
                                                                    Long[] categoryIds,
                                                                    Integer orderBy,
                                                                    Integer size,
                                                                    Integer isHot,
                                                                    Integer isPic,
                                                                    Integer isRecommend) {
        PageHelper.startPage(1, size);
        return new PageInfo<>(contentMapper.selectByTopicContentListBySiteIdAndCategoryIds(siteId,categoryIds,orderBy,isHot,isPic,isRecommend));
    }

    @Cacheable(key = "'page-pageNumber-'+#p0+'-siteId-'+#p1+'-categoryId-'+#p2")
    @Override
    public PageInfo<Map> page(Integer pageNumber,Integer siteId, Long categoryId) {

        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category)) throw new CmsException("栏目["+categoryId+"]不存在！");
        TCmsModel model = modelService.findById(category.getModelId());

        /* 如果当前栏目有内容就直接返回*/
        PageHelper.startPage(pageNumber,category.getPageSize());
        List<Map> list =contentMapper.selectByCategoyId(categoryId,siteId,model.getTableName());
        if(!CmsUtil.isNullOrEmpty(list)&&list.size()>0)
            return new PageInfo<>(list);

        /*
        * 查询当前栏目的子类
        * todo 有必要确定子栏目的内容模型是否必须和父栏目一致,暂时以父栏目的模型为主
        */
        PageHelper.startPage(pageNumber,category.getPageSize());
        return new PageInfo<>(contentMapper.selectByCategoyParentId(this.findChildIds(category.getCategoryId()),siteId,model.getTableName()));
    }

    private String findChildIds(Long categoryId){
        String catIds="";
        TCmsCategory category = new TCmsCategory();
        category.setParentId(categoryId);
        List<TCmsCategory> list =categoryService.findList(category);
        if(CmsUtil.isNullOrEmpty(list))return categoryId.toString();
        for (TCmsCategory subCat :list){
            catIds+=subCat.getCategoryId()+","+findChildIds(subCat.getCategoryId());
        }
        return catIds.substring(0,catIds.length()-1);
    }

    @Async
    @Override
    public void viewUpdate(Long contentId){
        TCmsContent content = this.findById(contentId);
        content.setContentId(contentId);
        content.setViewNum(content.getViewNum()+1);
        contentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public String findAllMonthCount() {
        Map result = this.contentMapper.selectAllMonthCount();
        if(CmsUtil.isNullOrEmpty(result))throw new SystemException("统计报表查询失败！");
        String json = "{" +
                "  \"tooltip\": {\n" +
                "    \"trigger\": \"axis\"" +
                "  },\n" +
                "  \"legend\": {\n" +
                "    \"data\": [\n" +
                "      \"内容数量\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"toolbox\": {\n" +
                "    \"show\": true,\n" +
                "    \"feature\": {\n" +
                "      \"magicType\": {\n" +
                "        \"show\": true,\n" +
                "        \"type\": [\n" +
                "          \"line\",\n" +
                "          \"bar\"\n" +
                "        ]\n" +
                "      },\n" +
                "      \"restore\": {\n" +
                "        \"show\": true\n" +
                "      },\n" +
                "      \"saveAsImage\": {\n" +
                "        \"show\": true\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"calculable\": true,\n" +
                "  \"xAxis\": [\n" +
                "    {\n" +
                "      \"type\": \"category\",\n" +
                "      \"data\": [\n" +
                "        \"1月\",\n" +
                "        \"2月\",\n" +
                "        \"3月\",\n" +
                "        \"4月\",\n" +
                "        \"5月\",\n" +
                "        \"6月\",\n" +
                "        \"7月\",\n" +
                "        \"8月\",\n" +
                "        \"9月\",\n" +
                "        \"10月\",\n" +
                "        \"11月\",\n" +
                "        \"12月\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "  \"yAxis\": [\n" +
                "    {\n" +
                "      \"type\": \"value\",\n" +
                "      \"splitArea\": {\n" +
                "        \"show\": true\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"series\": [\n" +
                "    {\n" +
                "      \"name\": \"内容数量\",\n" +
                "      \"type\": \"bar\",\n" +
                "      \"data\": [\n" +
                "        "+result.get("一月份")+",\n" +
                "        "+result.get("二月份")+",\n" +
                "        "+result.get("三月份")+",\n" +
                "        "+result.get("四月份")+",\n" +
                "        "+result.get("五月份")+",\n" +
                "        "+result.get("六月份")+",\n" +
                "        "+result.get("七月份")+",\n" +
                "        "+result.get("八月份")+",\n" +
                "        "+result.get("九月份")+",\n" +
                "        "+result.get("十月份")+",\n" +
                "        "+result.get("十一月份")+",\n" +
                "        "+result.get("十二月份")+"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return json;
    }

    @Cacheable(key = "'content-all-count'")
    @Override
    public Integer AllCount() {
        return this.contentMapper.selectCount(new TCmsContent());
    }

    @Override
    public List<TCmsContent> findByCategoryId(Long categoryId) {
        TCmsContent content = new TCmsContent();
        content.setCategoryId(categoryId);
        return this.contentMapper.select(content);
    }
}
