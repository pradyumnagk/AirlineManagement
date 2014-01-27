package airlineSystem;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<A, B> extends LinkedHashMap<A, B>
{
	private static final long serialVersionUID = 1L;
	private final int maxEntries;
	public int hits;
	public int miss;

	public Cache(final int maxEntries)
	{
		super(maxEntries + 1, 1.0f, true);
		this.maxEntries = maxEntries;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<A, B> eldest)
	{
		return super.size() > maxEntries;
	}

	public int getHits()
	{
		return hits;
	}

	public void setHits(int hits)
	{
		this.hits = hits;
	}

	public int getMiss()
	{
		return miss;
	}

	public void setMiss(int miss)
	{
		this.miss = miss;
	}

}
