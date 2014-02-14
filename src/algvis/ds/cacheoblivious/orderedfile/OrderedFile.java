package algvis.ds.cacheoblivious.orderedfile;

import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

import java.util.ArrayList;

public class OrderedFile extends BST {

    public static String adtName = "cacheoblivious";
    public static String dsName = "orderedfile";

    OrderedFileNode root = null;

    @Override
    public void draw(View V) {
        if (root != null) {
            root.drawTree(V);
        }
    }

    // TODO virtualize in BST or higher
    @Override
    public String getName() {
        return dsName;
    }

    @Override
    public BSTNode getRoot() {
        return root;
    }

    public OrderedFile(VisPanel panel) {
        super(panel);

        // TODO autosize etc
        initialize(2, new int[] {1, 0, 2, 0, 3, 0, 4, 0});
        reposition();
    }

    int leafSize;
    ArrayList<OrderedFileNode> leaves;

    private void initialize(int leafSize, int[] elements) {
        // Generate leaf nodes
        ArrayList<OrderedFileNode> nodes = new ArrayList<OrderedFileNode>();
        for (int i = 0; i < elements.length; i += leafSize) {
            OrderedFileNode leaf = new OrderedFileNode(this, leafSize);
            for (int j = 0; j < leafSize; j++) {
                leaf.setElement(j, elements[i+j]);
            }
            nodes.add(leaf);
        }

        this.leafSize = leafSize;
        leaves = nodes;

        // TODO assumes power-of-two leafs for complete binary tree

        // Merge into tree
        while (nodes.size() > 1) {
            ArrayList<OrderedFileNode> merged = new ArrayList<OrderedFileNode>();
            for (int i = 0; i < nodes.size(); i += 2) {
                OrderedFileNode parent = new OrderedFileNode(this, leafSize);
                parent.linkLeft(nodes.get(i));
                parent.linkRight(nodes.get(i+1));
                merged.add(parent);
            }
            nodes = merged;
        }

        root = nodes.get(0);
    }

    public double thresholdSparse(int height) {
        int depth = root.height - height;

        // 1/2 at root, 1/4 at leaves
        return 0.5 - 0.25*((double)depth / root.height);
    }
    public double thresholdDense(int height) {
        int depth = root.height - height;

        // 3/4 at root, 1 at leaves
        return 0.75 + 0.25*((double)depth / root.height);
    }
}
