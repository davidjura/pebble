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

import it.unimi.dsi.fastutil.ints.IntIterator;
import org.pebble.core.PebbleBytesStore;
import org.pebble.core.decoding.InputBitStream;

import java.io.IOException;

/**
 * Implements the iterator of an incremental lists, which removes all original duplications of elements. The resulting
 * behaviour is like iterating over an strictly incremental list.
 */
class IncrementalReferenceUniqueIterator extends ReferenceIterator {

    /**
     * @param listIndex offset of the current list that is described in terms of reference.
     * @param valueBitSize fixed number of bits used to represent value in list to be encoded. It can be any value
     *                     between 1bit and 31 bits
     * @param minIntervalSize min size of intervals used to encode the compressed list.
     * @param inputBitStream input bit stream used to read the compressed lists representations.
     * @param bytesStore mapping between list offsets and data bytes arrays and bytes offsets.
     * @throws IOException when there is an exception reading from <code>inputBitStream</code>.
     */
    public IncrementalReferenceUniqueIterator(
        final int listIndex,
        final int valueBitSize,
        final int minIntervalSize,
        final InputBitStream inputBitStream,
        final PebbleBytesStore bytesStore
    ) throws IOException {
        super(listIndex, valueBitSize, minIntervalSize, inputBitStream, bytesStore);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntIterator getReferenceListIterator(
        final int listIndex,
        final InputBitStream inputBitStream
    ) throws IOException {
        return IncrementalListUniqueIterator.build(listIndex, valueBitSize, minIntervalSize, inputBitStream, bytesStore);
    }

}
