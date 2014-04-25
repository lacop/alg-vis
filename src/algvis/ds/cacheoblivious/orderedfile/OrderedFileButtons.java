package algvis.ds.cacheoblivious.orderedfile;

import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class OrderedFileButtons extends Buttons {
    public OrderedFileButtons(VisPanel panel) {
        super(panel);
    }

    private IButton insertB;

    @Override
    protected void actionButtons(JPanel P) {
        insertB = new IButton("button-insert");
        insertB.setMnemonic(KeyEvent.VK_I);
        insertB.addActionListener(this);

        P.add(insertB);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        super.actionPerformed(evt);
        if (evt.getSource() == insertB) {
            Vector<Integer> args = I.getVI();
            // TODO range check
            if (args.size() != 2) {
                // TODO i18n?
                panel.statusBar.setText("enter position and value as two integers separated by space");
            }

            ((OrderedFile) D).insert(args.get(0), args.get(1));
        }
    }
}
