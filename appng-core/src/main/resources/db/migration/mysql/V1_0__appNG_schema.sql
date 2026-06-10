CREATE TABLE application (
        file_based bit NOT NULL,
        hidden bit NOT NULL,
        id integer NOT NULL auto_increment,
        privileged bit,
        snapshot bit NOT NULL,
        version datetime(6),
        application_version varchar(64),
        appng_version varchar(64),
        display_name varchar(64),
        name varchar(64) NOT NULL,
        time_stamp varchar(64),
        description varchar(8192),
        long_description longtext,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE authgroup (
        default_admin bit,
        id integer NOT NULL auto_increment,
        version datetime(6),
        name varchar(64) NOT NULL,
        description varchar(8192),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE authgroup_role (
        authgroup_id integer NOT NULL,
        role_id integer NOT NULL,
        PRIMARY KEY (authgroup_id, role_id)
    ) engine=InnoDB;

    CREATE TABLE database_connection (
        active bit NOT NULL,
        id integer NOT NULL auto_increment,
        managed bit NOT NULL,
        max_connections integer NOT NULL,
        min_connections integer NOT NULL check ((min_connections>=1)),
        site_id integer,
        version datetime(6),
        description varchar(255),
        driver_class varchar(255) NOT NULL,
        jdbc_url varchar(255) NOT NULL,
        name varchar(255) NOT NULL,
        username varchar(255) NOT NULL,
        validation_query varchar(255),
        password longblob,
        type enum ('HSQL','MSSQL','MYSQL','POSTGRESQL') NOT NULL,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE job_execution_record (
        duration integer,
        id integer NOT NULL auto_increment,
        run_once bit,
        end_time datetime(6),
        start_time datetime(6),
        application varchar(255),
        job_name varchar(255),
        node varchar(255),
        result varchar(255),
        site varchar(255),
        triggername varchar(255),
        custom_data tinytext,
        stacktraces longtext,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE permission (
        application_id integer,
        id integer NOT NULL auto_increment,
        version datetime(6),
        description varchar(8192),
        name varchar(255) NOT NULL,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE platform_event (
        id integer NOT NULL auto_increment,
        created datetime(6),
        application varchar(255),
        context varchar(255),
        ev_user varchar(255),
        event varchar(255),
        host_name varchar(255),
        origin varchar(255),
        request_id varchar(255),
        session_id varchar(255),
        ev_type enum ('CREATE','DELETE','ERROR','INFO','UPDATE','WARN'),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE property (
        mandatory bit,
        version datetime(6),
        description varchar(8192),
        default_value varchar(255),
        name varchar(255) NOT NULL,
        value varchar(255),
        blob_value tinyblob,
        clob_value tinytext,
        prop_type enum ('BOOLEAN','DECIMAL','INT','MULTILINE','PASSWORD','TEXT'),
        PRIMARY KEY (name)
    ) engine=InnoDB;

    CREATE TABLE repository (
        active bit NOT NULL,
        id integer NOT NULL auto_increment,
        published bit NOT NULL,
        strict bit NOT NULL,
        version datetime(6),
        name varchar(64) NOT NULL,
        remote_repository_name varchar(64),
        description varchar(8192),
        digest varchar(255),
        uri varbinary(255),
        accepted_certs tinyblob,
        mode enum ('ALL','SNAPSHOT','STABLE'),
        type enum ('LOCAL','REMOTE'),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE resource (
        application_id integer,
        id integer NOT NULL auto_increment,
        version datetime(6),
        description varchar(8192),
        checksum varchar(255),
        name varchar(255),
        bytes longblob NOT NULL,
        type enum ('APPLICATION','ASSET','BEANS_XML','DICTIONARY','JAR','RESOURCE','SQL','TPL','XML','XSL'),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE role (
        application_id integer,
        id integer NOT NULL auto_increment,
        version datetime(6),
        name varchar(64) NOT NULL,
        description varchar(8192),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE role_permission (
        permission_id integer NOT NULL,
        role_id integer NOT NULL,
        PRIMARY KEY (permission_id, role_id)
    ) engine=InnoDB;

    CREATE TABLE site (
        active bit NOT NULL,
        create_repository bit,
        id integer NOT NULL auto_increment,
        reload_count integer,
        version datetime(6),
        name varchar(64) NOT NULL,
        description varchar(8192),
        domain varchar(255) NOT NULL,
        host varchar(255) NOT NULL,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE site_application (
        active bit NOT NULL,
        application_id integer NOT NULL,
        connection_id integer,
        deletion_mark bit,
        reload_required bit,
        site_id integer NOT NULL,
        PRIMARY KEY (application_id, site_id)
    ) engine=InnoDB;

    CREATE TABLE site_hostalias (
        site_id integer NOT NULL,
        hostname varchar(255)
    ) engine=InnoDB;

    CREATE TABLE sites_granted (
        application_id integer NOT NULL,
        granted_site_id integer NOT NULL,
        site_id integer NOT NULL,
        PRIMARY KEY (application_id, granted_site_id, site_id)
    ) engine=InnoDB;

    CREATE TABLE subject (
        id integer NOT NULL auto_increment,
        language varchar(3) NOT NULL,
        locked bit,
        login_attempts integer,
        pw_change_policy tinyint check ((pw_change_policy between 0 and 3)),
        expiry_date datetime(6),
        last_login datetime(6),
        pw_last_changed datetime(6),
        version datetime(6),
        realname varchar(64) NOT NULL,
        description varchar(8192),
        digest varchar(255),
        email varchar(255),
        name varchar(255) NOT NULL,
        salt varchar(255),
        timezone varchar(255),
        type enum ('GLOBAL_GROUP','GLOBAL_USER','LOCAL_USER'),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE subject_authgroup (
        group_id integer NOT NULL,
        subject_id integer NOT NULL
    ) engine=InnoDB;

    CREATE TABLE template (
        id integer NOT NULL auto_increment,
        version datetime(6),
        appng_version varchar(255),
        description varchar(255),
        display_name varchar(255),
        long_desc varchar(255),
        name varchar(255),
        template_version varchar(255),
        timestamp varchar(255),
        tpl_type enum ('THYMELEAF','XSL'),
        PRIMARY KEY (id)
    ) engine=InnoDB;

    CREATE TABLE template_resource (
        id integer NOT NULL auto_increment,
        template_id integer,
        file_version datetime(6),
        version datetime(6),
        checksum varchar(255),
        name varchar(255),
        bytes longblob,
        PRIMARY KEY (id)
    ) engine=InnoDB;

    ALTER TABLE application 
       ADD CONSTRAINT UK__APPLICATION__NAME UNIQUE (name);

    ALTER TABLE authgroup 
       ADD CONSTRAINT UK__AUTHGROUP__NAME UNIQUE (name);

    ALTER TABLE repository 
       ADD CONSTRAINT UK__REPOSITORY__NAME UNIQUE (name);

    ALTER TABLE site 
       ADD CONSTRAINT UK__SITE__NAME UNIQUE (name);

    ALTER TABLE site 
       ADD CONSTRAINT UK__SITE__HOST UNIQUE (host);

    ALTER TABLE site 
       ADD CONSTRAINT UK__SITE__DOMAIN UNIQUE (domain);

    ALTER TABLE subject 
       ADD CONSTRAINT UK__SUBJECT__NAME UNIQUE (name);

    ALTER TABLE authgroup_role 
       ADD CONSTRAINT FK__AUTHGROUP_ROLE__ROLE 
       FOREIGN KEY (role_id) 
       REFERENCES role (id);

    ALTER TABLE authgroup_role 
       ADD CONSTRAINT FK__AUTHGROUP_ROLE__AUTHGROUP 
       FOREIGN KEY (authgroup_id) 
       REFERENCES authgroup (id);

    ALTER TABLE database_connection 
       ADD CONSTRAINT FK__DATABASE_CONNECTION__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site (id);

    ALTER TABLE permission 
       ADD CONSTRAINT FK__PERMISSION__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application (id);

    ALTER TABLE resource 
       ADD CONSTRAINT FK__RESOURCE__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application (id);

    ALTER TABLE role 
       ADD CONSTRAINT FK__ROLE__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application (id);

    ALTER TABLE role_permission 
       ADD CONSTRAINT FK__ROLE_PERMISSION__PERMISSION 
       FOREIGN KEY (permission_id) 
       REFERENCES permission (id);

    ALTER TABLE role_permission 
       ADD CONSTRAINT FK__ROLE_PERMISSION__ROLE 
       FOREIGN KEY (role_id) 
       REFERENCES role (id);

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application (id);

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__DATABASE_CONNECTION 
       FOREIGN KEY (connection_id) 
       REFERENCES database_connection (id);

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site (id);

    ALTER TABLE site_hostalias 
       ADD CONSTRAINT FK__SITE_HOSTALIAS__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site (id);

    ALTER TABLE sites_granted 
       ADD CONSTRAINT FK__SITES_GRANTED__SITE 
       FOREIGN KEY (granted_site_id) 
       REFERENCES site (id);

    ALTER TABLE sites_granted 
       ADD CONSTRAINT FK__SITES_GRANTED__SITE_APPLICATION 
       FOREIGN KEY (application_id, site_id) 
       REFERENCES site_application (application_id, site_id);

    ALTER TABLE subject_authgroup 
       ADD CONSTRAINT FK__SUBJECT_AUTHGROUP__AUTHGROUP 
       FOREIGN KEY (group_id) 
       REFERENCES authgroup (id);

    ALTER TABLE subject_authgroup 
       ADD CONSTRAINT FK__SUBJECT_AUTHGROUP__SUBJECT 
       FOREIGN KEY (subject_id) 
       REFERENCES subject (id);

    ALTER TABLE template_resource 
       ADD CONSTRAINT FK__TEMPLATE_RESOURCE__TEMPLATE_ID 
       FOREIGN KEY (template_id) 
       REFERENCES template (id);