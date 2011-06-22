/*
 * Copyright (c) 2011 Yahoo! Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License. See accompanying LICENSE file. 
 */
package io.s4.meter.controller.plugin.randomdoc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import io.s4.meter.common.EventGenerator;

/**
 * Custom event generator. Send documents comprised of words separated by spaces
 * and words comprised of random characters based on a predefined dictionary.
 * 
 * This class was intended for performance evaluation applications.
 * 
 * @author Leo Neumeyer
 * 
 */
@SuppressWarnings("serial")
public class RandomDocGenerator extends EventGenerator {

    private static Logger logger = Logger.getLogger(RandomDocGenerator.class);

    final private int wordSize;
    final private int numWordsPerDoc;
    transient private RandomWord wg;
    LinkedList<String> wordsList = new LinkedList<String>();
    long nbEvents = 0;

    /**
     * The generator can only be created using this constructor.
     * 
     * @param wordSize
     *            the number of characters in a word.
     * @param numWordsPerDoc
     *            the number of words in a document.
     * @see io.s4.meter.common.EventGenerator#EventGenerator(String hostname,
     *      String port, String s4StreamName, String s4EventClassName, float
     *      eventRate, long numEvents)
     */
    RandomDocGenerator(String hostname, String port, String s4StreamName, String s4EventClassName, float eventRate,
            long numEvents, int wordSize, int numWordsPerDoc) {
        super(hostname, port, s4StreamName, s4EventClassName, eventRate, numEvents);

        this.wordSize = wordSize;
        this.numWordsPerDoc = numWordsPerDoc;
    }

    /*
     * The init method is called by the base class after the non-transient
     * fields are set.
     * 
     * @see io.s4.meter.common.EventGenerator#init()
     */
    @Override
    protected void init() {

        logger.info("Started RandomDoc event generator.");
        wg = new RandomWord(0, wordSize);
    }

    /*
     * 
     * @see io.s4.meter.common.EventGenerator#start()
     */
    @Override
    protected JSONObject getDocument(String docId) throws JSONException {

        JSONObject jsonDoc;
        StringBuilder words = new StringBuilder();

        for (int j = 0; j < numWordsPerDoc; j++) {
            words.append(wg.getWord());
            words.append(" ");
        }

        /* Create JSON doc. */
        jsonDoc = new JSONObject();

        jsonDoc.put("id", docId);
        jsonDoc.put("text", words.toString());

        nbEvents++;

        /* Send event. */
        return jsonDoc;

    }

    /**
     * @return the wordSize
     */
    public int getWordSize() {
        return wordSize;
    }

    /**
     * @return the numWordsPerDoc
     */
    public int getNumWordsPerDoc() {
        return numWordsPerDoc;
    }

    @Override
    public void start() throws Exception {
        super.start();
        // regenerate words and dump them
        System.out.println("Regenerating words for " + nbEvents + " events and " + numWordsPerDoc
                + " words per doc from thread: " + Thread.currentThread().toString());
        RandomWord wg2 = new RandomWord(0, wordSize);
        for (int i = 0; i < nbEvents; i++) {
            for (int j = 0; j < numWordsPerDoc; j++) {
                String word = wg2.getWord();
                wordsList.add(word);
            }
        }
        HashSet<String> uniqueWords = new HashSet<String>();
        uniqueWords.addAll(wordsList);
        Map<String, Integer> wordsCounts = new HashMap<String, Integer>();
        for (String word : uniqueWords) {
            wordsCounts.put(word, Collections.frequency(wordsList, word));
        }

        File f = new File("generatedWords.txt");
        if (f.exists()) {
            if (!f.delete()) {
                throw (new IOException("Could not delete old generated words file"));
            }
        }

        if (!f.createNewFile()) {
            throw (new IOException("Could not create generated words file"));
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        Set<Entry<String, Integer>> entrySet = wordsCounts.entrySet();
        for (Entry<String, Integer> entry : entrySet) {
            bw.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        bw.close();

    }

}
