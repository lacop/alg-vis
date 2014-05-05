package algvis.ds.cacheoblivious.cobtree;

import algvis.ds.cacheoblivious.orderedfile.OrderedFile;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
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
        vEBtree.drawArray = false;
        orderedFile = new OrderedFile(panel, vEBtree);
    }

    @Override
    public void insert(int x) {
        start(new COBTreeInsert(this, x));
    }

    @Override
    public void find(int x) {
        start(new COBTreeFind(this, x));
    }

    @Override
    public void clear() {
        //init(new ArrayList(Arrays.asList(new Integer[]{1, 3, 5, 7})));
        init(new ArrayList(Arrays.asList(new Integer[]{1, 3, 5, 7, 9, 11, 13, 15})));
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

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        vEBtree.storeState(state);
        orderedFile.storeState(state);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        vEBtree.restoreState(state);
        orderedFile.restoreState(state);
    }

    @Override
    public String stats() {
        return "";
    }

    @Override
    public Rectangle2D getBoundingBox() {
        Rectangle2D vEBbb = vEBtree.getBoundingBox();
        Rectangle2D ofbb = orderedFile.getBoundingBox();

        if (vEBbb != null) {
            return vEBbb.createUnion(ofbb);
        }

        return ofbb;
    }
}
