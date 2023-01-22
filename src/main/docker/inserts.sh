#!/bin/sh

source ./config.conf

PGPASSWORD=$PASS psql -h $HOST -p $PORT -U $USER -d $DB -c \
"INSERT INTO tb_user VALUES 
(gen_random_uuid(), '\$2a\$10\$qdwBS9XeUEini1AjyV1ZnesbPPC6h22YFgxchCxyNuoGf2gY7AQR2', 'admin'),
(gen_random_uuid(), '\$2a\$10\$qdwBS9XeUEini1AjyV1ZnesbPPC6h22YFgxchCxyNuoGf2gY7AQR2', 'user'),
(gen_random_uuid(), '\$2a\$10\$qdwBS9XeUEini1AjyV1ZnesbPPC6h22YFgxchCxyNuoGf2gY7AQR2', 'supporter');
INSERT INTO tb_role VALUES 
('c0b5c616-6f47-46b6-bd20-162e25b509e4', 'ROLE_ADMIN'),
(gen_random_uuid(), 'ROLE_COMMUN_USER'),
('4093b733-2498-44b7-8ce9-e5a24685fc8d', 'ROLE_SUPPORTER');
INSERT INTO tb_users_roles  
(SELECT user_id, (SELECT role_id from tb_role where role_name = 'ROLE_ADMIN') FROM tb_user WHERE username = 'admin');
INSERT INTO tb_users_roles  
(SELECT user_id, (SELECT role_id from tb_role where role_name = 'ROLE_COMMUN_USER') FROM tb_user WHERE username = 'user');
INSERT INTO tb_users_roles  
(SELECT user_id, (SELECT role_id from tb_role where role_name = 'ROLE_SUPPORTER') FROM tb_user WHERE username = 'supporter');
INSERT INTO tb_user_chamado  
(SELECT user_id, username from tb_user);"
