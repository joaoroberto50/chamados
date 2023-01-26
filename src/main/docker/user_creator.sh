#!/bin/sh

source ./config.conf

function insert_into {
    var=$(python -c "from bcrypt_pass import get_hash; print(get_hash(\"$2\"))")
    PGPASSWORD=$PASS psql -h $HOST -p $PORT -U $USER -d $DB -c \ "INSERT INTO tb_user VALUES (gen_random_uuid(), '"${var}"', '"$1"'); \
INSERT INTO tb_user_chamado (SELECT user_id, username from tb_user WHERE username = '"$1"'); \
INSERT INTO tb_users_roles \
(SELECT user_id, (SELECT role_id from tb_role where role_name = '"$3"') FROM tb_user WHERE username = '"$1"');"
}

if [ "$1" = "-h" ]; then
    echo -e "-h \t\t help
-c \t\t for command
   \t\t write in the following order: username pass role
   \t\t example: ./user_creator -c user secretpass USER
   \t\t AVAILABLE ROLES: ADMIN, SUPPORTER and USER"
elif [ $# -ge 4 ]; then
    if [ "$1" = "-c" ]; then
        if [ "S4" = "ADMIN" ]; then
            insert_into $2 $3 'ROLE_ADMIN'
        elif [ "$4" = "USER" ]; then
            insert_into $2 $3 'ROLE_COMMUN_USER'
        elif [ "$4" = "SUPPORTER" ]; then
            insert_into $2 $3 'ROLE_SUPPORTER'
        else
            echo "$4 is not a roll. Use 'ADMIN', 'SUPPORTER' or 'USER'. Without quotation marks."
    fi
    else
        echo "$1 is not a command. Use -h for help."
    fi
else
    echo "Insufficients args. Type -h for help"
fi
