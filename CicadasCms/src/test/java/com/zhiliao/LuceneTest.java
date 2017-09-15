package com.zhiliao;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.DateUtil;
import com.zhiliao.component.lucene.LuceneManager;
import com.zhiliao.component.lucene.util.IndexObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneTest {

	@Autowired
	LuceneManager luceneManager;

	@Test
	public void create() {

		IndexObject object2 = new IndexObject();
		object2.setTitle("2说白了只是为了自己方便使用");
		object2.setId("21");
		object2.setKeywords("第二个关键字");
		object2.setUrl("www.baidu.com");
		object2.setPostDate(DateUtil.now());
		object2.setDescription(
				"说白了只是为了自己方便使用，并没显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneManager.create(object2);

		IndexObject object3 = new IndexObject();
		object3.setTitle("3说白了只是为了自己方便使用");
		object3.setId("31");
		object3.setKeywords("第三个关键字");
		object3.setUrl("www.baidu.com");
		object3.setPostDate(DateUtil.now());
		object3.setDescription(
				"说白了只是为了自己方便使用，并没什么新奇台支持；完美支持 LaTex 数学公式、脚注、尾注等，支持使用本地 MathJax 调用，不需要在线访问 MathJax CDN；"
						+ "用户可配置的 Markdown 语法高亮显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown/ LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneManager.create(object3);

		IndexObject object4 = new IndexObject();
		object4.setTitle("4说白了只是为了自己方便使用");
		object4.setId("41");
		object4.setKeywords("第四个关键字");
		object4.setUrl("www.baidu.com");
		object4.setPostDate(DateUtil.now());
		object4.setDescription(
				"说白了只是为了自己方便使用，并没什么新奇的东西。我使用 pandoc 来转化 markdown，但是我不想在修改文件时总是在编辑器、文字终端和浏览器间换来换去，因此我写了一个简单的编辑器，它在后台调用 pandoc 将当前编辑的 markdown 内容转化为 HTML，而后将 HTML 在 smark 中的浏览器中显示出来，就是这么回事。Smark 依赖于 pandoc、Qt 4.8 和 MathJax，在此向上述软件包开发者们致敬。请注意继承于 pandoc 的发布协议，Smark 同样遵循 GPL，如有任何疑问请联系 elerao.ao@gmail.com，我将尽快做出回复。"
						+ "主要特性：Wi持 LaTex 数学公式、脚注、尾注等，支持使用本地 MathJax 调用，不需要在线访问 MathJax CDN；"
						+ "用户可配置的 Markdown 语法高亮显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 HTML、 Miscrosoft Word、OpenOffice / LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneManager.create(object4);
	}

	@Test
	public void delete() {
		IndexObject object4 = new IndexObject();
		object4.setId("41");
		luceneManager.delete(object4);
	}

	@Test
	public void deleteAll() {
		luceneManager.deleteAll();
	}

	@Test
	public void update() {
		IndexObject object4 = new IndexObject();
		object4.setTitle("4说白了只是为了自己方便使用");
		object4.setId("31");
		object4.setKeywords("第四个关键字");
		object4.setUrl("www");
		object4.setPostDate(DateUtil.now());
		object4.setDescription(
				"说白了只是为了自己方便使用，并没什么新奇的东西。我使用 pandoc 来转化 markdown，但是我不想在修改文件时总是在编辑器、文字终端和浏览器间换来换去，因此我写了一个简单的编辑器，它在后台调用 pandoc 将当前编辑的 markdown 内容转化为 HTML，而后将 HTML 在 smark 中的浏览器中显示出来，就是这么回事。Smark 依赖于 pandoc、Qt 4.8 和 MathJax，在此向上述软件包开发者们致敬。请注意继承于 pandoc 的发布协议，Smark 同样遵循 GPL，如有任何疑问请联系 elerao.ao@gmail.com，我将尽快做出回复。"
						+ "主要特性：Wi持 LaTex 数学公式、脚注、尾注等，支持使用本地 MathJax 调用，不需要在线访问 MathJax CDN；"
						+ "用户可配置的 Markdown 语法高亮显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 HTML、 Miscrosoft Word、OpenOffice / LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneManager.update(object4);
	}

	@Test
	public void serach(){
		PageInfo p = luceneManager.page(1,10,"白了只是为了自己方便使用");
		List<IndexObject> list = p.getList();
		for(IndexObject obj:list){
			System.out.println(obj.getId());
			System.out.println(obj.getTitle());
			System.out.println(obj.getKeywords());
			System.out.println(obj.getDescription());
			System.out.println(obj.getUrl());
			System.out.println(obj.getPostDate());
		}
	}



}
