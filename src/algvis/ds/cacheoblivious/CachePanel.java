package algvis.ds.cacheoblivious;

import algvis.core.DataStructure;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.internationalization.ChLabel;
import algvis.internationalization.IButton;
import algvis.ui.VisPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CachePanel extends JPanel implements ActionListener {

    protected final VisPanel panel;
    protected final StaticTree D;

    protected ChLabel stats;

    public CachePanel(VisPanel panel) {
        this.panel = panel;
        D = (StaticTree) panel.D;
        assert D != null : "data structure not initialized yet";

        setLayout(new FlowLayout());
        setBorder(BorderFactory.createTitledBorder("Cache"));

        stats = new ChLabel("");
        add(stats);

        D.cache = new SingleBlockCache(this, 4, false);
        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void refresh() {
        stats.setText(D.cache.stats());
    }
}
