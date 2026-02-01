CREATE TABLE account_roles
(
    account_id UUID NOT NULL,
    role_id    UUID NOT NULL,
    CONSTRAINT pk_account_roles PRIMARY KEY (account_id, role_id)
);

CREATE TABLE accounts
(
    id         UUID         NOT NULL,
    username   VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

CREATE TABLE likes
(
    id         UUID NOT NULL,
    post_id    UUID NOT NULL,
    account_id UUID NOT NULL,
    created_at BIGINT,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE posts
(
    id         UUID         NOT NULL,
    content    VARCHAR(300) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    account_id UUID         NOT NULL,
    parent_id  UUID,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE role
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

ALTER TABLE role
    ADD CONSTRAINT uc_role_name UNIQUE (name);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES accounts (id);

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES accounts (id);

ALTER TABLE posts
    ADD CONSTRAINT FK_POSTS_ON_PARENT FOREIGN KEY (parent_id) REFERENCES posts (id);

ALTER TABLE account_roles
    ADD CONSTRAINT fk_accrol_on_account FOREIGN KEY (account_id) REFERENCES accounts (id);

ALTER TABLE account_roles
    ADD CONSTRAINT fk_accrol_on_role FOREIGN KEY (role_id) REFERENCES role (id);