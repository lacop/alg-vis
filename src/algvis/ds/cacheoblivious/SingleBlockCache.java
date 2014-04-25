package algvis.ds.cacheoblivious;

public class SingleBlockCache implements Cache {
    private int blockSize;
    private int blockStart;

    private boolean aligned;

    private int readCount = 0;
    private int accessCount = 0;

    public SingleBlockCache(int blockSize, boolean aligned) {
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

        if (isLoaded(position))
            return;

        readCount++;

        if (aligned) {
            // Memory-aligned cache
            blockStart = (position / blockSize) * blockSize;
        } else {
            blockStart = position;
        }

    }

}
