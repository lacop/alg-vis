package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;

public class OrderedFileNode extends BSTNode {

    private int leafSize;
    private boolean[] leafOccupied;
    private int[] leafElements;

    public OrderedFileNode (DataStructure D, int leafSize) {
        super(D, Node.NOKEY, ZDepth.NODE);

        this.leafSize = leafSize;
        leafOccupied = new boolean[leafSize];
        leafElements = new int[leafSize];
    }

    static final double leafElementRadius = Node.RADIUS;

    public void setElement(int i, int val) {
        leafOccupied[i] = true;
        leafElements[i] = val;
    }

    /*@Override
    public int getKey() {
        return 42;
    } */


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
            super.drawKey(v);
            return;
        }

        double cellX = x - (leafSize - 1)*leafElementRadius;
        for (int i = 0; i < leafSize; i++) {
            v.drawString("" + leafElements[i], cellX + 2 * i * leafElementRadius, y, Fonts.NORMAL);
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

    /*
    @Override
    public Rectangle2D getBoundingBox() {
        if (!isLeaf()) {
            return super.getBoundingBox();
        }

        double horRadius = leafElementRadius * leafSize;
        return new Rectangle2D.Double(x - horRadius, y - leafElementRadius, 2 * horRadius, 2 * leafElementRadius);
    }                 */

}
