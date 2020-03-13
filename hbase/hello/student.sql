processlist, status, table_help, version, whoami

create_namespace 'hello'
drop_namespace 'hello'
list_namespace
list_namespace_tables 'hbase'
describe_namespace 'hello'
alter_namespace 'hello',{METHOD => 'set', NAME => 'world'}
alter_namespace 'hello',{METHOD => 'unset', NAME => 'hello'}
alter_namespace 'hello',{METHOD => 'set', 'hello' => 'world'}

create
create 'hello:person','info'
create 'hello:animal',{NAME => 'info', VERSIONS => 5}
disable_all 'hello:.*'
disable 'hello:person'
is_disabled 'hello:person'

enable_all 'hello:.*'
enable 'hello:person'
is_enabled 'hello:person'

drop 'hello:person'
drop_all 'hello:.*'
list
desc 'hello:person'
alter'hello:person'

get_table
alter_async, alter_status,
exists, ,list_regions,
locate_region, show_filters

append 'hello:person','1','info:nanme','hiscat'
person.append '1','info:nanme','hiscat'
count 'hello:person'
person.count
delete 'hello:person', '1', 'info:name',1900000
person.delete '1','info:name',109999
deleteall 'hello:person', '1', 'info:name',1900000
get 'hello:person', '1', {TIMERANGE => [0, 1983921325587]}
person.get '1',['info:name']
person.put '1','info:height','180'
person.scan  COLUMNS => ['info:name'], LIMIT => 10, STARTROW => '0',STOPROW => '1'
 hbase> scan 'hbase:meta'
  hbase> scan 'hbase:meta', {COLUMNS => 'info:regioninfo'}
  hbase> scan 'ns1:t1', {COLUMNS => ['c1', 'c2'], LIMIT => 10, STARTROW => 'xyz'}
  hbase> scan 't1', {COLUMNS => ['c1', 'c2'], LIMIT => 10, STARTROW => 'xyz'}
  hbase> scan 't1', {COLUMNS => 'c1', TIMERANGE => [1303668804, 1303668904]}
  hbase> scan 't1', {REVERSED => true}
  hbase> scan 't1', {ALL_METRICS => true}
  hbase> scan 't1', {METRICS => ['RPC_RETRIES', 'ROWS_FILTERED']}
  hbase> scan 't1', {ROWPREFIXFILTER => 'row2', FILTER => "
    (QualifierFilter (>=, 'binary:xyz')) AND (TimestampsFilter ( 123, 456))"}
  hbase> scan 't1', {FILTER =>
    org.apache.hadoop.hbase.filter.ColumnPaginationFilter.new(1, 0)}
  hbase> scan 't1', {CONSISTENCY => 'TIMELINE'}
For setting the Operation Attributes
  hbase> scan 't1', { COLUMNS => ['c1', 'c2'], ATTRIBUTES => {'mykey' => 'myvalue'}}
  hbase> scan 't1', { COLUMNS => ['c1', 'c2'], AUTHORIZATIONS => ['PRIVATE','SECRET']}

truncate,
truncate_preserve
