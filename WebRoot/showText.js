var st_timer = null;
$(function(){
	showInit();
});


function showInit(){
	//透明黑色背景
	checkShowInit = true;
	$("<div id='st_mask' onclick='closeMask()'></div>").appendTo("body").css({
		'width' : '100%',
		'height' : '100%',
		'background' : 'rgba(0,0,0,.4)',
		'position' : 'fixed',
		'left' : '0','top' : '0',
		'display' : 'none',
		'z-index' : '1'
	});
	//--------------------------------在body最后添加Confirm的节点
	$("<div id='st_confirmBox'></div>").appendTo("body").css({
		'width' : '100%',
		'position' : 'fixed',
		'left' : '0',
		'top' : '34%',
		'text-align' : 'center',
		'display' : 'none',
		'z-index' : '2',
	});
	$("<div id='st_confirm'></div>").appendTo("#st_confirmBox").css({
		'width' : '80%',
		'margin' : '0 auto',
		'background' : '#fff',
		'border-radius' : '3px',
		'overflow' : 'hidden',
		'padding-top' : '20px',
		'text-align' : 'center',

	});
	$("<span id='st_confirm_text'></span>").appendTo("#st_confirm").css({
		'background' : '#fff',
		'overflow' : 'hidden',
		'padding-top' : '20px',
		'text-align' : 'center',
		'display' : 'block',
		'padding' : '15px 8px 30px',
		
	});
	$("<span class='st_confirm_btn cancel'></span>").appendTo("#st_confirm").css({
		'background' : '#fff',
		'color' : '#8d8d8d',
		'padding' : '8px',
		'text-align' : 'center',
		'display' : 'block',
		'width' : '50%',
		'margin' : '0 auto',
		'float' : 'left',
		'box-sizing' : 'border-box',
		'border-top' : '1px solid #cfcfcf',
		'overflow' : 'hidden',
		'text-overflow' : 'ellipsis',
		'white-space' : 'nowrap'
	});
	$("<span class='st_confirm_btn success'></span>").appendTo("#st_confirm").css({
		'background' : '#1b79f8',
		'color' : '#fff',
		'padding' : '8px',
		'text-align' : 'center',
		'display' : 'block',
		'width' : '50%',
		'margin' : '0 auto',
		'float' : 'left',
		'box-sizing' : 'border-box',
		'border-top' : '1px solid #1b79f8',
		'overflow' : 'hidden',
		'text-overflow' : 'ellipsis',
		'white-space' : 'nowrap'
	});
	$("<div></div>").appendTo("#st_confirm").css({
		'clear' : 'both',
		'display' : 'block',
	});

	//--------------------------------在body最后添加Alert节点
	$("<div id='st_alertBox'></div>").appendTo("body").css({
		'width' : '100%',
		'position' : 'fixed',
		'left' : '0',
		'top' : '34%',
		'text-align' : 'center',
		'display' : 'none',
		'z-index' : '2',
	});
	$("<div id='st_alert'></div>").appendTo("#st_alertBox").css({
		'width' : '80%',
		'margin' : '0 auto',
		'background' : '#fff',
		'border-radius' : '2px',
		'overflow' : 'hidden',
		'padding-top' : '20px',
		'text-align' : 'center',
	});
	$("<span id='st_alert_text'></span>").appendTo("#st_alert").css({
		'background' : '#fff',
		'overflow' : 'hidden',
		'padding-top' : '20px',
		'text-align' : 'center',
		'display' : 'block',
		'padding' : '15px 8px 30px',
	});
	$("<span id='st_alert_btn' onclick='closeMask()'></span>").appendTo("#st_alert").css({
		'background' : '#1b79f8',
		'color' : '#fff',
		'padding' : '8px',
		'text-align' : 'center',
		'display' : 'block',
		'width' : '72%',
		'margin' : '0 auto',
		'margin-bottom' : '20px',
		'border-radius' : '2px',
		'overflow' : 'hidden',
		'text-overflow' : 'ellipsis',
		'white-space' : 'nowrap'
	});

	//---------------------------------在body最后添加Toast节点
	$("<div id='st_toastBox'></div>").appendTo("body").css({
		'width' : '100%',
		'position' : 'fixed',
		'left' : '0',
		'bottom' : '10%',
		'text-align' : 'center',
		'display' : 'none'
	});
	$("<span id='st_toastContent'></span>").appendTo("#st_toastBox").css({
		'color' : '#fff',
		'background' : 'rgba(0,0,0,.8)',
		'padding' : '4px 12px',
		'border-radius' : '4px',
		'max-width' : '80%',
		'display' : 'inline-block'
	});

}
function showToast(obj){
	if(!obj.text){
		return false;
	}
	clearTimeout(st_timer);
	$('#st_toastBox').hide();

	var text = obj.text;
	var time = parseInt(obj.time ? obj.time : 2300);
	var speed = obj.speed ? obj.speed : 'normal';
	var bottom = obj.bottom ? obj.bottom : '10%';
	if(obj.zindex){
		var zindex = parseInt(obj.zindex);
		$('#st_mask').css({'z-index':zindex-1});
		$('#st_toastBox').css({'z-index' : zindex});
	}else{
		$('#st_mask').css({'z-index' : 1});
		$('#st_toastBox').css({'z-index' : 2});
	}

	$('#st_toastBox').css({'bottom' : bottom});

	$('#st_toastContent').text(text);
	$('#st_toastBox').fadeIn(speed);
	st_timer = setTimeout(function(){
		$('#st_toastBox').fadeOut();
	},time);
	
}

