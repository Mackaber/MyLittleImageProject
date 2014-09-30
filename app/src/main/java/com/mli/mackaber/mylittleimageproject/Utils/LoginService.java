package com.mli.mackaber.mylittleimageproject.utils;

import retrofit.RetrofitError;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by mackaber on 30/09/14.
 */
public interface LoginService {
    @Headers({
            "Content-type: application/json"
    })
    @POST("/api/v1/sessions")
    LoginDetails login(@Body LoginCredentials loginCredentials) throws RetrofitError;

    public class User {
        private String email;
        private String password;

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

    public class LoginCredentials {
        private User user;

        public LoginCredentials(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
        public void setUser(User user) {
            this.user = user;
        }

    }

    public class LoginDetails {
        private boolean success;
        private String info;
        private String auth_token;

        public boolean isSuccess() {
            return success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getInfo() {
            return info;
        }
        public void setInfo(String info) {
            this.info = info;
        }

        public String getAuth_token() {
            return auth_token;
        }
        public void setAuth_token(String auth_token) {
            this.auth_token = auth_token;
        }

    }
}