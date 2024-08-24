package DAO;

import Model.Message;
import Util.ConnectionUtil;

/*
    Handles Message related-database queries

    message_id int primary key auto_increment,
    posted_by int,
    message_text varchar(255),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)

*/
public class MessageDAO {

}
