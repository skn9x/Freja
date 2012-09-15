package freja.di;

/**
 * Context スコープのコンポーネントが用いるコンテキスト情報を表します。
 * @author SiroKuro
 */
public interface Context {
	/**
	 * このコンテキストから、指定のコンポーネント名を持つオブジェクトを取得します。
	 * コンポーネントが見つからない場合など、必要に応じてコンポーネントの生成も行います。
	 * @param componentName コンポーネント名
	 * @param factory コンポーネントの生成に用いる ComponentFactory オブジェクト
	 * @return コンポーネントオブジェクト
	 */
	public Object getComponent(String componentName, ComponentFactory factory);
	
	/**
	 * このコンテキストの名前を返します。
	 * @return コンテキスト名
	 */
	public String getName();
}
