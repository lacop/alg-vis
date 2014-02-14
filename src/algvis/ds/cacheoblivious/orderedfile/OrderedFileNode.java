package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class OrderedFileNode extends BSTNode {

    private int leafSize;
    //private boolean[] leafOccupied;
    private int[] leafElements;

    public OrderedFileNode (DataStructure D, int leafSize) {
        super(D, Node.NOKEY, ZDepth.NODE);

        this.leafSize = leafSize;
        //leafOccupied = new boolean[leafSize];
        leafElements = new int[leafSize];
    }

    static final double leafElementRadius = Node.RADIUS;

    public void setElement(int i, int val) {
        //leafOccupied[i] = true;
        leafElements[i] = val;
    }

    /*@Override
    public int getKey() {
        return 42;
    } */


    @Override
    public Color getBgColor() {
        if (isLeaf()) {
            return super.getBgColor();
        }

        if (getDensity() < ((OrderedFile)D).thresholdSparse(height) ||
            getDensity() > ((OrderedFile)D).thresholdDense(height)) {
            return Color.red;
        } else {
            return Color.green;
        }
    }

    @Override
    protected void drawBg(View v) {
        if (!isLeaf()) {
            super.drawBg(v);
            return;
        }

        // Leaf outer box
        v.setColor(getBgColor());
        v.fillRect(x, y, leafElementRadius * leafSize, leafElementRadius);
        v.setColor(getFgColor());
        v.drawRect(x, y, leafElementRadius * leafSize, leafElementRadius);

        // Inner dividers
        double cellX = x - (leafSize - 1)*leafElementRadius;
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
                v.drawString("" + leafElements[i], cellX + 2 * i * leafElementRadius, y, Fonts.NORMAL);
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
        leftw = rightw = (int)(leafSize*leafElementRadius) + DataStructure.minsepx/2;
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

}
