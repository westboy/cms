package com.zhiliao.mybatis.model.slave;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "res_base_info")
public class ResBaseInfo implements Serializable {
    @Id
    @Column(name = "res_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer resId;

    @Column(name = "res_name")
    private String resName;

    @Column(name = "server_id")
    private String serverId;

    @Column(name = "res_address")
    private String resAddress;

    @Column(name = "res_size")
    private Integer resSize;

    @Column(name = "thumbnails_address")
    private String thumbnailsAddress;

    @Column(name = "video_time")
    private String videoTime;

    @Column(name = "md5_value")
    private String md5Value;

    /**
     * 封面
     */
    @Column(name = "video_image")
    private String videoImage;

    /**
     * 资源被引用次数
     */
    @Column(name = "refered_num")
    private Integer referedNum;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    private Date addTime;

    @Column(name = "swf_num")
    private Integer swfNum;

    private static final long serialVersionUID = 1L;

    /**
     * @return res_id
     */
    public Integer getResId() {
        return resId;
    }

    /**
     * @param resId
     */
    public void setResId(Integer resId) {
        this.resId = resId;
    }

    /**
     * @return res_name
     */
    public String getResName() {
        return resName;
    }

    /**
     * @param resName
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * @return server_id
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * @param serverId
     */
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    /**
     * @return res_address
     */
    public String getResAddress() {
        return resAddress;
    }

    /**
     * @param resAddress
     */
    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    /**
     * @return res_size
     */
    public Integer getResSize() {
        return resSize;
    }

    /**
     * @param resSize
     */
    public void setResSize(Integer resSize) {
        this.resSize = resSize;
    }

    /**
     * @return thumbnails_address
     */
    public String getThumbnailsAddress() {
        return thumbnailsAddress;
    }

    /**
     * @param thumbnailsAddress
     */
    public void setThumbnailsAddress(String thumbnailsAddress) {
        this.thumbnailsAddress = thumbnailsAddress;
    }

    /**
     * @return video_time
     */
    public String getVideoTime() {
        return videoTime;
    }

    /**
     * @param videoTime
     */
    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }

    /**
     * @return md5_value
     */
    public String getMd5Value() {
        return md5Value;
    }

    /**
     * @param md5Value
     */
    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    /**
     * 获取封面
     *
     * @return video_image - 封面
     */
    public String getVideoImage() {
        return videoImage;
    }

    /**
     * 设置封面
     *
     * @param videoImage 封面
     */
    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    /**
     * 获取资源被引用次数
     *
     * @return refered_num - 资源被引用次数
     */
    public Integer getReferedNum() {
        return referedNum;
    }

    /**
     * 设置资源被引用次数
     *
     * @param referedNum 资源被引用次数
     */
    public void setReferedNum(Integer referedNum) {
        this.referedNum = referedNum;
    }

    /**
     * 获取添加时间
     *
     * @return add_time - 添加时间
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * 设置添加时间
     *
     * @param addTime 添加时间
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * @return swf_num
     */
    public Integer getSwfNum() {
        return swfNum;
    }

    /**
     * @param swfNum
     */
    public void setSwfNum(Integer swfNum) {
        this.swfNum = swfNum;
    }
}