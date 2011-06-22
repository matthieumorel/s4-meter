package io.s4.meter.controller.plugin.wordcountdump;

import io.s4.dispatcher.EventDispatcher;
import io.s4.meter.controller.plugin.randomdoc.Document;
import io.s4.meter.controller.plugin.randomdoc.DumpRequest;
import io.s4.processor.AbstractPE;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * 
 * This PE generates a dump request and dispactches in to a special "local broadcast" stream. 
 * 
 * Within a node, events received in this stream are actually sent to all PEs subscribed to the stream.
 *
 */
public class WordCountDumperPE extends AbstractPE {

    private EventDispatcher dispatcher;
    private String outputStreamName;
    private String dumpFilePath;
    private String id;

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public String getOutputStreamName() {
        return outputStreamName;
    }

    public void setOutputStreamName(String outputStreamName) {
        this.outputStreamName = outputStreamName;
    }

    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public void processEvent(Document doc) throws IOException {

        
        if (doc.getText() == null || !"dump".equals(doc.getText())) {
            return;
        }

        File dump = new File(dumpFilePath);
        if (dump.exists()) {
            if (!dump.delete()) {
                throw new IOException("Dump file already exists [" + dump.getAbsolutePath() + "]");
            }
        }
        if (!dump.createNewFile()) {
            throw new IOException("Cannot create dump file [" + dump.getAbsolutePath() +"]");
        } 

        // send a broadcast dump event to PEs
        dispatcher.dispatchEvent("Words_broadcast", new DumpRequest(dumpFilePath));

        Logger.getLogger(getClass()).info("Finished dumping all states in " + dumpFilePath);
    }

    @Override
    public void output() {
    }

    public void setDumpFilePath(String dumpFilePath) {
        this.dumpFilePath = dumpFilePath;
    }

    public String getDumpFilePath() {
        return dumpFilePath;
    }

}
