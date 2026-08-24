CREATE TABLE IF NOT EXISTS geo_merchant (
    id varchar(32) NOT NULL COMMENT 'ID',
    tenant_id int DEFAULT 0 COMMENT 'Tenant id',
    merchant_name varchar(200) DEFAULT NULL COMMENT 'Merchant name',
    aliases varchar(500) DEFAULT NULL COMMENT 'Aliases',
    category varchar(100) DEFAULT NULL COMMENT 'Category',
    province varchar(100) DEFAULT NULL COMMENT 'Province',
    city varchar(100) DEFAULT NULL COMMENT 'City',
    district varchar(100) DEFAULT NULL COMMENT 'District',
    address varchar(500) DEFAULT NULL COMMENT 'Address',
    lng decimal(10,6) DEFAULT NULL COMMENT 'Longitude',
    lat decimal(10,6) DEFAULT NULL COMMENT 'Latitude',
    phone varchar(50) DEFAULT NULL COMMENT 'Phone',
    opening_hours varchar(500) DEFAULT NULL COMMENT 'Opening hours',
    service_area varchar(500) DEFAULT NULL COMMENT 'Service area',
    website varchar(500) DEFAULT NULL COMMENT 'Website',
    mini_program varchar(500) DEFAULT NULL COMMENT 'Mini program',
    logo varchar(500) DEFAULT NULL COMMENT 'Logo',
    description text COMMENT 'Description',
    status int DEFAULT 0 COMMENT 'Status: 0 draft, 1 active, 2 inactive',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_merchant_tenant (tenant_id),
    KEY idx_geo_merchant_city (city),
    KEY idx_geo_merchant_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO merchant';

CREATE TABLE IF NOT EXISTS geo_question_bank (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    question_type varchar(20) DEFAULT NULL COMMENT 'Question type',
    question text COMMENT 'Question',
    intent varchar(500) DEFAULT NULL COMMENT 'Intent',
    region varchar(200) DEFAULT NULL COMMENT 'Region',
    priority int DEFAULT 3 COMMENT 'Priority 1-5',
    source varchar(200) DEFAULT NULL COMMENT 'Source',
    status int DEFAULT 0 COMMENT 'Status',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_question_merchant (merchant_id),
    KEY idx_geo_question_type (question_type),
    KEY idx_geo_question_region (region)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO question bank';

CREATE TABLE IF NOT EXISTS geo_knowledge_item (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    category varchar(100) DEFAULT NULL COMMENT 'Category',
    fact varchar(200) DEFAULT NULL COMMENT 'Fact',
    value text COMMENT 'Value',
    source_type varchar(50) DEFAULT NULL COMMENT 'Source type',
    source_url varchar(500) DEFAULT NULL COMMENT 'Source url',
    owner_id varchar(50) DEFAULT NULL COMMENT 'Owner id',
    verified_at datetime DEFAULT NULL COMMENT 'Verified time',
    valid_from datetime DEFAULT NULL COMMENT 'Valid from',
    valid_to datetime DEFAULT NULL COMMENT 'Valid to',
    status int DEFAULT 0 COMMENT 'Status',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_knowledge_merchant (merchant_id),
    KEY idx_geo_knowledge_category (category),
    KEY idx_geo_knowledge_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO knowledge item';

CREATE TABLE IF NOT EXISTS geo_article (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    question_id varchar(32) DEFAULT NULL COMMENT 'Question id',
    title varchar(500) DEFAULT NULL COMMENT 'Title',
    title_type varchar(20) DEFAULT NULL COMMENT 'Title type',
    summary varchar(2000) DEFAULT NULL COMMENT 'Summary',
    content_md longtext COMMENT 'Markdown content',
    status int DEFAULT 0 COMMENT 'Status',
    review_status int DEFAULT 0 COMMENT 'Review status',
    reviewer_id varchar(50) DEFAULT NULL COMMENT 'Reviewer id',
    published_at datetime DEFAULT NULL COMMENT 'Published time',
    canonical_url varchar(1000) DEFAULT NULL COMMENT 'Canonical url',
    eeat_experience_score int DEFAULT NULL COMMENT 'E-E-A-T experience score',
    eeat_expertise_score int DEFAULT NULL COMMENT 'E-E-A-T expertise score',
    eeat_authority_score int DEFAULT NULL COMMENT 'E-E-A-T authority score',
    eeat_trust_score int DEFAULT NULL COMMENT 'E-E-A-T trust score',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_article_merchant (merchant_id),
    KEY idx_geo_article_status (status),
    KEY idx_geo_article_question (question_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO article';

CREATE TABLE IF NOT EXISTS geo_channel (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    platform varchar(50) DEFAULT NULL COMMENT 'Platform',
    channel_name varchar(200) DEFAULT NULL COMMENT 'Channel name',
    config_encrypted text COMMENT 'Encrypted config',
    enabled int DEFAULT 0 COMMENT 'Enabled',
    rate_limit int DEFAULT 0 COMMENT 'Rate limit',
    status int DEFAULT 0 COMMENT 'Status',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_channel_merchant (merchant_id),
    KEY idx_geo_channel_platform (platform)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO channel';

CREATE TABLE IF NOT EXISTS geo_publish_task (
    id varchar(32) NOT NULL COMMENT 'ID',
    article_id varchar(32) DEFAULT NULL COMMENT 'Article id',
    channel_id varchar(32) DEFAULT NULL COMMENT 'Channel id',
    status int DEFAULT 0 COMMENT 'Status',
    external_id varchar(200) DEFAULT NULL COMMENT 'External id',
    external_url varchar(1000) DEFAULT NULL COMMENT 'External url',
    error_code varchar(100) DEFAULT NULL COMMENT 'Error code',
    error_msg varchar(2000) DEFAULT NULL COMMENT 'Error message',
    retry_count int DEFAULT 0 COMMENT 'Retry count',
    scheduled_at datetime DEFAULT NULL COMMENT 'Scheduled time',
    published_at datetime DEFAULT NULL COMMENT 'Published time',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_publish_article (article_id),
    KEY idx_geo_publish_channel (channel_id),
    KEY idx_geo_publish_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO publish task';

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000001', '', 'GEO运营', '/geo', 'layouts/RouteView', 1, '', null, 0, null, '0', 90, 0, 'ant-design:radar-chart-outlined', 0, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000011', '202608170000000001', '商家主档', '/geo/merchant', 'geo/merchant/index', 1, '', null, 1, null, '1', 1, 0, 'ant-design:shop-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000101', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000001', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000102', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000011', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000012', '202608170000000001', '问题库', '/geo/questionBank', 'geo/questionBank/index', 1, '', null, 1, null, '1', 2, 0, 'ant-design:question-circle-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000013', '202608170000000001', '知识库', '/geo/knowledge', 'geo/knowledge/index', 1, '', null, 1, null, '1', 3, 0, 'ant-design:database-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000014', '202608170000000001', '文章工坊', '/geo/article', 'geo/article/index', 1, '', null, 1, null, '1', 4, 0, 'ant-design:file-text-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000103', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000012', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000104', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000013', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000105', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000014', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000015', '202608170000000001', '发布渠道', '/geo/channel', 'geo/channel/index', 1, '', null, 1, null, '1', 5, 0, 'ant-design:api-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000016', '202608170000000001', '发布任务', '/geo/publishTask', 'geo/publishTask/index', 1, '', null, 1, null, '1', 6, 0, 'ant-design:send-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000017', '202608170000000001', '监测任务', '/geo/monitorTask', 'geo/monitorTask/index', 1, '', null, 1, null, '1', 7, 0, 'ant-design:radar-chart-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000018', '202608170000000001', 'AI回答记录', '/geo/mention', 'geo/mention/index', 1, '', null, 1, null, '1', 8, 0, 'ant-design:message-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000019', '202608170000000001', '舆情事件', '/geo/sentimentEvent', 'geo/sentimentEvent/index', 1, '', null, 1, null, '1', 9, 0, 'ant-design:alert-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608170000000020', '202608170000000001', '实验中心', '/geo/experiment', 'geo/experiment/index', 1, '', null, 1, null, '1', 10, 0, 'ant-design:experiment-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-17 00:00:00', 'admin', '2026-08-17 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000106', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000015', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000107', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000016', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000108', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000017', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000109', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000018', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000110', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000019', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608170000000111', 'f6817f48af4fb3af11b9e8bf182f618b', '202608170000000020', null, '2026-08-17 00:00:00', '0:0:0:0:0:0:0:1');

CREATE TABLE IF NOT EXISTS geo_monitor_task (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    name varchar(200) DEFAULT NULL COMMENT 'Task name',
    query_set_json longtext COMMENT 'Query set json',
    engine_config_json longtext COMMENT 'Engine config json',
    cadence varchar(50) DEFAULT NULL COMMENT 'Cadence',
    last_run_at datetime DEFAULT NULL COMMENT 'Last run time',
    enabled int DEFAULT 0 COMMENT 'Enabled',
    status int DEFAULT 0 COMMENT 'Status',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_monitor_merchant (merchant_id),
    KEY idx_geo_monitor_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO monitor task';

CREATE TABLE IF NOT EXISTS geo_mention (
    id varchar(32) NOT NULL COMMENT 'ID',
    monitor_task_id varchar(32) DEFAULT NULL COMMENT 'Monitor task id',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    engine varchar(50) DEFAULT NULL COMMENT 'Engine',
    query text COMMENT 'Query',
    occurred_at datetime DEFAULT NULL COMMENT 'Occurred time',
    answer_text longtext COMMENT 'Answer text',
    mentioned int DEFAULT 0 COMMENT 'Mentioned',
    position int DEFAULT NULL COMMENT 'Position',
    source_urls_json longtext COMMENT 'Source urls json',
    accuracy_score double DEFAULT NULL COMMENT 'Accuracy score',
    sentiment varchar(20) DEFAULT NULL COMMENT 'Sentiment',
    raw_json longtext COMMENT 'Raw json',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_mention_task (monitor_task_id),
    KEY idx_geo_mention_merchant (merchant_id),
    KEY idx_geo_mention_engine (engine)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO mention';

CREATE TABLE IF NOT EXISTS geo_sentiment_event (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    platform varchar(50) DEFAULT NULL COMMENT 'Platform',
    event_type varchar(50) DEFAULT NULL COMMENT 'Event type',
    title varchar(500) DEFAULT NULL COMMENT 'Title',
    content longtext COMMENT 'Content',
    sentiment varchar(20) DEFAULT NULL COMMENT 'Sentiment',
    severity int DEFAULT 0 COMMENT 'Severity',
    status int DEFAULT 0 COMMENT 'Status',
    owner_id varchar(50) DEFAULT NULL COMMENT 'Owner id',
    source_url varchar(1000) DEFAULT NULL COMMENT 'Source url',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_sentiment_merchant (merchant_id),
    KEY idx_geo_sentiment_platform (platform),
    KEY idx_geo_sentiment_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO sentiment event';

CREATE TABLE IF NOT EXISTS geo_experiment (
    id varchar(32) NOT NULL COMMENT 'ID',
    merchant_id varchar(32) DEFAULT NULL COMMENT 'Merchant id',
    name varchar(200) DEFAULT NULL COMMENT 'Experiment name',
    control_group_json longtext COMMENT 'Control group json',
    variant_group_json longtext COMMENT 'Variant group json',
    status int DEFAULT 0 COMMENT 'Status',
    started_at datetime DEFAULT NULL COMMENT 'Started time',
    ended_at datetime DEFAULT NULL COMMENT 'Ended time',
    conclusion longtext COMMENT 'Conclusion',
    create_by varchar(50) DEFAULT NULL,
    create_time datetime DEFAULT NULL,
    update_by varchar(50) DEFAULT NULL,
    update_time datetime DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_geo_experiment_merchant (merchant_id),
    KEY idx_geo_experiment_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='GEO experiment';
