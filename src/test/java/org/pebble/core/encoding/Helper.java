package org.pebble.core.encoding;

/**
 *  Copyright 2015 Groupon
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import it.unimi.dsi.fastutil.ints.IntList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helper {

    private static final int DEFAULT_DATA_SIZE = 1024;

    public static String toBinaryString(byte[] bytes, int bitSize) {
        StringBuilder stringBuilder = new StringBuilder(bitSize);
        for (int i = 0, byteSize = (int) Math.ceil(bitSize / 8.0); i < byteSize; i++) {
            for (int j = 0; j < 8 && i * 8 + j < bitSize; j++) {
                stringBuilder.append(bytes[i] >> (7 - j) & 0x1);
            }
        }
        return stringBuilder.toString();
    }

    public static Output getOutput() {
        final byte[] buffer = new byte[DEFAULT_DATA_SIZE];
        final OutputSuccinctStream out = new OutputSuccinctStream(buffer);
        return new Output(buffer, out);
    }

    public static class Output {

        public final byte[] buffer;
        public final OutputSuccinctStream stream;

        private Output(byte[] buffer, OutputSuccinctStream stream) {
            this.buffer = buffer;
            this.stream = stream;
        }

        public void close() throws IOException {
            stream.close();
        }

    }

    public static <T, Q extends Map<T, IntList>> Map<T, List<Integer>> translateToUtilsCollection(final Q map) {
        //TODO: fix equals of Int2ReferenceMap or replace with trove library.
        final Map<T, List<Integer>> translatedMap = new HashMap<T, List<Integer>>();
        List<Integer> list;
        for(Map.Entry<T, IntList> entry : map.entrySet()) {
            translatedMap.put(entry.getKey(), list = new ArrayList<Integer>());
            for(int value : entry.getValue()) {
                list.add(value);
            }
        }
        return translatedMap;
    }

    public static <T, Q extends List<T>> List<List<T>> translateToUtilsCollection(final Q[] lists) {
        //TODO: fix equals of Int2ReferenceMap or replace with trove library.
        final List<List<T>> translatedLists = new ArrayList<List<T>>();
        List<T> translatedList;
        for(Q list : lists) {
            translatedLists.add(translatedList = new ArrayList<T>());
            for(T value : list) {
                translatedList.add(value);
            }
        }
        return translatedLists;
    }

}
