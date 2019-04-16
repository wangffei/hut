var box = $("<div id='test-box' style='width:100%;z-index:-1;height:"+$(document).height()+"px;position:fixed;top:0px;'></div>")

var box_do = {
	box_clear : function(){
		box.children().each(function(){
			$(this).remove()
		})
	},
	box_remove : function(){
		$("#test-box").remove()		
	},
	box_add : function(a){
		box.append(a)		
	},
	box_show : function(){
		$("body").append(box)
	},
	span_do : function(data,size){
		data = data.trim()
		var span = $("<span style='visibility:hidden;font-size:"+size+";'>"+data+"</span>")
		box_do.box_add(span)
		box_do.box_show()
		return span.width()+"."+span.height();
	}
}

var msgBox = {
	boxShow_F : function(value){
		
		var time = arguments[1] ? arguments[1] : 1300 ;
		
		var size = arguments[2] ? arguments[2] : "20px" ;
		
		var b = box_do.span_do(value,size)
		box_do.box_clear()
		box_do.box_remove()
		var len = b.split("\.")[0]*1
		var height = b.split("\.")[1]*1
		
		var div = $("<div class='msgBox' style='z-index:9999;left:"+($(document).width()-len-10)/2+"px;position:absolute;top:"+($(document).height()-height-6)/2+"px;width:"+(len+10)+"px;height:"+(height+6)+"px;background:black;'><div style='color:white;width:"+(len+3)+"px;font-size:"+size+";margin:3px auto 0px;'>"+value+"</div></div>")
		$("body").append(div)
		function show(){
			div.remove() ;
		}

		window.setTimeout(show,time)
		return div;
	},
	boxShow_L : function(value){
		var size = arguments[2] ? arguments[2] : "20px" ;
		var b = box_do.span_do(value,size);
		box_do.box_clear();
		box_do.box_remove();
		var len = b.split("\.")[0]*1;
		var height = b.split("\.")[1]*1;
		
		var div = $("<div class='msgBox' style='background:rgba(0,0,0,0.6);filter:alpha(opacity=60);-moz-opacity:0.6;-khtml-opacity: 0.6;opacity: 0.6;width:"+$(document).width()+"px;height:"+$(document).height()+"px;position:absolute;top:0px;'><div style='z-index:9999;left:"+($(document).width()-len-10)/2+"px;position:absolute;top:"+($(document).height()-height-50)/2+"px;width:"+(len+10)+"px;height:"+(height+50)+"px;background:black;'><div style='color:white;width:"+(len+3)+"px;font-size:"+size+";margin:3px auto 0px;'>"+value+"</div><img style='display:block;width:30px;height:30px;margin:10px auto 0px;' src='uploading.gif'></div></div>")
		$("body").append(div)

		return div;
	},
	boxRemove : function(a){
		a.remove() ;
	}
}