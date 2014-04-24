package algvis.ds.cacheoblivious.cobtree;

import algvis.core.Algorithm;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileInsert;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.cacheoblivious.statictree.StaticTreeNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.REL;

import java.util.ArrayList;

public class COBTreeInsert extends Algorithm {

    final COBTree tree;
    final int key;

    protected COBTreeInsert(COBTree tree, int key) {
        super(tree.panel);

        this.tree = tree;
        this.key = key;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        // Step 1 - find successor key in tree
        BSTNode node = tree.vEBtree.getRoot();
        addStep(node, REL.TOP, "cobtree-insert-find-start");
        pause();

        COBTreeFind cobFind = new COBTreeFind(this, tree, key);
        cobFind.setLastStep(false);
        cobFind.runAlgorithm();

        node = cobFind.getLastNode();
        node.mark();
        if (cobFind.getFound()) {
            addStep(node, REL.TOP, "cobtree-insert-existing");
            pause();
            node.unmark();

            return;
        }

        addStep(node, REL.TOP, "cobtree-insert-found");
        pause();
        node.unmark();

        // Step 2 - insert at that position into ordered file
        StaticTreeNode stNode = (StaticTreeNode) node;
        int position = stNode.orderedFileOffset*tree.orderedFile.leafSize + stNode.orderedFilePos;
        OrderedFileInsert ofInsert = new OrderedFileInsert(this, tree.orderedFile, position, key);
        ofInsert.runAlgorithm();

        // OF has doubled, rebuild vEB tree
        if (ofInsert.minOffset == -1 && ofInsert.maxOffset == -1) {
            ArrayList<Integer> leaves = new ArrayList<Integer>();
            ((OrderedFileNode) tree.orderedFile.getRoot()).getElements(leaves, true);

            // Form full BST max tree over leaves
            tree.vEBtree.initWithLeaves(leaves);
            tree.reposition();

            // TODO better animation? but layout is broken
            addStep(tree.vEBtree.getRoot(), REL.TOP, "cobtree-insert-of-resized");
            pause();

            // No need for traversal, already up to date
            return;
        }

        // Step 3 - update affected keys
        // Go through in post-order traversal
        addStep(tree.vEBtree.getRoot(), REL.TOP, "cobtree-insert-traverse");
        pause();

        postOrderTraverse(tree.vEBtree.getRoot(), ofInsert.minOffset, ofInsert.maxOffset);
    }

    private boolean postOrderTraverse(BSTNode node, int minOffset, int maxOffset) throws InterruptedException {
        int newKey = 0;

        if (node.isLeaf()) {
            StaticTreeNode stNode = (StaticTreeNode) node;
            // Ignore leaves outside modified interval
            if (stNode.orderedFileOffset < minOffset || stNode.orderedFileOffset > maxOffset) {
                return false;
            }

            // Get new key from ordered file
            OrderedFileNode ofNode = tree.orderedFile.leaves.get(stNode.orderedFileOffset);
            newKey = ofNode.getElement(stNode.orderedFilePos);
        } else {
            // Update child nodes first
            boolean leftChanged = postOrderTraverse(node.getLeft(), minOffset, maxOffset);
            boolean rightChanged = postOrderTraverse(node.getRight(), minOffset, maxOffset);

            // Neither child node has changed, skip
            if (!leftChanged && !rightChanged) {
                return false;
            }

            // Set key to  max of child keys
            newKey = Math.max(node.getLeft().getKey(), node.getRight().getKey());
        }

        // No change
        if (newKey == node.getKey()) {
            return false;
        }

        if (node.isLeaf()) {
            addStep(node, REL.TOP, "cobtree-insert-update-leaf");
        } else {
            addStep(node, REL.TOP, "cobtree-insert-update-node");
        }

        node.mark();
        pause();
        node.unmark();

        node.setKey(newKey);

        return true;
    }


}
