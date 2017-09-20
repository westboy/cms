package com.zhiliao.component.beetl.thread;

import com.zhiliao.component.beetl.html.HtmlObject;
import com.zhiliao.common.exception.SystemException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class HtmlThread implements InitializingBean,DisposableBean, Runnable {

	private static LinkedBlockingQueue<HtmlObject> htmlQueue = new LinkedBlockingQueue<>();
	private boolean run =true ;
	private Thread thread;

	public HtmlThread(){
		this.thread = new Thread(this);
	}

	@Override
	public void run() {
		while (run) {
			try {
				HtmlObject obj = htmlQueue.take();
				File file = new File(obj.getFileUrl());
				System.out.println("--->" + obj.getFileUrl());
				if (!file.exists())
					file.getParentFile().mkdirs();
				OutputStreamWriter os = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
				os.write(obj.getContent());
				os.flush();
				os.close();
			} catch (Exception e) {
				throw new SystemException(e.getMessage());
			}
		}
	}

	public static void addHtml(HtmlObject htmlObj) {

		htmlQueue.offer(htmlObj);
	}

	public static int size() {
		return htmlQueue.size();
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			HtmlObject h = new HtmlObject();
			h.setContent("conntent" + i);
			h.setFileUrl("F:\\web\\" + i + ".html");
			addHtml(h);
		}
		Thread t = new Thread(new HtmlThread());
		t.start();
	}

	@Override
	public void destroy(){
        this.run=false;
	}

	@Override
	public void afterPropertiesSet() {
		thread.start();
	}
}
