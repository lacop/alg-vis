package algvis.ds.cacheoblivious.cobtree;

import algvis.core.Algorithm;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileInsert;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.cacheoblivious.statictree.StaticTreeNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;

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
        while (!node.isLeaf()) {
            if (node.getLeft().getKey() >= key) {
                node = node.getLeft();
            } else {
                node = node.getRight();
            }
        }
        // TODO mark each step, pause, ...
        // TODO access nodes to show cache use

        node.mark();
        //pause();
        node.unmark();

        if (node.getKey() == key) {
            // TODO already existing key
            return;
        }


        // Step 2 - insert at that position into ordered file
        StaticTreeNode stNode = (StaticTreeNode) node;
        int position = stNode.orderedFileOffset*tree.orderedFile.leafSize + stNode.orderedFilePos;
        OrderedFileInsert ofInsert = new OrderedFileInsert(tree.orderedFile, position, key);
        ofInsert.runAlgorithm();

        // OF has doubled, rebuild vEB tree
        if (ofInsert.minOffset == -1 && ofInsert.maxOffset == -1) {
            ArrayList<Integer> leaves = new ArrayList<Integer>();
            ((OrderedFileNode) tree.orderedFile.getRoot()).getElements(leaves, true);

            // Form full BST max tree over leaves
            tree.vEBtree.initWithLeaves(leaves);

            tree.reposition();

            // No need for traversal, already up to date
            return;
        }

        // Step 3 - update affected keys
        // Go through in post-order traversal
        postOrderTraverse(tree.vEBtree.getRoot());
    }

    private void postOrderTraverse(BSTNode node) {
        // TODO skip nodes outside of changed interval
        if (node.isLeaf()) {
            // Get key from ordered file
            StaticTreeNode stNode = (StaticTreeNode) node;
            node.setKey(tree.orderedFile.leaves.get(stNode.orderedFileOffset).getElement(stNode.orderedFilePos));

            return;
        }

        // Update child nodes first
        postOrderTraverse(node.getLeft());
        postOrderTraverse(node.getRight());

        // Set key to  max of child keys
        node.setKey(Math.max(node.getLeft().getKey(), node.getRight().getKey()));
    }


}
