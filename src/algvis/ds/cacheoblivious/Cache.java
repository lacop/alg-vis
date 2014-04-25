package algvis.ds.cacheoblivious;

public interface Cache {
    public String stats();

    public boolean isLoaded(int position);

    public void access(int position);
}
