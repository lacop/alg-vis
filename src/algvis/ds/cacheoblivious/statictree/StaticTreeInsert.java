package algvis.ds.cacheoblivious.statictree;

import algvis.core.Algorithm;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTInsert;
import algvis.ds.dictionaries.bst.BSTNode;

public class StaticTreeInsert extends BSTInsert {

    public StaticTreeInsert(BST T, BSTNode v) {
        super(T, v);
    }

    public StaticTreeInsert(BST T, BSTNode v, Algorithm a) {
        super(T, v, a);
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        super.runAlgorithm();
        ((StaticTree) T).setOrder();
    }
}
