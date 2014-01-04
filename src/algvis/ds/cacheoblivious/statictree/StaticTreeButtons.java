package algvis.ds.cacheoblivious.statictree;

import algvis.core.Dictionary;
import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class StaticTreeButtons extends Buttons {

    private IButton findB;

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
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if (evt.getSource() == findB) {
            if (panel.history.canRedo())
                panel.newAlgorithmPool();
            Vector<Integer> args = I.getVI();
            for (int x : args) {
                ((Dictionary) D).find(x);
            }
        }
    }
}
