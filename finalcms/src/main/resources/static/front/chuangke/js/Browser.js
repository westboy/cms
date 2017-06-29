$(function() {
	var chrome = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
	if(chrome) {
		var chromeVersion = parseInt(chrome[2], 10);
		//chrome浏览器 这里会输出chrome版本，自己判断下
		
	} else {
		var ieVersion = detectIE();
		if(ieVersion !== false) {
			//ie浏览器 这里会输出ie版本，自己判断下
			//alert(ieVersion)
		} else {
			//其他浏览器
		}
	}

	function detectIE() {
		var ua = window.navigator.userAgent;

		// Test values; Uncomment to check result …

		// IE 10
		// ua = 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)';

		// IE 11
		// ua = 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko';

		// Edge 12 (Spartan)
		// ua = 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36 Edge/12.0';

		// Edge 13
		// ua = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586';

		var msie = ua.indexOf('MSIE ');
		if(msie > 0) {
			$(".browser_div").show()
			// IE 10 or older => return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf('.', msie)), 10);
			
		}

		var trident = ua.indexOf('Trident/');
		if(trident > 0) {
			// IE 11 => return version number
			var rv = ua.indexOf('rv:');
			return parseInt(ua.substring(rv + 3, ua.indexOf('.', rv)), 10);
		}

		var edge = ua.indexOf('Edge/');
		if(edge > 0) {
			// Edge (IE 12+) => return version number
			return parseInt(ua.substring(edge + 5, ua.indexOf('.', edge)), 10);

		}

		// other browser
		return false;
	}
})