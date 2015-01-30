/* 
 * Copyright 2014 Wouter Pinnoo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.pinnoo.garbagecalendar.data;

import java.io.Serializable;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class Sector implements Serializable {

    private static final long serialVersionUID = -843402748713889036L;
    private AreaType type;
    private String code;

    public Sector(AreaType type, String code) {
        this.type = type;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Sector) {
            Sector s = (Sector) o;
            return s.getCode().equals(getCode())
                    && s.getType().equals(getType());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 23 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return type.toString() + code;
    }

    public Sector(String str) {
        if (str.matches("[LlVv][0-9][0-9]")) {
            switch (str.charAt(0)) {
                case 'L':
                    type = AreaType.L;
                    break;
                case 'V':
                    type = AreaType.V;
                    break;
                default:
                    type = AreaType.NONE;
            }
            code = str.substring(1);
        } else {
            type = AreaType.CITY;
            code = str;
        }
    }

    public AreaType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
}
