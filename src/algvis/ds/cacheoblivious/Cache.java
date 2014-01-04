package algvis.ds.cacheoblivious;

public interface Cache {
    public int getBlockSize();
    public int getReadCount();
    public int getAccessCount();

    public boolean isLoaded(int position);

    public void access(int position);
}
