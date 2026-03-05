CREATE TABLE bookmarks
(
    account_id UUID NOT NULL,
    post_id    UUID NOT NULL
);

ALTER TABLE bookmarks
    ADD CONSTRAINT fk_bookmarks_on_account FOREIGN KEY (account_id) REFERENCES accounts (id);

ALTER TABLE bookmarks
    ADD CONSTRAINT fk_bookmarks_on_post FOREIGN KEY (post_id) REFERENCES posts (id);