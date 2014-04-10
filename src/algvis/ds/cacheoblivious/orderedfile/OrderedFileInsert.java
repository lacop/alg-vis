package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.Algorithm;

import java.util.ArrayList;

public class OrderedFileInsert extends Algorithm {
    final OrderedFile OF;
    final int pos;
    final int value;

    // Offset range of changed interval
    int minOffset = -1;
    int maxOffset = -1;

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
        // There will always be at least one empty slot to fit
        int leafOffset = pos / OF.leafSize;
        int leafPos = pos % OF.leafSize;

        OrderedFileNode insertLeaf = OF.leaves.get(leafOffset);
        insertLeaf.insertAtPos(leafPos, value);
        //pause();

        // Step 2 - Walk up the tree until balanced node is found
        // Make sure there will be empty spot in every leaf
        OrderedFileNode node = insertLeaf;
        while (node != null) {
            node.mark();
            //pause();
            // Needs to be withing density thresholds
            if (node.densityWithinThresholds()) {
                // Need to have enough space to leave empty slot in every leaf after rebalance
                if (node.extraEmptySlots() >= 0) {
                    // Can balance subtree rooted in this node
                    break;
                }
            }

            // Need to go higher
            node.unmark();
            node = (OrderedFileNode) node.getParent();
        }
        // Couldn't find suitable subtree to rebalance
        // => root is unbalanced start over with fresh ordered file
        if (node == null) {
            // Collect all elements in order
            ArrayList<Integer> elements = new ArrayList<Integer>();
            OF.root.getElements(elements, false);

            // TODO for prettier animation instead insert just new nodes and connect them
            OF.initialize(elements);

            return;
            // TODO report to COBTree?
        }

        // TODO interval highlighting like in interval tree?
        node.mark();
        //pause();
        node.unmark();

        // Step 3 - Evenly rebalance interval
        ArrayList<Integer> elements = new ArrayList<Integer>();
        // Collect all elements in order
        node.getElements(elements, false);

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
            // Only fill leafsSize - 1 to always leave one empty slot for insert
            // TODO make full? but then need make insert more complicated
            if (leftover > 0 && end - start < OF.leafSize - 1) {
                int extra = Math.min(leftover, OF.leafSize - 1 - (end - start));
                leftover -= extra;
                end += extra;
            }

            leaf.insertEvenly(elements.subList(start, end));
            start = end;

            // Update affected range
            if (minOffset == -1 || minOffset > leaf.offset) minOffset = leafOffset;
            if (maxOffset == -1 || maxOffset < leaf.offset) maxOffset = leafOffset;
        }


    }
}
