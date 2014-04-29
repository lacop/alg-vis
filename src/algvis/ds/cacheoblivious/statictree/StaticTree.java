package algvis.ds.cacheoblivious.statictree;

import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.cacheoblivious.Cache;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class StaticTree extends BST {

    public static String adtName = "cacheoblivious";
    public static String dsName = "statictree";

    public Cache cache = null;

    public int arrayY = -5 * Node.RADIUS;
    public int maxOrder = 1;

    public StaticTree(VisPanel panel) {
        super(panel);
    }

    @Override
    public void insert(int x) {
        start(new StaticTreeInsert(this, new StaticTreeNode(this, x, ZDepth.ACTIONNODE)));
    }

    public void setOrder() {
        start(new StaticTreeSetOrder(this));
    }

    public void fullInsert(int q, int offset) {
        insert((q + 1) / 2 + offset);
        if (q > 1) {
            fullInsert(q/2, offset);
            fullInsert(q/2, offset + (q+1)/2);
        }
    }

    @Override
    public void clear() {
        initialize(1);
    }

    void initialize(int height) {
        super.clear();

        panel.pauses = false;

        int nodes = (1 << height) - 1;
        fullInsert(nodes, 0);

        // Pause after insert is complete
        start(new Runnable() {
            @Override
            public void run() {
                panel.pauses = true;
            }
        });
    }

    public BSTNode getLeafByOrder(int i) {
        if (getRoot() == null) {
            return null;
        }

        // Make sure node heights are correct
        getRoot().calcTree();

        // Traverse from top
        BSTNode node = getRoot();
        while (!node.isLeaf()) {
            int leftCount = (int) Math.pow(2, node.height - 2);
            if (i < leftCount) {
                node = node.getLeft();
            } else {
                i -= leftCount;
                node = node.getRight();
            }
        }

        return node;
    }

    // TODO similar code in OrderedFile, extract somewhere
    public void initWithLeaves(List<Integer> leaves) {
        ArrayList<BSTNode> nodes = new ArrayList<BSTNode>();
        for (int i = 0; i < leaves.size(); i++) {
            BSTNode leaf = new StaticTreeNode(this, leaves.get(i), ZDepth.NODE);
            nodes.add(leaf);
        }

        // Merge into tree
        while (nodes.size() > 1) {
            ArrayList<BSTNode> merged = new ArrayList<BSTNode>();
            for (int i = 0; i < nodes.size(); i += 2) {
                BSTNode parent = new StaticTreeNode(this, Math.max(nodes.get(i).getKey(), nodes.get(i+1).getKey()), ZDepth.NODE);
                parent.linkLeft(nodes.get(i));
                parent.linkRight(nodes.get(i + 1));
                merged.add(parent);
            }
            nodes = merged;
        }

        root = nodes.get(0);

        // Set memory order
        setOrder();
    }

    @Override
    public String stats() {
        return "";
    }

    @Override
    public void reposition() {
        super.reposition();

        panel.screen.V.setBounds(x1, arrayY, x2, y2);
    }
}
