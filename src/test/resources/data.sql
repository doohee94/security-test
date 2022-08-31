SET
REFERENTIAL_INTEGRITY FALSE;

insert into public.authority (authority_id, role)
values (1, 'ROLE_ADMIN'),
       (2, 'ROLE_USER');

insert into public.users (user_id, name, password, authority_id)
values (1, 'admin', 'admin', 1),
       (2, 'user', 'user', 2);

SET
    REFERENTIAL_INTEGRITY TRUE;
