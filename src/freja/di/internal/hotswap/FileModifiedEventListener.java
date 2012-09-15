package freja.di.internal.hotswap;

import java.io.File;
import java.util.EventListener;

/**
 * @author SiroKuro
 */
public interface FileModifiedEventListener extends EventListener {
	public void onFileModified(Object sender, File file);
}
