package algvis.ds.cacheoblivious.statictree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.VisPanel;

public class StaticTreePanel extends VisPanel {

    public static Class<? extends DataStructure> DS = StaticTree.class;

    public StaticTreePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new StaticTree(this);
        scene.add(D);
        buttons = new StaticTreeButtons(this);
    }

    @Override
    public void start() {
        super.start();

        // TODO visualize tree creation?
        this.pauses = false;
        fullInsert(31, 0);

        // Pause after insert is complete
        D.start(new Runnable() {
            @Override
            public void run() {
                pauses = true;
            }
        });
    }

    private void fullInsert(int q, int offset) {
        D.insert((q+1)/2 + offset);
        if (q > 1) {
            fullInsert(q/2, offset);
            fullInsert(q/2, offset + (q+1)/2);
        }
    }
}
