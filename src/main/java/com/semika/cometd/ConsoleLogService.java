package com.semika.cometd;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;

/**
 * 
 * @author semika
 *
 */
@Service
public class ConsoleLogService implements ConsoleLogListener {
    @Inject
    private BayeuxServer bayeuxServer;
    @Session
    private LocalSession sender;

    public void onUpdates(String line) {
        
            // Create the channel name using the stock symbol
            String channelName = "/console";

            // Initialize the channel, making it persistent and lazy
            bayeuxServer.createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {
                public void configureChannel(ConfigurableServerChannel channel) {
                    channel.setPersistent(true);
                    channel.setLazy(true);
                }
            });

            // Convert the Update business object to a CometD-friendly format
            Map<String, Object> data = new HashMap<String, Object>(4);
            data.put("line", line);

            // Publish to all subscribers
            ServerChannel channel = bayeuxServer.getChannel(channelName);
            channel.publish(sender, data, null);
    }
}