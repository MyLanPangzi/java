# Kubectl Apply
1. Calculate the fields to delete. These are the fields present in last-applied-configuration and missing from the configuration file.
2. 比较注释与配置文件，删除注释中有但配置文件没有的字段。
3. Calculate the fields to add or set. These are the fields present in the configuration file whose values don’t match the live configuration.
4. 比较配置文件与live对象的字段值，设置配置文件中的值。