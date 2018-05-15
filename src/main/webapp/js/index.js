$(function(){
	$(".cate-title li").hover(function(){
		$(this).css("color","dodgerblue");
		$(this).find(".cate-text").css("color","#000").stop().slideDown();
	},function(){
		$(this).css("color","#000");
		$(this).find(".cate-text").stop().slideUp();
	})
})