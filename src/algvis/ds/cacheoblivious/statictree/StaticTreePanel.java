package algvis.ds.cacheoblivious.statictree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ds.cacheoblivious.SingleBlockCache;
import algvis.ui.NewVisPanel;
import algvis.ui.VisPanel;

public class StaticTreePanel extends NewVisPanel {

    public static Class<? extends DataStructure> DS = StaticTree.class;

    public StaticTreePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new StaticTree(this, new SingleBlockCache(4, false));
        scene.add(D);
        buttons = new StaticTreeButtons(this);
    }

    @Override
    public void start() {
        super.start();

        // TODO visualize tree creation?
        this.pauses = false;
        ((StaticTree) D).fullInsert(31, 0);

        // Pause after insert is complete
        D.start(new Runnable() {
            @Override
            public void run() {
                pauses = true;
            }
        });
    }
}
