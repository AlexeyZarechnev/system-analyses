#!/bin/bash

CREATE TABLE kafka_numbers
(
    value String
)
ENGINE = Kafka
SETTINGS
    kafka_broker_list = 'kafka:9092',
    kafka_topic_list = 'numbers',
    kafka_group_name = 'clickhouse_consumer',
    kafka_format = 'LineAsString',
    kafka_num_consumers = 1,
    kafka_handle_error_mode = 'stream';

# All

CREATE TABLE numbers_all
(
    num Nullable(Int64),
    raw_value String
)
ENGINE = MergeTree
ORDER BY tuple();

CREATE MATERIALIZED VIEW mv_numbers_all
TO numbers_all
AS
SELECT
    toInt64OrNull(value) AS num,
    value AS raw_value
FROM kafka_numbers;

# Sum of valid

CREATE TABLE numbers_sum
(
    positive_sum Int64,
    negative_sum Int64
)
ENGINE = SummingMergeTree
ORDER BY tuple();


CREATE MATERIALIZED VIEW mv_numbers_sum
TO numbers_sum
AS
SELECT
    sumIf(num, num > 0) AS positive_sum,
    sumIf(num, num < 0) AS negative_sum
FROM numbers_all
WHERE num IS NOT NULL;

# invalid

CREATE TABLE dlq_numbers
(
    raw_value String,
    error String
)
ENGINE = MergeTree
ORDER BY tuple();

CREATE MATERIALIZED VIEW mv_numbers_dlq
TO dlq_numbers
AS
SELECT
    raw_value,
    'Invalid integer' AS error
FROM numbers_all
WHERE num IS NULL;






