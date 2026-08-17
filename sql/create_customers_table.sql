CREATE TABLE public.customers (
    id bigserial NOT NULL,
    "name" varchar(100) NOT NULL,
    email varchar(255) NOT NULL,
    phone varchar(20) NULL,
    status varchar(20) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    CONSTRAINT customers_email_key UNIQUE (email),
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);
