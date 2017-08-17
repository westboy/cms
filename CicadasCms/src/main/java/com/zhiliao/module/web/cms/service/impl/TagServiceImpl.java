package com.zhiliao.module.web.cms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.module.web.cms.service.TagService;
import com.zhiliao.mybatis.mapper.TCmsTagContentMapper;
import com.zhiliao.mybatis.mapper.TCmsTagMapper;
import com.zhiliao.mybatis.model.TCmsTag;
import com.zhiliao.mybatis.model.TCmsTagContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TCmsTagContentMapper tagContentMapper;

    @Autowired
    private TCmsTagMapper tagMapper;

    @Override
    public String TagJsonList(String tagWord) {

        Example example = new Example(TCmsTag.class);
        example.createCriteria()
                .andCondition("tag_name like '%"+tagWord+"%'");
        List<TCmsTag> tags = CmsUtil.isNullOrEmpty(tagMapper.selectByExample(example))?tagMapper.selectAll():tagMapper.selectByExample(example);
        JSONArray jsonArray = new JSONArray();
        if (tags != null) {
            for (TCmsTag tag : tags) {
                JSONObject object = new JSONObject();
                object.put("id", tag.getTagId()<10?"0"+tag.getTagId():tag.getTagId());
                object.put("label", tag.getTagName());
                object.put("value", tag.getTagName());
                jsonArray.add(object);
            }
            return jsonArray.toJSONString();
        }
        return "";
    }


    @Override
    public boolean save(Long contentId, String tag) {
        TCmsTag cmsTag = new TCmsTag();
        cmsTag.setTagName(tag);
        cmsTag = tagMapper.selectOne(cmsTag);
        if (cmsTag != null) {
            cmsTag.setCount(cmsTag.getCount() + 1);
            tagMapper.updateByPrimaryKey(cmsTag);
            return AddOrUpdateTagContent(contentId, cmsTag.getTagId()) > 0 ? true : false;
        }
        cmsTag = new TCmsTag();
        cmsTag.setTagName(tag);
        cmsTag.setLetter(PinyinUtil.convertLower(cmsTag.getTagName()));
        cmsTag.setCount(1);
        tagMapper.insertSelective(cmsTag);
        AddOrUpdateTagContent(contentId, cmsTag.getTagId());
        return AddOrUpdateTagContent(contentId, cmsTag.getTagId()) > 0 ? true : false;
    }

    @Override
    public String delete(Integer[] ids) {
        if (ids != null) {
            for (Integer id : ids) {
                tagMapper.deleteByPrimaryKey(id);
                TCmsTagContent ti = new TCmsTagContent();
                ti.setTagId(id);
                tagContentMapper.delete(ti);
            }
        }
        return JsonUtil.toSUCCESS("操作成功", "tag-tab", false);
    }

    @Override
    public List<TCmsTag> tagList() {
        return tagMapper.selectAll();
    }

    @Override
    public PageInfo page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        return new PageInfo<>(tagList());
    }

    @Override
    public Integer AddOrUpdateTagContent(Long contentId, Integer tagId) {
        TCmsTagContent infoTag = new TCmsTagContent();
        infoTag.setTagId(tagId);
        infoTag.setContentId(Long.valueOf(contentId));
        infoTag = tagContentMapper.selectOne(infoTag);
        if (infoTag == null) {
            infoTag = new TCmsTagContent();
            infoTag.setTagId(tagId);
            infoTag.setContentId(Long.valueOf(contentId));
            return tagContentMapper.insertSelective(infoTag);
        }
        return tagContentMapper.updateByPrimaryKey(infoTag);
    }
}
