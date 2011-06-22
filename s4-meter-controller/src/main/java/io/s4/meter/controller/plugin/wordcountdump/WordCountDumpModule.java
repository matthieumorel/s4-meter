package io.s4.meter.controller.plugin.wordcountdump;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Provides;
import com.google.inject.Singleton;

import io.s4.meter.common.EventGenerator;
import io.s4.meter.controller.ControllerModule;
import io.s4.meter.controller.plugin.randomdoc.RandomDocGenerator;
import io.s4.meter.controller.plugin.randomdoc.RandomDocModule;

/**
 * Configures a module for generating dump requests
 *
 */
public class WordCountDumpModule extends ControllerModule {
    
    private static Logger logger = Logger.getLogger(WordCountDumpModule.class);
    
    @Provides
    @Singleton
    List<EventGenerator> provideEventGenerators() throws Exception {

        String[] hostnames = config.getStringArray("s4Adaptor.hostnames");
        String[] ports = config.getStringArray("s4Adaptor.ports");
        List<EventGenerator> eventGenerators = new ArrayList<EventGenerator>();
        EventGenerator gen;

        if (hostnames.length != ports.length) {
            logger.error("Mismatched length: s4Adaptor.hostnames and s4Adaptor.ports");
            throw new Exception();
        }

        int numAdaptors = hostnames.length;
        for (int i = 0; i < numAdaptors; i++) {
            gen = new WordCountDumpGenerator(hostnames[i], ports[i],
                    config.getString("s4App.streamName"),
                    config.getString("s4App.eventClassName")
                    );
            eventGenerators.add(gen);
        }
        return eventGenerators;
    }

}
