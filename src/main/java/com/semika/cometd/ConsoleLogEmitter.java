package com.semika.cometd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author semika
 *
 */
public class ConsoleLogEmitter {
    
	private Timer timer;
    private ConsoleLogListener logListener;
    private Long filePointer = 0L;
    private String logFilePath;

    public ConsoleLogEmitter(String logFilePath) {
    	this.logFilePath = logFilePath;
    }

    public void start() {
    	try {
    		if (this.logFilePath == null) {
    			//Default tomcat's catalina.out file
    			String logFile = System.getProperty("catalina.base") + File.separator + "logs" + File.separator + "catalina.out";
    			setLogFilePath(logFile);
    		} 
    		File file = new File(this.logFilePath); 
    		RandomAccessFile raf = new RandomAccessFile(file, "r");
    		while((raf.readLine()) != null) {}
    		setFilePointer(raf.getFilePointer());
    		raf.seek(raf.getFilePointer());
    		startTimer(raf);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void stop() {
         timer.cancel();
    }

    private void startTimer(final RandomAccessFile raf) throws IOException {  
    	timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask() {
    		@Override
    		public void run() {
    			try {
    				StringBuilder sb = new StringBuilder();
    	    		String str = null;
    				while((str = raf.readLine()) != null) {
    					
    					String[] tokens = str.split("\n");
    					StringBuffer buff = new StringBuffer();
    					for (String token : tokens) {
    					    buff.append(token).append("<br>"); 
    					}
    					
    					String newLineLessString = buff.toString();
    					String spaceLessString = newLineLessString.replaceAll("[\\s]", "&nbsp;");
    					
    					sb.append(spaceLessString);
    				} 
    				getLogListener().onUpdates(sb.toString()); 
    				setFilePointer(raf.getFilePointer());
    				raf.seek(raf.getFilePointer());
    		        
    			} catch (IOException e) { 
    				e.printStackTrace();
    			}
    		}
    	}, 0, 500);
    }
    
	public Long getFilePointer() {
		return filePointer;
	}

	public void setFilePointer(Long filePointer) {
		this.filePointer = filePointer;
	}

	public ConsoleLogListener getLogListener() {
		return logListener;
	}

	public void setLogListener(ConsoleLogListener logListener) {
		this.logListener = logListener;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
    
}