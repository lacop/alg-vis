package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.cacheoblivious.statictree.StaticTreeButtons;
import algvis.ui.VisPanel;

public class OrderedFilePanel extends VisPanel {
    public static Class<? extends DataStructure> DS = OrderedFile.class;

    public OrderedFilePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new OrderedFile(this);
        scene.add(D);
        buttons = new OrderedFileButtons(this);
    }
}
