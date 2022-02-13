;(function($) {
	$.fn.disable = function() {
		this.attr('disabled', 'disabled');
	}
	$.fn.enable = function() {
		this.removeAttr('disabled');
	}
})(jQuery);

;(function($){
	$.fn.serializeFormData = function() {
		var form = $(this);
		var values = form.serializeArray();
		var data = {};				
		if (values) {
			$(values).each(function(k,v){
				if(v.name in data && (typeof data[v.name] != 'object')) {
					var element = form.find('[name="'+v.name+'"]');
					//Only for muti select element we need to send array of values
					if(element.is('select') && element.attr('multiple')!=undefined) {
						var prevValue = data[v.name];
						data[v.name] = new Array();
						data[v.name].push(prevValue)
					}
				}
				if(typeof data[v.name] == 'object' ) {
					data[v.name].push(v.value);
				}else{
					data[v.name]=v.value;
				}				
			});
		}
		// If data-type="autocomplete", pickup data-value="..." set
		var autocompletes = $('[data-type="autocomplete"]', $(this));
		$(autocompletes).each(function(i){
			var ac = $(autocompletes[i]);
			data[ac.attr('name')] = ac.data('value');
		});		
		return data;
	};
	function getCookie(cname) {
		var name = cname + "=";
		var decodedCookie = decodeURIComponent(document.cookie);
		var ca = decodedCookie.split(';');
		for(var i = 0; i <ca.length; i++) {
		  var c = ca[i];
		  while (c.charAt(0) == ' ') {
			c = c.substring(1);
		  }
		  if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		  }
		}
		return "";
	  }
	$('body').on('click', '#changepassword', function (e) {
		var oldpassword = $('#password-old').val();
		var passwordnew = $('#password-new').val();
		var passwordconfirm = $('#password-confirm').val();
		if(oldpassword == '') {
			app.helper.showErrorNotification({ 'message' : "Please fill the old password!"});
			return;
		}
		if(passwordnew == '' || passwordnew !== passwordconfirm)
		{
			app.helper.showErrorNotification({ 'message' : "Password confirm not correct!"});
			return;
		}		
		var cookie = getCookie("access_token");
		$.ajax({
			type: 'POST',
			url: 'http://demo.tmsapp.vn:9002/api/v1/agent/changepass?password=' + passwordnew + '&oldpassword=' + oldpassword,
			beforeSend: function (xhr) {
				xhr.setRequestHeader('Authorization', 'Bearer ' + cookie);
			},
			success: function(result){
				console.log(result);                    
				if(result.code != 200) {
					app.helper.showErrorNotification({ 'message' : result.message});
				}else {
					app.helper.showSuccessNotification({ 'message' : result.message});
					$('#modalChangePassForm').modal('toggle');
				}
			}
		});
	});
	
})(jQuery);

;(function($) {
	// Case-insensitive :icontains expression
	$.expr[':'].icontains = function(obj, index, meta, stack){
		return (obj.textContent || obj.innerText || jQuery(obj).text() || '').toLowerCase().indexOf(meta[3].toLowerCase()) >= 0;
	}
})(jQuery);