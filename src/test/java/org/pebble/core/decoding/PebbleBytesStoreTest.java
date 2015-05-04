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

package org.pebble.core.decoding;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.pebble.UnitTest;
import org.pebble.core.PebbleBytesStore;
import org.pebble.core.decoding.iterators.Helper.Input;

import static org.junit.Assert.assertEquals;
import static org.pebble.core.decoding.iterators.Helper.getInput;

@Category(UnitTest.class)
public class PebbleBytesStoreTest {

    @Test
    public void getInputBitStreamItShouldReturnInputBitStreamWithCursorInExpectedPosition() throws Exception {
        final int expectedOffset = 3;
        final Input input = getInput("1 1 1 1 1");
        final PebbleBytesStore bytesStore = new PebbleBytesStore() {
            @Override
            protected byte[] get(int listIndex) {
                return input.buffer;
            }

            @Override
            protected long offset(int listIndex) {
                return expectedOffset;
            }
        };
        final int listIndex = 1;

        final InputBitStream inputBitStream = bytesStore.getInputBitStream(listIndex);

        assertEquals(expectedOffset, inputBitStream.position());
    }

}
