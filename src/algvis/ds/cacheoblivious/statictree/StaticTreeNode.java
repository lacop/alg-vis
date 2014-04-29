package algvis.ds.cacheoblivious.statictree;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.ds.cacheoblivious.Cache;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class StaticTreeNode extends BSTNode {
    protected StaticTreeNode(DataStructure D, int key, int x, int y) {
        super(D, key, x, y);
    }

    public StaticTreeNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
    }

    public StaticTreeNode(DataStructure d, int key, int x, int y, int zDepth) {
        super(d, key, x, y, zDepth);
    }

    @Override
    protected void drawTree2(View v) {
        super.drawTree2(v);
        v.drawStringTop(""+order, x, y, Fonts.NORMAL);
    }

    private int order = -1;
    public void setOrder(int i) {
        this.order = i;
    }

    // TODO private + get/set
    public int orderedFileOffset;
    public int orderedFilePos;

    @Override
    public void access() {
        Cache c = ((StaticTree) D).cache;

        if (c != null) {
            c.access(order);
            setColor(NodeColor.RED);
        }
    }

    @Override
    public Color getBgColor() {
        Cache c = ((StaticTree) D).cache;

        if (c != null) {
            if (((StaticTree) D).cache.isLoaded(order)) {
                return super.getBgColor().brighter();
            } else {
                return super.getBgColor().darker();
            }
        } else {
            return super.getBgColor();
        }
    }

    @Override
    protected void rebox() {
        super.rebox();

        // Pack leaves closer together
        if (isLeaf()) {
            leftw = rightw = DataStructure.minsepx / 3;
        }
    }

    private int getArrayX() {
        int halfOrder = (((StaticTree) D).maxOrder - 1)/2;
        int pos = order - halfOrder;
        return pos*2*Node.RADIUS;
    }

    @Override
    protected void drawBg(View v) {
        super.drawBg(v);

        int arrayY = ((StaticTree) D).arrayY;
        int arrayX = getArrayX();
        v.setColor(getBgColor());
        v.fillRect(arrayX, arrayY, Node.RADIUS, Node.RADIUS);
        v.setColor(getFgColor());
        v.drawRect(arrayX, arrayY, Node.RADIUS, Node.RADIUS);
    }

    @Override
    protected void drawKey(View v) {
        super.drawKey(v);

        int arrayY = ((StaticTree) D).arrayY;
        int arrayX = getArrayX();
        v.setColor(getFgColor());
        v.drawString(toString(), arrayX, arrayY, Fonts.NORMAL);
    }
}
