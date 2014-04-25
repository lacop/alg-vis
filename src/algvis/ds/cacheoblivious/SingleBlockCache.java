package algvis.ds.cacheoblivious;

public class SingleBlockCache extends Cache {
    private int blockSize;
    private int blockStart;

    private boolean aligned;

    private int readCount = 0;
    private int accessCount = 0;

    public SingleBlockCache(CachePanel panel, int blockSize, boolean aligned) {
        super(panel);
        this.blockSize = blockSize;
        blockStart = -blockSize; // start + size = 0 => everything is unloaded

        this.aligned = aligned;
    }

    @Override
    public String stats() {
        StringBuilder sb = new StringBuilder();
        sb.append("BS: " + blockSize + "  ");
        sb.append("AC: " + accessCount + "  ");
        sb.append("RC: " + readCount + " ; ");

        return sb.toString();
    }

    @Override
    public boolean isLoaded(int position) {
        return (blockStart <= position) && (position < blockStart + blockSize);
    }

    @Override
    public void access(int position) {
        accessCount++;

        if (!isLoaded(position)) {
            readCount++;

            if (aligned) {
                // Memory-aligned cache
                blockStart = (position / blockSize) * blockSize;
            } else {
                blockStart = position;
            }
        }

        refresh();
    }

}
