/*
 * Copyright (c) 2011 Yahoo! Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 	        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License. See accompanying LICENSE file. 
 */

package io.s4.meter.controller.plugin.randomdoc;

import io.s4.dispatcher.EventDispatcher;
import io.s4.processor.AbstractPE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author Leo Neumeyer
 * 
 */
public class WordCounterPE extends AbstractPE {

    private String id;
    transient private EventDispatcher dispatcher;
    private String outputStreamName;
    private int threshold;
    private int count;
    private Word word;
    private int previousCount;
    private long lastUpdated;
    // keep date of first update ever
    private long firstUpdated=-1;

    public void setId(String id) {
        this.id = id;
    }

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

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void processEvent(Word word) {
        this.word = word;
        count += word.getCount();
        lastUpdated = System.currentTimeMillis();
        if (firstUpdated==-1) {
            firstUpdated = lastUpdated;
        }
    }

    /**
     * User-defined handling of a dump request. 
     * 
     * In this specific case, we create a formatted string representing the state of the PE, and we append it to a file.
     * 
     * @param dump dump request
     */
    public void processEvent(DumpRequest dump) {
        if (word ==null) {
            System.out.println("Cannot dump state for " + getId() + " : No word yet!");
            return;
        }
        System.out.println("Dumping state for " + getId() + " + " + state2String());
        try {
            File f = new File(dump.getDumpFilePath()+"_"+ Inet4Address.getLocalHost().getHostName());
            FileWriter fw = null;
            try {
                fw = new FileWriter(f, true);
                fw.append(state2String() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fw!=null) {
                    try {
                        fw.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
    }

    private String state2String() {
        return word.getDocId() + ";" + word.getWord() + ";" + count + ";" + id + ";" + firstUpdated + ";"+ lastUpdated;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.s4.processor.ProcessingElement#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.s4.processor.AbstractPE#output()
     */
    @Override
    public void output() {
        if (previousCount!=count) {
            // only display when count changed
            System.out.printf("Doc ID: %20s, Word: %s, Count: %4d\n", word.getDocId(), word.getWord(), count);
            previousCount = count;
        }
    }

}
