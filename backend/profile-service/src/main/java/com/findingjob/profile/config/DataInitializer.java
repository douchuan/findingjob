package com.findingjob.profile.config;

import com.findingjob.profile.entity.SkillTag;
import com.findingjob.profile.repository.SkillTagRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    /**
     * Seed initial skill tags on first run.
     * These are common programming languages, frameworks, and tools.
     */
    @Bean
    CommandLineRunner seedSkillTags(SkillTagRepository skillTagRepository) {
        return args -> {
            if (skillTagRepository.count() > 0) {
                return; // Already seeded
            }

            List<SkillTag> tags = List.of(
                    // Languages
                    tag("Java", "language"), tag("Python", "language"), tag("JavaScript", "language"),
                    tag("TypeScript", "language"), tag("Go", "language"), tag("Rust", "language"),
                    tag("Kotlin", "language"), tag("Swift", "language"), tag("C++", "language"),
                    tag("Ruby", "language"), tag("PHP", "language"), tag("Scala", "language"),

                    // Frontend
                    tag("React", "framework"), tag("Vue.js", "framework"), tag("Angular", "framework"),
                    tag("Next.js", "framework"), tag("Svelte", "framework"),
                    tag("Ant Design", "framework"), tag("Tailwind CSS", "framework"),

                    // Backend
                    tag("Spring Boot", "framework"), tag("Django", "framework"), tag("Flask", "framework"),
                    tag("Express", "framework"), tag("FastAPI", "framework"), tag("Gin", "framework"),
                    tag("NestJS", "framework"),

                    // Database
                    tag("MySQL", "database"), tag("PostgreSQL", "database"), tag("MongoDB", "database"),
                    tag("Redis", "database"), tag("Elasticsearch", "database"),

                    // DevOps / Tools
                    tag("Docker", "tool"), tag("Kubernetes", "tool"), tag("Linux", "tool"),
                    tag("Git", "tool"), tag("CI/CD", "tool"), tag("Jenkins", "tool"),
                    tag("Terraform", "tool"), tag("AWS", "tool"), tag("阿里云", "tool"),

                    // Architecture
                    tag("Microservices", "architecture"), tag("REST API", "architecture"),
                    tag("GraphQL", "architecture"), tag("gRPC", "architecture"),

                    // Testing
                    tag("JUnit", "testing"), tag("Selenium", "testing"), tag("Jest", "testing"),
                    tag("Cypress", "testing"),

                    // Other
                    tag("Machine Learning", "other"), tag("大数据", "other"),
                    tag("区块链", "other"), tag("移动端开发", "other")
            );

            skillTagRepository.saveAll(tags);
        };
    }

    private SkillTag tag(String name, String category) {
        SkillTag tag = new SkillTag();
        tag.setName(name);
        tag.setCategory(category);
        tag.setUsageCount(0);
        return tag;
    }
}
