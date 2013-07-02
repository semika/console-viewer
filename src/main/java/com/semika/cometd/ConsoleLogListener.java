/**
 * 
 */
package com.semika.cometd;

import java.util.EventListener;

/**
 * @author semika
 *
 */
public interface ConsoleLogListener extends EventListener {
	
	void onUpdates(String line);
}