function showAlert(obj){
	
	if(!obj.text){
		return false;
	}else{
		var text = obj.text;
		var bgColor = obj.bgColor ? obj.bgColor : '#1b79f8';
		var color = obj.color ? obj.color : '#fff';
		var btnText = obj.btnText ? obj.btnText : '确定';
		var top = obj.top ? obj.top : '34%';

		if(obj.zindex){
			var zindex = parseInt(obj.zindex);
			$('#st_mask').css({'z-index':zindex-1});
			$('#st_alertBox').css({'z-index' : zindex});
		}else{
			$('#st_mask').css({'z-index' : 1});
			$('#st_alertBox').css({'z-index' : 2});
		}

		$('#st_alert_text').text(text);
		$('#st_alert_btn').css({'background' : bgColor});
		$('#st_alert_btn').css({'color':color});
		$('#st_alert_btn').text(btnText);
		$('#st_alertBox').css({'top' : top});
		$('#st_mask,#st_alertBox').show();

		if(obj.success){
			$('#st_alert_btn').off('click').on('click',function(){
				obj.success();
			});
		}
	}

}
function showConfirm(obj){
	if(!obj.text){
		return false;
	}
	var text = obj.text;
	var rightText = obj.rightText ? obj.rightText : '确定';
	var rightBgColor = obj.rightBgColor ? obj.rightBgColor : '#1b79f8';
	var rightColor = obj.rightColor ? obj.rightColor : '#fff';

	var leftText = obj.leftText ? obj.leftText : '取消';
	var top = obj.top ? obj.top : '34%';
	if(obj.zindex){
		var zindex = parseInt(obj.zindex);
		$('#st_mask').css({'z-index':zindex-1});
		$('#st_confirmBox').css({'z-index' : zindex});
	}else{
		$('#st_mask').css({'z-index' : 1});
		$('#st_confirmBox').css({'z-index' : 2});
	}

	$('#st_confirm_text').text(text);
	$('.st_confirm_btn.cancel').text(leftText);
	$('.st_confirm_btn.success').text(rightText);
	$('.st_confirm_btn.success').css({
		'background' : rightBgColor,
		'color' : rightColor,
		'border-top' : '1px solid '+rightBgColor,
	});
	$('#st_confirmBox').css({'top' : top});
	$('#st_mask,#st_confirmBox').show();

	if(obj.cancel){
		$('.st_confirm_btn.cancel').off('click').on('click',function(){
			closeMask();
			obj.cancel();
		})
	}else{
		$('.st_confirm_btn.cancel').off('click').on('click',function(){
			closeMask();
		});
	}
	if(obj.success){
		$('.st_confirm_btn.success').off('click').on('click',function(){
			closeMask();
			obj.success();
		})
	}else{
		$('.st_confirm_btn.success').off('click').on('click',function(){
			closeMask();
		});
	}
}
function closeMask(){
	$('#st_mask,#st_alertBox,#st_confirmBox').hide();
}
