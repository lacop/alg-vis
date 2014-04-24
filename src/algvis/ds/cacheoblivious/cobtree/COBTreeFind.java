package algvis.ds.cacheoblivious.cobtree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.REL;

public class COBTreeFind extends BSTFind {

    protected COBTreeFind(COBTree tree, int key) {
        super(tree.panel, tree.vEBtree, key, null);
    }

    @Override
    protected boolean found(BSTNode w) {
        return w.getKey() == K && w.isLeaf();
    }

    @Override
    protected boolean goRight(BSTNode w) {
        return w.getLeft() != null && w.getLeft().getKey() < K;
    }
}