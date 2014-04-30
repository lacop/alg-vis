package algvis.ui.view;

import algvis.ui.VisPanel;

import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.erichseifert.vectorgraphics2d.SVGGraphics2D;

public class ExportView extends View {
    SVGGraphics2D g;

    public ExportView(VisPanel panel) {
        super(panel);

        Rectangle2D bb = panel.scene.getBoundingBox();

        g = new SVGGraphics2D(bb.getMinX(), bb.getMinY(), bb.getWidth(), bb.getHeight());
        setGraphics(g, (int) bb.getWidth(), (int) bb.getHeight());
    }

    public void save() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
            String filename = dateFormat.format(new Date());
            FileOutputStream file = new FileOutputStream(filename + ".svg");
            file.write(g.getBytes());
            file.close();
        } catch (IOException e) {

        }
    }
}
