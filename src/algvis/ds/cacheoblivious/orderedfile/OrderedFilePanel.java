package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.ds.cacheoblivious.statictree.StaticTreeButtons;
import algvis.ui.VisPanel;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderedFilePanel extends VisPanel {
    public static Class<? extends DataStructure> DS = OrderedFile.class;

    public OrderedFilePanel(Settings S) {
        super(S);
    }

    @Override
    protected void initDS() {
        D = new OrderedFile(this);

        ((OrderedFile) D).initialize(new ArrayList(Arrays.asList(new Integer[]{1, 3, 5, 7})));
        //((OrderedFile) D).initialize(new ArrayList(Arrays.asList(new Integer[]{1, 3, 5, 7, 9, 11, 12, 15})));

        scene.add(D);
        buttons = new OrderedFileButtons(this);
    }
}
