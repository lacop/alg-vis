package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ZDepth;
import algvis.ds.cacheoblivious.statictree.StaticTreeNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.internationalization.Languages;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
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

    int markedLeaf = -1;
    public void mark(int leafPos) {
        markedLeaf = leafPos;
    }
    @Override
    public void unmark() {
        super.unmark();
        markedLeaf = -1;
    }

    public boolean densityWithinThresholds() {
        return getDensity() >= ((OrderedFile)D).thresholdSparse(height) &&
               getDensity() <= ((OrderedFile)D).thresholdDense(height);
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
    protected void drawEdge(View v) {
        if (!isLeaf()) {
            super.drawEdge(v);
            return;
        }

        // Draw leaf edges from corners to avoid overlapping offset numbers
        v.setColor(Color.black);
        double cornerX, cornerY;
        if (getParent().getLeft() == this) {
            cornerX = x + leafSize*leafElementRadius; // Right corner for left child
        } else {
            cornerX = x - leafSize*leafElementRadius; // Left corner for right child
        }
        if (((OrderedFile) D).vEBtree != null) {
            cornerY = y + leafElementRadius; // Bottom corner for upside down
        } else {
            cornerY = y - leafElementRadius; // Top corner for normal
        }

        v.drawLine(cornerX, cornerY, getParent().x, getParent().y);

        // Edges linking leafs in OF and vEB
        double cellX = x - (leafSize - 1)*leafElementRadius;
        if (((OrderedFile) D).vEBtree != null) {
            // Connect to vEB leaves
            for (int i = 0; i < leafSize; i++) {
                BSTNode leaf = ((OrderedFile) D).vEBtree.getLeafByOrder(offset*leafSize + i);
                v.drawLine(cellX + 2*i*leafElementRadius, y, leaf.x, leaf.y);

                // TODO move to init/somewhere else
                ((StaticTreeNode) leaf).orderedFileOffset = offset;
                ((StaticTreeNode) leaf).orderedFilePos = i;
            }
        }
    }

    @Override
    protected void drawBg(View v) {
        if (!isLeaf()) {
            super.drawBg(v);
            return;
        }

        double cellX = x - (leafSize - 1)*leafElementRadius;

        // Leaf outer box
        if (marked) {
            v.setColor(Color.yellow);
        } else {
            v.setColor(getBgColor());
        }
        v.fillRect(x, y, leafElementRadius * leafSize, leafElementRadius);

        // Highlight marked element
        if (markedLeaf >= 0) {
            v.setColor(Color.yellow);
            v.fillRect(cellX + 2*markedLeaf*leafElementRadius, y, leafElementRadius, leafElementRadius);
        }

        // Outer box
        v.setColor(getFgColor());
        v.drawRect(x, y, leafElementRadius * leafSize, leafElementRadius);
        // Inner dividers
        for (int i = 0; i < leafSize; i++) {
            v.drawSqr(cellX + 2*i*leafElementRadius, y, leafElementRadius);
        }
    }

    @Override
    protected void drawKey(View v) {
        v.setColor(getFgColor());
        if (!isLeaf()) {
            v.drawString(formatDensity(getDensity()), x, y, Fonts.NORMAL);
        } else {
            double cellX = x - (leafSize - 1)*leafElementRadius;
            for (int i = 0; i < leafSize; i++) {
                // Draw elements inside, ignore empty
                if (leafElements[i] != 0) {
                    v.drawString("" + leafElements[i], cellX + 2 * i * leafElementRadius, y, Fonts.NORMAL);
                }

                // Draw index underneath
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

    private String formatDensity(double density) {
        return Math.round(density*100) + "%";
    }

    public void drawThresholds(View v, BSTNode xBoundsNode) {
        v.setColor(getFgColor());

        double rx = x + xBoundsNode.rightw;

        final String label = Languages.getString("of-allowed-density");
        v.drawStringRight(label, rx, y-separationY, Fonts.TYPEWRITER);

        // Center under label
        double cx = rx + Fonts.TYPEWRITER.fm.stringWidth(label)/2;

        OrderedFileNode node = this;
        OrderedFile DS = (OrderedFile) D;
        while (node != null) {
            String text = formatDensity(DS.thresholdSparse(node.height));
            text += "-";
            text += formatDensity(DS.thresholdDense(node.height));

            v.drawString(text, cx, node.y, Fonts.TYPEWRITER);
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

        BSTNode left = null, right = null;
        if (isLeaf()) {
            // Align OF leaves under vEB leaves
            if (((OrderedFile)D).vEBtree != null) {
                // Center each group
                left = ((OrderedFile) D).vEBtree.getLeafByOrder(offset*leafSize);
                right = ((OrderedFile) D).vEBtree.getLeafByOrder((offset + 1)*leafSize -1);
            }
        } else {
            // Align between child nodes
            left = getLeft();
            right = getRight();
        }

        if (left == null || right == null) return;
        // Center between left and right
        goTo((left.tox + right.tox) / 2, left.toy - separationY);
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);

        HashtableStoreSupport.store(state, hash + "leafSize", leafSize);
        HashtableStoreSupport.store(state, hash + "leafElements", leafElements.clone());
        HashtableStoreSupport.store(state, hash + "offset", offset);
        HashtableStoreSupport.store(state, hash + "markedLeaf", markedLeaf);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);

        final Object leafSize = state.get(hash + "leafSize");
        if (leafSize != null) {
            this.leafSize = (Integer) leafSize;
        }
        final Object leafElements = state.get(hash + "leafElements");
        if (leafElements != null) {
            this.leafElements = (int[]) leafElements;
        }
        final Object offset = state.get(hash + "offset");
        if (offset != null) {
            this.offset = (Integer) offset;
        }
        final Object markedLeaf = state.get(hash + "markedLeaf");
        if (markedLeaf != null) {
            this.markedLeaf = (Integer) markedLeaf;
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        if (!isLeaf()) {
            return super.getBoundingBox();
        }

        double cellX = x - (leafSize - 1)*leafElementRadius;
        return new Rectangle2D.Double(cellX-leafElementRadius, y-leafElementRadius, 2*leafElementRadius*leafSize, 2*leafElementRadius);
    }
}