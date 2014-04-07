package algvis.ds.cacheoblivious.cobtree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.DictButtons;
import algvis.ui.VisPanel;

public class COBTreePanel extends VisPanel {
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
}