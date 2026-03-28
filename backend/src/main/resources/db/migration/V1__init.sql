CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(64) NOT NULL,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                           updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE plans (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(64) NOT NULL,
                       prices JSONB NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TYPE subscription_status AS ENUM ('active', 'paused', 'cancelled', 'expired');

CREATE TABLE subscriptions (
                               id SERIAL PRIMARY KEY,
                               customer_id INTEGER NOT NULL REFERENCES customers(id),
                               plan_id INTEGER NOT NULL REFERENCES plans(id),
                               discount INTEGER NOT NULL DEFAULT 0,
                               start_date TIMESTAMPTZ NOT NULL,
                               end_date TIMESTAMPTZ,
                               status subscription_status NOT NULL DEFAULT 'active',
                               created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE usage_events (
                              id SERIAL PRIMARY KEY,
                              idempotency_key VARCHAR(255) NOT NULL UNIQUE,
                              customer_id INTEGER NOT NULL REFERENCES customers(id),
                              metric VARCHAR(64) NOT NULL,
                              quantity DECIMAL NOT NULL,
                              timestamp TIMESTAMPTZ NOT NULL,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TYPE billing_status AS ENUM ('draft', 'open', 'paid', 'voided');

CREATE TABLE billing_records (
                                 id SERIAL PRIMARY KEY,
                                 customer_id INTEGER NOT NULL REFERENCES customers(id),
                                 period_start TIMESTAMPTZ NOT NULL,
                                 period_end TIMESTAMPTZ NOT NULL,
                                 amount DECIMAL NOT NULL,
                                 status billing_status NOT NULL DEFAULT 'draft',
                                 created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
