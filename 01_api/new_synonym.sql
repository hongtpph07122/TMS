INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','LOGIN', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) + 1 FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','LOGOUT', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','ASSIGNED', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','BUSY', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','BREAK', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','ON_CALL', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','WRAP_UP', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));
INSERT INTO cf_synonym 
(synonym_id, "type", "name", value, type_id)
VALUES ((SELECT MAX(synonym_id) + 1 FROM cf_synonym),'AGENT STATE','REGISTERED', (SELECT COALESCE(MAX(value), 0) + 1 FROM cf_synonym WHERE type = 'AGENT STATE'), (SELECT MAX(type_id) FROM cf_synonym));

