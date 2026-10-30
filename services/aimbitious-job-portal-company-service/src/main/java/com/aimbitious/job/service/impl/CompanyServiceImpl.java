package com.aimbitious.job.service.impl;

import com.aimbitious.job.domain.CompanyStatus;
import com.aimbitious.job.domain.CompanyType;
import com.aimbitious.job.domain.IndustryType;
import com.aimbitious.job.dto.response.CompanyRequest;
import com.aimbitious.job.dto.response.CompanyResponse;
import com.aimbitious.job.dto.response.CompanySummaryResponse;
import com.aimbitious.job.exception.CompanyException;
import com.aimbitious.job.exception.ResourceNotFoundException;
import com.aimbitious.job.mapper.CompanyMapper;
import com.aimbitious.job.model.Company;
import com.aimbitious.job.model.CompanyLocation;
import com.aimbitious.job.model.embeddable.SocialLink;
import com.aimbitious.job.repository.CompanyLocationRepository;
import com.aimbitious.job.repository.CompanyRepository;
import com.aimbitious.job.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyLocationRepository locationRepository;

    // ── Create ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CompanyResponse createCompany(Long ownerId, CompanyRequest req) throws CompanyException {
        if (companyRepository.existsByOwnerId(ownerId)) {
            throw new CompanyException("You already have a company registered. " +
                    "Only one company per account is allowed.");
        }
        if (companyRepository.existsByName(req.getName())) {
            throw new CompanyException("Company with name '" + req.getName() + "' already exists");
        }
        if (req.getRegistrationNumber() != null
                && companyRepository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new CompanyException("Company with registration number '"
                    + req.getRegistrationNumber() + "' already exists");
        }

        String slug = generateUniqueSlug(req.getName());

        Company company = Company.builder()
                .name(req.getName())
                .slug(slug)
                .tagline(req.getTagline())
                .description(req.getDescription())
                .logoUrl(req.getLogoUrl())
                .coverImageUrl(req.getCoverImageUrl())
                .website(req.getWebsite())
                .email(req.getEmail())
                .phone(req.getPhone())
                .foundedYear(req.getFoundedYear())
                .companySize(req.getCompanySize())
                .companyType(req.getCompanyType())
                .industryType(req.getIndustryType())
                .registrationNumber(req.getRegistrationNumber())
                .ownerId(ownerId)
                .socialLinks(mapSocialLinks(req.getSocialLinks()))
                .build();

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    // ── Read ──────────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long id) throws ResourceNotFoundException {
        Company company = getCompanyEntityById(id);
        return CompanyMapper.toResponse(company);
    }

    @Override
    public CompanySummaryResponse getCompanySummaryById(Long id) throws ResourceNotFoundException {
        Company company = getCompanyEntityById(id);
        return CompanyMapper.toSummaryResponse(company);
    }


    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany(Long ownerId) throws ResourceNotFoundException {
        Company company = companyRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("No company found for this account"));

        return CompanyMapper.toResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies(CompanyType companyType, IndustryType industryType, CompanyStatus status) {
        return companyRepository.findByFilters(companyType, industryType, status).stream()
                .map(CompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CompanyResponse updateCompany(Long companyId, Long ownerId, CompanyRequest req)
            throws ResourceNotFoundException, CompanyException {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);

        if (!company.getName().equals(req.getName())
                && companyRepository.existsByName(req.getName())) {
            throw new CompanyException("Company with name '" + req.getName() + "' already exists");
        }
        if (req.getRegistrationNumber() != null
                && !req.getRegistrationNumber().equals(company.getRegistrationNumber())
                && companyRepository.existsByRegistrationNumber(req.getRegistrationNumber())) {
            throw new CompanyException("Registration number '" + req.getRegistrationNumber() + "' already in use");
        }

        company.setName(req.getName());
        company.setTagline(req.getTagline());
        company.setDescription(req.getDescription());
        company.setLogoUrl(req.getLogoUrl());
        company.setCoverImageUrl(req.getCoverImageUrl());
        company.setWebsite(req.getWebsite());
        company.setEmail(req.getEmail());
        company.setPhone(req.getPhone());
        company.setFoundedYear(req.getFoundedYear());
        company.setCompanySize(req.getCompanySize());
        company.setCompanyType(req.getCompanyType());
        company.setIndustryType(req.getIndustryType());
        company.setRegistrationNumber(req.getRegistrationNumber());
        company.setSocialLinks(mapSocialLinks(req.getSocialLinks()));

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    // ── Admin actions ─────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CompanyResponse verifyCompany(Long companyId) throws ResourceNotFoundException {
        Company company = getCompanyEntityById(companyId);
        company.setVerified(true);
        company.setVerifiedAt(LocalDateTime.now());
        company.setStatus(CompanyStatus.ACTIVE);

        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Override
    @Transactional
    public CompanyResponse deactivateCompany(Long companyId) throws ResourceNotFoundException {
        Company company = getCompanyEntityById(companyId);
        company.setActive(false);
        company.setStatus(CompanyStatus.SUSPENDED);
        List<CompanyLocation> locations = locationRepository.findByCompanyIdAndActiveTrue(companyId);
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteCompany(Long companyId, Long ownerId)
            throws ResourceNotFoundException, CompanyException {
        Company company = getCompanyEntityById(companyId);
        assertOwner(company, ownerId);
        companyRepository.delete(company);
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Company getCompanyEntityById(Long id) throws ResourceNotFoundException {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    // ── Private utilities ─────────────────────────────────────────────────────

    private void assertOwner(Company company, Long ownerId) throws CompanyException {
        if (!company.getOwnerId().equals(ownerId)) {
            throw new CompanyException("You are not the owner of this company");
        }
    }

    private String generateUniqueSlug(String name) {
        String base = name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-");
        if (!companyRepository.existsBySlug(base)) {
            return base;
        }
        int counter = 1;
        while (companyRepository.existsBySlug(base + "-" + counter)) {
            counter++;
        }
        return base + "-" + counter;
    }

    private List<SocialLink> mapSocialLinks(List<CompanyRequest.SocialLinkEntry> entries) {
        if (entries == null) return new ArrayList<>();
        return entries.stream()
                .map(e -> SocialLink.builder()
                        .platform(e.getPlatform())
                        .url(e.getUrl())
                        .build())
                .collect(Collectors.toList());
    }
}