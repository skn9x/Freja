package freja.di.config.empty;

import freja.di.Configuration;
import freja.di.Container;

/**
 * �����s��Ȃ� Configuration �I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public class EmptyConfiguration implements Configuration {
	/**
	 * EmptyConfiguration �̃C���X�^���X�ł��B
	 */
	public static final EmptyConfiguration INSTANCE = new EmptyConfiguration();
	
	protected EmptyConfiguration() {
		;
	}
	
	/**
	 * �����s���܂���B
	 */
	@Override
	public void config(Container container) {
		;
	}
}
