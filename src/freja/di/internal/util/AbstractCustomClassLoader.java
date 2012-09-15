package freja.di.internal.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author SiroKuro
 */
public abstract class AbstractCustomClassLoader extends ClassLoader {
	
	private static final String PACKAGE_PREFIX_JAVA = "java.";
	
	private static final String PACKAGE_PREFIX_FREJA_DI = "freja.di.";
	
	public AbstractCustomClassLoader() {
		super();
	}
	
	public AbstractCustomClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	protected Class<?> defineClass(String name) throws ClassNotFoundException {
		InputStream in = getResourceAsStream(toResourcePath(name));
		if (in != null) {
			try {
				byte[] data = toByteArray(in);
				return defineClass(name, data, 0, data.length);
			}
			catch (IllegalAccessError err) {
				throw new ClassNotFoundException(name, err);
			}
			catch (ClassFormatError err) {
				throw new ClassNotFoundException(name, err);
			}
			catch (IOException ex) {
				throw new ClassNotFoundException(name, ex);
			}
		}
		else {
			throw new ClassNotFoundException(name);
		}
	}
	
	protected Package definePackageIfAbsent(String name) {
		Package pk = getPackage(name);
		if (pk == null) {
			try {
				return definePackage(name, null, null, null, null, null, null, null);
			}
			catch (IllegalArgumentException ex) {
				// exception ignore
				return getPackage(name);
			}
		}
		else {
			return pk;
		}
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			ClassLoader parent = getParent();
			if (parent != null) {
				return parent.loadClass(name);
			}
			else {
				return super.findClass(name);
			}
		}
		catch (IllegalAccessError err) {
			throw new ClassNotFoundException(name, err);
		}
		catch (ClassFormatError err) {
			throw new ClassNotFoundException(name, err);
		}
	}
	
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> result = findLoadedClass(name);
		if (result == null) {
			result = findClass(name);
		}
		if (resolve) {
			resolveClass(result);
		}
		return result;
	}
	
	protected static String getPackageName(String className) {
		int index = className.lastIndexOf('.');
		if (0 < index) {
			return className.substring(0, index);
		}
		else {
			return "";
		}
	}
	
	protected static boolean isSystemClass(String name) {
		return name.startsWith(PACKAGE_PREFIX_JAVA) || name.startsWith(PACKAGE_PREFIX_FREJA_DI);
	}
	
	protected static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		byte[] buffer = new byte[8192];
		int size;
		while (0 < (size = input.read(buffer))) {
			data.write(buffer, 0, size);
		}
		return data.toByteArray();
	}
	
	protected static String toResourcePath(String name) {
		return name.replace('.', '/') + ".class";
	}
}
