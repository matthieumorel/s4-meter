package io.s4.meter.controller.plugin.wordcountdump;

import io.s4.meter.common.EventGenerator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Generates a dump request event
 *
 */
public class WordCountDumpGenerator extends EventGenerator {

    protected WordCountDumpGenerator(String hostname, String port, String s4StreamName, String s4EventClassName) {
        super(hostname, port, s4StreamName, s4EventClassName, 1, 1);
    }

    @Override
    protected JSONObject getDocument(String docId) throws JSONException {
        JSONObject jsonDoc = new JSONObject();
        jsonDoc.put("text", "dump");
        return jsonDoc;
    }

    @Override
    protected void init() {
    }

}
