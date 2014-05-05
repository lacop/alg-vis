package algvis.ds.cacheoblivious.statictree;

import algvis.core.Dictionary;
import algvis.ds.cacheoblivious.CachePanel;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class StaticTreeButtons extends Buttons implements ChangeListener {

    private IButton findB;
    private IButton increaseB;
    private IButton decreaseB;

    private IRadioButton classic;
    private IRadioButton vEB;

    protected StaticTreeButtons(VisPanel panel) {
        super(panel);
    }

    @Override
    protected void actionButtons(JPanel P) {
        findB = new IButton("button-find");
        findB.setMnemonic(KeyEvent.VK_F);
        findB.addActionListener(this);

        P.add(findB);
    }

    @Override
    protected void otherButtons(JPanel P) {
        increaseB = new IButton("button-increase-size");
        increaseB.setMnemonic(KeyEvent.VK_I);
        increaseB.addActionListener(this);

        decreaseB = new IButton("button-decrease-size");
        decreaseB.setMnemonic(KeyEvent.VK_D);
        decreaseB.addActionListener(this);

        P.add(increaseB);
        P.add(decreaseB);
    }

    @Override
    protected JPanel initThirdRow() {
        JPanel third = new JPanel();
        third.setLayout(new FlowLayout());

        classic = new IRadioButton("radio-order-classic");
        vEB = new IRadioButton("radio-order-vEB");
        vEB.setSelected(true);

        ButtonGroup orderTypes = new ButtonGroup();
        orderTypes.add(classic);
        orderTypes.add(vEB);

        classic.addChangeListener(this);
        vEB.addChangeListener(this);

        third.add(classic);
        third.add(vEB);

        return third;
    }

    @Override
    public void setOtherEnabled(boolean enabled) {
        super.setOtherEnabled(enabled);

        findB.setEnabled(enabled);
        increaseB.setEnabled(enabled);
        decreaseB.setEnabled(enabled);
    }

    @Override
    protected void initRandom() {
        // Intentionally empty to avoid initializing random button
    }

    CachePanel cachePanel;
    @Override
    protected JPanel initSide() {
        cachePanel = new CachePanel(panel);
        return cachePanel;
    }

    @Override
    public void refresh() {
        super.refresh();
        cachePanel.refresh();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if (evt.getSource() == findB) {
            if (panel.history.canRedo())
                panel.newAlgorithmPool();
            Vector<Integer> args = I.getVI();
            for (int x : args) {
                ((Dictionary) D).find(x);
            }
        } else if (evt.getSource() == increaseB) {
            int height = ((StaticTree) D).getRoot().height;
            ((StaticTree) D).initialize(height + 1);
        } else if (evt.getSource() == decreaseB) {
            int height = ((StaticTree) D).getRoot().height;
            if (height > 1) {
                ((StaticTree) D).initialize(height - 1);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (vEB.isSelected()) {
            ((StaticTree) D).orderType = StaticTreeSetOrder.OrderType.vEBOrder;
        } else if (classic.isSelected()) {
            ((StaticTree) D).orderType = StaticTreeSetOrder.OrderType.classicOrder;
        }

        ((StaticTree) D).setOrder();
    }
}
