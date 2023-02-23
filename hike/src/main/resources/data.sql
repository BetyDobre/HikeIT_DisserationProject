insert ignore into roles values(1, 'ADMIN');
insert ignore into roles values(2, 'USER');
insert ignore into roles values(3, 'BLOGGER');

insert ignore into users(id, authProvider, nume, prenume, username, parola, email, dataNastere, sex, pozaProfil, createdOn) values(1,'LOCAL','Dobre', 'Betty', 'betydbr', '$2a$10$9cyJxHIS19vw/Udl5gAx7u9bxsuhPZLTUFeBSDFmTwRPsTCPiJZA2', 'bety@gmail.com', STR_TO_DATE('19-03-2000', '%d-%c-%Y'), 'F', null, SYSDATE());
insert ignore into users_roles values (1, 1);

insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (1, 'Piatra Craiului', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\piatraCraiului.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (2, 'Parang', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\parang4.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (3, 'Bucegi', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\bucegi.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (4, 'Calimani', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\calimani.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (5, 'Fagaras', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\fagaras.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (6, 'Bihor Vladeasa', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\bihorVladeasa.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (7, 'Bistritei', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\bistritei.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (8, 'Buzaului', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\buzaului.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (9, 'Ceahlau', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\ceahlau1.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (10, 'Ciucas', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\ciucas2.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (11, 'Cozia', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\cozia1.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (12, 'Godeanu', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\godeanu.jpg'));
insert ignore into grupe_muntoase(id, titlu, pozaHarta) values (13, 'Macin', LOAD_FILE('C:\\Users\\Betty\\Desktop\\HikeIT_DisserationProject\\hike\\src\\main\\resources\\static\\img\\grupeMuntoase\\macin.jpg'));





