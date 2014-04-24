package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.Algorithm;
import algvis.ui.view.REL;

import java.util.ArrayList;

public class OrderedFileInsert extends Algorithm {
    final OrderedFile OF;
    final int pos;
    final int value;

    // Offset range of changed interval
    public int minOffset = -1;
    public int maxOffset = -1;

    public OrderedFileInsert(OrderedFile OF, int pos, int value) {
        super(OF.panel);

        this.OF = OF;
        this.pos = pos;
        this.value = value;
    }

    public OrderedFileInsert(Algorithm a, OrderedFile OF, int pos, int value) {
        super(OF.panel, a);
        this.OF = OF;
        this.pos = pos;
        this.value = value;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        // Step 1 - insert into leaf group
        // There will always be at least one empty slot to fit
        int leafOffset = pos / OF.leafSize;
        int leafPos = pos % OF.leafSize;

        // Find the leaf
        OrderedFileNode insertLeaf = OF.leaves.get(leafOffset);

        addStep(insertLeaf.x, insertLeaf.y, 200, REL.BOTTOM, "of-insert-group-find");
        insertLeaf.mark(leafPos);
        pause();

        // Rewrite group in this leaf
        insertLeaf.insertAtPos(leafPos, value);

        addStep(insertLeaf.x, insertLeaf.y, 200, REL.BOTTOM, "of-insert-group-rewrite");
        insertLeaf.mark();
        pause();
        insertLeaf.unmark();

        // Step 2 - Walk up the tree until balanced node is found
        // Make sure there will be empty spot in every leaf
        OrderedFileNode node = insertLeaf;
        while (node != null) {
            node.mark();
            addStep(node.x, node.y, 200, REL.TOP, "of-insert-find-balanced");
            pause();
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
            addStep(0, 0, 200, REL.TOP, "of-insert-root-unbalanced");
            pause();

            ArrayList<Integer> elements = new ArrayList<Integer>();
            OF.root.getElements(elements, false);

            // TODO for prettier animation instead insert just new nodes and connect them
            OF.initialize(elements);

            return;
            // TODO report to COBTree?
        }

        // TODO interval highlighting like in interval tree?
        node.mark();
        addStep(node.x, node.y, 200, REL.TOP, "of-insert-found-balanced");
        pause();
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

        // Mark nodes
        int avgX = 0;
        for(OrderedFileNode leaf : leaves) {
            avgX += leaf.x;
            leaf.mark();
        }
        avgX /= leaves.size();

        addStep(avgX, leaves.get(0).y, 200, REL.BOTTOM, "of-insert-interval");
        pause();

        for(OrderedFileNode leaf : leaves) {
            leaf.unmark();
        }
    }
}
