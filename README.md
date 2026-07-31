# FindingJob — A Skill-Centric Job Platform

> **v1.0-MVP** · Built with Spring Boot 3.x + React 18 + PostgreSQL

A job platform that puts **skills at the center** — unlike traditional job sites that bury technical ability behind long resumes. Candidates showcase verified skills and open-source contributions, while both sides rate each other on **professional conduct** to build trust.

---

## Core Features

### For Jobseekers
- **Skill Tags** — Add skills from a curated dictionary, label proficiency (Beginner / Familiar / Expert), earn HR-verifications
- **Open-Source Showcase** — Link GitHub/Gitee to display your top-starred repos (optional)
- **Resume Privacy** — Resumes are private by default; HRs must request access, approved links expire in 7 days
- **Company Ratings** — Rate companies on hiring conduct (fair process, promise kept, etc.) to help others avoid bad actors

### For HR / Recruiters
- **Skill-Based Search** — Search candidates by skill tags, experience range, and expected position
- **Strict Company Verification** — Business license + manual review ensures real identities
- **Resume Request Flow** — Request access with 7-day expiring download links
- **Candidate Conduct Ratings** — Rate candidates on professionalism (showed up, info accurate, etc.) — only after resume access is granted

### For Admins
- **Company Verification Review** — Approve/reject business licenses with comments
- **Report Moderation** — Review flagged ratings, hide abusive content
- **Dashboard** — Track MVP targets (100 jobseekers, 10 companies, 50 resume requests)

### Platform-Wide
- **Account Deletion** — 7-day cooling period, personal data wiped, ratings anonymized
- **In-App Notifications** — Resume requests, approvals, expirations, new ratings
- **Role-Based Access** — Three roles (Jobseeker / HR / Admin) with mutually exclusive permissions

---

## Tech Stack

### Backend

| Component | Choice |
|-----------|--------|
| Framework | Spring Boot 3.3.5 (Java 17) |
| Architecture | Microservices (7 services, no Spring Cloud) |
| Auth | JustAuth + Spring Security + JWT |
| ORM | Spring Data JPA (Hibernate) |
| Database | PostgreSQL 15+ (single instance, schema-per-service) |
| Rate Limiting | Resilience4j |
| API Docs | SpringDoc (Swagger UI) |
| File Storage | Strategy pattern: Local / Aliyun OSS / Tencent COS / S3 |

### Frontend

| Component | Choice |
|-----------|--------|
| Framework | React 18 + TypeScript |
| Build | Vite |
| UI | Ant Design (responsive) |
| Server State | React Query |
| Client State | Zustand |
| PWA | vite-plugin-pwa |
| Admin UI | Ant Design Pro (`/admin/*` routes) |

### Infrastructure

| Environment | Tool |
|-------------|------|
| Development | Docker Compose |
| Production | Kubernetes (K8s) |
| API Gateway | Nginx Ingress Controller |
| Config | K8s ConfigMap + Secret / `.env` (dev) |

---

## Architecture

```
┌─────────────────────────────────────────────────┐
│     React Frontend (Vite + Ant Design + PWA)     │
│     Responsive Web + Mobile + Admin Panel        │
└──────────────────────┬──────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────┐
│         K8s Ingress (Nginx Ingress)              │
│         Routing / TLS / Rate Limiting             │
└──┬────┬────┬────┬────┬────┬──┬──────────────────┘
   │    │    │    │    │    │  │
   ▼    ▼    ▼    ▼    ▼    ▼  ▼
 ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐ ┌──┐
 │AU│ │PR│ │CO│ │RA│ │RE│ │ST│ │NO│
 │TH│ │OF│ │MP│ │TIN│ │SUM│ │OR│ │TIF│
 └┬─┘ └┬─┘ └┬─┘ └┬─┘ └┬─┘ └──┘ └┬─┘
  │    │    │    │    │          │
  ▼    ▼    ▼    ▼    ▼          ▼
┌──────────────────────────────────────┐
│     PostgreSQL (Single, Multi-Schema) │
└──────────────────────────────────────┘
```

### Microservices

