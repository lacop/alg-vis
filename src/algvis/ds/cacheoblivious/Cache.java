package algvis.ds.cacheoblivious;

public abstract class Cache {

    private CachePanel panel;
    protected Cache(CachePanel panel) {
        this.panel = panel;
    }

    public abstract String stats();

    public abstract boolean isLoaded(int position);

    public abstract void access(int position);

    public void refresh() {
        panel.refresh();
    }
}
