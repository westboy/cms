/*!
	Picbox v2.2
	(c) 2010 Ben Kay <http://bunnyfire.co.uk>

	Based on code from Slimbox v1.7 - The ultimate lightweight Lightbox clone
	(c) 2007-2009 Christophe Beyls <http://www.digitalia.be>
	
	Uses jQuery-mousewheel Version: 3.0.2
	(c) 2009 Brandon Aaron <http://brandonaaron.net>
	
	MIT-style license.
*/

(function($) {
	
	var win = $(window), options, images, activeImage = -1, activeURL, prevImage, nextImage, ie6 = ((window.XMLHttpRequest == undefined) && (ActiveXObject != undefined)), browserIsCrap, middleX, middleY, imageX, imageY, currentSize, initialSize, imageDrag, timer, fitsOnScreen,
	
	// Preload images
	preload = {}, preloadPrev = new Image(), preloadNext = new Image(),
	
	// DOM elements
	overlay, closeBtn, image, prevBtn, nextBtn, leftBtn, rightBtn, bottom, caption, nav, number,
	
	// Effects
	fxOverlay, fxResize,
	
	iebrowser = $.browser.msie && (($.browser.version == "6.0") || ($.browser.version == "7.0") || ($.browser.version == "8.0")),
	
	// CSS classes
	zoomed = "pbzoomed", greyed = "pbgreyed";
	
	/*
		Initialization
	*/
	
	$(document).ready(function() {
		$(document.body).append(
			$([
				overlay = $('<div id="pbOverlay" />').click(close).append(
					closeBtn = $('<div id="pbCloseBtn" />')[0]
				)[0],
				image = $('<img id="pbImage" />').dblclick(doubleClick)[0],
				bottom = $('<div id="pbBottom" />').append([
					caption = $('<div id="pbCaption" />')[0],
					$('<div id="pbNav" />').append([
						leftBtn = $('<a id="pbLeftBtn" href="#" />').click(rotateLeft)[0],
						prevBtn = $('<a id="pbPrevBtn" href="#" />').click(previous)[0],
						zoomBtn  = $('<a id="pbZoomBtn" href="#" />').click(doubleClick)[0],
						leftBtn = $('<a id="pbRightBtn" href="#" />').click(rotateRight)[0],
						nextBtn = $('<a id="pbNextBtn" href="#" />').click(next)[0]
					])[0],
					number = $('<div id="pbNumber" />')[0]
				])[0]
			]).css("display", "none")
		);
		
		browserIsCrap = ie6 || (overlay.currentStyle && (overlay.currentStyle.position != "fixed"));
		if (browserIsCrap) {
			$([overlay, closeBtn, image, bottom]).css("position", "absolute");
		}
		
		$(image).attr("data-rotate", 0);
		
		$(image).tinyDrag(function() {
			var i = $(image), pos = i.position();
			imageX = (pos.left - win.scrollLeft()) + i.width() / 2;
			imageY = (pos.top - win.scrollTop()) + i.height() / 2;
			$(zoomBtn).addClass(zoomed);
		});
	});
	
	$.picbox = function(_images, startImage, _options) {
		options = $.extend({
			loop: false,					// Allows to navigate between first and last images
			overlayOpacity: 0.8,			// 1 is opaque, 0 is completely transparent (change the color in the CSS file)
			overlayFadeDuration: 200,		// Duration of the overlay fade-in and fade-out animations (in milliseconds)
			resizeDuration: 300,			// Duration of each of the image resize animations (in milliseconds)
			resizeEasing: "swing",			// swing uses the jQuery default easing
			controlsFadeDelay: 3000,		// Time delay before controls fade when not moving the mouse (in milliseconds)
			counterText: false,				// Counter text. Use {x} for current image and {y} for total e.g. Image {x} of {y}
			hideFlash: true,				// Hides flash elements on the page when picbox is activated. NOTE: flash elements must have wmode parameter set to "opaque" or "transparent" if this is set to false
			closeKeys: [27, 88, 67],		// Array of keycodes to close Picbox, default: Esc (27), 'x' (88), 'c' (67)
			previousKeys: [37, 80],			// Array of keycodes to navigate to the previous image, default: Left arrow (37), 'p' (80)
			nextKeys: [39, 78],				// Array of keycodes to navigate to the next image, default: Right arrow (39), 'n' (78)
			margins: 0						// Margin between the image and the sides of the window (in pixels)
		}, _options || {});


		// The function is called for a single image, with URL and Title as first two arguments
		if (typeof _images == "string") {
			_images = [[_images, startImage]];
			startImage = 0;
		}
		
		$(overlay).css("opacity", 0).fadeTo(options.overlayFadeDuration, options.overlayOpacity);
		$(bottom).css("display", "");
		mouseMove(); // So controls dissapear if even if mouse is never moved
		position();
		setup(1);

		images = _images;
		options.loop = options.loop && (images.length > 1);
		return changeImage(startImage);
	}

	$.fn.picbox = function(_options, linkMapper, linksFilter) {
		linkMapper = linkMapper || function(el) {
			return [el.href, el.title];
		};

		linksFilter = linksFilter || function() {
			return true;
		};

		var links = this;
		
		$(links).unbind("click").click(function() {
			var link = this, linksMapped = [];
			// Build the list of images that will be displayed
			filteredLinks = $.grep(links, function(el) {
				return linksFilter.call(link, el);
			});
			
			// Can't use $.map() as it flattens array
			for (var i = 0; i < filteredLinks.length; i++)
				linksMapped[i] = linkMapper(filteredLinks[i]);
			return $.picbox(linksMapped, $.inArray(this, filteredLinks), _options);
		});

		return links;
	}
	
	/*
		Internal functions
	*/
	
	function position() {
		var scroll = {x: win.scrollLeft(), y: win.scrollTop()}
		middleX = win.width() / 2;
		middleY = win.height() / 2;
		
		if (browserIsCrap) {
			middleX = middleX + scroll.x;
			middleY = middleY + scroll.y;
			$(overlay).css({left: scroll.x, top: scroll.y, width: win.width(), height: win.height()});
		}

		$(image).css({top: middleY, left: middleX, width: '1px', height: '1px'});
	}
	
	function setup(open) {
 		if (options.hideFlash) {
			$.each(["object", "embed"], function(i, val) {
				$(val).each(function() {
					// jQuery 1.4 doesn't allow .data() on object tags
					if (open) this._picbox = this.style.visibility;
					this.style.visibility = open ? "hidden" : this._picbox;
				});
			});
		}
		/* www.jq22.com */
		overlay.style.display = "";

		var fn = open ? "bind" : "unbind";
		$(document)[fn]("keydown", keyDown);
		$(document)[fn]("mousewheel", scrollZoom);
		$(document)[fn]("mousemove", mouseMove);
		$(bottom)[fn]("mouseover", function(){preventFade(1)});
		$(bottom)[fn]("mouseout", preventFade);
	}
	
	function keyDown(event) {
		var code = event.keyCode;
		// Prevent default keyboard action (like navigating inside the page)
		return $.inArray(code, options.closeKeys) >= 0 ? close()
			: $.inArray(code, options.nextKeys) >= 0 ? next()
			: $.inArray(code, options.previousKeys) >= 0 ? previous()
			: false;
	}

	function mouseMove() {
	  flashFade([bottom, prevBtn, zoomBtn, nextBtn, leftBtn, rightBtn]);
	}
	
	function flashFade(targets, out) {
		clearTimeout(timer);
		$(targets).fadeIn();
		targets = out ? $.merge(targets, out) : targets;
		timer = setTimeout(function(){$(targets).fadeOut()}, options.controlsFadeDelay);
	}
	
	function preventFade(over) {
		var fn = 1 == over ? "unbind" : "bind";
		$(document)[fn]("mousemove", mouseMove);
		clearTimeout(timer);
	}
	
	function previous() {
		return changeImage(prevImage, true);
	}

	function next() {
		return changeImage(nextImage, true);
	}
	
	function rotateLeft() {
		return rotateImage(false);
	}
	
	function rotateRight() {
		return rotateImage(true);
	}
	
	function changeImage(imageIndex, noAnim) {

		if (imageIndex >= 0) {
			activeImage = imageIndex;
			activeURL = images[imageIndex][0];
			prevImage = (activeImage || (options.loop ? images.length : 0)) - 1;
			nextImage = ((activeImage + 1) % images.length) || (options.loop ? 0 : -1);

			stop();
			overlay.className = "pbLoading";
			$(image).css("display", "none");

			if (!images[activeImage][1]) $(caption).html("").hide();
			else $(caption).html(images[activeImage][1]).show();
			$(number).html((((images.length > 1) && options.counterText) || "").replace(/{x}/, activeImage + 1).replace(/{y}/, images.length));
			if (prevImage >= 0) {preloadPrev.src = images[prevImage][0]; $(prevBtn).removeClass(greyed);}
			if (nextImage >= 0) {preloadNext.src = images[nextImage][0]; $(nextBtn).removeClass(greyed);}

			

			preload = new Image();
			preload.onload = function(){showImage(noAnim);};
			preload.src = activeURL;
		}

		return false;
	}
	
	function showImage(noAnim) {
		resetImageCenter();

		var mw = win.width() - options.margins, mh = win.height() - options.margins, size = 1;
		if ((preload.width > mw) || (preload.height > mh)) {
			size = Math.min(mw / preload.width, mh / preload.height);
			$(zoomBtn).removeClass(greyed);
			fitsOnScreen = false;
		} else {
			$(zoomBtn).addClass(greyed);
			fitsOnScreen = true;
		}
			
		currentSize = initialSize = size;

		resizeImage(size, noAnim);

		$(image).attr("src", activeURL);
		$(image).css("display", "");
		//$(image).rotate(0);
		$(image).rotateImg({"angle" : 0});
		overlay.className = "";
		
		flashFade([bottom], [prevBtn, zoomBtn, nextBtn]);
	}
	/* www.jq22.com */
	function resizeImage(to, noAnim) {

		var amount = to / currentSize;
		imageX = middleX - (middleX - imageX) * amount;
		imageY = middleY - (middleY - imageY) * amount;

		currentSize = to;

		var	width = preload.width * to,
			height = preload.height * to,
			// round these as some browsers don't like very small css values
			left = imageX - (width / 2) >> 0,
			top = imageY - (height / 2) >> 0,
		
		dur = noAnim ? 0 : options.resizeDuration, fn = (0 == to) ? function(){$(image).hide()}:function(){};
		$(image).animate({width: width, height: height, top: top, left: left}, {queue:false, duration: dur, easing: options.resizeEasing, complete: fn});
		
		return false;
	}
	
	/* 图片的旋转 */
	function rotateImage(clockwise) {
		var deg = parseInt($(image).attr("data-rotate"));
		if(clockwise){
			deg = (deg + 1) % 4;
		}else{
			if(deg == 0){
				deg = 3;	
			}else{
				deg = (deg - 1) % 4;
			}
		}
		//$(image).rotate(deg * 90);
		$(image).rotateImg({"angle" : deg * 90});
		$(image).attr("data-rotate", deg);
		return false;
	}

	function resetImageCenter() {
		imageX = middleX;
		imageY = middleY;
	}

	function scrollZoom(e, delta) {
		$(zoomBtn).addClass(zoomed);
		var to = currentSize + delta * currentSize / 10;
		return resizeImage(to);
	}

	function doubleClick() {
		if (currentSize == initialSize && imageX == middleX && imageY == middleY && !fitsOnScreen) { 
			$(zoomBtn).addClass(zoomed);
			return resizeImage(1);
		} else {
			$(zoomBtn).removeClass(zoomed);
			resetImageCenter();
			return resizeImage(initialSize);
		}
	}

	function stop() {
		preload.onload = function(){};
		preload.src = preloadPrev.src = preloadNext.src = activeURL;
		$(image).stop();
		$([prevBtn, nextBtn]).addClass(greyed);
		$(zoomBtn).removeClass(zoomed);
	}

	function close() {
		if (activeImage >= 0) {
			stop();
			activeImage = prevImage = nextImage = -1;
			resizeImage(0);
			setup();
			$(bottom).stop().hide();
			$(overlay).stop().fadeOut();
		}

		return false;
	}
	
	/*!
	 * tinyDrag v0.9.2
	 * (c) 2010 Ben Kay <http://bunnyfire.co.uk>
	 */
	
	$.fn.tinyDrag = function(callback) {
		return $.tinyDrag(this, callback);
	}

	$.tinyDrag = function(el, callback) {
		var mouseStart, elStart, moved, doc = $(document), abs = Math.abs;
		el.mousedown(function(e) {
			moved = false;
			mouseStart = {x: e.pageX, y: e.pageY};
			elStart = {x: parseInt(el.css("left")), y: parseInt(el.css("top"))}
			doc.mousemove(drag).mouseup(stop);
			return false;
		});
		
		function drag(e) {
			var x = e.pageX, y = e.pageY;
			if (moved) {
				el.css({left: elStart.x + (x - mouseStart.x), top: elStart.y + (y - mouseStart.y)});
			} else {
				if (abs(x - mouseStart.x) > 1 || abs(y - mouseStart.y) > 1)
					moved = true;
			}
			return false;
		}
		
		function stop() {
			doc.unbind("mousemove", drag).unbind("mouseup");
			moved&&callback&&callback()
		}
		
		return el;
	}


	/*!
	 * jQuery Mousewheel
	 * Version: 3.0.2
	 * (c) 2009 Brandon Aaron (http://brandonaaron.net)
	 */
	
	var types = ['DOMMouseScroll', 'mousewheel'];
	
	$.event.special.mousewheel = {
		setup: function() {
			if ( this.addEventListener )
				for ( var i=types.length; i; )
					this.addEventListener( types[--i], handler, false );
			else
				this.onmousewheel = handler;
		},
		
		teardown: function() {
			if ( this.removeEventListener )
				for ( var i=types.length; i; )
					this.removeEventListener( types[--i], handler, false );
			else
				this.onmousewheel = null;
		}
	};
	
	$.fn.extend({
		mousewheel: function(fn) {
			return fn ? this.bind("mousewheel", fn) : this.trigger("mousewheel");
		},
		
		unmousewheel: function(fn) {
			return this.unbind("mousewheel", fn);
		}
	});
	
	
	function handler(event) {
		var args = [].slice.call( arguments, 1 ), delta = 0, returnValue = true;
		
		event = $.event.fix(event || window.event);
		event.type = "mousewheel";
		
		if ( event.wheelDelta ) delta = event.wheelDelta/120;
		if ( event.detail     ) delta = -event.detail/3;
		
		// Add events and delta to the front of the arguments
		args.unshift(event, delta);
	
		return $.event.handle.apply(this, args);
	}
	
	/*!
	 * rotateImg
	 * by van
	 */
	 
	$.fn.rotateImg = function(options){
		var defaults = {
			scale:1,
			angle:0		
		}
		var options = $.extend(defaults, options);
		this.each(function(){
			var $img=$(this);
			var angle = parseFloat(options.angle) || 0;
				var rad = angle * (Math.PI / 180);
				var m11 = Math.cos(rad) * options.scale, m12 = -1 * Math.sin(rad) * options.scale, m21 = Math.sin(rad) * options.scale, m22 = m11;
				if (iebrowser) {
					$img.css("filter", "progid:DXImageTransform.Microsoft.Matrix(M11="+ m11 +",M12="+ m12 +",M21="+ m21 +",M22="+ m22 +",SizingMethod='auto expand')");
				}else{
					$img.css({
						"transform" : "rotate(" + options.angle + "deg)",
						"-ms-transform" : "rotate(" + options.angle + "deg)",
						"-webkit-transform" : "rotate(" + options.angle + "deg)",
						"-o-transform" : "rotate(" + options.angle + "deg)",
						"-moz-transform" : "rotate(" + options.angle + "deg)"
					});
				}
		});
	};

})(jQuery);


if (!/android|iphone|ipod|series60|symbian|windows ce|blackberry/i.test(navigator.userAgent)) {
	jQuery(function($) {
		$("a[rel^='lightbox']").picbox({/* Put custom options here */}, null, function(el) {
			return (this == el) || ((this.rel.length > 8) && (this.rel == el.rel));
		});
	});
}
/* www.jq22.com */