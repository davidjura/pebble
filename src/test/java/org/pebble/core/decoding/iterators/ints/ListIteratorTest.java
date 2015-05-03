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

package org.pebble.core.decoding.iterators.ints;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.pebble.UnitTest;
import org.pebble.core.PebbleBytesStore;
import org.pebble.core.PebbleOffsetsStore;
import org.pebble.core.decoding.iterators.Helper;
import org.pebble.utils.BytesArrayPebbleBytesStore;
import org.pebble.utils.LongListPebbleOffsetsStore;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@Category(UnitTest.class)
public class ListIteratorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void whenNonRemainingElementsHasNextItShouldReturnFalse() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 1 1");
        final ListIterator iterator = buildIterator(input);

        assertFalse(iterator.hasNext());
    }

    @Test
    public void whenRemainingElementsHasNextItShouldReturnTrue() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 0100 11 1 1");
        final ListIterator iterator = buildIterator(input);

        assertTrue(iterator.hasNext());
    }

    @Test
    public void whenNonRemainingElementsNextIntItShouldReturnMinusOne() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 1 1");
        final ListIterator iterator = buildIterator(input);

        assertEquals(-1, iterator.nextInt());
    }

    @Test
    public void whenEvenDiffIndexRemainingElementsNextIntItShouldReturnFirstRemainingElement() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 0100 11 1 1");
        final ListIterator iterator = buildIterator(input);
        final int expectedFirstRemainingElement = 3;

        assertEquals(expectedFirstRemainingElement, iterator.nextInt());
    }

    @Test
    public void whenOddDiffIndexRemainingElementsNextIntItShouldReturnFirstRemainingElement() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 0101 11 1 1 0101 0100");
        final ListIterator iterator = buildIterator(input);
        iterator.nextInt();
        final int expectedFirstRemainingElement = 3;

        assertEquals(expectedFirstRemainingElement, iterator.nextInt());
    }

    @Test
    public void whenInputStreamIsCorruptedItShouldThrowExpectedIllegalStateException() throws IOException {
        final Helper.Input input = Helper.getInput("1 1 0101 11 1 1");
        final ListIterator iterator = buildIterator(input);
        expectedException.expect(IllegalStateException.class);

        iterator.nextInt();
    }

    private static ListIterator buildIterator(final Helper.Input input) throws IOException {
        final int valueBitSize = 2;
        final int listIndex = 0;
        final PebbleOffsetsStore offsetsStore = new LongListPebbleOffsetsStore(new long[] {0L});
        PebbleBytesStore bytesStore = new BytesArrayPebbleBytesStore(input.buffer, offsetsStore);
        return ListIterator.build(
            listIndex,
            valueBitSize,
            bytesStore
        );
    }

}
