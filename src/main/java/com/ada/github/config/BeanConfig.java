package com.ada.github.config;


import com.ada.github.service.GithubRepositoryInterface;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CorsRegistration corsRegistration = registry.addMapping("/**")
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:3000");
                corsRegistration.allowCredentials(true);
            }
        };
    }


    @Bean
    public OkHttpClient.Builder okHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS);
    }

    @Bean
    public GithubRepositoryInterface githubRepositoryInterface(OkHttpClient.Builder okHttpClient , Interceptor baseRetrofitInterceptor) {

        okHttpClient.addInterceptor(baseRetrofitInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient.build())
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GithubRepositoryInterface.class);
    }

    @Bean
    public Interceptor baseRetrofitInterceptor() {
        
        return chain -> {
            Response response = chain.proceed(chain.request());
            
            if(!response.isSuccessful()){
                if(response.code() >= 500) {
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY , "error.%d".formatted(response.code()));
                } else if (response.code() == 401) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "error.%d".formatted(response.code()));
                } else if (response.code() == 404) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.%d".formatted(response.code()));
                } else {
                    throw new ResponseStatusException(HttpStatus.valueOf(response.code()), "error.%d".formatted(response.code()));
                }
            }

            return response;
        };
    }
}
