package freja.di.config.empty;

import freja.di.Configuration;
import freja.di.Container;

/**
 * 何も行わない Configuration オブジェクトです。
 * @author SiroKuro
 */
public class EmptyConfiguration implements Configuration {
	/**
	 * EmptyConfiguration のインスタンスです。
	 */
	public static final EmptyConfiguration INSTANCE = new EmptyConfiguration();
	
	protected EmptyConfiguration() {
		;
	}
	
	/**
	 * 何も行いません。
	 */
	@Override
	public void config(Container container) {
		;
	}
}
