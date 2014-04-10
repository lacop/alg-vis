package algvis.ds.cacheoblivious.cobtree;

import algvis.ds.cacheoblivious.orderedfile.OrderedFile;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.dictionaries.bst.BST;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.util.ArrayList;
import java.util.List;

public class COBTree extends BST {

    public static String adtName = "cacheoblivious";
    // TODO rename
    public static String dsName = "cobtree";

    StaticTree vEBtree;
    OrderedFile orderedFile;

    protected COBTree(VisPanel panel) {
        super(panel);

        vEBtree = new StaticTree(panel);
        orderedFile = new OrderedFile(panel, vEBtree);
    }

    @Override
    public void insert(int x) {
        start(new COBTreeInsert(this, x));
    }

    @Override
    public void draw(View V) {
        super.draw(V);

        // OF draws cross-link edges, make it first so vEB leafs can overlap them
        orderedFile.draw(V);
        vEBtree.draw(V);
    }

    public void init(List<Integer> keys) {
        // Insert keys into ordered file
        orderedFile.initialize(keys);

        // Retrieve all ordered file elements in order
        // This will form the leaves for vEB tree
        ArrayList<Integer> leaves = new ArrayList<Integer>();
        ((OrderedFileNode) orderedFile.getRoot()).getElements(leaves, true);

        // Form full BST max tree over leaves
        vEBtree.initWithLeaves(leaves);

        reposition();
    }

    @Override
    public void move() {
        super.move();

        vEBtree.move();
        orderedFile.move();
    }

    @Override
    public void reposition() {
        super.reposition();
        vEBtree.reposition();
        orderedFile.reposition();
    }
}
