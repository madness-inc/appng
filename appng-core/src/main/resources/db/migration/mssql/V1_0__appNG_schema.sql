CREATE TABLE application (
        file_based bit NOT NULL,
        hidden bit NOT NULL,
        id int identity NOT NULL,
        privileged bit,
        snapshot bit NOT NULL,
        version datetimeoffset(7),
        application_version varchar(64),
        appng_version varchar(64),
        display_name varchar(64),
        name varchar(64) NOT NULL,
        time_stamp varchar(64),
        description varchar(max),
        long_description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE authgroup (
        default_admin bit,
        id int identity NOT NULL,
        version datetimeoffset(7),
        name varchar(64) NOT NULL,
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE authgroup_role (
        authgroup_id int NOT NULL,
        role_id int NOT NULL,
        PRIMARY KEY (authgroup_id, role_id)
    );

    CREATE TABLE database_connection (
        active bit NOT NULL,
        id int identity NOT NULL,
        managed bit NOT NULL,
        max_connections int NOT NULL,
        min_connections int NOT NULL check ((min_connections>=1)),
        site_id int,
        version datetimeoffset(7),
        description varchar(255),
        driver_class varchar(255) NOT NULL,
        jdbc_url varchar(255) NOT NULL,
        name varchar(255) NOT NULL,
        type varchar(255) NOT NULL check ((type in ('MYSQL','MSSQL','POSTGRESQL','HSQL'))),
        username varchar(255) NOT NULL,
        validation_query varchar(255),
        password varbinary(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE job_execution_record (
        duration int,
        id int identity NOT NULL,
        run_once bit,
        end_time datetime2(7),
        start_time datetime2(7),
        application varchar(255),
        job_name varchar(255),
        node varchar(255),
        result varchar(255),
        site varchar(255),
        triggername varchar(255),
        custom_data varchar(max),
        stacktraces varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE permission (
        application_id int,
        id int identity NOT NULL,
        version datetimeoffset(7),
        name varchar(255) NOT NULL,
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE platform_event (
        id int identity NOT NULL,
        created datetimeoffset(7),
        application varchar(255),
        context varchar(255),
        ev_type varchar(255) check ((ev_type in ('CREATE','UPDATE','DELETE','INFO','ERROR','WARN'))),
        ev_user varchar(255),
        event varchar(255),
        host_name varchar(255),
        origin varchar(255),
        request_id varchar(255),
        session_id varchar(255),
        PRIMARY KEY (id)
    );

    CREATE TABLE property (
        mandatory bit,
        version datetimeoffset(7),
        default_value varchar(255),
        name varchar(255) NOT NULL,
        prop_type varchar(255) check ((prop_type in ('INT','DECIMAL','BOOLEAN','TEXT','PASSWORD','MULTILINE'))),
        value varchar(255),
        blob_value varbinary(max),
        clob_value varchar(max),
        description varchar(max),
        PRIMARY KEY (name)
    );

    CREATE TABLE repository (
        active bit NOT NULL,
        id int identity NOT NULL,
        published bit NOT NULL,
        strict bit NOT NULL,
        version datetimeoffset(7),
        name varchar(64) NOT NULL,
        remote_repository_name varchar(64),
        digest varchar(255),
        mode varchar(255) check ((mode in ('ALL','STABLE','SNAPSHOT'))),
        type varchar(255) check ((type in ('LOCAL','REMOTE'))),
        uri varbinary(255),
        accepted_certs varbinary(max),
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE resource (
        application_id int,
        id int identity NOT NULL,
        version datetimeoffset(7),
        checksum varchar(255),
        name varchar(255),
        type varchar(255) check ((type in ('BEANS_XML','JAR','XML','XSL','SQL','TPL','RESOURCE','ASSET','DICTIONARY','APPLICATION'))),
        bytes varbinary(max) NOT NULL,
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE role (
        application_id int,
        id int identity NOT NULL,
        version datetimeoffset(7),
        name varchar(64) NOT NULL,
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE role_permission (
        permission_id int NOT NULL,
        role_id int NOT NULL,
        PRIMARY KEY (permission_id, role_id)
    );

    CREATE TABLE site (
        active bit NOT NULL,
        create_repository bit,
        id int identity NOT NULL,
        reload_count int,
        version datetimeoffset(7),
        name varchar(64) NOT NULL,
        domain varchar(255) NOT NULL,
        host varchar(255) NOT NULL,
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE site_application (
        active bit NOT NULL,
        application_id int NOT NULL,
        connection_id int,
        deletion_mark bit,
        reload_required bit,
        site_id int NOT NULL,
        PRIMARY KEY (application_id, site_id)
    );

    CREATE TABLE site_hostalias (
        site_id int NOT NULL,
        hostname varchar(255)
    );

    CREATE TABLE sites_granted (
        application_id int NOT NULL,
        granted_site_id int NOT NULL,
        site_id int NOT NULL,
        PRIMARY KEY (application_id, granted_site_id, site_id)
    );

    CREATE TABLE subject (
        id int identity NOT NULL,
        language varchar(3) NOT NULL,
        locked bit,
        login_attempts int,
        pw_change_policy smallint check ((pw_change_policy between 0 and 3)),
        expiry_date datetime2(7),
        last_login datetime2(7),
        pw_last_changed datetime2(7),
        version datetimeoffset(7),
        realname varchar(64) NOT NULL,
        digest varchar(255),
        email varchar(255),
        name varchar(255) NOT NULL,
        salt varchar(255),
        timezone varchar(255),
        type varchar(255) check ((type in ('LOCAL_USER','GLOBAL_USER','GLOBAL_GROUP'))),
        description varchar(max),
        PRIMARY KEY (id)
    );

    CREATE TABLE subject_authgroup (
        group_id int NOT NULL,
        subject_id int NOT NULL
    );

    CREATE TABLE template (
        id int identity NOT NULL,
        version datetimeoffset(7),
        appng_version varchar(255),
        description varchar(255),
        display_name varchar(255),
        long_desc varchar(255),
        name varchar(255),
        template_version varchar(255),
        timestamp varchar(255),
        tpl_type varchar(255) check ((tpl_type in ('XSL','THYMELEAF'))),
        PRIMARY KEY (id)
    );

    CREATE TABLE template_resource (
        id int identity NOT NULL,
        template_id int,
        file_version datetimeoffset(7),
        version datetimeoffset(7),
        checksum varchar(255),
        name varchar(255),
        bytes varbinary(max),
        PRIMARY KEY (id)
    );

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
       REFERENCES role;

    ALTER TABLE authgroup_role 
       ADD CONSTRAINT FK__AUTHGROUP_ROLE__AUTHGROUP 
       FOREIGN KEY (authgroup_id) 
       REFERENCES authgroup;

    ALTER TABLE database_connection 
       ADD CONSTRAINT FK__DATABASE_CONNECTION__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site;

    ALTER TABLE permission 
       ADD CONSTRAINT FK__PERMISSION__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application;

    ALTER TABLE resource 
       ADD CONSTRAINT FK__RESOURCE__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application;

    ALTER TABLE role 
       ADD CONSTRAINT FK__ROLE__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application;

    ALTER TABLE role_permission 
       ADD CONSTRAINT FK__ROLE_PERMISSION__PERMISSION 
       FOREIGN KEY (permission_id) 
       REFERENCES permission;

    ALTER TABLE role_permission 
       ADD CONSTRAINT FK__ROLE_PERMISSION__ROLE 
       FOREIGN KEY (role_id) 
       REFERENCES role;

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__APPLICATION 
       FOREIGN KEY (application_id) 
       REFERENCES application;

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__DATABASE_CONNECTION 
       FOREIGN KEY (connection_id) 
       REFERENCES database_connection;

    ALTER TABLE site_application 
       ADD CONSTRAINT FK__SITE_APPLICATION__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site;

    ALTER TABLE site_hostalias 
       ADD CONSTRAINT FK__SITE_HOSTALIAS__SITE 
       FOREIGN KEY (site_id) 
       REFERENCES site;

    ALTER TABLE sites_granted 
       ADD CONSTRAINT FK__SITES_GRANTED__SITE 
       FOREIGN KEY (granted_site_id) 
       REFERENCES site;

    ALTER TABLE sites_granted 
       ADD CONSTRAINT FK__SITES_GRANTED__SITE_APPLICATION 
       FOREIGN KEY (application_id, site_id) 
       REFERENCES site_application;

    ALTER TABLE subject_authgroup 
       ADD CONSTRAINT FK__SUBJECT_AUTHGROUP__AUTHGROUP 
       FOREIGN KEY (group_id) 
       REFERENCES authgroup;

    ALTER TABLE subject_authgroup 
       ADD CONSTRAINT FK__SUBJECT_AUTHGROUP__SUBJECT 
       FOREIGN KEY (subject_id) 
       REFERENCES subject;

    ALTER TABLE template_resource 
       ADD CONSTRAINT FK__TEMPLATE_RESOURCE__TEMPLATE_ID 
       FOREIGN KEY (template_id) 
       REFERENCES template;