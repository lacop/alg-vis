package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.Algorithm;
import algvis.ds.intervaltree.IntervalNode;

import java.util.ArrayList;

public class OrderedFileInsert extends Algorithm {
    final OrderedFile OF;
    final int pos;
    final int value;

    public OrderedFileInsert(OrderedFile OF, int pos, int value) {
        super(OF.panel);

        this.OF = OF;
        this.pos = pos;
        this.value = value;
    }

    // TODO pause and highlight
    // TODO explain steps
    @Override
    public void runAlgorithm() throws InterruptedException {
        // Step 1 - insert into leaf group
        int leafOffset = pos / OF.leafSize;
        int leafPos = pos % OF.leafSize;

        OrderedFileNode insertLeaf = OF.leaves.get(leafOffset);
        insertLeaf.insertAtPos(leafPos, value);

        // Step 2 - Walk up the tree until balanced node is found
        OrderedFileNode node = insertLeaf;
        while (!node.densityWithinThresholds()) {
            node = (OrderedFileNode) node.getParent();
            if (node == null) {
                // Parent is unbalanced
                // TODO double size
                return;
            }
        }

        // TODO interval highlighting like in interval tree?
        node.mark();
        //pause();
        node.unmark();

        // Step 3 - Evenly rebalance interval
        ArrayList<Integer> elements = new ArrayList<Integer>();
        // Collect all elements in order
        node.insertElements(elements);

        // Get all leaves belonging to this interval
        ArrayList<OrderedFileNode> leaves = new ArrayList<OrderedFileNode>();
        node.getLeaves(leaves);

        // TODO recursive redistribution? cleaner code probably

        int share = elements.size() / leaves.size();
        int leftover = elements.size() % leaves.size();

        // Redistribute evenly
        int start = 0;

        for(OrderedFileNode leaf : leaves) {
            int end = start + share;
            // Put leftover at beginning
            // TODO recursive, put leftover into different subtrees?
            if (leftover > 0 && end - start < OF.leafSize) {
                int extra = Math.min(leftover, OF.leafSize - (end - start));
                leftover -= extra;
                end += extra;
            }

            leaf.insertEvenly(elements.subList(start, end));
            start = end;
        }


    }
}
