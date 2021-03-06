/*
 * Copyright (c) 2010 Yahoo! Inc. All rights reserved.
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

import java.io.Serializable;

/**
 * A data class for sending text documents.
 * 
 * @author Leo Neumeyer
 *
 */
@SuppressWarnings("serial")
public class Document  implements Serializable {
    private String id;
    private String text;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{id:")
          .append(id)         
          .append(",text:")
          .append(text)                      
          .append("}");

        return sb.toString();
    }
}
