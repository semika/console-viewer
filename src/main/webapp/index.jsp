<html>
<head>
    <title>Server Console Viewer</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd/cometd.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd/AckExtension.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/org/cometd/ReloadExtension.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery-1.8.2.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/jquery.cometd-reload.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/app.js"></script>
    
    <style type="text/css">
    	
    	body {
    		overflow: hidden;
    		background-color: #CCCCCC;
    	}
	     #status {
	     	overflow: auto;
	     	font-family: verdana;
	     	font-size: 12px;
	     	color:#FFFFFF;
	     	background-color: #000000;
	        height: 95%;	
	     }
	     
	     #statusMessage {
	     	font-family: verdana;
	     	font-size: 12px;
	     	color:#FF0000;
	     	font-weight: bold;
	     }
    </style>
</head>

<body>
    <div>
	    <button id="btnClear" name="btnClear" onclick="$.consoleReader.clear();">Clear</button>
	    <button id="btnStop"  name="btnStop"  onclick="$.consoleReader.disconnect();">Disconnect</button>
	    <button id="btnStart" name="btnStart" onclick="$.consoleReader.connect();">Connect</button>
		<span id="statusMessage"></span>
    </div>
    <div id="status"></div>
    <script type="text/javascript">
        var config = {
            contextPath: '${pageContext.request.contextPath}'
        };
    
	    $(document).ready(function() {
	    	$.consoleReader.init(); 
	    });
    </script>
</body>

</html>
