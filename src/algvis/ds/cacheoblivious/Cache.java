package algvis.ds.cacheoblivious;

import algvis.internationalization.Languages;

import java.util.LinkedList;

public class Cache {

    private int blockSize;
    private int blockCount;
    private boolean aligned;

    private LinkedList<Integer> blocks;

    private CachePanel panel;

    private int readCount = 0;
    private int accessCount = 0;

    protected Cache(CachePanel panel, int blockSize, int blockCount, boolean aligned) {
        this.panel = panel;

        this.blockSize = blockSize;
        this.blockCount = blockCount;
        this.aligned = aligned;

        clear();
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;

        while (blocks.size() > blockCount) {
            blocks.removeFirst();
        }
    }

    public void clear() {
        blocks = new LinkedList<Integer>();

        readCount = 0;
        accessCount = 0;

        panel.refresh();
    }

    public String stats() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(Languages.getString("cache-access-count") + ": " + accessCount + "<br>");
        sb.append(Languages.getString("cache-read-count") + ": " + readCount + "<br>");
        sb.append("</html>");
        return sb.toString();
    }

    public boolean isLoaded(int position) {
        for (Integer blockStart : blocks) {
            if ((blockStart <= position) && (position < blockStart + blockSize)) {
                return true;
            }
        }

        return false;
    }

    public void access(int position) {
        accessCount++;

        if (!isLoaded(position)) {
            readCount++;

            load(position);
        }

        panel.refresh();
    }

    public void load(int position) {
        int blockStart;
        if (aligned) {
            // Memory-aligned cache
            blockStart = (position / blockSize) * blockSize;
        } else {
            blockStart = position;
        }

        if (blocks.size() == blockCount) {
            blocks.removeFirst();
        }

        blocks.addLast(blockStart);
    }
}
