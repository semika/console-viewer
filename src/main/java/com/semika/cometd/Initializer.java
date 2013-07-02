package com.semika.cometd;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * 
 * @author semika
 *
 */
public class Initializer extends HttpServlet {
	
	private static final String START = "start";
	
    private ConsoleLogEmitter emitter;

    @Override
    public void init() throws ServletException {
    	
    	String logFilePath = getInitParameter("logFilePath");
    	
        // Create the emitter
        emitter = new ConsoleLogEmitter(logFilePath);

        // Retrieve the CometD service instantiated by AnnotationCometdServlet
        ConsoleLogService service = (ConsoleLogService)getServletContext().getAttribute(ConsoleLogService.class.getName());

        // Register the service as a listener of the emitter
        emitter.setLogListener(service);

        // Start the emitter
        emitter.start();
    }

    @Override
    public void destroy() {
        emitter.stop();
    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
    	doService(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doService(req, resp);
	}

    public void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String opCode = request.getParameter("opCode");
    	
    	if (START.equals(opCode)) {
    		emitter.start();
    	} else {
    		emitter.stop();
    	}
    	
    	response.setContentType("application/json");
    	PrintWriter out = response.getWriter();
    	
    	Map<String, String> result = new HashMap<String, String>(); 
    	result.put("status", "success"); 
    	Gson gson = new Gson();
    	out.write(gson.toJson(result)); 
    	out.flush();
    }
}