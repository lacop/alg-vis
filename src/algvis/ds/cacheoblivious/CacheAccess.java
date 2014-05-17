package algvis.ds.cacheoblivious;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.cacheoblivious.statictree.StaticTreeNode;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.REL;

public class CacheAccess extends Algorithm {
    StaticTreeNode node = null;
    public CacheAccess(VisPanel panel, Algorithm a, BSTNode node) {
        super(panel, a);
        if (node instanceof StaticTreeNode) {
            this.node = (StaticTreeNode) node;
        }
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        if (node == null) {
            return;
        }

        Cache cache = ((StaticTree) node.D).cache;
        if (cache == null) {
            return;
        }

        CachePanel cpanel = cache.panel;
        int order = node.getOrder();

        if (cache.isLoaded(order)) {
            node.setColor(NodeColor.GREEN); // cache hit
            if (cpanel.pause()) {
                addStep(node, REL.TOP, "cache-hit");
                pause();
            }
        } else {
            node.setColor(NodeColor.RED); // cache miss
            if (cpanel.pause()) {
                addStep(node, REL.TOP, "cache-miss");
                pause();
            }
        }

        cache.access(order);

    }
}
