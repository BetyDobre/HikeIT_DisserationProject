insert ignore into roles values(1, 'ADMIN');
insert ignore into roles values(2, 'USER');

insert ignore into users(id, nume, prenume, username, parola, email, dataNastere, sex, pozaProfil, createdOn) values(1, 'Betty', 'Dobre', 'betydbr', '1234', 'bety@gmail.com', STR_TO_DATE('19-03-2000', '%d-%c-%Y'), 'F', null, SYSDATE());
insert ignore into users_roles values (1, 1);