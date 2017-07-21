/*!
 * B-JUI  v1.2 (http://b-jui.com)
 * Git@OSC (http://git.oschina.net/xknaan/B-JUI)
 * Copyright 2014 K'naan (xknaan@163.com).
 * Licensed under Apache (http://www.apache.org/licenses/LICENSE-2.0)
 */

/* ========================================================================
 * B-JUI: bjui-plugins.js  v1.2
 * @author K'naan (xknaan@163.com)
 * http://git.oschina.net/xknaan/B-JUI/blob/master/BJUI/js/bjui-plugins.js
 * ========================================================================
 * Copyright 2014 K'naan.
 * Licensed under Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * ======================================================================== */

+function ($) {
    'use strict';
    
    $(document).on(BJUI.eventType.initUI, function(e) {
        var $box    = $(e.target)
        
        // UI init begin...
        
        /* i-check */
        var $icheck = $box.find('[data-toggle="icheck"]')
        
        $icheck.each(function(i) {
            var $element = $(this),
                id       = $element.attr('id'),
                name     = $element.attr('name'),
                label    = $element.data('label')
            
            if (label) $element.after('<label for="'+ id +'" class="ilabel">'+ label +'</label>')
            
            $element
                .on('ifCreated', function(e) {
                    /* Fixed validate msgbox position */
                    var $parent = $(this).closest('div'),
                        $ilabel = $parent.next('[for="'+ id +'"]')
                    
                    $parent.attr('data-icheck', name)
                    $ilabel.attr('data-icheck', name)
                })
                .iCheck({
                    checkboxClass : 'icheckbox_minimal-purple',
                    radioClass    : 'iradio_minimal-purple',
                    increaseArea  : '20%' // optional
                })
                .on('ifChanged', function() {
                    /* Trigger validation */
                    $(this).trigger('validate')
                })
            
            if ($element.prop('disabled')) $element.iCheck('disable')
        })
        /* i-check check all */
        $icheck.filter('.checkboxCtrl').on('ifChanged', function(e) {
            var checked = e.target.checked == true ? 'check' : 'uncheck'
            var group   = $(this).data('group')
            
            $box.find(':checkbox[name="'+ group +'"]').iCheck(checked)
        })
        
        /* fixed ui style */
        $box.find(':text, :password, textarea, :button, a.btn').each(function() {
            var $element = $(this), $tabledit = $element.closest('table.bjui-tabledit')
            
            if (($element.is(':text') || $element.is(':password') || $element.isTag('textarea')) && !$element.hasClass('form-control'))
                $element.addClass('form-control')
            if ($element.is(':button')) {
                var icon = $element.data('icon'), large = $element.data('large'), oldClass = $element.attr('class')
                
                if (!$element.hasClass('btn')) 
                    $element.removeClass().addClass('btn').addClass(oldClass)
                if (icon) {
                    var _icon = 'fa-'+ icon.replace('fa-', '')
                    
                    if (!$element.data('bjui.icon')) {
                        $element.html('<i class="fa '+ _icon +'"></i> '+ $element.html())
                            .data('bjui.icon', true)
                    }
                }
            }
            if ($element.isTag('a')) {
                var icon = $element.data('icon'), large = $element.data('large')
                
                if (icon) {
                    var _icon = 'fa-'+ icon.replace('fa-', '')
                    
                    if (!$element.data('bjui.icon')) {
                        $element.html('<i class="fa '+ _icon +'"></i> '+ $element.html())
                            .data('bjui.icon', true)
                    }
                }
            }
            if ($element.isTag('textarea')) {
                var toggle = $element.data('toggle')
                
                if (toggle && toggle == 'autoheight' && $.fn.autosize) $element.addClass('autosize').autosize()
            }
            if (!$tabledit.length) {
                var size = $element.attr('size') || $element.attr('cols'), width = size * 10
                
                if (!size) return
                if (width) $element.css('width', width)
            }
        })
        
        /* form validate */
        $box.find('form[data-toggle="validate"]').each(function() {
            var $element = $(this), alertmsg = (typeof $element.data('alertmsg') == 'undefined') ? true : $element.data('alertmsg')
            
            $(this)
                .validator({
                    valid: function(form) {
                        $(form).bjuiajax('ajaxForm', $(form).data())
                    },
                    validClass : 'ok',
                    theme      : 'red_right_effect'
                })
                .on('invalid.form', function(e, form, errors) {
                    if (alertmsg) $(form).alertmsg('error', FRAG.validateErrorMsg.replace('#validatemsg#', BJUI.regional.validatemsg).replaceMsg(errors.length))
                })
        })
        
        /* moreSearch */
        $box.find('[data-toggle="moresearch"]').each(function() {
            var $element = $(this),
                $parent  = $element.closest('.bjui-pageHeader'),
                $more    = $parent && $parent.find('.bjui-moreSearch'),
                name     = $element.data('name')
            
            if (!$element.attr('title')) $element.attr('title', '更多查询条件')
            $element.click(function(e) {
                if (!$more.length) {
                    BJUI.debug('Not created \'moresearch\' box[class="bjui-moreSearch"]!')
                    return
                }
                $more.css('top', $parent.outerHeight() - 1)
                if ($more.is(':visible')) {
                    $element.html('<i class="fa fa-angle-double-down"></i>')
                    if (name) $('body').data('moresearch.'+ name, false)
                } else {
                    $element.html('<i class="fa fa-angle-double-up"></i>')
                    if (name) $('body').data('moresearch.'+ name, true)
                }
                $more.fadeToggle('slow', 'linear')
                
                e.preventDefault()
            })
            
            if (name && $('body').data('moresearch.'+ name)) {
                $more.css('top', $parent.outerHeight() - 1).fadeIn()
                $element.html('<i class="fa fa-angle-double-up"></i>')
            }
        })
        
        /* bootstrap - select */
        var $selectpicker       = $box.find('select[data-toggle="selectpicker"]')
        var bjui_select_linkage = function($obj, $next) {
            var refurl    = $obj.data('refurl')
            var _setEmpty = function($select) {
                var $_nextselect = $($select.data('nextselect'))
                
                if ($_nextselect && $_nextselect.length) {
                    var emptytxt = $_nextselect.data('emptytxt') || '&nbsp;'
                    
                    $_nextselect.html('<option>'+ emptytxt +'</option>').selectpicker('refresh')
                    _setEmpty($_nextselect)
                }
            }
            
            if (($next && $next.length) && refurl) {
                var val = $obj.data('val'), nextVal = $next.data('val')
                
                if (typeof val == 'undefined') val = $obj.val()
                $.ajax({
                    type     : 'POST',
                    dataType : 'json', 
                    url      : refurl.replace('{value}', encodeURIComponent(val)), 
                    cache    : false,
                    data     : {},
                    success  : function(json) {
                        if (!json) return
                        
                        var html = '', selected = ''
                        
                        $.each(json, function(i) {
                            var value, label
                            
                            if (json[i] && json[i].length) {
                                value = json[i][0]
                                label = json[i][1]
                            } else {
                                value = json[i].value
                                label = json[i].label
                            }
                            if (typeof nextVal != 'undefined') selected = value == nextVal ? ' selected' : ''
                            html += '<option value="'+ value +'"'+ selected +'>' + label + '</option>'
                        })
                        
                        $obj.removeAttr('data-val').removeData('val')
                        $next.removeAttr('data-val').removeData('val')
                        
                        if (!html) {
                            html = $next.data('emptytxt') || '&nbsp;'
                            html = '<option>'+ html +'</option>'
                        }
                        
                        $next.html(html).selectpicker('refresh')
                        _setEmpty($next)
                    },
                    error   : BJUI.ajaxError
                })
            }
        }
        
        $selectpicker.each(function() {
            var $element  = $(this)
            var options   = $element.data()
            var $next     = $(options.nextselect)
            
            $element.addClass('show-tick')
            if (!options.style) $element.data('style', 'btn-default')
            if (!options.width) $element.data('width', 'auto')
            if (!options.container) $element.data('container', 'body')
            else if (options.container == true) $element.attr('data-container', 'false').data('container', false)
            
            $element.selectpicker()
            
            if ($next && $next.length && (typeof $next.data('val') != 'undefined'))
                bjui_select_linkage($element, $next)
        })
        
        /* bootstrap - select - linkage && Trigger validation */
        $selectpicker.change(function() {
            var $element    = $(this)
            var $nextselect = $($element.data('nextselect'))
            
            bjui_select_linkage($element, $nextselect)
            
            /* Trigger validation */
            if ($element.attr('aria-required')) {
                $element.trigger('validate')
            }
        })
        
        /* zTree - plugin */
        $box.find('[data-toggle="ztree"]').each(function() {
            var $this = $(this), op = $this.data(), options = op.options, _setting
            
            if (options && typeof options == 'string') options = options.toObj()
            if (options) $.extend(op, typeof options == 'object' && options)
            
            _setting = op.setting
            
            if (!op.nodes) {
                op.nodes = []
                $this.find('> li').each(function() {
                    var $li   = $(this)
                    var node  = $li.data()
                    
                    if (node.pid) node.pId = node.pid
                    node.name = $li.html()
                    op.nodes.push(node)
                })
                $this.empty()
            } else {
                if (typeof op.nodes == 'string') {
                    if (op.nodes.trim().startsWith('[') || op.nodes.trim().startsWith('{')) {
                        op.nodes = op.nodes.toObj()
                    } else {
                        op.nodes = op.nodes.toFunc()
                    }
                }
                if (typeof op.nodes == 'function') {
                    op.nodes = op.nodes.call(this)
                }
                
                $this.removeAttr('data-nodes')
            }
            
            if (!op.showRemoveBtn) op.showRemoveBtn = false
            if (!op.showRenameBtn) op.showRenameBtn = false
            if (op.addHoverDom && typeof op.addHoverDom != 'function')       op.addHoverDom    = (op.addHoverDom == 'edit')    ? _addHoverDom    : op.addHoverDom.toFunc()
            if (op.removeHoverDom && typeof op.removeHoverDom != 'function') op.removeHoverDom = (op.removeHoverDom == 'edit') ? _removeHoverDom : op.removeHoverDom.toFunc()
            if (!op.maxAddLevel)   op.maxAddLevel   = 2
            
            var setting = {
                view: {
                    addHoverDom    : op.addHoverDom || null,
                    removeHoverDom : op.removeHoverDom || null,
                    addDiyDom      : op.addDiyDom ? op.addDiyDom.toFunc() : null
                },
                edit: {
                    enable        : op.editEnable,
                    showRemoveBtn : op.showRemoveBtn,
                    showRenameBtn : op.showRenameBtn
                },
                check: {
                    enable    : op.checkEnable,
                    chkStyle  : op.chkStyle,
                    radioType : op.radioType
                },
                callback: {
                    onClick       : op.onClick      ? op.onClick.toFunc()      : null,
                    beforeDrag    : op.beforeDrag   ? op.beforeDrag.toFunc()   : _beforeDrag,
                    beforeDrop    : op.beforeDrop   ? op.beforeDrop.toFunc()   : _beforeDrop,
                    onDrop        : op.onDrop       ? op.onDrop.toFunc()       : null,
                    onCheck       : op.onCheck      ? op.onCheck.toFunc()      : null,
                    beforeRemove  : op.beforeRemove ? op.beforeRemove.toFunc() : null,
                    onRemove      : op.onRemove     ? op.onRemove.toFunc()     : null,
                    onNodeCreated : _onNodeCreated,
                    onCollapse    : _onCollapse,
                    onExpand      : _onExpand
                },
                data: {
                    simpleData: {
                        enable: op.simpleData || true
                    },
                    key: {
                        title: op.title || ''
                    }
                }
            }
            
            if (_setting && typeof _setting == 'string') _setting = _setting.toObj()
            if (_setting) $.extend(true, setting, typeof _setting == 'object' && _setting)
            
            $.fn.zTree.init($this, setting, op.nodes)
            
            var IDMark_A = '_a'
            var zTree    = $.fn.zTree.getZTreeObj($this.attr('id'))
            
            if (op.expandAll) zTree.expandAll(true)
            
            // onCreated
            function _onNodeCreated(event, treeId, treeNode) {
                if (treeNode.faicon) {
                    var $a    = $('#'+ treeNode.tId +'_a')
                    
                    if (!$a.data('faicon')) {
                        $a.data('faicon', true)
                          .addClass('faicon')
                          .find('> span.button').append('<i class="fa fa-'+ treeNode.faicon +'"></i>')
                    }
                }
                if (op.onNodeCreated) {
                    op.onNodeCreated.toFunc().call(this, event, treeId, treeNode)
                }
            }
            // onCollapse
            function _onCollapse(event, treeId, treeNode) {
                if (treeNode.faiconClose) {
                    $('#'+ treeNode.tId +'_ico').find('> i').attr('class', 'fa fa-'+ treeNode.faiconClose)
                }
                console.log('11')
                if (op.onCollapse) {
                    op.onCollapse.toFunc().call(this, event, treeId, treeNode)
                }
            }
            // onExpand
            function _onExpand(event, treeId, treeNode) {
                if (treeNode.faicon && treeNode.faiconClose) {
                    $('#'+ treeNode.tId +'_ico').find('> i').attr('class', 'fa fa-'+ treeNode.faicon)
                }
                if (op.onExpand) {
                    op.onExpand.toFunc().call(this, event, treeId, treeNode)
                }
            }
            // add button, del button
            function _addHoverDom(treeId, treeNode) {
                var level = treeNode.level
                var $obj  = $('#'+ treeNode.tId + IDMark_A)
                var $add  = $('#diyBtn_add_'+ treeNode.id)
                var $del  = $('#diyBtn_del_'+ treeNode.id)
                
                if (!$add.length) {
                    if (level < op.maxAddLevel) {
                        $add = $('<span class="tree_add" id="diyBtn_add_'+ treeNode.id +'" title="添加"></span>')
                        $add.appendTo($obj);
                        $add.on('click', function(){
                            zTree.addNodes(treeNode, {name:'新增Item'})
                        })
                    }
                }
                
                if (!$del.length) {
                    var $del = $('<span class="tree_del" id="diyBtn_del_'+ treeNode.id +'" title="删除"></span>')
                    
                    $del
                        .appendTo($obj)
                        .on('click', function(event) {
                            var delFn = function() {
                                $del.alertmsg('confirm', '确认要删除 '+ treeNode.name +' 吗？', {
                                    okCall: function() {
                                        zTree.removeNode(treeNode)
                                        if (op.onRemove) {
                                            var fn = op.onRemove.toFunc()
                                            
                                            if (fn) fn.call(this, event, treeId, treeNode)
                                        }
                                    },
                                    cancelCall: function () {
                                        return
                                    }
                                })
                            }
                            
                            if (op.beforeRemove) {
                                var fn = op.beforeRemove.toFunc()
                                
                                if (fn) {
                                    var isdel = fn.call(fn, treeId, treeNode)
                                    
                                    if (isdel && isdel == true) delFn()
                                }
                            } else {
                                delFn()
                            }
                        }
                    )
                }
            }
            
            // remove add button && del button
            function _removeHoverDom(treeId, treeNode) {
                var $add = $('#diyBtn_add_'+ treeNode.id)
                var $del = $('#diyBtn_del_'+ treeNode.id)
                
                if ($add && $add.length) {
                    $add.off('click').remove()
                }
                
                if ($del && $del.length) {
                    $del.off('click').remove()
                }
            }
            
            // Drag
            function _beforeDrag(treeId, treeNodes) {
                for (var i = 0; i < treeNodes.length; i++) {
                    if (treeNodes[i].drag === false) {
                        return false
                    }
                }
                return true
            }
            
            function _beforeDrop(treeId, treeNodes, targetNode, moveType) {
                return targetNode ? targetNode.drop !== false : true
            }
        })
        
        /* zTree - drop-down selector */
        var $selectzTree = $box.find('[data-toggle="selectztree"]')
        
        $selectzTree.each(function() {
            var $this   = $(this)
            var options = $this.data(),
                $tree   = $(options.tree),
                w       = parseFloat($this.css('width')),
                h       = $this.outerHeight()
            
            options.width   = options.width || $this.outerWidth()
            options.height  = options.height || 'auto'
            
            if (!$tree || !$tree.length) return
            
            var treeid = $tree.attr('id')
            var $box   = $('#'+ treeid +'_select_box')
            var setPosition = function($box) {
                var top        = $this.offset().top,
                    left       = $this.offset().left,
                    $clone     = $tree.clone().appendTo($('body')),
                    treeHeight = $clone.outerHeight()
                
                $clone.remove()
                
                var offsetBot = $(window).height() - treeHeight - top - h,
                    maxHeight = $(window).height() - top - h
                
                if (options.height == 'auto' && offsetBot < 0) maxHeight = maxHeight + offsetBot
                $box.css({top:(top + h), left:left, 'max-height':maxHeight})
            }
            
            $this.click(function() {
                if ($box && $box.length) {
                    setPosition($box)
                    $box.show()
                    return
                }
                
                var zindex = 2
                var dialog = $.CurrentDialog
                
                if (dialog && dialog.length) {
                    zindex = dialog.css('zIndex') + 1
                }
                $box  = $('<div id="'+ treeid +'_select_box" class="tree-box"></div>')
                            .css({position:'absolute', 'zIndex':zindex, 'min-width':options.width, height:options.height, overflow:'auto', background:'#FAFAFA', border:'1px #EEE solid'})
                            .hide()
                            .appendTo($('body'))
                
                $tree.appendTo($box).css('width','100%').data('fromObj', $this).removeClass('hide').show()
                setPosition($box)
                $box.show()
            })
            
            $('body').on('mousedown', function(e) {
                var $target = $(e.target)
                
                if (!($this[0] == e.target || ($box && $box.length > 0 && $target.closest('.tree-box').length > 0))) {
                    $box.hide()
                }
            })
            
            var $scroll = $this.closest('.bjui-pageContent')
            
            if ($scroll && $scroll.length) {
                $scroll.scroll(function() {
                    if ($box && $box.length) {
                        setPosition($box)
                    }
                })
            }
            
            //destroy selectzTree
            $this.on('destroy.bjui.selectztree', function() {
                $box.remove()
            })
        })
        
        /* accordion */
        $box.find('[data-toggle="accordion"]').each(function() {
            var $this = $(this), hBox = $this.data('heightbox'), height = $this.data('height')
            var initAccordion = function(hBox, height) {
                var offsety   = $this.data('offsety') || 0,
                    height    = height || ($(hBox).outerHeight() - (offsety * 1)),
                    $pheader  = $this.find('.panel-heading'),
                    h1        = $pheader.outerHeight()
                
                h1 = (h1 + 1) * $pheader.length
                $this.css('height', height)
                height = height - h1
                $this.find('.panel-collapse').find('.panel-body').css('height', height)
            }
            
            if ($this.find('> .panel').length) {
                if (hBox || height) {
                    initAccordion(hBox, height)
                    $(window).resize(function() {
                        initAccordion(hBox, height)
                    })
                    
                    $this.on('hidden.bs.collapse', function (e) {
                        var $last = $(this).find('> .panel:last'), $a = $last.find('> .panel-heading > h4 > a')
                        
                        if ($a.hasClass('collapsed'))
                            $last.css('border-bottom', '1px #ddd solid')
                    })
                }
            }
        })
        
        /* Kindeditor */
        $box.find('[data-toggle="kindeditor"]').each(function() {
            var $editor = $(this), options = $editor.data()
            if (options.items && typeof options.items == 'string')
                options.items = options.items.replaceAll('\'', '').replaceAll(' ', '').split(',')
            if (options.afterUpload)         options.afterUpload = options.afterUpload.toFunc()
            if (options.afterSelectFile) options.afterSelectFile = options.afterSelectFile.toFunc()
            if (options.confirmSelect)     options.confirmSelect = options.confirmSelect.toFunc()
            
            var htmlTags = {
                font : [/*'color', 'size', 'face', '.background-color'*/],
                span : ['.color', '.background-color', '.font-size', '.font-family'
                        /*'.color', '.background-color', '.font-size', '.font-family', '.background',
                        '.font-weight', '.font-style', '.text-decoration', '.vertical-align', '.line-height'*/
                ],
                div : ['.margin', '.padding', '.text-align'
                        /*'align', '.border', '.margin', '.padding', '.text-align', '.color',
                        '.background-color', '.font-size', '.font-family', '.font-weight', '.background',
                        '.font-style', '.text-decoration', '.vertical-align', '.margin-left'*/
                ],
                table: ['align', 'width'
                        /*'border', 'cellspacing', 'cellpadding', 'width', 'height', 'align', 'bordercolor',
                        '.padding', '.margin', '.border', 'bgcolor', '.text-align', '.color', '.background-color',
                        '.font-size', '.font-family', '.font-weight', '.font-style', '.text-decoration', '.background',
                        '.width', '.height', '.border-collapse'*/
                ],
                'td,th': ['align', 'valign', 'width', 'height', 'colspan', 'rowspan'
                        /*'align', 'valign', 'width', 'height', 'colspan', 'rowspan', 'bgcolor',
                        '.text-align', '.color', '.background-color', '.font-size', '.font-family', '.font-weight',
                        '.font-style', '.text-decoration', '.vertical-align', '.background', '.border'*/
                ],
                a : ['href', 'target', 'name'],
                embed : ['src', 'width', 'height', 'type', 'loop', 'autostart', 'quality', '.width', '.height', 'align', 'allowscriptaccess'],
                img : ['src', 'width', 'height', 'border', 'alt', 'title', 'align', '.width', '.height', '.border'],
                'p,ol,ul,li,blockquote,h1,h2,h3,h4,h5,h6' : [
                    'class', 'align', '.text-align', '.color', /*'.background-color', '.font-size', '.font-family', '.background',*/
                    '.font-weight', '.font-style', '.text-decoration', '.vertical-align', '.text-indent', '.margin-left'
                ],
                pre : ['class'],
                hr : ['class', '.page-break-after'],
                'br,tbody,tr,strong,b,sub,sup,em,i,u,strike,s,del' : []
            }

            KindEditor.create($editor, {
                pasteType                : options.pasteType,
                minHeight                : options.minHeight || 260,
                autoHeightMode           : options.autoHeight || false,
                items                    : options.items || KindEditor.options.items,
                uploadJson               : options.uploadJson,
                fileManagerJson          : options.fileManagerJson,
                allowFileManager         : options.allowFileManager || true,
                fillDescAfterUploadImage : options.fillDescAfterUploadImage || true, //上传图片成功后转到属性页，为false则直接插入图片[设为true方便自定义函数(X_afterSelect)]
                afterUpload              : options.afterUpload,
                afterSelectFile          : options.afterSelectFile,
                X_afterSelect            : options.confirmSelect,
                htmlTags                 : htmlTags,
                cssPath                  : [
                                                BJUI.PLUGINPATH + 'kindeditor_4.1.10/editor-content.css', 
                                                BJUI.PLUGINPATH + 'kindeditor_4.1.10/plugins/code/prettify.css'
                                           ],
                afterBlur                : function() { this.sync() }
            })
        })

        $box.find('[data-toggle="wangEditor"]').each(function() {
            var $this = $(this)
            var editor = new wangEditor($this);
            var uploadUrl = $this.data('upload');
            editor.config.uploadImgUrl= uploadUrl;
            editor.config.hideLinkImg = true;
            editor.config.uploadImgFileName='file';
            editor.config.menus =  [
                'source',
                '|',
                'bold',
                'underline',
                'italic',
                'strikethrough',
                'eraser',
                'forecolor',
                'bgcolor',
                '|',
                'quote',
                'fontfamily',
                'fontsize',
                'lineheight',
                'indent',
                'head',
                'unorderlist',
                'orderlist',
                'alignleft',
                'aligncenter',
                'alignright',
                '|',
                'link',
                'unlink',
                'table',
                'emotion',
                '|',
                'img',
                'video',
                'location',
                'insertcode',
                '|',
                'undo',
                'redo',
                'fullscreen'
            ];
            editor.config.fontsizes = {
                // 格式：'value': 'title'
                1: '10px',
                2: '13px',
                3: '16px',
                4: '19px',
                5: '22px',
                6: '25px',
                7: '28px'
            };
            editor.create();
        })

        /* colorpicker */
        $box.find('[data-toggle="colorpicker"]').each(function() {
            var $this     = $(this)
            var isbgcolor = $this.data('bgcolor')
            
            $this.colorpicker()
            if (isbgcolor) {
                $this.on('changeColor', function(ev) {
                    $this.css('background-color', ev.color.toHex())
                })
            }
        })
        
        $box.find('[data-toggle="clearcolor"]').each(function() {
            var $this   = $(this)
            var $target = $this.data('target') ? $($this.data('target')) : null
            
            if ($target && $target.length) {
                $this.click(function() {
                    $target.val('')
                    if ($target.data('bgcolor')) $target.css('background-color', '')
                })
            }
        })
        
        /* tooltip */
        $box.find('[data-toggle="tooltip"]').each(function() {
            $(this).tooltip()
        })
        
        /* fixed dropdown-menu width */
        $box.find('[data-toggle="dropdown"]').parent().on('show.bs.dropdown', function(e) {
            var $this = $(this), width = $this.outerWidth(), $menu = $this.find('> .dropdown-menu'), menuWidth = $menu.outerWidth()
            
            if (width > menuWidth) {
                $menu.css('min-width', width)
            }
        })

        /* WebUploader */
        if (WebUploader) {
            var initWebUploader = function($element, index) {
                var old = $element.data('webuploader'), options = $element.data('options')

                if (old) {
                    old.destroy()
                    $element.data('webuploader.wrap').remove()
                }

                if (options) {
                    if (typeof options === 'string') {
                        options = options.trim().toObj()
                    }

                    if (typeof options === 'object') {
                        $element.hide()

                        var $wrap = $('<div id="uploader" class="mutiUpload"><div class="queueList"><div id="dndArea" class="placeholder"><div id="filePicker"></div><p>或将文件拖到这里</p></div></div><div class="statusBar" style="display:none;"><div class="progress"><span class="text">0%</span><span class="percentage"></span></div><div class="info"></div><div class="btns"><div  id="filePicker2"></div><div class="uploadBtn">开始上传</div></div></div>'),
                            // 图片容器
                            $queue = $('<ul class="filelist"></ul>').appendTo($wrap.find('.queueList')),
                            // 状态栏，包括进度和控制按钮
                            $statusBar = $wrap.find('.statusBar'),
                            // 文件总体选择信息。
                            $info = $statusBar.find('.info'),
                            // 上传按钮
                            $upload = $wrap.find('.uploadBtn'),
                            // 没选择文件之前的内容。
                            $placeHolder = $wrap.find('.placeholder'),
                            // 总体进度条
                            $progress = $statusBar.find('.progress').hide(),
                            // 添加的文件数量
                            fileCount = 0,
                            // 添加的文件总大小
                            fileSize = 0,
                            // 优化retina, 在retina下这个值是2
                            ratio = window.devicePixelRatio || 1,
                            // 缩略图大小
                            thumbnailWidth = 110 * ratio,
                            thumbnailHeight = 110 * ratio,
                            // 可能有pedding, ready, uploading, confirm, done.
                            state = 'pedding',
                            // 所有文件的进度信息，key为file id
                            percentages = {},
                            supportTransition = (function() {
                                var s = document.createElement('p').style,
                                    r = 'transition' in s ||
                                        'WebkitTransition' in s ||
                                        'MozTransition' in s ||
                                        'msTransition' in s ||
                                        'OTransition' in s

                                s = null

                                return r
                            })(),
                            // 图片访问基地址
                            basePath = options.basePath || '',
                            // WebUploader实例
                            uploader,
                            // 上传文件的单位(单位 + 类型)
                            upunit = options.upunit || '张图片'

                        // 当有文件添加进来时执行，负责view的创建
                        var addFile = function(file, isuploaded, _index) {
                            if (!file && options.uploaded) {
                                $.each(options.uploaded.split(','), function(i, n) {
                                    var uploadedFile = {id:'WU_FILE_UP_'+ i, name:n.trim(), src:basePath + n.trim()}

                                    addFile(uploadedFile, true, i)

                                    fileCount++
                                })

                                if (fileCount) {
                                    $placeHolder.addClass('element-invisible');
                                    $statusBar.show()
                                    setState('uploaded')
                                }

                                return
                            }

                            if (fileCount >= options.fileNumLimit) {
                                $statusBar.find('#filePicker2').hide()
                            }

                            var uploadedAttr = isuploaded && upunit === '张图片' ? ' style="cursor:pointer;" data-toggle="dialog" data-options="{id:\'bjui-dialog-view-upload-image\', image:\''+ encodeURIComponent(file.src) +'\', width:800, height:500, mask:true, title:\'查看已上传图片\'}"' : '',
                                $li = $('<li class="'+ (isuploaded ? 'uploaded' : '') +'" id="'+ file.id +'_'+ index +'">' +
                                    '<p class="title">' + file.name + '</p>' +
                                    '<p class="imgWrap" '+ uploadedAttr +'></p>'+
                                    '<p class="progress"><span></span></p>' +
                                    '</li>'),
                                $btns = $('<div class="file-panel">' +
                                    '<span class="cancel">删除</span>' +
                                    '<span class="rotateRight">向右旋转</span>' +
                                    '<span class="rotateLeft">向左旋转</span></div>').appendTo($li),
                                $prgress = $li.find('p.progress span'),
                                $imgWrap = $li.find('p.imgWrap'),
                                $info = $('<p class="error"></p>'),
                                text = '',
                                showError = function(code) {
                                    switch(code) {
                                        case 'exceed_size':
                                            text = '文件大小超出'

                                            break
                                        case 'interrupt':
                                            text = '上传暂停'

                                            break
                                        default:
                                            text = '上传失败，请重试'

                                            break
                                    }

                                    $info.text(text).appendTo($li)
                                }

                            if (!isuploaded) {
                                if (file.getStatus() === 'invalid') {
                                    showError(file.statusText)
                                } else {
                                    // @todo lazyload
                                    $imgWrap.text('预览中')
                                    uploader.makeThumb(file, function(error, src) {
                                        if (error) {
                                            $imgWrap.text('不能预览')
                                            return
                                        }

                                        var img = $('<img src="'+src+'">')

                                        $imgWrap.empty().append(img)
                                    }, thumbnailWidth, thumbnailHeight)

                                    percentages[file.id] = [file.size, 0]
                                    file.rotation = 0
                                }

                                file.on('statuschange', function(cur, prev) {
                                    if (prev === 'progress') {
                                        $prgress.hide().width(0)
                                    } else if (prev === 'queued') {

                                    }

                                    // 成功
                                    if (cur === 'error' || cur === 'invalid') {
                                        showError(file.statusText)
                                        percentages[file.id][1] = 1
                                    } else if (cur === 'interrupt') {
                                        showError('interrupt')
                                    } else if (cur === 'queued') {
                                        percentages[file.id][1] = 0
                                    } else if (cur === 'progress') {
                                        $info.remove()
                                        $prgress.css('display', 'block')
                                    } else if (cur === 'complete') {
                                        $li.append('<span class="success"></span>')
                                    }

                                    $li.removeClass('state-' + prev).addClass('state-' + cur)
                                })
                            } else {
                                $imgWrap.empty().append('<img src="'+ file.src +'">')
                                if (options.initUploaded) {
                                    var arr = options.initUploaded.split(',')

                                    $li.append('<input type="hidden" class="upload" name="'+ (options.upname || $element.data('name')) +'" value="'+ arr[_index] +'">')
                                }
                            }

                            $li.on('mouseenter', function() {
                                $btns.stop().animate({height: 30})
                            })

                            $li.on('mouseleave', function() {
                                $btns.stop().animate({height: 0})
                            })

                            $btns.on('click', 'span', function() {
                                var index = $(this).index(),
                                    deg

                                switch (index) {
                                    case 0:
                                        if (isuploaded) {
                                            fileCount --
                                            removeFile(file)

                                            if (!fileCount) {
                                                setState('pedding');
                                            }
                                            uploader.refresh()
                                            updateTotalProgress()
                                        } else {
                                            uploader.removeFile(file)
                                        }

                                        return
                                    case 1:
                                        file.rotation += 90

                                        break
                                    case 2:
                                        file.rotation -= 90

                                        break
                                }

                                if (supportTransition) {
                                    deg = 'rotate(' + file.rotation + 'deg)'
                                    $imgWrap.css({
                                        '-webkit-transform': deg,
                                        '-mos-transform': deg,
                                        '-o-transform': deg,
                                        'transform': deg
                                    })
                                } else {
                                    $imgWrap.css('filter', 'progid:DXImageTransform.Microsoft.BasicImage(rotation='+ (~~((file.rotation/90)%4 + 4)%4) +')')
                                }
                            })

                            $li.appendTo($queue)
                        }

                        // 负责view的销毁
                        var removeFile = function(file) {
                            var $li = $wrap.find('#'+ file.id +'_'+ index)

                            delete percentages[file.id]
                            updateTotalProgress()

                            $li.off().find('.file-panel').off().end().remove()

                            if (fileCount < options.fileNumLimit) {
                                $statusBar.find('#filePicker2').show()
                            }
                        }

                        var updateTotalProgress = function() {
                            var loaded = 0,
                                total  = 0,
                                spans  = $progress.children(),
                                percent

                            $.each(percentages, function(k, v) {
                                total  += v[0]
                                loaded += v[0] * v[1]
                            })

                            percent = total ? loaded / total : 0

                            spans.eq(0).text(Math.round(percent * 100) + '%')
                            spans.eq(1).css('width', Math.round(percent * 100) + '%')
                            updateStatus()
                        }

                        var updateStatus = function() {
                            var text = '', stats;

                            if (state === 'ready') {
                                text = '选中'+ fileCount + upunit + '，共'+ WebUploader.formatSize(fileSize) +'。'
                            } else if (state === 'confirm') {
                                stats = uploader.getStats()
                                if (stats.uploadFailNum) {
                                    text = '已成功上传'+ stats.successNum + upunit +'，'+
                                        stats.uploadFailNum + upunit +'上传失败，<a class="retry" href="#">重新上传</a> 或 <a class="ignore" href="#">忽略</a>'
                                }

                            } else if (state === 'uploaded') {
                                text = '已上传'+ fileCount + upunit
                            } else {
                                stats = uploader.getStats()
                                text = '共'+ fileCount + upunit +'（' + WebUploader.formatSize(fileSize) +'），已上传' + stats.successNum

                                if (stats.uploadFailNum) {
                                    text += '，失败' + stats.uploadFailNum
                                }
                            }

                            $info.html(text)
                            $element.data('fileCount', fileCount)
                        }

                        var setState = function(val) {
                            var file, stats

                            if (val === state) {
                                return
                            }

                            $upload.removeClass('state-' + state)
                            $upload.addClass('state-' + val)
                            state = val

                            switch (state) {
                                case 'pedding':
                                    $placeHolder.removeClass('element-invisible')
                                    $queue.parent().removeClass('filled')
                                    $queue.hide();
                                    $statusBar.addClass('element-invisible')
                                    uploader.refresh()

                                    break
                                case 'ready':
                                    $placeHolder.addClass('element-invisible')
                                    $wrap.find('#filePicker2').removeClass('element-invisible')
                                    $queue.parent().addClass('filled')
                                    $queue.show()
                                    $statusBar.removeClass('element-invisible')
                                    uploader.refresh()
                                    $upload.removeClass('disabled')

                                    break
                                case 'uploading':
                                    $wrap.find('#filePicker2').addClass('element-invisible')
                                    $progress.show()
                                    $upload.text('暂停上传')

                                    break
                                case 'paused':
                                    $progress.show()
                                    $upload.text('继续上传')

                                    break
                                case 'confirm':
                                    $progress.hide()
                                    $upload.text('开始上传').addClass('disabled')

                                    stats = uploader.getStats()
                                    if (stats.successNum && !stats.uploadFailNum) {
                                        setState('finish')
                                        return
                                    }

                                    break
                                case 'finish':
                                    stats = uploader.getStats()
                                    if (stats.successNum) {

                                    } else {
                                        // 没有成功的图片，重设
                                        state = 'done'

                                        BJUI.alertmsg('info', '上传失败！')
                                    }

                                    break
                                case 'uploaded':
                                    $upload.text('开始上传').addClass('disabled')

                                    break
                            }

                            if (state !== 'uploaded')
                                updateStatus()
                        }

                        $wrap.insertAfter($element)

                        if (!WebUploader.Uploader.support()) {
                            alert('Web Uploader 不支持您的浏览器！如果你使用的是IE浏览器，请尝试升级 flash 播放器');
                            throw new Error('WebUploader does not support the browser you are using.');
                        }

                        // 是否允许重新上传
                        if (typeof options.reupload === 'undefined')
                            options.reupload = true

                        options = $.extend(true, {}, {
                            pick: {
                                id: $wrap.find('#filePicker'),
                                label: '点击选择图片'
                            },
                            dnd: $wrap.find('.queueList'),
                            paste: false,
                            accept: {
                                title: 'Images',
                                extensions: 'gif,jpg,jpeg,bmp,png',
                                mimeTypes: 'image/*'
                            },
                            swf: BJUI.PLUGINPATH + 'webuploader/Uploader.swf', // swf文件路径
                            disableGlobalDnd: false,
                            chunked: false,
                            server: null,
                            fileNumLimit: 300,
                            fileSizeLimit: 200 * 1024 * 1024,        // 200 M
                            fileSingleSizeLimit: 50 * 1024 * 1024    // 50 M
                        }, options)

                        // 实例化
                        uploader = WebUploader.create(options)

                        // 如果有已上传的图片(编辑时)
                        if (typeof $element.data('uploaded') !== 'undefined')
                            options.uploaded = $element.data('uploaded')
                        if (options.uploaded) {
                            // 将已上传图片加到队列
                            addFile()

                            $queue.parent().addClass('filled')
                        }

                        // 上传成功
                        uploader.on('uploadSuccess', function(file, response) {
                            if (response[BJUI.keys.statusCode] != BJUI.statusCode.ok) {
                                BJUI.alertmsg('error', response.message)
                            } else {
                                var $li = $wrap.find('#'+ file.id +'_'+ index)

                                $li.find('input.upload').remove().end()
                                    .append('<input type="hidden" class="upload" name="'+ (options.upname || $element.data('name')) +'" value="'+ response.filename +'">')
                            }
                        })
                        // 上传失败
                        uploader.on('uploadError', function(file, response) {
                            BJUI.alertmsg('error', response.message)
                        })

                        // 添加“添加文件”的按钮，
                        if (options.fileNumLimit > 1) {
                            uploader.addButton({
                                id: $wrap.find('#filePicker2'),
                                label: '继续添加'
                            })

                            if (fileCount >= options.fileNumLimit)
                                $statusBar.find('#filePicker2').hide()
                        }

                        uploader.onUploadProgress = function(file, percentage) {
                            var $li = $wrap.find('#'+ file.id +'_'+ index),
                                $percent = $li.find('.progress span')

                            $percent.css('width', percentage * 100 + '%')
                            percentages[file.id][1] = percentage
                            updateTotalProgress()
                        }

                        uploader.onFileQueued = function(file) {
                            fileCount++
                            fileSize += file.size

                            if (fileCount === 1) {
                                $placeHolder.addClass('element-invisible');
                                $statusBar.show()
                            }

                            addFile(file)
                            setState('ready')
                            updateTotalProgress()
                        }

                        uploader.onFileDequeued = function(file) {
                            fileCount--
                            fileSize -= file.size

                            if (!fileCount) {
                                setState('pedding');
                            }

                            removeFile(file)
                            updateTotalProgress()
                        }

                        uploader.on('all', function(type) {
                            var stats

                            switch(type) {
                                case 'uploadFinished':
                                    setState('confirm')

                                    break
                                case 'startUpload':
                                    setState('uploading')

                                    break
                                case 'stopUpload':
                                    setState('paused')

                                    break
                            }
                        })

                        uploader.onError = function(code) {
                            if (code === 'Q_EXCEED_NUM_LIMIT') {
                                BJUI.alertmsg('info', '只允许上传'+ options.fileNumLimit + upunit +'！')
                            } else if (code === 'Q_TYPE_DENIED') {
                                BJUI.alertmsg('info', '不支持的文件类型！')
                            } else if (code === 'F_EXCEED_SIZE') {
                                BJUI.alertmsg('info', '文件太大了！')
                            } else if (code === 'F_DUPLICATE') {
                                BJUI.alertmsg('info', '已添加过该文件！')
                            } else {
                                BJUI.alertmsg('info', code)
                            }
                        }

                        $upload.on('click', function() {
                            if ($(this).hasClass('disabled')) {
                                return false
                            }

                            if (state === 'ready') {
                                uploader.upload()
                            } else if (state === 'paused') {
                                uploader.upload()
                            } else if (state === 'uploading') {
                                uploader.stop()
                            }
                        })

                        $info.on('click', '.retry', function() {
                            uploader.retry()
                        })

                        $info.on('click', '.ignore', function() {
                            alert('todo')
                        })

                        $upload.addClass('state-' + state)
                        updateTotalProgress()

                        $element.data('webuploader', uploader).data('webuploader.wrap', $wrap)
                    }
                }
            }

            $box.find('input[data-toggle="webuploader"]').each(function(i) {
                initWebUploader($(this), i)

                $(this).on('reload.webuploader', function() {
                    initWebUploader($(this), i)
                })
            })
        }


        /* not validate */
        $box.find('form[data-toggle="ajaxform"]').each(function() {
            $(this).validator({ignore: ':input'})
            $(this).validator('destroy')
        })

        /* ========================================================================
         * @description highCharts
         * @author 小策一喋 <xvpindex@qq.com>
         * @Blog http://www.topjui.com
         * ======================================================================== */
        var $highcharts = $box.find('[data-toggle="highcharts"]')
        
        $highcharts.each(function(){
            var $element = $(this)
            var options  = $element.data()
            
            $.get(options.url, function(chartData){
                $element.highcharts(chartData)
            }, 'json')
        })

        /* ========================================================================
         * @description ECharts
         * @author 小策一喋 <xvpindex@qq.com>
         * @Blog http://www.topjui.com
         * ======================================================================== */
        var $echarts = $box.find('[data-toggle="echarts"]')
        
        $echarts.each(function(){
            var $element = $(this)
            var options  = $element.data()
            var theme    = options.theme ? options.theme : 'blue'
            var typeArr  = options.type.split(',')

            require.config({
                paths: {
                    echarts: BJUI.PLUGINPATH + 'echarts'
                }
            })

            require(
                [
                    'echarts',
                    'echarts/theme/' + theme,
                    'echarts/chart/' + typeArr[0],
                    typeArr[1] ? 'echarts/chart/' + typeArr[1] : 'echarts'
                ],
                function (ec,theme) {
                    var myChart = ec.init($element[0],theme)

                    $.get(options.url, function(chartData){
                        myChart.setOption(chartData)
                    }, 'json')
                }
            )
        })
        
    })
    
}(jQuery);