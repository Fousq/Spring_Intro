create table if not exists ticket(
    id bigint primary key,
    event_id bigint,
    user_id bigint,
    booked boolean default false,
    foreign key (event_id) references event(id),
    foreign key (user_id) references user_account(id)
);