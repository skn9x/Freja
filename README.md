Freja
===========================================
Freja はシンプルなDIコンテナです。

使い方
------

#### コンテナからコンポーネントを取得

    public class FrejaTest {
        @Test
        public void test() {
            assertEquals("tama", Freja.get(Cat.class).getName());   // 取得
        }
        
        private interface Cat {
            @Override
            public String toString();
        }
        
        @Component
        private static class Tama implements Cat {
            @Override
            public String toString() {
                return "tama";
            }
        }
    }


#### アノテーションによるバインド

    public class FrejaTest {
        @Bind
        private Cat cat1;   // バインド先
        
        @Before
        public void setUp() throws Exception {
            Freja.bind(this);
        }
        
        @Test
        public void test() {
            assertEquals("tama", cat1.getName());
        }
        
        private interface Cat {
            public String getName();
        }
        
        @Component
        private static class Tama implements Cat {
            @Override
            public String getName() {
                return "tama";
            }
        }
    }

#### 優先順位を考慮してバインド

    public class FrejaTest {
        @Bind
        private Cat cat1;
        
        @Before
        public void setUp() throws Exception {
            Freja.bind(this);
        }
        
        @Test
        public void test() {
            assertEquals("koma", cat1.getName());
        }
        
        private interface Cat {
            public String getName();
        }
        
        @Component
        private static class Tama implements Cat {
            @Override
            public String getName() {
                return "tama";
            }
        }
        
        @Component(priority = 100)  // Tama よりも Koma が優先度高い
        private static class Koma implements Cat {
            @Override
            public String getName() {
                return "koma";
            }
        }
    }


ライセンス
----------
Copyright &copy; 2009-2012 SiroKuro  
Distributed under the [MIT License][mit].  
詳細は `FREJA-LICENSE.TXT` を参照してください。  

[MIT]: http://www.opensource.org/licenses/mit-license.php
