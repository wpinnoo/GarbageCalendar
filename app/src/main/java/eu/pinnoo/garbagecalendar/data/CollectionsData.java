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

import java.util.ArrayList;
import java.util.List;

import eu.pinnoo.garbagecalendar.data.caches.CollectionCache;

/**
 *
 * @author Wouter Pinnoo <pinnoo.wouter@gmail.com>
 */
public class CollectionsData implements DataContainer {

    private static final CollectionsData instance = new CollectionsData();
    private ArrayList<Collection> collections;

    private CollectionsData() {
        collections = new ArrayList<Collection>();
    }

    public static CollectionsData getInstance() {
        return instance;
    }

    @Override
    public int initialize() {
        if (collections == null || collections.isEmpty()) {
            collections = CollectionCache.getInstance().get(LocalConstants.CacheName.COLLECTIONS_DATA.toString());
            return 0;
        } else {
            return 1;
        }
    }

    public void resetCollections() {
        if (isSet()) {
            collections.clear();
        }
        CollectionCache.getInstance().clear();
    }

    public void setCollections(ArrayList<Collection> list) {
        resetCollections();
        collections = list;
        CollectionCache.getInstance().put(LocalConstants.CacheName.COLLECTIONS_DATA.toString(), collections);
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public boolean isSet() {
        return collections != null && !collections.isEmpty();
    }
}
