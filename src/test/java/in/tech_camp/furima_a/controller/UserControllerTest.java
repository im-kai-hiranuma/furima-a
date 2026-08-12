package in.tech_camp.furima_a.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import in.tech_camp.furima_a.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController の画面表示・レスポンスのテスト")
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        // テスト前に手動でMockMvcを起動します
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Nested
    @DisplayName("新規登録画面の表示テスト")
    class SignUpPageTests {

        @Test
        @DisplayName("GET /users/sign_up - 200 OK で新規登録画面が表示され、userFormがモデルにセットされること")
        void showSignUp_success() throws Exception {
            mockMvc.perform(get("/users/sign_up"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("users/sign_up"))
                    .andExpect(model().attributeExists("userForm"));
        }
    }

    @Nested
    @DisplayName("ログイン画面の表示テスト")
    class SignInPageTests {

        @Test
        @DisplayName("GET /users/sign_in - 200 OK でログイン画面が表示されること")
        void showSignIn_success() throws Exception {
            mockMvc.perform(get("/users/sign_in"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("users/sign_in"));
        }
    }
}