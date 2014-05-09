package algvis.ui.view;

import algvis.ds.dictionaries.bst.BST;
import algvis.ui.VisPanel;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D;

public class ExportView extends View {
    PDFGraphics2D g;

    public ExportView(VisPanel panel) {
        super(panel);

        Rectangle2D bb = panel.scene.getBoundingBox();

        g = new PDFGraphics2D(bb.getMinX(), bb.getMinY(), bb.getMaxX(), bb.getMaxY());

        setGraphics(g, (int) bb.getWidth(), (int) bb.getHeight());

        g.translate(0, -bb.getMinY());
        g.setFontRendering(VectorGraphics2D.FontRendering.VECTORS);
    }

    public void save() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
            String filename = dateFormat.format(new Date());
            FileOutputStream file = new FileOutputStream(filename + ".pdf");
            file.write(g.getBytes());
            file.close();
        } catch (IOException e) {

        }
    }

    // TODO: text bubbles don't render properly in PDF, for now just hide them
    @Override
    public void drawTextBubble(String s, int x, int y, int w, int alpha, REL pos) {
        //super.drawTextBubble(s, x, y, w, alpha, pos);
    }
    @Override
    public void drawTextBubble(String s, int x, int y, int w, int alpha, REL pos, int gapx, int gapy) {
        //super.drawTextBubble(s, x, y, w, alpha, pos, gapx, gapy);
    }
}
