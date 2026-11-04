package com.dev.Expense_tracker_App.repo;

import com.dev.Expense_tracker_App.Entities.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserInfo, Long>
{
    public UserInfo findByUsername(String username);
}
