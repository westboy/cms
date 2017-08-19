springboot+mybatis+beetl开发的一款CMS，支持自定义内容模型、模板标签、全站静态化等功能（功能不断完善中.....）
QQ交流群:1482196
# Quick Start
##### 1、将sql目录下db.sql导入本地数据库，修改resources目录下的plication.properties配置文件。
![](https://raw.githubusercontent.com/westboy/CicadasCms/master/CicadasCms/doc/s1.png)
![](https://raw.githubusercontent.com/westboy/CicadasCms/master/CicadasCms/doc/s2.png)
##### 2、 运行CmsApplication类下main方法启动项目（超级管理员：admin，密码：123456）

![后台首页](https://raw.githubusercontent.com/westboy/CicadasCms/master/CicadasCms/doc/index.png "后台首页")
![内容管理](https://static.oschina.net/uploads/space/2017/0818/172741_OaDV_1409333.png "内容管理")
![内容管理](https://static.oschina.net/uploads/space/2017/0818/172843_eANP_1409333.png "内容管理")
![站点管理](https://static.oschina.net/uploads/space/2017/0818/172606_58lG_1409333.png "站点管理")
![定时任务](https://static.oschina.net/uploads/space/2017/0818/172806_vCHS_1409333.png "定时任务")
![模板管理](https://static.oschina.net/uploads/space/2017/0818/172932_JFLd_1409333.png "模板管理")
##### 3、 演示地址（账号：testdemo1，密码：testdemo1）
###### 前台：http://demo.westboy.net（博客模版做的博客网站） 
###### 后台：http://demo.westboy.net/admin/login
# Other
##### 1、相关技术
> springboot
、mybatis
、[beetl](http://www.ibeetl.com "beetl")
、shiro
、[mybatis通用mapper](http://git.oschina.net/free/Mapper "通用mapper")（整合maven方式代码生成）
、[七牛云存储](https://portal.qiniu.com/signup?code=3lb7ah8vdj0ia "七牛云存储")
、[B-JUI v1.2](http://www.b-jui.com/download/ "B-JUI v1.2")
##### 2、栏目列表标签
```html
  <#cms_category_list  siteId="${site.siteId}" categoryId="0" isNav="0"   var="bean">
                @if(isNotEmpty(category)&&category.categoryId==bean.categoryId){
                <a id="topnav_current" href="${bean.url!}">${bean.categoryName!}</a>
                @}else{
                <a href="${bean.url!}">${bean.categoryName!}</a>
                @}
</#cms_category_list>
```
##### 3、栏目标签
```html
  <#cms_category  categoryId="${content.categoryId}" isParent="0" var="bean">
        <span>分类：【<a href="${bean.url!}">${bean.categoryName!}</a>】</span>
 </#cms_category>
```
##### 4、内容列表标签
```html
    <#cms_content_list  siteId="${site.siteId}" categoryId="45" target="1" orderBy="2" isHot="0" hasChild="1" size="15"   titleLen="15"   var="content">
            <div class="blogs">
                <h3><a href="${content.url!}">${content.title!}</a></h3>
                @if(isEmpty(content.thumb)){
                <figure><img src="${resPath}/blog/images/01.jpg" ></figure>
                @}else{
                <figure><img src="${content.thumb}" ></figure>
                @}
                <ul>
                    <p>${content.description!}</p>
                    <a href="${content.url!}" target="_blank" class="readmore">阅读全文&gt;&gt;</a>
                </ul>
                <p class="autor"><span>作者：${content.author!}</span>

                    <#cms_category  categoryId="${content.categoryId}" isParent="0" var="bean">
                    <span>分类：【<a href="${bean.url!}">${bean.categoryName!}</a>】</span>
                    </#cms_category>

                    <span>浏览（<a href="${content.url!}">${content.viewNum!}</a>）</span>
                </p>
                <div class="dateview">${content.inputdate!,dateFormat="yyyy-MM-dd"}</div>
            </div>
 </#cms_content_list>
```
##### 5、内容分页标签（这个标签只能用在list页面）
```html
 <#cms_content_page  siteId="${site.siteId!}"  titleLen="15" page="${page!}"  var="content">
            <div class="newblog">
                <ul>
                    <h3><a href="${content.url!}">${content.title!}</a></h3>
                    <div class="autor">
                        <span>作者：${content.author!}</span>
                    <#cms_category  categoryId="${content.categoryId}" isParent="0" var="bean">
                    <span>分类：[<a href="${bean.url!}">${bean.categoryName!}</a>]</span>
                    </#cms_category>
                        <span>浏览（<a href="${content.url!}">${content.viewNum!}</a>）</span>
                    </div>
                    <p>${content.description!}</p>
                </ul>
                @if(isEmpty(content.thumb)){
                <figure><img src="${resPath}/blog/images/01.jpg" ></figure>
                @}else{
                <figure><img src="${content.thumb}" ></figure>
                @}
                <div class="dateview">${content.inputdate!,dateFormat="yyyy-MM-dd"}</div>
            </div>
            </#cms_content_page>
```
##### 6、分页标签
```html
 <#cms_pagination siteId="${site.siteId}" categoryId="${category.categoryId}" page="${page}"  var="page" >
                @//上一页
                @${page.last}
                @for( change in page.changePage){
                @if(!change.isLink){
                <b>${change.value!}</b>
                @}else{
                ${change.url!}
                @}
                @}
                @//下一页
                @${page.next}
</#cms_pagination>
```
