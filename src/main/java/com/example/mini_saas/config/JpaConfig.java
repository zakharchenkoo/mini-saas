package com.example.mini_saas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA-related configuration.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.example.mini_saas")
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaConfig {
    // JPA auditing enabled for @CreatedDate, @LastModifiedDate
}