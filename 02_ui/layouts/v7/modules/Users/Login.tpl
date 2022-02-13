{*+**********************************************************************************
* The contents of this file are subject to the vtiger CRM Public License Version 1.1
* ("License"); You may not use this file except in compliance with the License
* The Original Code is: vtiger CRM Open Source
* The Initial Developer of the Original Code is vtiger.
* Portions created by vtiger are Copyright (C) vtiger.
* All Rights Reserved.
************************************************************************************}
{* modules/Users/views/Login.php *}

{strip}
	{$MODULE_NAME}
	<link href="https://fonts.googleapis.com/css?family=Roboto&display=swap" rel="stylesheet">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<style>
		body {
			background:  url(layouts/v7/resources/Images/background.png);
			background-position: center;
			background-size: cover;
			width: 100%;
			height: 96%;
			color: #FFFFFF;
			font-size: 16px;
			font-family: 'Roboto', sans-serif !important;
			background-repeat: no-repeat;
		}
		hr {
			margin-top: 15px;
			background-color: #7C7C7C;
			height: 2px;
			border-width: 0;
		}
		h3, h4 {
			margin-top: 0px;
		}
		hgroup {
			text-align:center;
			margin-top: 4em;
		}
		input {
			font-size: 16px;
			padding: 10px 10px 10px 35px;
			-webkit-appearance: none;
			display: block;
			color: #636363;
			width: 100%;
			border: none;
			border-radius: 0;
			border-bottom: 1px solid #757575;
		}
		input:focus {
			outline: none;
		}
		label {
			font-size: 16px;
			font-weight: normal;
			position: absolute;
			pointer-events: none;
			left: 35px;
			top: 10px;
			transition: all 0.2s ease;
			color: #767676;
		}
		.pass_eye{
			position: absolute;
			top: 50%;
			transform: translateY(-50%);
			right: 10px;
			z-index: 4;
			color: #367BF5;
			opacity: 0.5;
			font-weight: 500;
			font-size: 12px;
			line-height: 14px;
			cursor: pointer;
		}
		.pass_eye:hover{
			opacity: 1;
		}
		input:focus ~ label, input.used ~ label {
			top: -30px;
			transform: scale(.75);
			left: -12px;
			font-size: 18px;
			line-height: 19px;
			color: #FFFFFF;
		}
		input:focus ~ .bar:before, input:focus ~ .bar:after {
			width: 50%;
		}
		.widgetHeight {
			height: 410px;
			margin-top: 20px !important;
		}
		.loginDiv {
			width: 450px;
			/*margin: 0 auto;*/
			position: absolute;
			top: 45%;
			transform: translateY(-50%);
			left: 24%;
			margin: 0;
		}
		.user-logo {
			margin: 0 auto;
			padding-top: 40px;
			padding-bottom: 5px;
		}
		.text-logo{
			font-size: 16px;
			line-height: 28px;
			letter-spacing: 0.15px;
			text-align: center;
			padding-bottom: 40px;
		}
		.group {
			position: relative;
			margin: 20px 20px 40px;
		}
		.group > i{
			position: absolute;
			color: black;
			top: 50%;
			transform: translateY(-50%);
			left: 10px;
			font-weight: bold;
		}
		.failureMessage {
			color: red;
			display: block;
			text-align: center;
			padding: 0px 0px 10px;
		}
		.successMessage {
			color: green;
			display: block;
			text-align: center;
			padding: 0px 0px 10px;
		}
		.app-footer p {
			margin-top: 0px;
		}
		.bar {
			position: relative;
			display: block;
			width: 100%;
		}
		.bar:before, .bar:after {
			content: '';
			width: 0;
			bottom: 1px;
			position: absolute;
			height: 1px;
			background: #35aa47;
			transition: all 0.2s ease;
		}
		.bar:before {
			left: 50%;
		}
		.bar:after {
			right: 50%;
		}
		.button {
			position: relative;
			display: inline-block;
			padding: 9px;
			margin: .3em 0 1.5em 0;
			width: 100%;
			vertical-align: middle;
			color: #fff;
			font-size: 16px;
			line-height: 20px;
			-webkit-font-smoothing: antialiased;
			text-align: center;
			letter-spacing: 1px;
			background: transparent;
			border: 0;
			cursor: pointer;
			transition: all 0.15s ease;
		}
		.button:focus {
			outline: 0;
		}
		.buttonBlue {
			background: #367BF5;
			border-radius: 4px;
			/*background-image: linear-gradient(to bottom, #35aa47 0px, #35aa47 100%)*/
		}
		.forgotPasswordLink{
			display: block;
			text-align: center;
		}
		.forgotPasswordLink:hover{
			color:#367BF5;
		}
		.widgetHeight{
			height: 450px;
		}

		//Animations
		@keyframes inputHighlighter {
			from {
				background: #4a89dc;
			}
			to 	{
				width: 0;
				background: transparent;
			}
		}
		@keyframes ripples {
			0% {
				opacity: 0;
			}
			25% {
				opacity: 1;
			}
			100% {
				width: 200%;
				padding-bottom: 200%;
				opacity: 0;
			}
		}
	</style>

	<span class="app-nav"></span>
	<div>
		<div class="loginDiv widgetHeight">
			<img class="img-responsive user-logo" src="layouts/v7/resources/Images/Logo_Ecommerce.png">
			<div class="text-logo"><span>Please login to continue.</span></div>
			<div>
				<span class="{if !$ERROR}hide{/if} failureMessage" id="validationMessage">{$MESSAGE}</span>
				<span class="{if !$MAIL_STATUS}hide{/if} successMessage">{$MESSAGE}</span>
			</div>

			<div id="loginFormDiv">
				<form class="form-horizontal" method="POST" action="index.php">
					<input type="hidden" name="module" value="Users"/>
					<input type="hidden" name="action" value="Login"/>
					<div class="group">
						<i class="fa fa-user-o" aria-hidden="true"></i>
						<input id="username" type="text" name="username">
						<span class="bar"></span>
						<label>Username</label>
					</div>
					<div class="group">
						<i class="fa fa-unlock-alt" aria-hidden="true"></i>
						<input id="password" type="password" name="password">
						<span class="bar"></span>
						<label>Password</label>
						<span class="pass_eye">SHOW</span>
					</div>
					<div class="group">
						<button type="submit" class="button buttonBlue">Sign in</button><br>
						<a class="forgotPasswordLink">Forgot Password?</a>
					</div>
					<input type="hidden" name="login_type" value="customer" >
				</form>
			</div>

			<div id="forgotPasswordDiv" class="hide">
				<form class="form-horizontal" action="forgotPassword.php" method="POST">
					<div class="group">
						<i class="fa fa-unlock-alt" aria-hidden="true"></i>
						<input id="fusername" type="text" name="username">
						<span class="bar"></span>
						<label>Username</label>
					</div>
					<div class="group">
						<i class="fa fa-envelope-o" aria-hidden="true"></i>
						<input id="email" type="email" name="emailId">
						<span class="bar"></span>
						<label>Email</label>
					</div>
					<div class="group">
						<button type="submit" class="button buttonBlue forgot-submit-btn">Reset Password</button><br>
						<span><a class="forgotPasswordLink"><i class="fa fa-long-arrow-left" aria-hidden="true"></i> Back to Sign In</a></span>
					</div>
				</form>
			</div>
		</div>
		<script>
			jQuery(document).ready(function () {
				var validationMessage = jQuery('#validationMessage');
				var forgotPasswordDiv = jQuery('#forgotPasswordDiv');

				var loginFormDiv = jQuery('#loginFormDiv');
				loginFormDiv.find('#password').focus();

				loginFormDiv.find('a').click(function () {
					jQuery('.text-logo').text('');
					loginFormDiv.toggleClass('hide');
					forgotPasswordDiv.toggleClass('hide');
					validationMessage.addClass('hide');
				});

				forgotPasswordDiv.find('a').click(function () {
					jQuery('.text-logo').text('Please login to continue.');
					loginFormDiv.toggleClass('hide');
					forgotPasswordDiv.toggleClass('hide');
					validationMessage.addClass('hide');
				});

				loginFormDiv.find('button').on('click', function () {
					var username = loginFormDiv.find('#username').val();
					var password = jQuery('#password').val();
					var result = true;
					var errorMessage = '';
					if (username === '') {
						errorMessage = 'Please enter valid username';
						result = false;
					} else if (password === '') {
						errorMessage = 'Please enter valid password';
						result = false;
					}
					if (errorMessage) {
						validationMessage.removeClass('hide').text(errorMessage);
					}
					return result;
				});

				forgotPasswordDiv.find('button').on('click', function () {
					var username = jQuery('#forgotPasswordDiv #fusername').val();
					var email = jQuery('#email').val();

					var email1 = email.replace(/^\s+/, '').replace(/\s+$/, '');
					var emailFilter = /^[^@]+@[^@.]+\.[^@]*\w\w$/;
					var illegalChars = /[\(\)\<\>\,\;\:\\\"\[\]]/;

					var result = true;
					var errorMessage = '';
					if (username === '') {
						errorMessage = 'Please enter valid username';
						result = false;
					} else if (!emailFilter.test(email1) || email == '') {
						errorMessage = 'Please enter valid email address';
						result = false;
					} else if (email.match(illegalChars)) {
						errorMessage = 'The email address contains illegal characters.';
						result = false;
					}
					if (errorMessage) {
						validationMessage.removeClass('hide').text(errorMessage);
					}
					return result;
				});
				jQuery('input').blur(function (e) {
					var currentElement = jQuery(e.currentTarget);
					if (currentElement.val()) {
						currentElement.addClass('used');
					} else {
						currentElement.removeClass('used');
					}
				});

				var ripples = jQuery('.ripples');
				ripples.on('click.Ripples', function (e) {
					jQuery(e.currentTarget).addClass('is-active');
				});

				ripples.on('animationend webkitAnimationEnd mozAnimationEnd oanimationend MSAnimationEnd', function (e) {
					jQuery(e.currentTarget).removeClass('is-active');
				});
				loginFormDiv.find('#username').focus();

				var slider = jQuery('.bxslider').bxSlider({
					auto: true,
					pause: 4000,
					nextText: "",
					prevText: "",
					autoHover: true
				});
				jQuery('.bx-prev, .bx-next, .bx-pager-item').live('click',function(){ slider.startAuto(); });
				jQuery('.bx-wrapper .bx-viewport').css('background-color', 'transparent');
				jQuery('.bx-wrapper .bxslider li').css('text-align', 'left');
				jQuery('.bx-wrapper .bx-pager').css('bottom', '-15px');

				var params = {
					theme		: 'dark-thick',
					setHeight	: '100%',
					advanced	:	{
										autoExpandHorizontalScroll:true,
										setTop: 0
									}
				};
				jQuery('.scrollContainer').mCustomScrollbar(params);
			});
			jQuery(document).ready(function () {
				$('.pass_eye').click(function(){
					$(this).toggleClass('show');
					if($('.pass_eye').hasClass('show')){
						$(this).text('HIDEN');
						$('#password').attr('type', 'text');
					} else {
						$(this).text('SHOW');
						$('#password').attr('type', 'password');
					}
				});
			});

		</script>
	</div>
	{/strip}