#!/usr/bin/python

from bcrypt import hashpw


SALT = b'$2a$10$qdwBS9XeUEini1AjyV1Zne'

def get_hash(password='test123'):
    return hashpw(str.encode(password), SALT).decode('UTF-8') 

