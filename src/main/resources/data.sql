insert into customer(customer_id, first_name, last_name) values(1,'Mark', 'Wood');
insert into account(account_id, type, balance) values (1,'SAVINGS', 10.0);
insert into transaction(transaction_id, customer_id , account_id, type, amount, balance, description, transaction_date_time) values (1,1,1,'DEPOSIT',10.0, 10.0, 'Deposit', '2022-10-10 23:59:58');