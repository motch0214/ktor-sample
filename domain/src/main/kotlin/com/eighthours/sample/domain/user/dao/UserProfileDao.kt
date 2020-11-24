package com.eighthours.sample.domain.user.dao

import com.eighthours.sample.domain.user.UserProfile
import org.seasar.doma.Dao
import org.seasar.doma.Insert
import org.seasar.doma.Update
import org.seasar.doma.jdbc.Result

@Dao
interface UserProfileDao {

    @Insert
    fun insert(profile: UserProfile): Result<UserProfile>

    @Update
    fun update(profile: UserProfile): Result<UserProfile>
}