| Service | Port | Responsibility |
|---------|:---:|----------------|
| **auth-service** | 8001 | User registration/login, JWT, OAuth, SMS, account deletion |
| **profile-service** | 8002 | Jobseeker profile, skill tags, work experience, certificates, GitHub projects, search |
| **company-service** | 8003 | Company info, HR profiles, company verification |
| **rating-service** | 8004 | Bidirectional conduct ratings, reports, admin moderation |
| **resume-service** | 8005 | Resume upload (PDF), request/approve flow, 7-day expiring links |
| **storage-service** | 8006 | Unified file storage (Local / OSS / COS / S3 strategy pattern) |
| **notification-service** | 8007 | In-app notifications, unread count, mark-as-read |

---

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+
- Docker & Docker Compose

### Backend

```bash
# 1. Build all modules and package JARs
mvn clean package -DskipTests

# 2. Run tests (optional)
mvn test

# 3. Start all services with PostgreSQL
docker compose up -d
```

> **Apple Silicon users:** The base image is `eclipse-temurin:17-jre` (not `-alpine`),
> which supports arm64. If `docker compose build` fails due to Docker Hub network issues,
> pull the images first:
>
> ```bash
> docker pull eclipse-temurin:17-jre
> docker pull postgres:15-alpine
> docker compose up -d
> ```

### Frontend

```bash
cd frontend
npm install
npm run dev        # Dev server on :5173
npm run build      # Production build
```

### Access

| Service | URL |
|---------|-----|
| Frontend | http://localhost:5173 |
| Admin Panel | http://localhost:5173/admin |
| Auth Swagger | http://localhost:8001/swagger-ui.html |
| Profile Swagger | http://localhost:8002/swagger-ui.html |
| Company Swagger | http://localhost:8003/swagger-ui.html |
| Rating Swagger | http://localhost:8004/swagger-ui.html |
| Resume Swagger | http://localhost:8005/swagger-ui.html |
| Storage Swagger | http://localhost:8006/swagger-ui.html |
| Notification Swagger | http://localhost:8007/swagger-ui.html |

---

## Project Structure

```
findingjob/
├── docs/
│   └── PRD.md                          # Product Requirements Document
├── frontend/                           # React frontend
│   ├── src/
│   │   ├── api/                        # API client (Axios + JWT interceptor)
│   │   ├── components/                 # Shared components
│   │   ├── layouts/                    # Main layout + Admin layout
│   │   ├── pages/                      # Page components
│   │   │   └── admin/                  # Admin panel pages
│   │   ├── stores/                     # Zustand stores
│   │   └── types/                      # TypeScript types
│   └── package.json
├── backend/
│   ├── common/                         # Shared module (DTOs, exceptions, JWT, security)
│   ├── auth-service/                   # Authentication & user management
│   ├── profile-service/                # Jobseeker profiles & skill system
│   ├── company-service/                # Companies & HR management
│   ├── rating-service/                 # Conduct ratings & reports
│   ├── resume-service/                 # Resume upload & approval flow
│   ├── storage-service/                # File storage abstraction
│   └── notification-service/           # In-app notifications
├── docker-compose.yml                  # Development orchestration
├── k8s/                                # Kubernetes deployment configs
│   ├── namespace.yaml
│   ├── ingress.yaml
│   ├── postgresql/
│   └── auth-service/
├── pom.xml                             # Maven multi-module parent
└── .scratch/                           # Issue tracker & specs
    └── job-platform/
        ├── spec.md                     # Full product spec
        └── issues/                     # 19 vertical-slice tickets
```

---

## Security

| Measure | Detail |
|---------|--------|
| JWT Auth | All APIs protected via Spring Security + JWT |
| No Passwords | OAuth-only login, no password storage |
| Resume Access Control | Only approved requests get expiring download links |
| Rate Limiting | Ingress layer + Resilience4j |
| File Validation | PDF-only for resumes, image types for certificates, size limits |
| CORS | Restricted to frontend domains |
| SQL Injection | JPA parameterized queries |
| XSS | Frontend output encoding |

---

## MVP Success Metrics

| Metric | Target |
|--------|--------|
| Active jobseekers | ≥ 100 |
| Verified companies | ≥ 10 |
| Resume requests | ≥ 50 |

---

## License

Private project — all rights reserved.
