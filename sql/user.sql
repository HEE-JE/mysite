-- UserRepository

desc user;

insert into user values(null, '관리자', 'admin@mysite.com', '1234', 'male', now());

select * from user;

-- findByEmailAndPassword
select no, name from user where email='windba78@naver.com' and password='q15975302Q!';

-- findByNo
select no, name, email, gender from user where no=1;

-- updateWithPassword
update user set name='또치', password='1111', gender='female' where no=4;

-- updateWithoutPassword
update user set name='또치', gender='female' where no=4;