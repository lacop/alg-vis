package algvis.ds.cacheoblivious.btree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.DictButtons;
import algvis.ui.VisPanel;

public class BTreePanel extends VisPanel {
    public static Class<? extends DataStructure> DS = BTree.class;

    public BTreePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new BTree(this);
        scene.add(D);
        buttons = new DictButtons(this);
    }
}
