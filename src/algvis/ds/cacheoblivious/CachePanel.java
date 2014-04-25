package algvis.ds.cacheoblivious;

import algvis.core.DataStructure;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.internationalization.ChLabel;
import algvis.internationalization.IButton;
import algvis.internationalization.ILabel;
import algvis.ui.VisPanel;
import javafx.collections.ListChangeListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CachePanel extends JPanel implements ActionListener, ChangeListener {

    protected final VisPanel panel;
    protected final StaticTree D;

    protected ChLabel stats;
    protected JSpinner spinBlockSize;
    protected JSpinner spinBlockCount;
    protected IButton clear;

    public CachePanel(VisPanel panel) {
        this.panel = panel;
        D = (StaticTree) panel.D;
        assert D != null : "data structure not initialized yet";

        initLayout();

        D.cache = new Cache(this, (Integer) spinBlockSize.getValue(), (Integer) spinBlockCount.getValue(), false);
        refresh();
    }

    private void initLayout() {
        JPanel settings = new JPanel();
        setLayout(new FlowLayout());

        settings.add(new ILabel("label-cache-blocksize"));
        spinBlockSize = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        spinBlockSize.addChangeListener(this);
        settings.add(spinBlockSize);

        settings.add(new ILabel("label-cache-blockcount"));
        spinBlockCount = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
        spinBlockCount.addChangeListener(this);
        settings.add(spinBlockCount);

        clear = new IButton("button-cache-clear");
        clear.addActionListener(this);
        settings.add(clear);

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout());

        stats = new ChLabel("");
        statsPanel.add(stats);


        setBorder(BorderFactory.createTitledBorder("Cache"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(settings);
        add(statsPanel);
    }

    public void refresh() {
        if (D.cache != null) {
            stats.setText(D.cache.stats());
        } else {
            stats.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            D.cache.clear();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spinBlockSize) {
            D.cache.setBlockSize((Integer) spinBlockSize.getValue());
        } else if (e.getSource() == spinBlockCount) {
            D.cache.setBlockCount((Integer) spinBlockCount.getValue());
        }
    }
}
