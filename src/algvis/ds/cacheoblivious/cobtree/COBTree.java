package algvis.ds.cacheoblivious.cobtree;

import algvis.ds.cacheoblivious.orderedfile.OrderedFile;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.util.ArrayList;
import java.util.List;

public class COBTree extends StaticTree {

    public static String adtName = "cacheoblivious";
    // TODO rename
    public static String dsName = "btree";

    private StaticTree vEBtree;
    private OrderedFile orderedFile;

    protected COBTree(VisPanel panel) {
        super(panel);

        vEBtree = new StaticTree(panel);
        orderedFile = new OrderedFile(panel, vEBtree);
        //orderedFile = new OrderedFile(panel);
    }

    @Override
    public void insert(int x) {
        //vEBtree.insert(x);
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
