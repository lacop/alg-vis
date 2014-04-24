package algvis.ds.cacheoblivious.cobtree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.DictButtons;
import algvis.ui.NewVisPanel;
import algvis.ui.VisPanel;

import java.util.ArrayList;
import java.util.Arrays;

public class COBTreePanel extends NewVisPanel {
    public static Class<? extends DataStructure> DS = COBTree.class;

    public COBTreePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new COBTree(this);
        scene.add(D);
        buttons = new DictButtons(this);
    }

    @Override
    public void start() {
        super.start();

        D.clear();
    }
}
