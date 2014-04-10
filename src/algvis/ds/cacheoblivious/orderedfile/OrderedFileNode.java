package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.cacheoblivious.statictree.StaticTreeNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.util.*;
import java.util.List;

public class OrderedFileNode extends BSTNode {

    private int leafSize;
    //private boolean[] leafOccupied;
    private int[] leafElements;

    public OrderedFileNode (DataStructure D, int leafSize) {
        super(D, Node.NOKEY, ZDepth.NODE);

        this.leafSize = leafSize;
        //leafOccupied = new boolean[leafSize];
        leafElements = new int[leafSize];

        // Draw upside down to link with vEB tree
        this.separationY *= ((OrderedFile) D).vEBtree != null ? -1 : 1;
    }

    static final double leafElementRadius = Node.RADIUS;

    int offset;
    public void setLeafOffset(int i) {
        offset = i;
    }

    public void setElement(int i, int val) {
        //leafOccupied[i] = true;
        leafElements[i] = val;
    }

    public int getElement(int i) {
        return leafElements[i];
    }

    public boolean densityWithinThresholds() {
        return getDensity() >= ((OrderedFile)D).thresholdSparse(height) &&
               getDensity() <= ((OrderedFile)D).thresholdDense(height);// &&
               //getDensity() < 1; // TODO hack to make sure we can always insert into leaf, avoid?
    }

    // Number of extra empty slots in this subtree
    // One empty is required in each leaf, rest is considered extra
    public int extraEmptySlots() {
        if (isLeaf()) {
            int empty = 0;
            for (int i = 0; i < leafSize; i++) {
                // TODO proper occupied status, allow inserting zero
                if (leafElements[i] == 0) empty++;
            }

            return empty - 1; // Need one in this leaf
        }

        return ((OrderedFileNode) getLeft()).extraEmptySlots() +
               ((OrderedFileNode) getRight()).extraEmptySlots();
    }

    @Override
    public Color getBgColor() {
        if (densityWithinThresholds()) {
            return Color.green;
        } else {
            return Color.red;
        }
    }

    @Override
    protected void drawBg(View v) {
        if (!isLeaf()) {
            super.drawBg(v);
            return;
        }

        double cellX = x - (leafSize - 1)*leafElementRadius;

        // Edges linking leafs in OF and vEB
        if (((OrderedFile) D).vEBtree != null) {
            // Connect to vEB leaves
            v.setColor(Color.black);
            for (int i = 0; i < leafSize; i++) {
                BSTNode leaf = ((OrderedFile) D).vEBtree.getLeafByOrder(offset*leafSize + i);
                v.drawLine(cellX + 2*i*leafElementRadius, y, leaf.x, leaf.y);

                // TODO move to init/somewhere else
                ((StaticTreeNode) leaf).orderedFileOffset = offset;
                ((StaticTreeNode) leaf).orderedFilePos = i;
            }
        }

        // Leaf outer box
        v.setColor(getBgColor());
        v.fillRect(x, y, leafElementRadius * leafSize, leafElementRadius);
        v.setColor(getFgColor());
        v.drawRect(x, y, leafElementRadius * leafSize, leafElementRadius);

        // Inner dividers
        for (int i = 0; i < leafSize; i++) {
            v.drawSqr(cellX + 2*i*leafElementRadius, y, leafElementRadius);
        }

    }

    @Override
    protected void drawKey(View v) {
        if (!isLeaf()) {
            v.drawString("" + getDensity(), x, y, Fonts.NORMAL);
        } else {
            double cellX = x - (leafSize - 1)*leafElementRadius;
            for (int i = 0; i < leafSize; i++) {
                // Draw elements inside
                v.drawString("" + leafElements[i], cellX + 2 * i * leafElementRadius, y, Fonts.NORMAL);

                // Draw index underneath
                // TODO should really draw indices in between or have one more at start/end, to allow inserting anywhere
                v.drawString("" + (i + offset*leafSize), cellX + 2 * i * leafElementRadius, y + leafElementRadius*2, Fonts.TYPEWRITER);
            }
        }
    }

