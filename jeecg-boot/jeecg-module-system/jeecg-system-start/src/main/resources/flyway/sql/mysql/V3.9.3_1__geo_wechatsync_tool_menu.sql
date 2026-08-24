INSERT INTO sys_permission (id, parent_id, name, url, component, is_route, component_name, redirect, menu_type, perms, perms_type, sort_no, always_show, icon, is_leaf, keep_alive, hidden, hide_tab, description, create_by, create_time, update_by, update_time, del_flag, rule_flag, status, internal_or_external)
VALUES ('202608190000000001', '202608170000000001', '环境检查', '/geo/wechatsync', 'geo/wechatsync/index', 1, '', null, 1, null, '1', 11, 0, 'ant-design:tool-outlined', 1, 0, 0, 0, null, 'admin', '2026-08-19 00:00:00', 'admin', '2026-08-19 00:00:00', 0, 0, '1', 0);

INSERT INTO sys_role_permission (id, role_id, permission_id, data_rule_ids, operate_date, operate_ip)
VALUES ('202608190000000101', 'f6817f48af4fb3af11b9e8bf182f618b', '202608190000000001', null, '2026-08-19 00:00:00', '0:0:0:0:0:0:0:1');
