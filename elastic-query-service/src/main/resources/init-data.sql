CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- use ids from keycloack user id
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('8e8c5f22-3a1a-4e21-9b67-5d15d8c3e881', 'app_user', 'Standard', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('870d3c40-9fd3-4616-bcbf-3b355a02eff4', 'app_admin', 'Admin', 'User');
INSERT INTO public.users(
	id, username, firstname, lastname)
	VALUES ('8b5b8449-b1d4-4412-8c26-77626398675b', 'app_super_user', 'Super', 'User');


-- document ids from elastic
insert into documents(id, document_id)
values ('c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 7027767952011865478);
insert into documents(id, document_id)
values ('f2b2d644-3a08-4acb-ae07-20569f6f2a01', 8335389889507986458);
insert into documents(id, document_id)
values ('90573d2b-9a5d-409e-bbb6-b94189709a19', 8634339801280483014);

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'8e8c5f22-3a1a-4e21-9b67-5d15d8c3e881', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'870d3c40-9fd3-4616-bcbf-3b355a02eff4', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(),'870d3c40-9fd3-4616-bcbf-3b355a02eff4', 'f2b2d644-3a08-4acb-ae07-20569f6f2a01', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '870d3c40-9fd3-4616-bcbf-3b355a02eff4', '90573d2b-9a5d-409e-bbb6-b94189709a19', 'READ');

insert into user_permissions(user_permission_id, user_id, document_id, permission_type)
values (uuid_generate_v4(), '8b5b8449-b1d4-4412-8c26-77626398675b', 'c1df7d01-4bd7-40b6-86da-7e2ffabf37f7', 'READ');