    @Override
    protected void rebox() {
        if (!isLeaf())     {
            super.rebox();
            return;
        }

        // TODO cleaner
        leftw = rightw = (int)(leafSize*leafElementRadius) + DataStructure.minsepx/4;
    }

    public double getDensity() {
        if (!isLeaf()) {
            double total = 0;
            total += ((OrderedFileNode) getLeft()).getDensity();
            total += ((OrderedFileNode) getRight()).getDensity();
            return total/2;
        }

        int full = 0;
        for (int i = 0; i < leafSize; i++) {
            // TODO proper occupied status, allow inserting zero
            if (leafElements[i] != 0) full++;
        }

        return (double)full/leafSize;
    }

    public void drawThresholds(View v, BSTNode xBoundsNode) {
        double lx = x - xBoundsNode.leftw;
        double rx = x + xBoundsNode.rightw;

        OrderedFileNode node = this;
        OrderedFile DS = (OrderedFile) D;
        while (node != null) {
            v.drawStringLeft("Sparse = " + DS.thresholdSparse(node.height), lx, node.y, Fonts.TYPEWRITER);
            v.drawStringRight("Dense  = " + DS.thresholdDense(node.height), rx, node.y, Fonts.TYPEWRITER);
            node = (OrderedFileNode) node.getLeft();
        }
    }

    public void insertAtPos(int pos, int value) {
        // TODO for now we prepend new value when conflicting, use in between indices for any position
        int oldpos = 0;
        int newpos = 0;

        int[] elements = leafElements.clone();

        while (newpos < leafSize) {
            if (oldpos >= pos && pos >= 0) {
                leafElements[newpos] = value;
                pos = -1;
            } else {
                while (oldpos < leafSize && elements[oldpos] == 0) oldpos++;

                if (oldpos >= leafSize) leafElements[newpos] = 0;
                else leafElements[newpos] = elements[oldpos];

                oldpos++;
            }

            newpos++;
        }

    }

    public void getElements(ArrayList<Integer> elements, boolean empty) {
        if (isLeaf()) {
            for(int i = 0; i < leafSize; i++) {
                if (empty || leafElements[i] != 0) {
                    elements.add(leafElements[i]);
                }
            }
        } else {
            // In order, left before right
            ((OrderedFileNode) getLeft()).getElements(elements, empty);
            ((OrderedFileNode) getRight()).getElements(elements, empty);
        }
    }

    public void getLeaves(ArrayList<OrderedFileNode> leaves) {
        if (isLeaf()) {
            leaves.add(this);
        } else {
            ((OrderedFileNode) getLeft()).getLeaves(leaves);
            ((OrderedFileNode) getRight()).getLeaves(leaves);
        }
    }

    public void insertEvenly(List<Integer> elements) {
        if (isLeaf()) {
            // TODO for now just insert at beginning
            // TODO assert it will fit

            // Clear
            for(int i = 0; i < leafSize; i++) {
                leafElements[i] = 0;
            }

            // Insert in order from beginning
            int i = 0;
            for(Integer el : elements) {
                leafElements[i++] = el;
            }
        } else {
            // Split in half, insert evenly into subtrees
            int half = elements.size() / 2;
            ((OrderedFileNode) getLeft()).insertEvenly(elements.subList(0, half));
            ((OrderedFileNode) getRight()).insertEvenly(elements.subList(half, elements.size()));
        }
    }

    @Override
    protected void repos() {
        super.repos();

        if (isLeaf()) {
            // Align OF leaves under vEB leaves
            if (((OrderedFile)D).vEBtree != null) {
                // Center each group
                BSTNode left = ((OrderedFile) D).vEBtree.getLeafByOrder(offset*leafSize);
                BSTNode right = ((OrderedFile) D).vEBtree.getLeafByOrder((offset + 1)*leafSize -1);
                if (left == null || right == null) return;

                goTo((left.tox + right.tox) / 2, left.toy - separationY);
            }
        } else {
            goTo(tox, getLeft().toy - separationY);
        }
    }
}