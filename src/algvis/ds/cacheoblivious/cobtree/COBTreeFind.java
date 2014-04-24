package algvis.ds.cacheoblivious.cobtree;

import algvis.core.Algorithm;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.REL;

public class COBTreeFind extends Algorithm {

    final COBTree tree;
    final int key;

    protected COBTreeFind(COBTree tree, int key) {
        super(tree.panel);

        this.tree = tree;
        this.key = key;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        // TODO reuse in COBTreeInsert
        BSTNode node = tree.vEBtree.getRoot();
        // TODO remove magic number 200
        addStep(node.x, node.y, 200, REL.TOP, "cobtree-find-start");
        pause();

        while (!node.isLeaf()) {
            node.mark();

            BSTNode nextnode = null;
            if (node.getLeft().getKey() >= key) {
                addStep(node.x, node.y, 200, REL.TOP, "cobtree-find-left", key, node.getLeft().getKey());
                nextnode = node.getLeft();
            } else {
                addStep(node.x, node.y, 200, REL.TOP, "cobtree-find-right", key, node.getLeft().getKey());
                nextnode = node.getRight();
            }

            pause();
            node.unmark();
            node = nextnode;
        }
        // TODO access nodes to show cache use

        // Found target leaf
        if (node.getKey() == key) {
            node.mark();
            addStep(node.x, node.y, 200, REL.TOP, "cobtree-find-found");
            pause();
            node.unmark();
        } else {
            addStep(node.x, node.y, 200, REL.TOP, "cobtree-find-notfound");
            pause();
        }
    }
}