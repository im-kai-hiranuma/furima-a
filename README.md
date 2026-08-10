## データベース設計 (ER図)

```mermaid
erDiagram
    users ||--o{ products : "出品する"
    users ||--o{ purchases : "購入する"
    products ||--o| purchases : "購入される"
    purchases ||--|| addresses : "配送先"

    users {
        bigint id PK
        string nickname "ニックネーム"
        string email UK "メールアドレス"
        string password "パスワード"
        string last_name "お名前(姓)"
        string first_name "お名前(名)"
        string last_name_kana "お名前カナ(姓)"
        string first_name_kana "お名前カナ(名)"
        date birthday "生年月日"
    }

    products {
        bigint id PK
        bigint user_id FK "出品者ID"
        string name "商品名"
        text description "商品の説明"
        integer category "カテゴリー"
        integer condition "商品の状態"
        integer delivery_fee "配送料の負担"
        integer prefecture "発送元の地域"
        integer until_delivery "発送までの日数"
        bigint price "価格"
        string img "画像URL/パス"
    }

    purchases {
        bigint id PK
        bigint user_id FK "購入者ID"
        bigint product_id FK "購入商品ID (UNIQUE)"
    }

    addresses {
        bigint id PK
        bigint purchase_id FK "購入情報ID"
        string post_number "郵便番号"
        integer prefecture "都道府県"
        string city "市区町村"
        string block "番地"
        string building "建物名"
        string phone "電話番号"
    }