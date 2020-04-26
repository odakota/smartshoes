--
-- Table structure for table province
--
create table province_tbl (
    id   bigint primary key,
    name varchar(50),
    code varchar(20)
);

--
-- Table structure for table district_tbl
--
create table district_tbl (
    id          bigint primary key,
    name        varchar(100),
    prefix      varchar(20),
    province_id bigint,
    foreign key (province_id) references province_tbl (id)
);

--
-- Table structure for table ward_tbl
--
create table ward_tbl (
    id          bigint primary key,
    name        varchar(50),
    prefix      varchar(20),
    province_id bigint,
    district_id bigint,
    foreign key (district_id) references district_tbl (id),
    foreign key (province_id) references province_tbl (id)
);

--
-- Table structure for table `qrtz_job_details`
--
create table qrtz_job_details (
    sched_name        varchar(120) not null,
    job_name          varchar(190) not null,
    job_group         varchar(190) not null,
    description       varchar(250) null,
    job_class_name    varchar(250) not null,
    is_durable        boolean      not null,
    is_nonconcurrent  boolean      not null,
    is_update_data    boolean      not null,
    requests_recovery boolean      not null,
    job_data          bytea        null,
    primary key (sched_name, job_name, job_group)
);

--
-- Table structure for table `qrtz_triggers`
--
create table qrtz_triggers (
    sched_name     varchar(120) not null,
    trigger_name   varchar(190) not null,
    trigger_group  varchar(190) not null,
    job_name       varchar(190) not null,
    job_group      varchar(190) not null,
    description    varchar(250) null,
    next_fire_time bigint       null,
    prev_fire_time bigint       null,
    priority       integer      null,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     bigint       not null,
    end_time       bigint       null,
    calendar_name  varchar(190) null,
    misfire_instr  smallint     null,
    job_data       bytea        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group)
        references qrtz_job_details (sched_name, job_name, job_group)
);

