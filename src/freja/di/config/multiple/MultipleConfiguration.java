package freja.di.config.multiple;

import freja.di.Configuration;
import freja.di.Container;

/**
 * 複数の {@link Configuration} を適用することのできる Configuration オブジェクトです。
 * @author SiroKuro
 */
public class MultipleConfiguration implements Configuration {
	private final Configuration[] configs;
	
	/**
	 * オブジェクトを初期化します。引数に指定された Configuration が順に使用されます。
	 * @param configs 適用する Configuration の配列
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
