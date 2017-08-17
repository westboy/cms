package com.zhiliao.mybatis.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_cms_tag")
public class TCmsTag implements Serializable {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer tagId;

    @Column(name = "tag_name")
    private String tagName;

    private String letter;

    private Integer count;

    private static final long serialVersionUID = 1L;

    /**
     * @return tag_id
     */
    public Integer getTagId() {
        return tagId;
    }

    /**
     * @param tagId
     */
    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    /**
     * @return tag_name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @param tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * @return letter
     */
    public String getLetter() {
        return letter;
    }

    /**
     * @param letter
     */
    public void setLetter(String letter) {
        this.letter = letter;
    }

    /**
     * @return count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }
}