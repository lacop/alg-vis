package algvis.ds.cacheoblivious.orderedfile;

import algvis.core.MyRandom;
import algvis.internationalization.IButton;
import algvis.internationalization.IString;
import algvis.internationalization.Languages;
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
            OrderedFile of = (OrderedFile) D;
            int length = of.leafSize * of.leaves.size();

            if (I.getText().isEmpty()) {
                // Insert single random value
                of.insert(MyRandom.Int(0, length), MyRandom.Int(1, of.maxRandValue));
                return;
            }

            Vector<Integer> args = I.getVI();
            if (args.size() != 2) {
                panel.statusBar.setText(Languages.getString("of-status-format"));
                return;
            }

            if (args.get(0) < 0 || args.get(0) >= length) {
                panel.statusBar.setText(Languages.getString("of-status-outofrange"));
                return;
            }

            of.insert(args.get(0), args.get(1));
        }
    }
}
