package algvis.ds.cacheoblivious.statictree;

import algvis.core.Algorithm;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;

import java.util.LinkedList;
import java.util.Queue;

public class StaticTreeSetOrder extends Algorithm {

    public enum OrderType {
        classicOrder,
        vEBOrder
    }

    protected final StaticTree T;
    protected final OrderType type;

    public StaticTreeSetOrder(StaticTree T, OrderType type) {
        super(T.panel);
        this.T = T;
        this.type = type;
    }

    private int order = 1;
    @Override
    public void runAlgorithm() throws InterruptedException {
        // Make sure tree node heights are correct
        T.getRoot().calcTree();

        if (type == OrderType.vEBOrder) {
            setvEBOrder((StaticTreeNode) T.getRoot(), T.getRoot().height);
        } else if (type == OrderType.classicOrder) {
            setClassicOrder((StaticTreeNode) T.getRoot(), order);
        } else {
            // Future orders?
        }

        // Set maximum order (for array drawing)
        T.maxOrder = order - 1;
    }

    private void setClassicOrder(StaticTreeNode node, int i) {
        node.setOrder(i);
        // Keep track of maximum
        if (i+1 > order) {
            order = i+1;
        }
        if (!node.isLeaf()) {
            setClassicOrder((StaticTreeNode) node.getLeft(), 2*i);
            setClassicOrder((StaticTreeNode) node.getRight(), 2*i+1);
        }
    }

    // TODO pause + explain?
    private void setvEBOrder(StaticTreeNode root, int height) {
        // TODO assumes full balanced binary tree

        if (height <= 2) {
            // Set order for small subtree

            root.setOrder(order++);
            if (height == 2 && root.getLeft() != null) {
                ((StaticTreeNode) root.getLeft()).setOrder(order++);
            }
            if (height == 2 && root.getRight() != null) {
                ((StaticTreeNode) root.getRight()).setOrder(order++);
            }
        } else {
            // Split horizontally to two, round so bottom is power of two
            int bottom = Integer.highestOneBit(height - 1);
            int top = height - bottom;

            // First recurse on top half
            setvEBOrder(root, top);

            // Then on bottom halves, starting at depth top+1
            // BFS to find them
            Queue<StaticTreeNode> queue = new LinkedList<StaticTreeNode>();
            queue.add(root);

            while (!queue.isEmpty()) {
                StaticTreeNode node = queue.remove();
                if (root.height - top == node.height) {
                    setvEBOrder(node, bottom);
                } else {
                    if (node.getLeft() != null) {
                        queue.add((StaticTreeNode) node.getLeft());
                    }
                    if (node.getRight() != null) {
                        queue.add((StaticTreeNode) node.getRight());
                    }
                }
            }
        }
    }
}
