package com.aimbitious.job.service;

import com.aimbitious.job.client.GeminiClient;
import com.aimbitious.job.payload.AiTextResponse;
import com.aimbitious.job.payload.JobDescriptionRequest;
import com.aimbitious.job.payload.SalaryRangeRequest;
import com.aimbitious.job.payload.SalaryRangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobAiService {

    private final GeminiClient geminiClient;

    String system_prompt = """
            You are a senior HR professional and technical recruiter with deep knowledge of the Indian job market (2025–2026).
            
            You specialize in writing job descriptions, compensation benchmarking, and talent acquisition.
            
            Always write in a professional, engaging, inclusive, and bias-free tone.
            
            When asked for JSON, respond ONLY with valid JSON — no explanation, no markdown fences.
            """;

    // ==================== Phase 1: Job Description Generator ====================

    public AiTextResponse generateJobDescription(JobDescriptionRequest req) throws Exception {

        String skills = req.getSkills() != null ? String.join("," , req.getSkills()) : "Not Specified";

        String prompt = """
                Write a comprehensive, engaging, and inclusive job description.
                
                Job Details:
                - Title: %s
                - Required Skills: %s
                - Experience Level: %s
                - Job Type: %s
                - Work Mode: %s
                - Category: %s
                - Additional Context: %s
                
                Format the response in clean Markdown with EXACTLY these sections:
                
                ## About the Role
                [2–3 compelling sentences describing the role and its impact]
                
                ## Key Responsibilities
                - [6 specific, action-oriented bullet points]
                
                ## Requirements
                - [5–6 must-have qualifications and skills]
                
                ## Nice to Have
                - [3–4 preferred skills or qualifications]
                
                ## What We Offer
                - [4–5 concise benefits or growth opportunities]
                
                ## Equal Opportunity Statement
                [2–3 inclusive sentences encouraging applicants from diverse backgrounds]
                
                Do NOT include placeholder company names.
                
                Rules:
                - Use professional, ATS-friendly language.
                - Keep the total length between 250–400 words.
                - Tailor responsibilities and requirements to the provided skills and experience level.
                - Avoid company-specific placeholders unless mentioned in the context.
                - Return only the job description in Markdown.
                """.formatted(
                        req.getTitle(),
                skills,
                req.getExperienceLevel() != null ? req.getExperienceLevel() : "not specified",
                req.getJobType() != null ? req.getJobType() : "not specified",
                req.getWorkMode() != null ? req.getWorkMode() : "not specified",
                req.getCategory() != null ? req.getCategory() : "not specified",
                req.getAdditionalContext() != null ? req.getAdditionalContext() : "none"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(system_prompt, prompt))
                .build();
    }

    // ==================== Phase 2: Job Requirement Auto-fill ====================

    public AiTextResponse generateJobRequirements(String title, String category) throws Exception {
        String prompt = """
                Generate professional job requirements and responsibilities for this role.
                
                Job Title: %s
                Category: %s
                
                Format in markdown:
                
                ## Responsibilities
                - [5 specific bullet points]
                
                ## Requirements
                - [5 specific bullet points]
                
                Keep it concise and ATS-friendly.
                """.formatted(
                        title,
                        category
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(system_prompt, prompt))
                .build();
    }


    // ==================== Phase 1: Salary Range Suggestion ====================

    public SalaryRangeResponse suggestSalaryRange(SalaryRangeRequest req) throws Exception {
        String skills = req.getSkills() != null ? String.join("," , req.getSkills()) : "Not Specified";

        String prompt = """
                Provide a realistic and competitive salary range for this role.
                
                Role Details:
                - Job Title: %s
                - Required Skills: %s
                - Experience Level: %s
                - Job Type: %s
                - Location: %s
                
                {
                  "minSalary": 600000,
                  "maxSalary": 1200000,
                  "currency": "INR",
                  "period": "YEARLY",
                  "marketInsight": "Brief 1-2 sentence insight about this role's compensation trend in the current Indian job market."
                }
                
                Rules:
                - Return ONLY valid JSON.
                - minSalary and maxSalary must be numbers (not strings).
                - Currency must always be "INR".
                - Period must always be "YEARLY".
                - Use realistic current Indian market rates based on the job title, skills, experience, job type, and location.
                - marketInsight must be concise (maximum 2 sentences).
                """.formatted(
                        req.getTitle(),
                        skills,
                req.getExperienceLevel() != null ? req.getExperienceLevel() : "FRESHER",
                req.getJobType() != null ? req.getJobType() : "FULL_TIME",
                req.getLocation() != null ? req.getLocation(): "India"

        );

        return geminiClient.generateJson(system_prompt, prompt, SalaryRangeResponse.class);
    }

    // ==================== Responsibilities Auto-fill ====================

    public AiTextResponse generateJobResponsibilities(String title, String category) throws Exception {

        String prompt = """
                Generate 6 specific, action-oriented job responsibilities for this role.
                
                Job Title: %s
                Category: %s
                
                Return ONLY a plain bullet list (no headings, no markdown headers):
                
                - [responsibility 1]
                - [responsibility 2]
                ...
                
                Keep each bullet concise (under 15 words), start with a strong action verb.
                """.formatted(title, category);

        return AiTextResponse.builder()
                .content(geminiClient.generateText(system_prompt, prompt))
                .build();
    }

    // ==================== Benefits Auto-fill ====================

    public AiTextResponse generateJobBenefits(String title, String category, String jobType) throws Exception {

        String prompt = """
                Generate 6 competitive, attractive job benefits for this role.
                
                Job Title: %s
                Category: %s
                Job Type: %s
                
                Return ONLY a plain bullet list (no headings, no markdown headers):
                
                - [benefit 1]
                - [benefit 2]
                ...
                
                Include a mix of: compensation perks, health/wellness, growth, flexibility, and culture benefits.
                
                Keep each bullet concise and specific.
                """.formatted(title,
                category != null ? category: "General",
                jobType != null ? jobType : "Full Time"
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(system_prompt, prompt))
                .build();
    }


    // ==================== Phase 3: Skills Recommendation for Job ====================

    public AiTextResponse recommendSkillsForJob(String jobTitle, String description) throws Exception {
        String prompt = """
                Recommend the most relevant skills for this job posting.
                
                Job Title: %s
                Description: %s
                
                List 8–10 specific, relevant skills that candidates should have.
                
                Return ONLY a comma-separated list of skill names.
                
                Example:
                Java, Spring Boot, PostgreSQL, Docker, REST APIs, Microservices
                """.formatted(
                        jobTitle, description
        );

        return AiTextResponse.builder()
                .content(geminiClient.generateText(system_prompt, prompt))
                .build();
    }

    // ==================== Tags Recommendation for Job ====================

    public AiTextResponse recommendTagsForJob(String jobTitle, String description) throws Exception {

        String prompt = """
        Recommend 8-10 relevant tags/keywords for this job posting that improve discoverability.

        Job Title: %s
        Description: %s

        Return ONLY a comma-separated list of short tag names (1-3 words each).

        Example: React, Frontend, JavaScript, Remote, Startup, Full Stack, Web Development
        """.formatted(jobTitle, description);

        return AiTextResponse.builder()
                .content( geminiClient.generateText(system_prompt, prompt))
                .build();
    }

}
