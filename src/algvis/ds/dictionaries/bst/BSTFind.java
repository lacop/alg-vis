/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.ds.dictionaries.bst;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.VisPanel;
import algvis.ui.view.REL;

import java.util.HashMap;

public class BSTFind extends Algorithm {
    private final BST T;
    protected final int K;
    private final HashMap<String, Object> result = new HashMap<String, Object>(); // node

    // Last compared node and found flag
    private boolean found = false;
    private BSTNode lastNode = null;

    // Display last "found"/"not found" message + animation?
    private boolean lastStep = true;

    public BSTFind(BST T, int x) {
        this(T, x, null);
    }

    public BSTFind(BST T, int x, Algorithm a) {
        this(T.panel, T, x, a);
    }

    public BSTFind(VisPanel panel, BST T, int x, Algorithm a) {
        super(panel, a);
        this.T = T;
        K = x;
    }

    protected boolean found(BSTNode w) {
        return w.getKey() == K;
    }

    protected boolean goRight(BSTNode w) {
        return w.getKey() < K;
    }

    protected void stepLeft(BSTNode v, BSTNode w) {
        addStep(v, REL.RIGHT, "bstfindleft", K, w.getKey());
    }

    protected void stepRight(BSTNode v, BSTNode w) {
        addStep(v, REL.LEFT, "bstfindright", K, w.getKey());
    }

    public boolean getFound() {
        return found;
    }

    public BSTNode getLastNode() {
        return lastNode;
    }

    public void setLastStep(boolean lastStep) {
        this.lastStep = lastStep;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader("find", K);
        result.put("node", null);
        final BSTNode v = new BSTNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(v, REL.BOTTOM, "empty");
            pause();
            addStep(v, REL.BOTTOM, "notfound");
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
        } else {
            BSTNode w = T.getRoot();
            v.goAbove(w);
            addStep(w, REL.BOTTOM, "bstfindstart");
            pause();
            while (true) {
                lastNode = w;
                w.access();
                if (w.isLeaf() || found(w)) {
                    if (found(w)) {
                        found = true;
                        if (lastStep) {
                            v.goTo(w);
                            addStep(w, REL.BOTTOM, "found");
                            v.setColor(NodeColor.FOUND);
                        }
                        result.put("node", w);
                        break;
                    } else {
                        if (lastStep) {
                            addStep(v, REL.BOTTOM, "notfound");
                            v.goDown();
                        }
                        v.setColor(NodeColor.NOTFOUND);
                        break;
                    }
                } else if (goRight(w)) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    stepRight(v, w);
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getLeft() != null) {
                        w.getLeft().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getRight() != null) {
                        w = w.getRight();
                        v.goAbove(w);
                    } else { // not found
                        if (lastStep) {
                            addStep(w, REL.BOTTOMLEFT, "notfound");
                            v.goRight();
                        }
                        v.setColor(NodeColor.NOTFOUND);
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    stepLeft(v, w);
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getRight() != null) {
                        w.getRight().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                        v.goAbove(w);
                    } else { // notfound
                        if (lastStep) {
                            addStep(w, REL.BOTTOMLEFT, "notfound");
                            v.goLeft();
                        }
                        v.setColor(NodeColor.NOTFOUND);
                        break;
                    }
                }
                pause();
            }
        }
        if (result.get("node") == null) {
            removeFromScene(v);
        }
        if (lastStep) {
            pause();
        }
        if (T.getRoot() != null) {
            T.getRoot().subtreeColor(NodeColor.NORMAL);
        }
        if (result.get("node") != null) {
            removeFromScene(v);
        }
    }

    @Override
    public HashMap<String, Object> getResult() {
        return result;
    }
}
