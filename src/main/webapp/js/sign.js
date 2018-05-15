$(function(){
	$(".signin-title").click(function(){
		$(this).css({
			width:"221px",
			height:"50px",
			border:"2px solid #fff",
			"border-bottom":"none"
		});
		$(".signup-title").css({
			width:"225px",
			height:"50px",
			border:"none",
			"border-bottom":"2px solid #fff"
		});
		$(".signin-form").show();
		$(".signup-form").hide();
	});
	$(".signup-title").click(function(){
		$(this).css({
			width:"223px",
			height:"50px",
			border:"2px solid #fff",
			"border-bottom":"none"
		});
		$(".signin-title").css({
			width:"225px",
			height:"50px",
			border:"none",
			"border-bottom":"2px solid #fff"
		});
		$(".signup-form").show();
		$(".signin-form").hide();
	});
	
	
	$(".login-butt").click(function(){
		// ajax 登录
		$.ajax({
			type:"GET",
			dataType: "JSON",
            contentType:'application/json;charset=UTF-8',
			url:"http://localhost:8080/education/ajaxUserLogin",
			data:$(".signin-form").serialize(),
			success:function(result){
				if(result.eventCode == 'error'){
					alert("密码或账户错误");
				}else{
					$("body").empty();
					$("body").css("background","none");
					$("body").load("index.html",function(){
						$(".header-sign").hide();
						$(".header-signined").show().html(result.username);
					});
				}
			}
		});
	});

    $(".logup-butt").click(function(){
        // ajax 注册
        $.ajax({
            type:"GET",
            dataType: "JSON",
            contentType:'application/json;charset=UTF-8',
            url:"http://localhost:8080/education/ajaxUserLogup",
            data:$(".signup-form").serialize(),
            success:function(result){
                if(result.eventCode == 'exist'){
                    alert("昵称已存在");
                } else if(result.eventCode == 'success'){
                    alert("注册成功！请登录");
                }
            }
        });
    });
})