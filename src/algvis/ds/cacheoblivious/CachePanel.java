package algvis.ds.cacheoblivious;

import algvis.core.DataStructure;
import algvis.ds.cacheoblivious.statictree.StaticTree;
import algvis.internationalization.ChLabel;
import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;
import algvis.internationalization.ILabel;
import algvis.ui.VisPanel;
import javafx.collections.ListChangeListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class CachePanel extends JPanel implements ActionListener, ChangeListener {

    protected final VisPanel panel;
    protected final StaticTree D;

    protected ChLabel stats;
    protected JSpinner spinBlockSize;
    protected JSpinner spinBlockCount;
    protected IButton clear;
    protected ICheckBox pause;
    protected ICheckBox aligned;

    public CachePanel(VisPanel panel) {
        this.panel = panel;
        D = (StaticTree) panel.D;
        assert D != null : "data structure not initialized yet";

        initLayout();

        D.cache = new Cache(this, (Integer) spinBlockSize.getValue(), (Integer) spinBlockCount.getValue(), true);
        refresh();
    }

    private void initLayout() {
        JPanel first = new JPanel();
        first.setLayout(new FlowLayout());

        first.add(new ILabel("label-cache-blocksize"));
        spinBlockSize = new JSpinner(new SpinnerNumberModel(4, 1, 8, 1));
        spinBlockSize.addChangeListener(this);
        first.add(spinBlockSize);

        first.add(new ILabel("label-cache-blockcount"));
        spinBlockCount = new JSpinner(new SpinnerNumberModel(2, 1, 8, 1));
        spinBlockCount.addChangeListener(this);
        first.add(spinBlockCount);

        clear = new IButton("button-cache-clear");
        clear.addActionListener(this);
        first.add(clear);


        JPanel second = new JPanel();
        second.setLayout(new FlowLayout());

        pause = new ICheckBox("button-pause", false);
        pause.addActionListener(this);
        second.add(pause);

        aligned = new ICheckBox("label-cache-aligned", true);
        aligned.addActionListener(this);
        second.add(aligned);


        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout());

        stats = new ChLabel("");
        statsPanel.add(stats);


        setBorder(BorderFactory.createTitledBorder("Cache"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(first);
        add(second);
        add(statsPanel);
    }

    public void refresh() {
        if (D.cache != null) {
            stats.setText(D.cache.stats());
        } else {
            stats.setText("");
        }
    }

    public boolean pause() {
        return pause.isSelected();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clear) {
            D.cache.clear();
        } else if (e.getSource() == aligned) {
            D.cache.setAligned(aligned.isSelected());
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
