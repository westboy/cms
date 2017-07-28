package com.zhiliao;

import com.github.pagehelper.PageInfo;
import com.zhiliao.component.lucene.LuceneDao;
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
	LuceneDao luceneDao;

	@Test
	public void create() {
//		IndexObject object = new IndexObject();
//		object.setTitle("说白了只是为了自己方便使用");
//		object.setId(1l);
//		object.setContent(
//				"说白了只是为了自己方便使用，并没什么新奇的东西。我使用 pandoc 来转化 markdown，但是我不想在修改文件时总是在编辑器、文字终端和浏览器间换来换去，因此我写了一个简单的编辑器，它在后台调用 pandoc 将当前编辑的 markdown 内容转化为 HTML，而后将 HTML 在 smark 中的浏览器中显示出来，就是这么回事。Smark 依赖于 pandoc、Qt 4.8 和 MathJax，在此向上述软件包开发者们致敬。请注意继承于 pandoc 的发布协议，Smark 同样遵循 GPL，如有任何疑问请联系 elerao.ao@gmail.com，我将尽快做出回复。"
//						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 HTML、 Miscrosoft Word、OpenOffice / LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
//		luceneDao.create(object);

		IndexObject object2 = new IndexObject();
		object2.setTitle("2说白了只是为了自己方便使用");
		object2.setId(2l);
		object2.setDescripton(
				"说白了只是为了自己方便使用，并没显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneDao.create(object2);

		IndexObject object3 = new IndexObject();
		object3.setTitle("3说白了只是为了自己方便使用");
		object3.setId(3l);
		object3.setDescripton(
				"说白了只是为了自己方便使用，并没什么新奇台支持；完美支持 LaTex 数学公式、脚注、尾注等，支持使用本地 MathJax 调用，不需要在线访问 MathJax CDN；"
						+ "用户可配置的 Markdown 语法高亮显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown/ LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneDao.create(object3);

		IndexObject object4 = new IndexObject();
		object4.setTitle("4说白了只是为了自己方便使用");
		object4.setId(4l);
		object4.setDescripton(
				"说白了只是为了自己方便使用，并没什么新奇的东西。我使用 pandoc 来转化 markdown，但是我不想在修改文件时总是在编辑器、文字终端和浏览器间换来换去，因此我写了一个简单的编辑器，它在后台调用 pandoc 将当前编辑的 markdown 内容转化为 HTML，而后将 HTML 在 smark 中的浏览器中显示出来，就是这么回事。Smark 依赖于 pandoc、Qt 4.8 和 MathJax，在此向上述软件包开发者们致敬。请注意继承于 pandoc 的发布协议，Smark 同样遵循 GPL，如有任何疑问请联系 elerao.ao@gmail.com，我将尽快做出回复。"
						+ "主要特性：Wi持 LaTex 数学公式、脚注、尾注等，支持使用本地 MathJax 调用，不需要在线访问 MathJax CDN；"
						+ "用户可配置的 Markdown 语法高亮显示，美观整洁；"
						+ "多种格式文件导出支持，可将当前 Markdown 文件另存为 HTML、 Miscrosoft Word、OpenOffice / LibreOffice ODT Document、Latex、PDF、reStructured Text、Media Wiki markup、epub 以及 plain txt 等格式文件输出；");
		luceneDao.create(object4);
	}

	@Test
	public void delete() {
		luceneDao.deleteAll();
	}

	@Test
	public void update() {
		IndexObject object4 = new IndexObject();
		object4.setTitle("哈哈哈哈哈哈哈哈哈哈哈哈");
		object4.setId(4l);
		object4.setDescripton("哈哈哈哈哈哈哈哈哈哈哈哈");
		luceneDao.update(object4);
	}

	@Test
	public void serach(){
		PageInfo p = luceneDao.page(1,10,"哈哈");
		List<IndexObject> list = p.getList();
		for(IndexObject obj:list){
			System.out.println(obj.getId());
			System.out.println(obj.getTitle());
			System.out.println(obj.getDescripton());
		}
	}



}
