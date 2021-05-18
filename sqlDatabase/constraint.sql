
ALTER TABLE badges ADD CONSTRAINT badges_FK FOREIGN KEY (id) REFERENCES users(id);

/* primär-nyckel för `users` är redan definerad i creates.sql */