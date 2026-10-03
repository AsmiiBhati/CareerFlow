CREATE TYPE application_status AS ENUM ('APPLIED','SCREENING','INTERVIEW','OFFER','ACCEPTED','REJECTED','WITHDRAWN');
CREATE TYPE interview_type     AS ENUM ('HR','TECHNICAL','MANAGERIAL','CODING');
CREATE TYPE interview_status   AS ENUM ('SCHEDULED','COMPLETED','CANCELLED');

CREATE TABLE users (
                       id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                       name          varchar(100) NOT NULL,
                       email         varchar(255) NOT NULL UNIQUE,
                       password_hash varchar(255) NOT NULL,
                       created_at    timestamp NOT NULL DEFAULT now(),
                       updated_at    timestamp NOT NULL DEFAULT now()
);
CREATE TABLE job_applications (
                                  id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                  user_id      uuid NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                  company_name varchar(150) NOT NULL,
                                  role         varchar(150) NOT NULL,
                                  location     varchar(150),
                                  status       application_status NOT NULL DEFAULT 'APPLIED',
                                  applied_at   timestamp NOT NULL DEFAULT now(),
                                  created_at   timestamp NOT NULL DEFAULT now(),
                                  updated_at   timestamp NOT NULL DEFAULT now()
);
CREATE TABLE interviews (
                            id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                            application_id uuid NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE,
                            scheduled_at   timestamp NOT NULL,
                            type           interview_type NOT NULL,
                            status         interview_status NOT NULL DEFAULT 'SCHEDULED',
                            notes          text
);
CREATE TABLE status_history (
                                id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                application_id uuid NOT NULL REFERENCES job_applications(id) ON DELETE CASCADE,
                                from_status    application_status,          -- NULL for the first entry
                                to_status      application_status NOT NULL,
                                changed_at     timestamp NOT NULL DEFAULT now()
);

CREATE INDEX idx_job_applications_user_status ON job_applications (user_id, status);
CREATE INDEX idx_job_applications_company     ON job_applications (lower(company_name));
CREATE INDEX idx_status_history_application   ON status_history (application_id, changed_at);
CREATE INDEX idx_interviews_application       ON interviews (application_id);