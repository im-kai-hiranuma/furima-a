package in.tech_camp.furima_a.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PortfolioBasicAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 環境変数から設定値を取得
        String expectedUser = System.getenv("BASIC_AUTH_USER");
        String expectedPass = System.getenv("BASIC_AUTH_PASSWORD");

        // 環境変数が設定されていない場合（ローカル等）はBasic認証をスキップして通過させる
        if (expectedUser == null || expectedUser.isEmpty() || expectedPass == null || expectedPass.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // ブラウザからのBasic認証ヘッダーを取得
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Basic ")) {
            String base64Credentials = header.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            // ID・パスワードが合致すれば合格！次の処理へ
            if (values.length == 2 && expectedUser.equals(values[0]) && expectedPass.equals(values[1])) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 認証失敗時：HTTP 401とBasic認証用ヘッダーを返してダイアログを出す（リダイレクトはさせない）
        response.setHeader("WWW-Authenticate", "Basic realm=\"Portfolio Access\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Unauthorized");
    }
}