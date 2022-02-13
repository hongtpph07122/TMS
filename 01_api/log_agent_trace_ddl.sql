CREATE TABLE log_agent_trace (
		id bigserial NOT NULL, -- auto increment
 		agent_id int4 NOT NULL, -- id agent, user_id referenced from or_user
 		activity_id int4 NOT NULL, -- id activity, type_id referenced from cf_synonym
 		object_type text NULL, -- name of object
		object_id int4 NULL DEFAULT 0, -- id object based on object type above
		object_value text NULL, -- id object based on object type above
		on_field text NULL,
		last_value text NULL, -- last value of activity, value referenced from cf_synonym which type is AGENT STATE
		value text NOT NULL, -- new value of activity, value referenced from cf_synonym which type is AGENT STATE
		message text NULL,
		action_time timestamp(6) NOT NULL, -- created time based on server timezone
		created_at timestamptz NOT NULL DEFAULT now()) -- created time based on database timezone
PARTITION BY RANGE (action_time);	

CREATE TABLE log_agent_trace_y2021m01 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-01-01') TO ('2021-02-01');
CREATE TABLE log_agent_trace_y2021m02 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-02-01') TO ('2021-03-01');
CREATE TABLE log_agent_trace_y2021m03 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-03-01') TO ('2021-04-01');
CREATE TABLE log_agent_trace_y2021m04 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-04-01') TO ('2021-05-01');
CREATE TABLE log_agent_trace_y2021m05 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-05-01') TO ('2021-06-01');
CREATE TABLE log_agent_trace_y2021m06 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-06-01') TO ('2021-07-01');
CREATE TABLE log_agent_trace_y2021m07 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-07-01') TO ('2021-08-01');
CREATE TABLE log_agent_trace_y2021m08 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-08-01') TO ('2021-09-01');
CREATE TABLE log_agent_trace_y2021m09 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-09-01') TO ('2021-10-01');
CREATE TABLE log_agent_trace_y2021m10 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-10-01') TO ('2021-11-01');
CREATE TABLE log_agent_trace_y2021m11 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-11-01') TO ('2021-12-01');
CREATE TABLE log_agent_trace_y2021m12 PARTITION OF log_agent_trace FOR VALUES FROM ('2021-12-01') TO ('2022-01-01');


CREATE INDEX log_agent_trace_y2021m01_id_idx ON log_agent_trace_y2021m01 USING btree (id);
CREATE INDEX log_agent_trace_y2021m01_agent_id_idx ON log_agent_trace_y2021m01 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m01_agent_activity_id_idx ON log_agent_trace_y2021m01 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m02_id_idx ON log_agent_trace_y2021m02 USING btree (id);
CREATE INDEX log_agent_trace_y2021m02_agent_id_idx ON log_agent_trace_y2021m02 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m02_agent_activity_id_idx ON log_agent_trace_y2021m02 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m03_id_idx ON log_agent_trace_y2021m03 USING btree (id);
CREATE INDEX log_agent_trace_y2021m03_agent_id_idx ON log_agent_trace_y2021m03 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m03_agent_activity_id_idx ON log_agent_trace_y2021m03 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m04_id_idx ON log_agent_trace_y2021m04 USING btree (id);
CREATE INDEX log_agent_trace_y2021m04_agent_id_idx ON log_agent_trace_y2021m04 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m04_agent_activity_id_idx ON log_agent_trace_y2021m04 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m05_id_idx ON log_agent_trace_y2021m05 USING btree (id);
CREATE INDEX log_agent_trace_y2021m05_agent_id_idx ON log_agent_trace_y2021m05 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m05_agent_activity_id_idx ON log_agent_trace_y2021m05 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m06_id_idx ON log_agent_trace_y2021m06 USING btree (id);
CREATE INDEX log_agent_trace_y2021m06_agent_id_idx ON log_agent_trace_y2021m06 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m06_agent_activity_id_idx ON log_agent_trace_y2021m06 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m07_id_idx ON log_agent_trace_y2021m07 USING btree (id);
CREATE INDEX log_agent_trace_y2021m07_agent_id_idx ON log_agent_trace_y2021m07 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m07_agent_activity_id_idx ON log_agent_trace_y2021m07 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m08_id_idx ON log_agent_trace_y2021m08 USING btree (id);
CREATE INDEX log_agent_trace_y2021m08_agent_id_idx ON log_agent_trace_y2021m08 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m08_agent_activity_id_idx ON log_agent_trace_y2021m08 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m09_id_idx ON log_agent_trace_y2021m09 USING btree (id);
CREATE INDEX log_agent_trace_y2021m09_agent_id_idx ON log_agent_trace_y2021m09 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m09_agent_activity_id_idx ON log_agent_trace_y2021m09 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m10_id_idx ON log_agent_trace_y2021m10 USING btree (id);
CREATE INDEX log_agent_trace_y2021m10_agent_id_idx ON log_agent_trace_y2021m10 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m10_agent_activity_id_idx ON log_agent_trace_y2021m10 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m11_id_idx ON log_agent_trace_y2021m11 USING btree (id);
CREATE INDEX log_agent_trace_y2021m11_agent_id_idx ON log_agent_trace_y2021m11 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m11_agent_activity_id_idx ON log_agent_trace_y2021m11 USING btree (agent_id, activity_id);

CREATE INDEX log_agent_trace_y2021m12_id_idx ON log_agent_trace_y2021m12 USING btree (id);
CREATE INDEX log_agent_trace_y2021m12_agent_id_idx ON log_agent_trace_y2021m12 USING btree (agent_id);
CREATE INDEX log_agent_trace_y2021m12_agent_activity_id_idx ON log_agent_trace_y2021m12 USING btree (agent_id, activity_id);


