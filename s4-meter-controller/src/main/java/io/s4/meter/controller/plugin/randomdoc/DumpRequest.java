package io.s4.meter.controller.plugin.randomdoc;

import java.io.Serializable;

/**
 * A message that represents a request for dumping current state
 *
 */
@SuppressWarnings("serial")
public class DumpRequest implements Serializable {
    
    private String dumpFilePath;
    
    public DumpRequest() {} 

    public DumpRequest(String dumpFilePath) {
        this.setDumpFilePath(dumpFilePath);
    }

    public void setDumpFilePath(String dumpFilePath) {
        this.dumpFilePath = dumpFilePath;
    }

    public String getDumpFilePath() {
        return dumpFilePath;
    }
    
    

}
