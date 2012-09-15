package freja.di.internal.hotswap;

import java.io.File;

/**
 * @author SiroKuro
 */
public interface HotSwapService {
	
	public void addFileModifiedListener(File target, FileModifiedEventListener listener);
	
	public void start();
	
}
