package freja.di.config.multiple;

import freja.di.Configuration;
import freja.di.Container;

/**
 * ������ {@link Configuration} ��K�p���邱�Ƃ̂ł��� Configuration �I�u�W�F�N�g�ł��B
 * @author SiroKuro
 */
public class MultipleConfiguration implements Configuration {
	private final Configuration[] configs;
	
	/**
	 * �I�u�W�F�N�g�����������܂��B�����Ɏw�肳�ꂽ Configuration �����Ɏg�p����܂��B
	 * @param configs �K�p���� Configuration �̔z��
	 */
	public MultipleConfiguration(Configuration... configs) {
		assert configs != null;
		this.configs = configs.clone();
	}
	
	@Override
	public void config(Container container) {
		for (Configuration config: configs) {
			if (config != null) {
				config.config(container);
			}
		}
	}
}