--
-- Table structure for table `qrtz_simple_triggers`
--
create table qrtz_simple_triggers (
    sched_name      varchar(120) not null,
    trigger_name    varchar(190) not null,
    trigger_group   varchar(190) not null,
    repeat_count    bigint       not null,
    repeat_interval bigint       not null,
    times_triggered bigint       not null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_cron_triggers`
--
create table qrtz_cron_triggers (
    sched_name      varchar(120) not null,
    trigger_name    varchar(190) not null,
    trigger_group   varchar(190) not null,
    cron_expression varchar(120) not null,
    time_zone_id    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_simprop_triggers`
--
create table qrtz_simprop_triggers (
    sched_name    varchar(120)   not null,
    trigger_name  varchar(190)   not null,
    trigger_group varchar(190)   not null,
    str_prop_1    varchar(512)   null,
    str_prop_2    varchar(512)   null,
    str_prop_3    varchar(512)   null,
    int_prop_1    int            null,
    int_prop_2    int            null,
    long_prop_1   bigint         null,
    long_prop_2   bigint         null,
    dec_prop_1    numeric(13, 4) null,
    dec_prop_2    numeric(13, 4) null,
    bool_prop_1   boolean        null,
    bool_prop_2   boolean        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_blob_triggers`
--
create table qrtz_blob_triggers (
    sched_name    varchar(120) not null,
    trigger_name  varchar(190) not null,
    trigger_group varchar(190) not null,
    blob_data     bytea        null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group)
        references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

--
-- Table structure for table `qrtz_calendars`
--
create table qrtz_calendars (
    sched_name    varchar(120) not null,
    calendar_name varchar(190) not null,
    calendar      bytea        not null,
    primary key (sched_name, calendar_name)
);

--
-- Table structure for table `qrtz_paused_trigger_grps`
--
create table qrtz_paused_trigger_grps (
    sched_name    varchar(120) not null,
    trigger_group varchar(190) not null,
    primary key (sched_name, trigger_group)
);

--
-- Table structure for table `qrtz_fired_triggers`
--
create table qrtz_fired_triggers (
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(190) not null,
    trigger_group     varchar(190) not null,
    instance_name     varchar(190) not null,
    fired_time        bigint       not null,
    sched_time        bigint       not null,
    priority          integer      not null,
    state             varchar(16)  not null,
    job_name          varchar(190) null,
    job_group         varchar(190) null,
    is_nonconcurrent  boolean      null,
    requests_recovery boolean      null,
    primary key (sched_name, entry_id)
);

--
-- Table structure for table `qrtz_scheduler_state`
--
create table qrtz_scheduler_state (
    sched_name        varchar(120) not null,
    instance_name     varchar(190) not null,
    last_checkin_time bigint       not null,
    checkin_interval  bigint       not null,
    primary key (sched_name, instance_name)
);

--
-- Table structure for table `qrtz_locks`
--
create table qrtz_locks (
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    primary key (sched_name, lock_name)
);

create index idx_qrtz_j_req_recovery on qrtz_job_details (sched_name, requests_recovery);
create index idx_qrtz_j_grp on qrtz_job_details (sched_name, job_group);

create index idx_qrtz_t_j on qrtz_triggers (sched_name, job_name, job_group);
create index idx_qrtz_t_jg on qrtz_triggers (sched_name, job_group);
create index idx_qrtz_t_c on qrtz_triggers (sched_name, calendar_name);
create index idx_qrtz_t_g on qrtz_triggers (sched_name, trigger_group);
create index idx_qrtz_t_state on qrtz_triggers (sched_name, trigger_state);
create index idx_qrtz_t_n_state on qrtz_triggers (sched_name, trigger_name, trigger_group, trigger_state);
create index idx_qrtz_t_n_g_state on qrtz_triggers (sched_name, trigger_group, trigger_state);
create index idx_qrtz_t_next_fire_time on qrtz_triggers (sched_name, next_fire_time);
create index idx_qrtz_t_nft_st on qrtz_triggers (sched_name, trigger_state, next_fire_time);
create index idx_qrtz_t_nft_misfire on qrtz_triggers (sched_name, misfire_instr, next_fire_time);
create index idx_qrtz_t_nft_st_misfire on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_state);
create index idx_qrtz_t_nft_st_misfire_grp on qrtz_triggers (sched_name, misfire_instr, next_fire_time, trigger_group,
                                                             trigger_state);

create index idx_qrtz_ft_trig_inst_name on qrtz_fired_triggers (sched_name, instance_name);
create index idx_qrtz_ft_inst_job_req_rcvry on qrtz_fired_triggers (sched_name, instance_name, requests_recovery);
create index idx_qrtz_ft_j_g on qrtz_fired_triggers (sched_name, job_name, job_group);
create index idx_qrtz_ft_jg on qrtz_fired_triggers (sched_name, job_group);
create index idx_qrtz_ft_t_g on qrtz_fired_triggers (sched_name, trigger_name, trigger_group);
create index idx_qrtz_ft_tg on qrtz_fired_triggers (sched_name, trigger_group);

--
-- Table structure for table branch_tbl
--
create table branch_tbl (
    id           bigint primary key generated always as identity,
    branch_name  varchar(128) not null,
    branch_code  varchar(16)  not null,
    branch_phone varchar(20),
    province_id  bigint,
    district_id  bigint,
    ward_id      bigint,
    street       varchar(500),
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);
create unique index branch_uq1 on branch_tbl (branch_code) where deleted_flag = false;

--
-- Table structure for table role_tbl
--
create table role_tbl (
    id           bigint primary key generated always as identity,
    role_name    varchar(128) not null,
    role_code    varchar(16)  not null,
    description  varchar(2000),
    branch_id    bigint,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (branch_id) references branch_tbl (id)
);
create unique index role_uq1 on role_tbl (role_code) where deleted_flag = false;

--
-- Table structure for table user_tbl
--
create table user_tbl (
    id           bigint primary key generated always as identity,
    user_name    varchar(64)  not null,
    password     varchar(256) not null,
    full_name    varchar(256) not null,
    sex          smallint,
    birth_date   date,
    avatar       varchar(500),
    email        varchar(45)  not null,
    phone        varchar(45)  not null,
    province_id  bigint,
    district_id  bigint,
    ward_id      bigint,
    street       varchar(500),
    branch_id    bigint,
    lock_flag    boolean      not null default false,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean               default false,
    foreign key (branch_id) references branch_tbl (id)
);
comment on column user_tbl.sex is 'Gender: 0-Other, 1-Male, 2-Female';
create unique index user_uq1 on user_tbl (user_name) where deleted_flag = false;
create unique index user_uq2 on user_tbl (phone) where deleted_flag = false;
create unique index user_uq3 on user_tbl (email) where deleted_flag = false;

--
-- Table structure for table permission_tbl
--
create table permission_tbl (
    id                   bigint primary key,
    permission_name      varchar(1024) not null,
    parent_id            bigint,
    url                  varchar(255),
    component            varchar(255),
    component_name       varchar(100),
    redirect             varchar(255),
    menu_type            smallint,
    perms                varchar(255),
    perms_type           varchar(10) default '0',
    sort_no              numeric(8, 2),
    always_show          boolean,
    icon                 varchar(100),
    is_route             boolean     default true,
    is_leaf              boolean     default false,
    keep_alive           boolean     default true,
    hidden               boolean     default false,
    description          varchar(255),
    rule_flag            boolean     default false,
    status               boolean     default true,
    internal_or_external boolean     default true,
    updated_by           bigint,
    created_by           bigint,
    updated_date         timestamp,
    created_date         timestamp,
    deleted_flag         boolean     default false
);
comment on column permission_tbl.always_show is 'Aggregate child routes';
comment on column permission_tbl.is_route is 'Whether to route the menu';
comment on column permission_tbl.is_leaf is 'Whether leaf node';
comment on column permission_tbl.keep_alive is 'Whether to cache the page';
comment on column permission_tbl.hidden is 'Whether to hide routes';
comment on column permission_tbl.rule_flag is 'Whether to add data permissions';
comment on column permission_tbl.internal_or_external is 'How to open the external menu';
comment on column permission_tbl.status is 'Button permission status';
comment on column permission_tbl.menu_type is 'Menu type (0: first-level menu; 1: sub-menu: 2: button permissions)';
comment on column permission_tbl.perms is 'Menu permission encoding';
comment on column permission_tbl.perms_type is 'Permission Policy 1 Display 2 Disable';
comment on column permission_tbl.redirect is 'First-level menu jump address';

--
-- Table structure for table permission_role_tbl
--
create table permission_role_tbl (
    id            bigint primary key generated always as identity,
    role_id       bigint not null,
    permission_id bigint not null,
    api_id        varchar(255),
    updated_by    bigint,
    created_by    bigint,
    updated_date  timestamp,
    created_date  timestamp,
    deleted_flag  boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (permission_id) references permission_tbl (id)
);

--
-- Table structure for table user_role_tbl
--
create table user_role_tbl (
    id           bigint primary key generated always as identity,
    role_id      bigint not null,
    user_id      bigint not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (role_id) references role_tbl (id),
    foreign key (user_id) references user_tbl (id)
);

--
-- Table structure for table access_token_tbl
--
create table access_token_tbl (
    id           bigint primary key generated always as identity,
    jti          varchar(2000) not null,
    refresh_jti  varchar(2000) not null,
    user_id      bigint        not null,
    client       varchar(50)   not null,
    created_date timestamp     not null
);

--
-- Table structure for table notification_tbl
--
create table notification_tbl (
    id           bigint primary key generated always as identity,
    title        varchar(64) not null,
    content      text        not null,
    start_date   timestamp   not null,
    end_date     timestamp,
    sender       varchar(128),
    priority     smallint,
    type         smallint,
    send_status  smallint,
    send_time    timestamp,
    cancel_time  timestamp,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);
comment on column notification_tbl.priority is 'Priority (1: Low, 2: Medium, 3: High)';
comment on column notification_tbl.type is 'Message Type 1: Notification Bulletin 2: System Message';
comment on column notification_tbl.send_status is 'Release status (1 unpublished, 2 published, 3 revoked)';

--
-- Table structure for table notification_user_tbl
--
create table notification_user_tbl (
    id              bigint primary key generated always as identity,
    user_id         bigint not null,
    notification_id bigint not null,
    is_read         boolean,
    read_time       timestamp,
    updated_by      bigint,
    created_by      bigint,
    updated_date    timestamp,
    created_date    timestamp,
    deleted_flag    boolean default false,
    foreign key (notification_id) references notification_tbl (id),
    foreign key (user_id) references user_tbl (id)
);

--
-- Table structure for table login_failed_tbl
--
create table login_failed_tbl (
    id           bigint primary key generated always as identity,
    user_name    varchar(64),
    phone        varchar(45),
    is_customer  boolean default false,
    created_date timestamp
);
--
-- Table structure for table customer_tbl
--
create table customer_tbl (
    id                       bigint primary key generated always as identity,
    customer_type            smallint     not null,
    full_name                varchar(256) not null,
    sex                      smallint default 0,
    birth_date               date,
    avatar                   varchar(500),
    email                    varchar(45),
    phone                    varchar(45),
    identifier               varchar(45),
    province_id              bigint,
    district_id              bigint,
    ward_id                  bigint,
    street                   varchar(500),
    customer_segment         smallint default 1,
    is_mail_magazine_receipt boolean  default false,
    disable_flag             boolean  default false,
    input_type               smallint     not null,
    updated_by               bigint,
    created_by               bigint,
    updated_date             timestamp,
    created_date             timestamp,
    deleted_flag             boolean  default false
);
comment on column customer_tbl.birth_date is 'This is the date of birth for individual customers and the date of establishment with business customers';
comment on column customer_tbl.sex is 'Gender: 0-Other, 1-Male, 2-Female';
comment on column customer_tbl.customer_type is 'Customer type: 1-Individual customers, 2-Corporate customers';
comment on column customer_tbl.input_type is 'Input type: 1-Online, 2-Offline';
comment on column customer_tbl.customer_segment is '1-Super small, 2-Small, 3-Medium, 4-Big';
comment on column customer_tbl.identifier is 'This is the identity card for individual customers and the tax code with business customers';

--
-- Table structure for table category_tbl
--
create table category_tbl (
    id           bigint primary key generated always as identity,
    parent_id    bigint,
    name         varchar(128) not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table color_tbl
--
create table color_tbl (
    id           bigint primary key generated always as identity,
    name         varchar(128) not null,
    code         varchar(16)  not null,
    sort_order   int,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table size_tbl
--
create table size_tbl (
    id           bigint primary key generated always as identity,
    name         varchar(128) not null,
    code         varchar(16)  not null,
    standard     smallint,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);
comment on column size_tbl.standard is 'standard (1: VN, 2: US, 3: UK)';

--
-- Table structure for table product_tbl
--
create table product_tbl (
    id                  bigint primary key generated always as identity,
    branch_id           bigint,
    category_id         bigint       not null,
    name                varchar(128) not null,
    code                varchar(16)  not null,
    price               numeric      not null,
    company_sales_price numeric      not null,
    path                varchar(500) not null,
    sale_start_at       timestamp,
    sale_end_at         timestamp,
    description         text,
    is_company_sales    boolean default false,
    updated_by          bigint,
    created_by          bigint,
    updated_date        timestamp,
    created_date        timestamp,
    deleted_flag        boolean default false,
    foreign key (branch_id) references branch_tbl (id),
    foreign key (category_id) references category_tbl (id)
);
create unique index prod_uq1 on product_tbl (code) where deleted_flag = false;

--
-- Table structure for table allocation_product_tbl
--
create table allocation_product_tbl (
    id           bigint primary key generated always as identity,
    product_id   bigint not null,
    branch_id    bigint not null,
    color_id     bigint not null,
    size_id      bigint not null,
    total        int,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (product_id) references product_tbl (id),
    foreign key (color_id) references color_tbl (id),
    foreign key (size_id) references size_tbl (id),
    foreign key (branch_id) references branch_tbl (id)
);

--
-- Table structure for table receipt_tbl
--
create table receipt_tbl (
    id            bigint primary key generated always as identity,
    code          varchar(64)  not null,
    bill_code     varchar(64)  not null,
    deliver       varchar(200) not null,
    receipt_date  date         not null,
    warehouse     varchar(500) not null,
    branch_id     bigint       not null,
    note          varchar(1024),
    approved_date date,
    approved_flag boolean default false,
    file_path     text,
    updated_by    bigint,
    created_by    bigint,
    updated_date  timestamp,
    created_date  timestamp,
    deleted_flag  boolean default false,
    foreign key (branch_id) references branch_tbl (id)
);

--
-- Table structure for table receipt_detail_tbl
--
create table receipt_detail_tbl (
    id           bigint primary key generated always as identity,
    product_id   bigint      not null,
    product_code varchar(16) not null,
    receipt_id   bigint      not null,
    amount       int         not null,
    detail       text,
    note         varchar(1024),
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (product_id) references product_tbl (id),
    foreign key (receipt_id) references receipt_tbl (id)
);

--
-- Table structure for table payment_tbl
--
create table payment_tbl (
    id           bigint primary key generated always as identity,
    name         varchar(1024),
    fee          int     default 0         not null,
    lower_limit  bigint  default 1         not null,
    upper_limit  bigint  default 999999999 not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table payment_detail_tbl
--
create table payment_detail_tbl (
    id             bigint primary key generated always as identity,
    payment_id     int         not null,
    bank           varchar(500),
    account_number varchar(64) not null,
    updated_by     bigint,
    created_by     bigint,
    updated_date   timestamp,
    created_date   timestamp,
    deleted_flag   boolean default false,
    foreign key (payment_id) references payment_tbl (id)
);

--
-- Table structure for table campaign_tbl
--
create table campaign_tbl (
    id             bigint primary key generated always as identity,
    name           varchar(1024) not null,
    branch_id      bigint        not null,
    start_date     timestamp     not null,
    end_date       timestamp     not null,
    description    varchar(2048),
    discount_type  int,
    discount_value bigint,
    updated_by     bigint,
    created_by     bigint,
    updated_date   timestamp,
    created_date   timestamp,
    deleted_flag   boolean default false,
    foreign key (branch_id) references branch_tbl (id)
);

--
-- Table structure for table campaign_tbl
--
create table campaign_item_tbl (
    id           bigint primary key generated always as identity,
    campaign_id  bigint not null,
    product_id   bigint not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false
);

--
-- Table structure for table sales_order_tbl
--
create table sales_order_tbl (
    id           bigint primary key generated always as identity,
    customer_id  bigint   not null,
    payment_id   bigint   not null,
    sale_type    smallint not null,
    order_type   smallint not null,
    status       smallint not null,
    branch_id    bigint   not null,
    tax          numeric,
    total_amount numeric  not null,
    updated_by   bigint,
    created_by   bigint,
    updated_date timestamp,
    created_date timestamp,
    deleted_flag boolean default false,
    foreign key (customer_id) references customer_tbl (id),
    foreign key (payment_id) references payment_tbl (id)
);
comment on column sales_order_tbl.status is 'Order status: 1-Unpaid; 2-Paid; 3-Cancel';

--
-- Table structure for table sales_order_detail_tbl
--
create table sales_order_detail_tbl (
    id                    bigint primary key generated always as identity,
    allocation_product_id bigint      not null,
    product_id            bigint      not null,
    product_code          varchar(16) not null,
    pre_price             numeric     not null,
    lst_price             numeric     not null,
    sales_order_id        bigint      not null,
    amount_product        int         not null,
    discount              numeric,
    discount_reason       text,
    updated_by            bigint,
    created_by            bigint,
    updated_date          timestamp,
    created_date          timestamp,
    deleted_flag          boolean default false,
    foreign key (allocation_product_id) references allocation_product_tbl (id),
    foreign key (sales_order_id) references sales_order_tbl (id)
);

--
-- Table structure for table report_setting_tbl
--
create table report_setting_tbl (
    id             bigint primary key generated always as identity,
    branch_id      bigint,
    report_type    smallint,
    daily_time     varchar(24),
    monthly_time   varchar(24),
    monthly_day    int,
    yearly_time    varchar(24),
    yearly_day     int,
    yearly_month   int,
    receiver       text     not null,
    process_status smallint not null,
    updated_by     bigint,
    created_by     bigint,
    updated_date   timestamp,
    created_date   timestamp,
    deleted_flag   boolean default false,
    foreign key (branch_id) references branch_tbl (id)
);
comment on column report_setting_tbl.process_status is 'Process status: 1-Pause; 2-In Progress;';
comment on column report_setting_tbl.report_type is 'Report type: 1-Inventory report; 2-Sales summary reports; 3-Reported revenue by Staff; 4-Report bestsellers';

--
-- Table structure for table inventory_report_tbl
--
create table inventory_report_tbl (
    id           bigint primary key generated always as identity,
    branch_id    bigint,
    report_date  date         not null,
    product_code varchar(16)  not null,
    product_name varchar(200) not null,
    final_total  bigint       not null,
    output_total bigint       not null,
    input_total  bigint       not null,
    pre_total    bigint       not null,
    created_date timestamp,
    foreign key (branch_id) references branch_tbl (id)
);

--
-- Table structure for table bestseller_report_tbl
--
create table bestseller_report_tbl (
    id           bigint primary key generated always as identity,
    branch_id    bigint,
    report_date  varchar(7)   not null,
    product_code varchar(16)  not null,
    product_name varchar(200) not null,
    total_sale   bigint       not null,
    order_number bigint       not null,
    created_date timestamp,
    foreign key (branch_id) references branch_tbl (id)
);