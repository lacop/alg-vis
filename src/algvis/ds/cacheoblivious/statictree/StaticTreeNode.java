package algvis.ds.cacheoblivious.statictree;

import algvis.core.DataStructure;
import algvis.core.NodeColor;
import algvis.ds.cacheoblivious.orderedfile.OrderedFileNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;

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
        ((StaticTree) D).cache.access(order);

        setColor(NodeColor.RED);
    }

    @Override
    public Color getBgColor() {
        if (((StaticTree) D).cache.isLoaded(order)) {
            return super.getBgColor().brighter();
        } else {
            return super.getBgColor().darker();
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
}
