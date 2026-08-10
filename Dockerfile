# --- 1. ビルド用のステージ ---
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app-src

# プロジェクトの全ファイルをコピー
COPY . .

# Gradleを使ってアプリケーションをビルド
RUN chmod +x gradlew && ./gradlew clean build -x test -x check --no-daemon

# --- 2. 実行用のステージ（本番環境） ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 本番用のJARだけを狙い撃ちしてコピー
COPY --from=builder /app-src/build/libs/*-SNAPSHOT.jar app.jar

# 画像が保存されるフォルダを作成
RUN mkdir -p src/main/resources/static/images/uploads

EXPOSE 8080

# Renderで動かすために、環境変数PORTを受け取れる元の設定に戻します
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=prod --server.port=${PORT:-8080}"]