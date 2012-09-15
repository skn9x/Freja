package freja.di;

/**
 * コンテナを初期化するためのオブジェクトを表します。
 * @author SiroKuro
 */
public interface Configuration {
	
	/**
	 * コンテナにコンポーネントを設定します。
	 * @param container 初期化対象のコンテナ
	 */
	public void config(Container container);
}
