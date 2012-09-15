package freja.di.internal.util.classfinder;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import freja.internal.logging.Log;

public class JarArchive extends Archive {
	private final File zipfile;
	
	private static final String EXT = ".class";
	private static final int EXT_LENGTH = EXT.length();
	
	public JarArchive(File zipfile) {
		this.zipfile = zipfile;
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
		final JarArchive other = (JarArchive) obj;
		if (zipfile == null) {
			if (other.zipfile != null) {
				return false;
			}
		}
		else if (!zipfile.equals(other.zipfile)) {
			return false;
		}
		return true;
	}
	
	@Override
	public Set<String> getAllClassName() {
		Set<String> result = new HashSet<String>();
		try {
			ZipFile zf = new ZipFile(zipfile);
			try {
				Enumeration<? extends ZipEntry> ents = zf.entries();
				while (ents.hasMoreElements()) {
					ZipEntry zip = ents.nextElement();
					String name = zip.getName();
					if (name.endsWith(EXT)) {
						result.add(toClassName(name));
					}
				}
			}
			finally {
				zf.close();
			}
		}
		catch (IOException ex) {
			result.clear();
			Log.debug(ex, "io error. ignore loading archive {0}.", zipfile);
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((zipfile == null) ? 0 : zipfile.hashCode());
		return result;
	}
	
	private static String toClassName(String name) {
		int index = name.lastIndexOf('/');
		if (0 <= index) {
			String packagename = name.substring(0, index).replace('/', '.');
			name = name.substring(index + 1, name.length() - EXT_LENGTH);
			return packagename + "." + name;
		}
		else {
			index = name.lastIndexOf('\\');
			if (0 <= index) {
				String packagename = name.substring(0, index).replace('\\', '.');
				name = name.substring(index + 1, name.length() - EXT_LENGTH);
				return packagename + "." + name;
			}
			else {
				return name;
			}
		}
	}
}
