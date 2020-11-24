package com.eighthours.sample.domain.user.dao

import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.domain.user.User
import com.eighthours.sample.domain.user.UserProfile
import org.seasar.doma.Dao
import org.seasar.doma.Select
import org.seasar.doma.experimental.Sql

@Dao
interface UserProfileQueryDao {

    @Sql(
        """
    select
      /*%expand*/*
    from user_profile
    where user_id = /* userId */''
    """
    )
    @Select
    fun select(userId: StringId<User>): UserProfile?
}
