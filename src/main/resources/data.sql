INSERT INTO users (id,UUID, name, email, password, created, modified, last_Login,is_Active)
VALUES (101,'1f324716-6a52-4d51-8b58-63060582d1cb','Juan Rodriguez','juan@rodriguez.org','$2a$10$tZvkSa9akb0lkihrQfzblOAfbcs7bvkbtiJZQ5d.lUhgVr3BHEMRK','26-01-2024 12:00:00','26-01-2024 13:00:00','26-01-2024 12:30:00',true); -- pass: Hunter22
INSERT INTO users (id,UUID, name, email, password, created, modified, last_Login,is_Active)
VALUES (102,'cea28a47-ca42-42a7-b51e-78d605aeda5c','Mike Ross','mike@ross.org','$2a$10$9SoSYFDC.h3WtipDtF/0.eCd55T5FqYPhVzFedsYBarUOit7TtFNO','26-01-2024 12:00:00',null,'26-01-2024 12:30:00',true); -- pass: King23
INSERT INTO users (id,UUID, name, email, password, created, modified, last_Login,is_Active)
VALUES (103,'49414c78-f032-4226-bb7c-b95240fc8355','Pedro Ramirez','pedro@ramirez.org','$2a$10$5NCg2JPynX8LaUvKhq4YIezBkbvLDcO/zlz5qrzR2cMa0GFbbruOa','02-01-2024 12:00:00',null,'27-01-2024 15:30:00',false); -- pass: Queen15


INSERT INTO phone (id, number, citycode, contrycode) VALUES (101,1234567,1,57);
INSERT INTO phone (id, number, citycode, contrycode) VALUES (102,7654321,1,57);
INSERT INTO phone (id, number, citycode, contrycode) VALUES (103,2468135,2,56);
INSERT INTO phone (id, number, citycode, contrycode) VALUES (104,135792,44,56);

INSERT INTO users_phones(user_id,phones_id) VALUES (101,101);
INSERT INTO users_phones(user_id,phones_id) VALUES (101,102);
INSERT INTO users_phones(user_id,phones_id) VALUES (102,103);
INSERT INTO users_phones(user_id,phones_id) VALUES (102,104);