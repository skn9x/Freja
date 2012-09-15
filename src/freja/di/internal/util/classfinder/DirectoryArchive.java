package freja.di.internal.util.classfinder;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DirectoryArchive extends Archive {
	public final File dir;
	
	private static final String EXT = ".class";
	private static final int EXT_LENGTH = EXT.length();
	
	public DirectoryArchive(File dir) {
		if (dir == null || !dir.isDirectory()) {
			throw new IllegalArgumentException();
		}
		this.dir = dir;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DirectoryArchive other = (DirectoryArchive) obj;
		if (dir == null) {
			if (other.dir != null) {
				return false;
			}
		}
		else if (!dir.equals(other.dir)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Set<String> getAllClassName() {
		Set<String> result = new HashSet<String>();
		seekAllClassName(dir, null, result);
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dir == null) ? 0 : dir.hashCode());
		return result;
	}
	
	private void seekAllClassName(File dir, String pkg, Set<String> result) {
		if (dir.isDirectory()) {
			for (File file: dir.listFiles()) {
				if (file.isDirectory()) {
					String subPkg = file.getName() + ".";
					if (pkg != null) {
						subPkg = pkg + subPkg;
					}
					seekAllClassName(file, subPkg, result);
				}
				else {
					String name = file.getName();
					if (name.toLowerCase().endsWith(EXT)) {
						name = name.substring(0, name.length() - EXT_LENGTH);
						if (pkg != null) {
							name = pkg + name;
						}
						result.add(name);
					}
				}
			}
		}
	}
	
}
