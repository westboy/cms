$(function() {
	//banner
	var width = $(".container").width();
	var h = width * 0.26;
	var h1 = width * 0.275;
	var h2 = width * 0.22;
	$(".banner_container").css("height", h);
	$(".banner").css("height", h1);
	$(".zone_banner img").css("height", h2);
	//首页切换
	jQuery(".slideTxtBox").slide({ effect: "leftLoop", switchLoad: "_src", delayTime: 1000 }); //switchLoad 解决图片加载慢 	(http://www.superslide2.com/otherDemo/T2.5/switchLoad.html);
	$(".scrollLoading").scrollLoading(); //scrollLoading 解决图片加载慢
	//(http://www.zhangxinxu.com/wordpress/2010/11/jquery%E9%A1%B5%E9%9D%A2%E5%9B%BE%E7%89%87%E7%AD%89%E5%85%83%E7%B4%A0%E6%BB%9A%E5%8A%A8%E5%8A%A8%E6%80%81%E5%8A%A0%E8%BD%BD%E5%AE%9E%E7%8E%B0/);
	//返回顶部
	$("#top").click(function() {
		$("html,body").animate({ scrollTop: 0 }, 2000)
	});
	//时间轴展示
	jQuery("#picScroll-top-4").slide({ mainCell: ".bd ul", autoPage: true, effect: "topLoop", autoPlay: true, vis: 4, pnLoop: false });
	jQuery("#picScroll-top-6").slide({ mainCell: ".bd ul", autoPage: true, effect: "topLoop", autoPlay: true, vis: 6, pnLoop: false });
	jQuery(".ladyScroll").slide({ mainCell:".dlList", effect:"leftLoop",vis:5, autoPlay:true});
	//分页
	$("#page").page({
		debug: true,
		showInfo: false,
		showJump: true,
		showPageSizes: false,
		remote: {
			url: 'data.json',
			success: function(data) {
				//
			}
		}
	});
	$("#page").on("pageClicked", function(event, pageIndex) {
		//
	}).on('jumpClicked', function(event, pageIndex) {
		//
	}).on('pageSizeChanged', function(event, pageSize) {
		//
	});
	//分页参数配置
	//total number 0 data-total	总数据数
	//pageSize number	10 data-page-size	每页显示数据条数
	//pageBtnCount number	11	data-page-btn-count	显示分页按钮数量（推荐设置奇数）
	//showFirstLastBtn boolean	true	data-show-first-last-btn	是否显示首页和尾页
	//firstBtnText string	null	data-first-btn-text	自定义首页按钮显示内容
	//lastBtnText string	null	data-first-btn-text	自定义首页按钮显示内容
	//prevBtnText string	?	data-first-btn-text	自定义上一页按钮显示内容
	//nextBtnText string	?	data-first-btn-text	自定义下一页按钮显示内容
	//loadFirstPage boolean	true	data-load-first-page	是否加载第一页数据（如果设置为false，需传入total）
	//showInfo boolean	false	data-show-info	是否显示分页信息
	//infoFormat string	{start} ~ {end} of {total} entires	data-info-format	自定义分页信息（{start}，{end}，{total}会替换成对应数值）
	//showJump boolean	false	data-show-jump	是否显示跳转按钮
	//jumpBtnText string	Go	data-jump-btn-text	跳转按钮显示内容
	//showPageSizes boolean	false	data-show-page-sizes	是否显示每页数据数按钮
	//pageSizeItems array	[5, 10, 15, 20]	data-page-size-items	自定义每页数据数
	//remote object	null	ajax获取数据配置;
	//资讯公告列表点击样式
	$(".info_nav a").click(function(){
		$(this).parent("li").addClass("active").siblings("li").removeClass("active")
	});
	//资讯分类下拉容器点击样式
	$(".kinds_switch").click(function(){
		$(this).attr("style","display: none;").siblings(".kinds_switch").attr("style","display: flex;");
	});
	$(".item_kinds li a").click(function(){
		$(this).addClass("active").parent("li").siblings("li").find("a").removeClass("active");
	});
	//晃动动画
	$(".shake li").on("mouseover mouseout",function(){
	 	$(this).toggleClass('animated shake')
	});
	//变大变小动画
	$(".pulse li").on("mouseover mouseout",function(){
		$(this).find("img.scrollLoading").toggleClass('animated pulse infinite')
	});
})