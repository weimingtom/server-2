package org.dna.mqtt.bechnmark;

import java.net.URISyntaxException;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class only publish MQTT messages to a define topic with a certain frequency.
 * 
 * 
 * @author andrea
 */
public class Producer implements Runnable {
    
    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);
    
    private String m_clientID;
    
    public static final int PUB_LOOP = 1000000;
    
    public Producer(String clientID) {
        m_clientID = clientID;
    }

    public void run() {
        MQTT mqtt = new MQTT();
        try {
//            mqtt.setHost("test.mosquitto.org", 1883);
            mqtt.setHost("localhost", 1883);
        } catch (URISyntaxException ex) {
            LOG.error(null, ex);
            return;
        }
        
        mqtt.setClientId(m_clientID);
        BlockingConnection connection = mqtt.blockingConnection();
        try {
            connection.connect();
        } catch (Exception ex) {
            LOG.error("Cant't CONNECT to the server", ex);
            return;
        }
        
        //TODO loop
        for (int i = 0; i < PUB_LOOP; i++) {
            try {
//                LOG.info("Publishing");
                String payload = "Hello world MQTT!!" + i;
                connection.publish("/topic", payload.getBytes(), QoS.AT_MOST_ONCE, false);
            } catch (Exception ex) {
                LOG.error("Cant't PUBLISH to the server", ex);
                return;
            }
        }
        try {
            LOG.info("Disconneting");
            connection.disconnect();
            LOG.info("Disconnected");
        } catch (Exception ex) {
            LOG.error("Cant't DISCONNECT to the server", ex);
        }
    }
    
}
