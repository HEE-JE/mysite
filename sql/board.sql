-- board

desc board;

insert into board values(null, '안녕하세요', 'ㄴㄴㄴ', 0, now(), (select max(g_no) from board a) + 1, 1, 1, 1);
insert into board values(null, '안녕하세요', 'ㄴㄴㄴ', 0, now(), 31, 1, 1, 1);
select * from board;
select max(g_no) from board;
select * from user;

select a.no, a.title, b.name, a.hit, date_format(a.reg_date, '%Y-%m-%d %r'), a.g_no, a.o_no, a.depth
	from board a, user b
    where a.user_no = b.no
	order by g_no desc, o_no asc;
    
select title, contents from board where g_no=1 and o_no=1;

update board set title='안녕하세요2', contents='ddd' where g_no=1 and o_no=1;

update board set o_no = o_no + 1 where g_no=39 and o_no < 3;

insert into board values(null, '2', '3', 0, now(), IFNULL((select max(g_no) from board a) + 1, 1), 1, 1, 1);

select count(*) as totalCount from board;

select a.no, a.title, b.name, a.hit, date_format(a.reg_date, '%Y-%m-%d %r'), a.g_no, a.o_no, a.depth, a.user_no 
	from board a, user b 
    where a.user_no = b.no 
    order by g_no desc, o_no asc
    limit (select (count(*) / 5) + 1 as a from board), 5;
    
    select (count(*) / 5) + 1 from board;




