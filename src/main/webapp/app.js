(function($) {
	
	$.consoleReader = {
			
			cometd: $.cometd,
			connected: false,
			wasConnected: false,
			isUserDisconnect:false,
			
		    clear: function() {
		    	$('#status').html("");
		    },
		    
			connectionEstablished: function() {
				$('#statusMessage').html("<span style='color:#0B3B0B;'>Connection Established<span>");
			},
			
			connectionBroken: function() {
				$('#statusMessage').html("<span style='color:#DF0101;'>Connection Broken<span>");
			},
			
			connectionClosed: function() {
				$('#statusMessage').html("<span style='color:#DF0101;'>Connection Closed<span>");
			},
			
			disconnect: function() {
				$('#statusMessage').html("<span style='color:#0B3B0B;'>Disconnecting...<span>");
				$.ajax({
					url:config.contextPath + "/init",
					type:'POST',
					dataType:'json',
					data:{opCode:'stop'},
					success: function(data) {
						$.consoleReader.cometd.disconnect(true);
						$.consoleReader.connectionClosed();
						$.consoleReader.isUserDisconnect = true;
					},
					error: function() {
					}
				});
			},
			
			connect: function() {
				$('#statusMessage').html("<span style='color:#0B3B0B;'>Connecting.......<span>");
				$.ajax({
					url:config.contextPath + "/init",
					type:'POST',
					dataType:'json',
					data:{opCode:'start'},
					success: function(data) {
						$.consoleReader.cometd.handshake();
					},
					error: function() {
						alert("error");
					}
				});
			},
			
			metaConnect: function(message) {
				if ($.consoleReader.cometd.isDisconnected()) {
					$.consoleReader.connected = false;
					$.consoleReader.connectionClosed();
	                return;
	            }

				$.consoleReader.wasConnected = $.consoleReader.connected;
				$.consoleReader.connected    = message.successful === true;
	            
	            if ((!$.consoleReader.wasConnected && $.consoleReader.connected) 
	            		|| ($.consoleReader.isUserDisconnect && $.consoleReader.connected)) {
	            	$.consoleReader.connectionEstablished();
	            } else if ($.consoleReader.wasConnected && !$.consoleReader.connected) {
	            	$.consoleReader.connectionBroken();
	            }
			},
			
			metaHandshake: function(message) {
				if (message.successful) {
					$.consoleReader.cometd.subscribe('/console', function(message) {
	                    var data   = message.data;
	                    var text = data.line;
	                    if (text.length > 0) {
	                    	var objDiv = document.getElementById("status");
	                    	objDiv.scrollTop = objDiv.scrollHeight + 500;
	                    	$('#status').append(text);
	                    }
	                });
	            }
			},
			
			init: function() {
				// Disconnect when the page unloads
		        $(window).unload(function() {
		        	$.consoleReader.cometd.disconnect(true);
		        	$.consoleReader.disconnect();
		        });
		        
		        var cometURL = location.protocol + "//" + location.host + config.contextPath + "/cometd";
		        $.consoleReader.cometd.configure({
		            url: cometURL,
		            logLevel: 'debug'
		        });
		        
				$.consoleReader.cometd.addListener('/meta/handshake', $.consoleReader.metaHandshake);
				$.consoleReader.cometd.addListener('/meta/connect', $.consoleReader.metaConnect);
			},
			
			onLoad: function() {
				$.consoleReader.init();
			}
	}
    
})(jQuery);
