# --- 1. ビルド用のステージ ---
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app-src

# プロジェクトの全ファイルをコピー
COPY . .

# Gradle Wrapperを使ってビルド
RUN chmod +x gradlew && ./gradlew clean build -x test -x check --no-daemon

# --- 2. 実行用のステージ（本番環境） ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# plain.jarとの重複エラーを防止
COPY --from=builder /app-src/build/libs/furima-a-0.0.1-SNAPSHOT.jar app.jar

# 画像保存用フォルダの作成
RUN mkdir -p src/main/resources/static/images/uploads

EXPOSE 8080

# Renderの512MBメモリ枠で落ちないよう -XX:MaxRAMPercentage=75.0 を追加
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75.0 -jar app.jar --spring.profiles.active=prod --server.port=${PORT:-8080}"]