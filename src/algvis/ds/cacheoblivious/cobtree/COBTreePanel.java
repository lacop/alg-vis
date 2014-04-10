package algvis.ds.cacheoblivious.cobtree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.DictButtons;
import algvis.ui.VisPanel;

import java.util.ArrayList;
import java.util.Arrays;

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

        // TODO FIXME
        // Won't work if it's not startup (default) DS, or after switching back
    }

    @Override
    public void start() {
        super.start();

        this.pauses = false;
        ((COBTree) D).init(new ArrayList(Arrays.asList(new Integer[]{1, 3, 5, 7}))); // TODO cleanup
        D.start(new Runnable() {
            @Override
            public void run() {
                pauses = true;
                // Reposition after insert to line up
                ((COBTree) D).reposition();
            }
        });
    }
}
