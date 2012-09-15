package freja.di.internal.hotswap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import freja.di.internal.util.CollectionMap;
import freja.internal.logging.Log;

/**
 * @author SiroKuro
 */
public class HotSwapAgent implements HotSwapService {
	private final Map<File, Long> lastModified = new HashMap<File, Long>();
	private final CollectionMap<File, FileModifiedEventListener> listeners = new CollectionMap<File, FileModifiedEventListener>();
	private final ClassFileChecker daemon = new ClassFileChecker();
	private Thread thread = null;
	
	public void addFileModifiedListener(File target, FileModifiedEventListener listener) {
		synchronized (this) {
			lastModified.put(target, target.lastModified());
			listeners.put(target, listener);
		}
	}
	
	public boolean getEnable() {
		return isRunning();
	}
	
	public void setEnable(boolean enable) {
		if (enable) {
			start();
		}
		else {
			requestStop();
		}
	}
	
	public void start() {
		synchronized (daemon) {
			if (thread != null) {
				return;
			}
			thread = new Thread(daemon);
		}
		thread.setDaemon(true);
		thread.setPriority(3);
		thread.setName(getClass().getName());
		thread.start();
	}
	
	private boolean isRunning() {
		return thread != null;
	}
	
	private void requestStop() {
		daemon.requestStop();
	}
	
	private class ClassFileChecker implements Runnable {
		private volatile boolean stop;
		
		public void requestStop() {
			stop = true;
		}
		
		@Override
		public void run() {
			Log.info("HotSwapAgent started...");
			try {
				stop = false;
				while (!stop) {
					try {
						Thread.sleep(4000);
						checkFiles();
					}
					catch (InterruptedException ex) {
						;
					}
				}
			}
			finally {
				Log.info("HotSwapAgent stopped...");
				synchronized (this) {
					stop = false;
					thread = null;
				}
			}
		}
		
		private void checkFiles() {
			synchronized (HotSwapAgent.this) {
				for (Map.Entry<File, Long> ents: lastModified.entrySet()) {
					File file = ents.getKey();
					long lm = file.lastModified();
					if (lm != (long) ents.getValue()) {
						lastModified.put(file, lm);
						
						Log.info("modified file {0} and notified HotSwap", file);
						for (FileModifiedEventListener listener: listeners.get(file)) {
							listener.onFileModified(HotSwapAgent.this, file);
						}
					}
				}
			}
		}
	}
}
