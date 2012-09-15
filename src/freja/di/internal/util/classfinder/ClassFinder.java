package freja.di.internal.util.classfinder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author SiroKuro
 */
public class ClassFinder {
	private Set<Archive> archives = new HashSet<Archive>();
	
	public void addClassPath(String cl, boolean includeJarFile) {
		File file = new File(cl);
		if (file.exists()) {
			FilenameFilter filter = new JarArchiveFileFilter();
			if (file.isDirectory()) {
				archives.add(new DirectoryArchive(file));
				if (includeJarFile) {
					for (File ff: file.listFiles(filter)) {
						archives.add(new JarArchive(ff));
					}
				}
			}
			else if (filter.accept(file.getParentFile(), file.getName())) {
				archives.add(new JarArchive(file));
			}
		}
	}
	
	public void addDefaultClassPath() {
		// addClassPath(System.getProperty("sun.boot.class.path"), true);
		addClassPathString(System.getProperty("java.ext.dirs"), true);
		addClassPathString(System.getProperty("java.class.path"), false);
	}
	
	public Set<String> getAllClassName() {
		Set<String> result = new TreeSet<String>();
		for (Archive arc: archives) {
			result.addAll(arc.getAllClassName());
		}
		return result;
	}
	
	private void addClassPathString(String paths, boolean includeJarFile) {
		if (paths != null) {
			String[] path;
			if (0 <= paths.indexOf(';')) {
				path = paths.split(";");
			}
			else if (0 <= paths.indexOf(':')) {
				path = paths.split(":");
			}
			else {
				path = new String[]{ paths };
			}
			for (String cl: path) {
				addClassPath(cl, includeJarFile);
			}
		}
	}
	
	private static class JarArchiveFileFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			name = name.toLowerCase();
			return name.endsWith(".jar") || name.endsWith(".zip");
		}
	}
}
